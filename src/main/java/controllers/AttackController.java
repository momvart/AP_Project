package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import exceptions.*;
import graphics.gui.AttackStage;
import javafx.application.Platform;
import menus.AttackMapItem;
import menus.IMenuClickListener;
import menus.Menu;
import menus.ParentMenu;
import models.World;
import models.attack.Attack;
import models.attack.AttackMap;
import models.buildings.Building;
import models.buildings.ElixirStorage;
import models.buildings.GoldStorage;
import models.soldiers.SoldierValues;
import serialization.AttackMapGlobalAdapter;
import serialization.BuildingGlobalAdapter;
import serialization.StorageGlobalAdapter;
import utils.ConsoleUtilities;
import utils.ICommandManager;
import utils.Point;
import views.AttackView;
import views.dialogs.DialogResult;
import views.dialogs.DialogResultCode;
import views.dialogs.TextInputDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;

public class AttackController implements IMenuClickListener, ICommandManager
{
    private static final int MAX_ATTACK_TURN = 10000;

    private static final String SELECT_UNIT_PATTERN = "select\\s+(?<type>\\w+)\\s+(?<count>\\d+)";
    private static final String PUT_UNIT_PATTERN = "put\\s+(?<type>\\w+)\\s+(?<count>\\d+)\\s+in\\s+(?<x>\\d+)\\s*,\\s*(?<y>\\d+)";

    private AttackView theView;
    private Attack theAttack;

    private boolean finished = false;

    public AttackController(AttackView view)
    {
        this.theView = view;
        theView.addClickListener(this);
    }

    public void start(List<Path> paths)
    {
        ParentMenu mainMenu = new ParentMenu(Menu.Id.ATTACK_MAIN_MENU, "");
        mainMenu.insertItem(new Menu(Menu.Id.ATTACK_LOAD_MAP, "Load Map"));
        paths.forEach(p -> mainMenu.insertItem(new AttackMapItem(mainMenu, p)));
        mainMenu.insertItem(new Menu(Menu.Id.ATTACK_MAIN_BACK, "back"));
        theView.setCurrentMenu(mainMenu, true);
        childCommandManager = theView;
    }

    public boolean isFinished()
    {
        return finished;
    }

    private void setTheAttack(Attack theAttack)
    {
        this.theAttack = theAttack;
        theView.setAttack(theAttack);
    }

    @Override
    public void onItemClicked(Menu menu)
    {
        try
        {
            switch (menu.getId())
            {
                case Menu.Id.ATTACK_LOAD_MAP:
                {
                    DialogResult result = theView.showOpenMapDialog();
                    if (result.getResultCode() != DialogResultCode.YES)
                        break;
                    String path = (String)result.getData(TextInputDialog.KEY_TEXT);
                    openMap(Paths.get(path), true);//TODO this code is expired. anytime in use consider the map reality
                    World.sSettings.getAttackMapPaths().add(path);
                    World.saveSettings();
                    theView.setCurrentMenu(new AttackMapItem(theView.getCurrentMenu(), Paths.get(path)), true);
                }
                break;
                case Menu.Id.ATTACK_LOAD_MAP_ITEM:
                {
                    openMap(((AttackMapItem)menu).getFilePath(), true);//TODO this code is expired. anytime in use consider the map reality
                }
                break;
                case Menu.Id.ATTACK_MAIN_BACK:
                    quitAttack(Attack.QuitReason.USER);
                    break;
                case Menu.Id.ATTACK_MAP_ATTACK:
                {
                    theView.setCurrentMenu(null, false);
                    Platform.runLater(() -> new AttackStage(theAttack, 1200, 900).setup());
                }
                break;
            }
        }
        catch (ConsoleException ex)
        {
            theView.showError(ex);
        }
    }

    private ICommandManager childCommandManager;

    @Override
    public void manageCommand(String command) throws ConsoleException
    {
        try
        {
            childCommandManager.manageCommand(command);
        }
        catch (InvalidCommandException ex)
        {
            if (theAttack == null)
                throw ex;
            Matcher m;
            if (command.matches("(?i)start\\s+select"))
            {
                startSelectingUnits();
                theView.viewMapStatus();
            }
            else if ((m = ConsoleUtilities.getMatchedCommand(PUT_UNIT_PATTERN, command)) != null)
            {
                Point location = new Point(Integer.parseInt(m.group("x")), Integer.parseInt(m.group("y")));
                putUnits(SoldierValues.getTypeByName(m.group("type")), Integer.parseInt(m.group("count")), location);
            }
            else if (command.matches("(?i)go\\s+next\\s+turn"))
                passTurn();
            else if ((m = ConsoleUtilities.getMatchedCommand("turn\\s+(\\d+)", command)) != null)
            {
                int count = Integer.parseInt(m.group(1));
                for (int i = 0; i < count; i++)
                    passTurn();
            }
            else if (command.matches("(?i)status\\s+resources"))
                theView.showResourcesStatus();
            else if ((m = ConsoleUtilities.getMatchedCommand("(?i)status\\s+unit\\s+(?<type>\\w+)", command)) != null)
                theView.showSoldiersStatus(SoldierValues.getTypeByName(m.group("type")));
            else if (command.matches("(?i)status\\s+units"))
                theView.showAllSoldiersStatus();
            else if ((m = ConsoleUtilities.getMatchedCommand("(?i)status\\s+tower\\s+(?<type>\\d+)", command)) != null)
                theView.showTowersStatus(Integer.parseInt(m.group("type")));
            else if (command.matches("(?i)status\\s+towers"))
                theView.showAllTowersStatus();
            else if (command.matches("(?i)status\\s+all"))
                theView.showAllAll();
            else if (command.matches("(?i)quit\\s+attack"))
                quitAttack(Attack.QuitReason.USER);
            else if (command.equalsIgnoreCase("showmap")) //Extension method for viewing map
                theView.viewMapStatus();
            else
                throw ex;
        }
    }


    private boolean unitsSelected = false;

    private void startSelectingUnits() throws ConsoleException
    {
        if (unitsSelected)
            throw new ConsoleException("You have already selected your units.", "You can only select units once in an attack.");
        String command;
        while (!(command = theView.getCommand()).matches("(?i)end\\s+select"))
            try
            {
                Matcher m = ConsoleUtilities.getMatchedCommand(SELECT_UNIT_PATTERN, command);
                if (m == null)
                    throw new InvalidCommandException(command, "Expected format: " + SELECT_UNIT_PATTERN);

                int soldierType = SoldierValues.getTypeByName(m.group("type"));
                try
                {
                    theAttack.addUnits(World.getVillage().selectUnit(soldierType, Integer.parseInt(m.group("count")), theAttack));
                }
                catch (NotEnoughSoldierException ex)
                {
                    theView.showError(ex);
                    theAttack.addUnits(World.getVillage().selectUnit(soldierType, ex.getCurrent(), theAttack));
                }
            }
            catch (ConsoleException ex)
            {
                theView.showError(ex);
            }
        unitsSelected = true;
    }

    private void putUnits(int soldierType, int count, Point location) throws ConsoleException
    {
        theAttack.putUnits(soldierType, count, location, false);
    }

    private void passTurn()
    {
        theAttack.passTurn();

        if (theAttack.getTurn() >= MAX_ATTACK_TURN)
            quitAttack(Attack.QuitReason.TURN);

        if (theAttack.areBuildingsDestroyed())
            quitAttack(Attack.QuitReason.MAP_DESTROYED);

        if (theAttack.areSoldiersDead())
            quitAttack(Attack.QuitReason.SOLDIERS_DIE);
    }

    private void quitAttack(Attack.QuitReason reason)
    {
        theAttack.quitAttack(reason);
        theView.showAttackEndMessage(reason);
        finished = true;
    }

    private void openMap(Path path, boolean isReal) throws MyJsonException, MyIOException
    {
        try (BufferedReader reader = Files.newBufferedReader(path))
        {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(AttackMap.class, new AttackMapGlobalAdapter())
                    .registerTypeAdapter(Building.class, new BuildingGlobalAdapter())
                    .registerTypeAdapter(GoldStorage.class, new StorageGlobalAdapter<>(GoldStorage.class))
                    .registerTypeAdapter(ElixirStorage.class, new StorageGlobalAdapter<>(ElixirStorage.class))
                    .create();

            AttackMap map = gson.fromJson(reader, AttackMap.class);
            setTheAttack(new Attack(map, isReal));
        }
        catch (JsonParseException ex)
        {
            throw new MyJsonException("There is no valid file in this location.", ex);
        }
        catch (IOException ex)
        {
            throw new MyIOException("There is no valid file in this location.", ex);
        }
    }
}
