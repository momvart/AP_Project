package views;

import exceptions.InvalidCommandException;
import models.*;
import models.Map;
import models.buildings.*;
import models.soldiers.*;
import menus.*;
import views.dialogs.*;

import java.util.*;

public class VillageView extends ConsoleMenuContainerView
{
    private Village village;

    public VillageView(Scanner scanner)
    {
        super(scanner);
        this.village = World.sCurrentGame.getVillage();
    }

    @Override
    protected void handleCommand(String command) throws InvalidCommandException
    {
        switch (command.toLowerCase())
        {
            case "whereami":
                showWhereAmI();
                break;
            default:
                super.handleCommand(command);
        }
    }

    @Override
    public void onMenuItemClicked(Menu menu)
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
            default:
                super.onMenuItemClicked(menu);
        }
    }

    public void showWhereAmI()
    {
        //TODO: check if has more possibilities.
        if (currentMenu instanceof IBuildingMenu)
            System.out.printf("Your are in %s %d\n",
                    ((IBuildingMenu)currentMenu).getBuilding().getName(),
                    ((IBuildingMenu)currentMenu).getBuilding().getBuildingNum());
        else if (currentMenu.getId() == Menu.Id.VILLAGE_MAIN_MENU) //Main menu
            System.out.println("You are in village.");
    }

    public void showResources()
    {
        Resource resource = village.getResources();
        System.out.println("Gold: " + resource.getGold());
        System.out.println("Elixir: " + resource.getElixir());
        System.out.println("Score: " + World.sCurrentGame.getScore());
    }

    public void showBuildingOverallInfo(Building building)
    {
        System.out.println("Level: " + building.getLevel());
        System.out.println("Health: " + building.getStrength());
    }

    public void showUpgradeInfo(Building building)
    {
        System.out.println("Upgrade Cost: " + building.getBuildingInfo().getBuildCost().toString(false));
    }

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
                System.out.print(map.isValid(j, i));
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

    public void showConstructionsStatus(List<Construction> constructions)
    {
        constructions.stream().sorted(Comparator.comparing(c -> c.getBuildingInfo().getName()))
                .forEach(System.out::println);
    }

    public DialogResult showSoldierTrainCountDialog()
    {
        return new NumberInputDialog(scanner, "How many of this soldier do you want to build?").showDialog();
    }

    public void showSoldierTrainingsStatus(Queue<Recruit> recruits)
    {
        //TODO: needs to be sorted
        for (Recruit r : recruits)
            System.out.println(r.toString());
    }

    public void showCampsCapacityInfo(Camp camp)
    {
        System.out.printf("Your camp capacity is %d / %d\n", village.getSoldiers().size(), camp.getCapacity());
    }

    public void showAvailableSoldiers()
    {
        int[] counts = new int[SoldierValues.SOLDIER_TYPES_COUNT];
        for (Soldier soldier : village.getSoldiers())
            counts[soldier.getType() - 1]++;
        for (int i = 0; i < counts.length; i++)
            if (counts[i] > 0)
                System.out.printf("%s x%d\n", SoldierValues.getSoldierInfo(i + 1), counts[i]);
    }

    public void showStorageSourceInfo(Storage storage)
    {
        System.out.printf("Your %s storage is %d / %d",
                storage instanceof GoldStorage ? "gold" : "elixir",
                storage.getCapacity() - storage.getFreeCapacity(),
                storage.getCapacity());
    }

    public void showAttackInfo(DefensiveTower tower)
    {
        //TODO: what's target?
        System.out.println("Target: ");
        System.out.println("Damage: " + tower.getAttackPower());
        System.out.println("Damage Range: " + tower.getRange());
    }
}
