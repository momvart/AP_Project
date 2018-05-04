package controllers;

import exceptions.*;
import menus.*;
import models.*;
import models.soldiers.*;
import utils.*;
import views.AttackView;
import views.VillageView;
import models.buildings.*;
import views.dialogs.*;

import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class VillageController implements IMenuClickListener, ICommandManager
{
    private VillageView theView;
    private Village theVillage;

    public VillageController(VillageView theView)
    {
        this.theVillage = World.sCurrentGame.getVillage();

        this.theView = theView;
        theView.addClickListener(this);
        childCommandManager = theView;
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
            if (command.equalsIgnoreCase("resources"))
                theView.showResources();
            else if ((m = ConsoleUtilities.getMatchedCommand("turn\\s+(\\d+)", command)) != null)
            {
                int count = Integer.parseInt(m.group(1));
                for (int i = 0; i < count; i++)
                    World.passTurn();
            }
            else if (command.equalsIgnoreCase("attack"))
            {
                AttackController controller = new AttackController(new AttackView(theView.getScanner()));
                childCommandManager = controller;
                controller.start(World.sSettings.getAttackMapPaths().stream().map(Paths::get).collect(Collectors.toList()));
            }
            else
                throw ex;
        }
    }

    @Override
    public void onItemClicked(Menu menu)
    {
        try
        {
            switch (menu.getId())
            {
                case Menu.Id.UPGRADE_COMMAND:
                {
                    Building building = ((IBuildingMenu)theView.getCurrentMenu()).getBuilding();
                    upgradeBuilding(building);
                }
                break;
                case Menu.Id.TH_AVAILABLE_BUILDING_ITEM:
                {
                    //TODO: construct method should check for location to be empty
                    BuildingInfo info = ((AvailableBuildingItem)menu).getBuildingInfo();
                    constructBuilding(info);
                }
                break;
                case Menu.Id.BARRACKS_TRAIN_ITEM:
                {
                    TrainSoldierItem item = (TrainSoldierItem)menu;

                    if (item.getAvailableCount() < 0)
                        throw new SoldierUnavailableException(SoldierValues.getSoldierInfo(item.getSoldierType()));

                    trainSoldier(item);
                }
                break;
                case Menu.Id.MINE_MINE:
                {
                    mineMine((Mine)((IBuildingMenu)theView.getCurrentMenu()).getBuilding());
                }
                break;
            }
        }
        catch (ConsoleException ex)
        {
            theView.showError(ex);
        }
        catch (ConsoleRuntimeException ex)
        {
            theView.showError(ex);
        }
    }

    private void trainSoldier(TrainSoldierItem item) throws NotEnoughResourceException, BuildingInConstructionException
    {
        Barracks barracks = (Barracks)((TrainSoldierSubmenu)theView.getCurrentMenu()).getBuilding();
        if (barracks.getBuildStatus() == BuildStatus.IN_CONSTRUCTION)
            throw new BuildingInConstructionException(barracks);
        DialogResult result = theView.showSoldierTrainCountDialog();
        if (result.getResultCode() != DialogResultCode.YES)
            return;

        barracks.trainSoldier(item.getSoldierType(), (int)result.getData(NumberInputDialog.KEY_NUMBER));
    }

    private void constructBuilding(BuildingInfo info) throws NotEnoughResourceException, NoAvailableBuilderException
    {
        if (theView.showConstructDialog(info.getName(), info.getBuildCost()).getResultCode() != DialogResultCode.YES)
            return;
        Point location;
        while (true)
        {
            DialogResult mapResult = theView.showConstructionMapDialog(info.getName(), theVillage.getMap());
            if (mapResult.getResultCode() == DialogResultCode.CANCEL)
                return;

            Matcher m = (Matcher)mapResult.getData(TextInputDialog.KEY_MATCHER);
            location = new Point(Integer.parseInt(m.group("x")) - 1, Integer.parseInt(m.group("y")) - 1);
            if (theVillage.getMap().isEmptyForBuilding(location))
                break;

            theView.showText("You can't build this building here. Please choose another cell.");
        }
        theVillage.construct(info.getType(), location);
    }

    private void upgradeBuilding(Building building) throws ConsoleException, ConsoleRuntimeException
    {
        if (building.getBuildStatus() == BuildStatus.IN_CONSTRUCTION)
            throw new BuildingInConstructionException(building);
        Resource cost = building.getBuildingInfo().getUpgradeCost();
        if (theView.showUpgradeDialog(building.getName(), cost).getResultCode() != DialogResultCode.YES)
            return;

        World.getVillage().upgradeBuilding(building);
    }

    private void mineMine(Mine mine) throws BuildingInConstructionException
    {
        if (mine.getBuildStatus() == BuildStatus.IN_CONSTRUCTION)
            throw new BuildingInConstructionException(mine);
        mine.mine();
    }
}
