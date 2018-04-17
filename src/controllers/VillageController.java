package controllers;

import menus.*;
import models.Village;
import models.World;
import views.VillageView;
import models.*;
import models.buildings.*;

public class VillageController
{
    private VillageView theView;
    private Village theVillage;
    private MainController parentController;

    public VillageController(MainController parentController, VillageView theView)
    {
        this.parentController = parentController;
        this.theView = theView;
        this.theVillage = World.sCurrentGame.getVillage();
    }

    public void start()
    {
        ParentMenu mainMenu = new ParentMenu(Menu.Id.VILLAGE_MAIN_MENU, "", new MenuTextCommandHandler())
                .insertItem(new ShowBuildingsMenu())
                .insertItem(Menu.Id.VILLAGE_RESOURCES, "resources");
        theView.setCurrentMenu(mainMenu, false);
        theView.startGetting();
    }
}
