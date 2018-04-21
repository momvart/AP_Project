package controllers;

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
g                Building building = ((IBuildingMenu)menu).getBuilding();
                Resource cost = building.getBuildingInfo().getUpgradeCost();
                if (theView.showUpgradeDialog(building.getName(), cost).getResultCode() != DialogResultCode.YES)
                    break;
                if (World.sCurrentGame.getVillage().getResources().isGreaterThanOrEqual(cost))
                    //TODO: call upgrade method.
                    ;
            }
            break;
        }
    }
}
