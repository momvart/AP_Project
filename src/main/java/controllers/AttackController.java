package controllers;

import com.google.gson.*;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import exceptions.*;
import menus.*;
import models.Attack;
import models.AttackMap;
import models.World;
import models.buildings.Building;
import models.soldiers.SoldierValues;
import serialization.AttackMapGlobalAdapter;
import serialization.BuildingGlobalAdapter;
import utils.ConsoleUtilities;
import utils.ICommandManager;
import utils.Point;
import views.AttackView;
import views.dialogs.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Matcher;

public class AttackController implements IMenuClickListener, ICommandManager
{
    private static final String SELECT_UNIT_PATTERN = "select\\s+(?<type>\\w+)\\s+(?<count>\\d+)";
    private static final String PUT_UNIT_PATTERN = "put\\s+(?<type>\\w+)\\s+(?<count>\\d+)\\s+in\\s+(?<x>\\d+)\\s*,\\s*(?<y>\\d+)";

    private AttackView theView;
    private Attack theAttack;

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
                    openMap(Paths.get(path));
                    World.sSettings.getAttackMapPaths().add(path);
                    World.saveSettings();
                    theView.setCurrentMenu(new AttackMapItem(theView.getCurrentMenu(), Paths.get(path)), true);
                }
                break;
                case Menu.Id.ATTACK_LOAD_MAP_ITEM:
                {
                    openMap(((AttackMapItem)menu).getFilePath());
                }
                break;
                case Menu.Id.ATTACK_MAIN_BACK:
                    quitAttack();
                    break;
                case Menu.Id.ATTACK_MAP_ATTACK:
                {
                    //theAttack = new Attack();
                    theView.setCurrentMenu(null, false);
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
                theAttack.passTurn();
            else if ((m = ConsoleUtilities.getMatchedCommand("turn\\s+(\\d+)", command)) != null)
            {
                int count = Integer.parseInt(m.group(1));
                for (int i = 0; i < count; i++)
                    theAttack.passTurn();
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
                quitAttack();
            else if (command.equalsIgnoreCase("showmap"))
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
        theAttack.putUnits(soldierType, count, location);
    }

    private void quitAttack()
    {
        theAttack.quitAttack();
        theView.showAttackEndMessage();
    }

    private void openMap(Path path) throws MyJsonException, MyIOException
    {
        try (BufferedReader reader = Files.newBufferedReader(path))
        {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(AttackMap.class, new AttackMapGlobalAdapter())
                    .registerTypeAdapter(Building.class, new BuildingGlobalAdapter())
                    .create();

            AttackMap map = gson.fromJson(reader, AttackMap.class);
            setTheAttack(new Attack(map));
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
