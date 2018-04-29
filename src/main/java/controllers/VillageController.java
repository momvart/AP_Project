package controllers;

import exceptions.*;
import menus.*;
import models.*;
import models.soldiers.*;
import utils.*;
import views.VillageView;
import models.buildings.*;
import views.dialogs.*;

import java.util.regex.Matcher;

public class VillageController implements IMenuClickListener, ICommandManager
{
    private VillageView theView;
    private Village theVillage;

    public VillageController(VillageView theView)
    {
        this.theVillage = World.sCurrentGame.getVillage();

        this.theView = theView;
        theView.addClickListener(this);
        ParentMenu mainMenu = new ParentMenu(Menu.Id.VILLAGE_MAIN_MENU, "", MenuTextCommandHandler.getInstance());
        mainMenu.insertItem(new ShowBuildingsMenu(mainMenu))
                .insertItem(Menu.Id.VILLAGE_RESOURCES, "resources");
        theView.setCurrentMenu(mainMenu, false);
    }

    @Override
    public void manageCommand(String command) throws InvalidCommandException
    {
        try
        {
            theView.manageCommand(command);
        }
        catch (InvalidCommandException ex)
        {
            Matcher m = null;
            if ((m = ConsoleUtilities.getMatchedCommand("turn\\s+(\\d+)", command)) != null)
            {
                int count = Integer.parseInt(m.group(1));
                for (int i = 0; i < count; i++)
                    World.passTurn();
            }
            else
                throw ex;
        }
    }

    @Override
    public void onItemClicked(Menu menu)
    {
        switch (menu.getId())
        {
            case Menu.Id.UPGRADE_COMMAND:
            {
                Building building = ((IBuildingMenu)theView.getCurrentMenu()).getBuilding();
                Resource cost = building.getBuildingInfo().getUpgradeCost();
                if (theView.showUpgradeDialog(building.getName(), cost).getResultCode() != DialogResultCode.YES)
                    break;
                try
                {
                    World.getVillage().upgradeBuilding(building);
                }
                catch (ConsoleException ex)
                {
                    theView.showError(ex);
                }
            }
            break;
            case Menu.Id.TH_AVAILABLE_BUILDING_ITEM:
            {
                //TODO: construct method should check for location to be empty
                //TODO: when building is in construction map should show its location as not empty.
                try
                {
                    BuildingInfo info = ((AvailableBuildingItem)menu).getBuildingInfo();
                    if (theView.showConstructDialog(info.getName(), info.getBuildCost()).getResultCode() != DialogResultCode.YES)
                    {break;}
                    Point location = null;
                    while (true)
                    {
                        DialogResult mapResult = theView.showConstructionMapDialog(info.getName(), theVillage.getMap());
                        if (mapResult.getResultCode() == DialogResultCode.CANCEL)
                            return;

                        Matcher m = (Matcher)mapResult.getData(TextInputDialog.KEY_MATCHER);
                        location = new Point(Integer.parseInt(m.group("x")) - 1, Integer.parseInt(m.group("y")) - 1);
                        if (theVillage.getMap().isEmpty(location.getX(), location.getY()))
                            break;

                        theView.showText("You can't build this building here. Please choose another cell.");
                    }
                    theVillage.construct(info.getType(), location);
                }
                catch (ConsoleException ex)
                {
                    theView.showError(ex);
                }
            }
            break;
            case Menu.Id.BARRACKS_TRAIN_ITEM:
            {
                TrainSoldierItem item = (TrainSoldierItem)menu;
                try
                {
                    if (item.getAvailableCount() < 0)
                        throw new SoldierUnavailableException(SoldierValues.getSoldierInfo(item.getSoldierType()));
                    DialogResult result = theView.showSoldierTrainCountDialog();
                    if (result.getResultCode() != DialogResultCode.YES)
                        break;
                    ((Barracks)((TrainSoldierSubmenu)theView.getCurrentMenu()).getBuilding()).trainSoldier(item.getSoldierType(), (int)result.getData(NumberInputDialog.KEY_NUMBER));
                }
                catch (ConsoleException ex)
                {
                    theView.showError(ex);
                }
            }
            break;
        }
    }
}
