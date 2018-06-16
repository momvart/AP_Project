package graphics.gui;

import graphics.Layer;
import menus.*;
import models.Resource;
import models.Village;
import models.World;
import models.buildings.*;
import models.soldiers.Recruit;
import models.soldiers.SoldierValues;

import java.util.Comparator;
import java.util.List;

public class GraphicVillageView extends GraphicMenuContainerView
{
    private Village village;

    public GraphicVillageView(Layer layer, double width, double height)
    {
        super(layer, width, height);
    }

    private void initialize()
    {
        ParentMenu mainMenu = new ParentMenu(Menu.Id.VILLAGE_MAIN_MENU, "", MenuTextCommandHandler.getInstance());
        mainMenu.insertItem(new ShowBuildingsMenu(mainMenu))
                .insertItem(Menu.Id.VILLAGE_RESOURCES, "resources");
        setCurrentMenu(mainMenu, false);
    }

    @Override
    public void manageCommand(String command)
    {
        switch (command.toLowerCase())
        {
            default:
                super.manageCommand(command);
        }
    }

    @Override
    public void onItemClicked(Menu menu)
    {
        switch (menu.getId())
        {
            case Menu.Id.VILLAGE_RESOURCES:
                showResources();
                break;
            case Menu.Id.OVERALL_INFO:
                showBuildingOverallInfo(((IBuildingMenu)currentMenu).getBuilding());
                break;
            case Menu.Id.UPGRADE_INFO:
                showUpgradeInfo(((IBuildingMenu)currentMenu).getBuilding());
                break;
            case Menu.Id.TH_STATUS:
                showConstructionsStatus(World.sCurrentGame.getVillage().getConstructionManager().getConstructions());
                break;
            case Menu.Id.BARRACKS_STATUS:
                showSoldierTrainingsStatus(((Barracks)((IBuildingMenu)currentMenu).getBuilding()).getTrainingManager().getRecruits());
                break;
            case Menu.Id.CAMP_CAPACITY_INFO:
                showCampsCapacityInfo();
                break;
            case Menu.Id.CAMP_SOLDIERS:
                showAvailableSoldiers();
                break;
            case Menu.Id.STORAGE_SRC_INFO:
                showStorageSourceInfo((Storage)((IBuildingMenu)currentMenu).getBuilding());
                break;
            case Menu.Id.DEFENSIVE_TARGET_INFO:
                showAttackInfo((DefensiveTower)((IBuildingMenu)currentMenu).getBuilding());
                break;
            default:
                super.onItemClicked(menu);
        }
    }

    @Override
    public void setCurrentMenu(ParentMenu menu, boolean showNow)
    {
        super.setCurrentMenu(menu, showNow);
    }

    public void showResources()
    {
        Resource resource = village.getResources();
        StringBuilder s = new StringBuilder();
        s.append("Gold: ").append(resource.getGold()).append("\n");
        s.append("Elixir: ").append(resource.getElixir()).append("\n");
        s.append("Score: ").append(World.sCurrentGame.getScore());
        showInfoBar(s.toString());
    }

    public void showBuildingOverallInfo(Building building)
    {
        String s = String.format("Level: %d\nHealth: %d", building.getLevel(), building.getStrength());
        showInfoBar(s);
    }

    public void showUpgradeInfo(Building building)
    {
        String s = String.format("Upgrade Cost: %s", building.getBuildingInfo().getBuildCost().toString(false));
        showInfoBar(s);
    }

    /*
    public DialogResult showConstructDialog(String buildingName, Resource cost)
    {
        return new SingleChoiceDialog(scanner, String.format("Do you want to build %s for %s?",
                buildingName,
                cost.toString(false))).showDialog();
    }

    public DialogResult showConstructionMapDialog(String buildingName, Map map)
    {
        //Printing current map
        for (int i = 0; i < map.getSize().getHeight(); i++)
        {
            for (int j = 0; j < map.getSize().getWidth(); j++)
                System.out.print(map.isEmptyForBuilding(j, i) ? 0 : 1);
            System.out.print('\n');
        }

        return new TextInputDialog(scanner,
                String.format("Where do you want to build %s?", buildingName),
                "\\((?<x>\\d+),(?<y>\\d+)\\)")
                .showDialog();
    }

    public DialogResult showUpgradeDialog(String buildingName, Resource cost)
    {
        return new SingleChoiceDialog(scanner, String.format("Do you want to upgrade %s for %s?",
                buildingName,
                cost.toString(false))).showDialog();
    }
    */
    public void showConstructionsStatus(List<Construction> constructions)
    {
        StringBuilder s = new StringBuilder();
        constructions.stream()
                .sorted(Comparator.comparing(c -> c.getBuildingInfo().getName()))
                .forEach(c -> s.append(String.format("%s %d\n", c.getBuildingInfo().getName(), c.getRemainingTurns())));
        showInfoBar(s.toString());
    }

    /*
    public DialogResult showSoldierTrainCountDialog()
    {
        return new NumberInputDialog(scanner, "How many of this soldier do you want to build?").showDialog();
    }
    */
    public void showSoldierTrainingsStatus(Iterable<Recruit> recruits)
    {
        StringBuilder s = new StringBuilder();
        for (Recruit r : recruits)
            s.append(r.toString()).append("\n");
        showInfoBar(s.toString());
    }

    public void showCampsCapacityInfo()
    {
        String s = String.format("Your camp capacity is %d / %d\n", village.getAllSoldiers().count(), village.getCampsCapacity());
        showInfoBar(s);
    }

    public void showAvailableSoldiers()
    {
        StringBuilder s = new StringBuilder();
        for (int i = 1; i <= SoldierValues.SOLDIER_TYPES_COUNT; i++)
            if (village.getSoldiers(i).size() > 0)
                s.append(String.format("%s x%d\n", SoldierValues.getSoldierInfo(i).getName(), village.getSoldiers(i).size()));
        showInfoBar(s.toString());
    }

    public void showStorageSourceInfo(Storage storage)
    {
        if (storage instanceof GoldStorage)
            showInfoBar(String.format("Your %s storage is %d / %d loaded.\n", "gold",
                    village.getResources().getGold(),
                    village.getTotalResourceCapacity().getGold()));
        else
            showInfoBar(String.format("Your %s storage is %d / %d loaded.\n", "elixir",
                    village.getResources().getElixir(),
                    village.getTotalResourceCapacity().getElixir()));
    }

    public void showAttackInfo(DefensiveTower tower)
    {
        StringBuilder s = new StringBuilder();
        s.append("Target: ").append(tower.getDefenseType().getPrintName()).append("\n");
        s.append("Damage: ").append(tower.getDamagePower()).append("\n");
        s.append("Damage Range: ").append(tower.getRange());
        showInfoBar(s.toString());
    }

}
