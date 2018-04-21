package controllers;

import exceptions.ConsoleException;
import menus.*;
import models.Village;
import models.World;
import views.VillageView;
import models.*;
import models.buildings.*;
import views.dialogs.DialogResultCode;

public class VillageController implements IMenuClickListener
{
    private VillageView theView;
    private Village theVillage;
    private MainController parentController;

    public VillageController(MainController parentController, VillageView theView)
    {
        this.parentController = parentController;
        this.theView = theView;
        theView.addClickListener(this);
        this.theVillage = World.sCurrentGame.getVillage();
    }

    public void start()
    {
        ParentMenu mainMenu = new ParentMenu(Menu.Id.VILLAGE_MAIN_MENU, "", MenuTextCommandHandler.getInstance());
        mainMenu.insertItem(new ShowBuildingsMenu(mainMenu))
                .insertItem(Menu.Id.VILLAGE_RESOURCES, "resources");
        theView.setCurrentMenu(mainMenu, false);
        theView.startGetting();
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
        }
    }
}
