package views;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import exceptions.*;
import graphics.gui.AttackStage;
import graphics.gui.VillageStage;
import javafx.application.Platform;
import menus.*;
import models.Map;
import models.Resource;
import models.Village;
import models.World;
import models.attack.Attack;
import models.attack.AttackMap;
import models.buildings.*;
import models.soldiers.Recruit;
import models.soldiers.SoldierValues;
import serialization.AttackMapGlobalAdapter;
import serialization.BuildingGlobalAdapter;
import serialization.StorageGlobalAdapter;
import utils.Point;
import views.dialogs.DialogResult;
import views.dialogs.DialogResultCode;
import views.dialogs.NumberInputDialog;
import views.dialogs.TextInputDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;

public class VillageView extends ConsoleMenuContainerView
{
    private Village village;
    private VillageStage villageStage;

    private Attack theAttack;

    public VillageView(Scanner scanner)
    {
        super(scanner);
        this.village = World.sCurrentGame.getVillage();
        initialize();
    }

    private void initialize()
    {
        ParentMenu mainMenu = new ParentMenu(Menu.Id.VILLAGE_MAIN_MENU, "", MenuTextCommandHandler.getInstance());
        mainMenu.insertItem(new ShowBuildingsMenu(mainMenu))
                .insertItem(Menu.Id.VILLAGE_RESOURCES, "resources");
        setCurrentMenu(mainMenu, false);

        Platform.runLater(() ->
        {
            villageStage = new VillageStage(village, 1200, 900);
            villageStage.setVillageView(this);
            villageStage.setUpAndShow();
            village.setVillageStage(villageStage);
        });
    }

    @Override
    public void manageCommand(String command) throws InvalidCommandException
    {
        switch (command.toLowerCase())
        {
            case "whereami":
                showWhereAmI();
                break;
            default:
                super.manageCommand(command);
        }
    }


    public void onItemClicked(Menu menu)
    {
        try
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
                case Menu.Id.UPGRADE_COMMAND:
                {
                    Building building = ((IBuildingMenu)getCurrentMenu()).getBuilding();
                    upgradeBuilding(building);
                }
                break;
                case Menu.Id.TH_AVAILABLE_BUILDING_ITEM:
                {
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
                    mineMine((Mine)((IBuildingMenu)getCurrentMenu()).getBuilding());
                }
                break;
                case Menu.Id.ATTACK_LOAD_MAP:
                {
                    DialogResult result = villageStage.showOpenMapDialog("Enter path please");
                    if (result.getResultCode() != DialogResultCode.YES)
                        break;
                    String path = (String)result.getData(TextInputDialog.KEY_TEXT);
                    World.sSettings.getAttackMapPaths().add(path);
                    World.saveSettings();
                    villageStage.onBtnAttackClick(null, null);
                }
                break;
                case Menu.Id.ATTACK_LOAD_MAP_ITEM:
                    openMap(((AttackMapItem)menu).getFilePath(), true);
                    break;
                case Menu.Id.ATTACK_MAP_ATTACK:
                    Platform.runLater(() -> new AttackStage(theAttack, 1200, 900).setUpAndShow());
                    break;
                case Menu.Id.ATTACK_MAP_INFO:
                    showMapInfo(theAttack.getMap());

                    break;
                default:
                    super.onItemClicked(menu);
            }
        }
        catch (ConsoleException ex)
        {
            showError(ex);
        }
    }

    @Override
    public void setCurrentMenu(ParentMenu menu, boolean showNow)
    {
        super.setCurrentMenu(menu, showNow);
        if (getCurrentMenu().getId() == Menu.Id.VILLAGE_MAIN_MENU)
            showWhereAmI();
    }

    public void showWhereAmI()
    {
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
        StringBuilder s = new StringBuilder();
        s.append("Gold: ").append(resource.getGold()).append("\n");
        s.append("Elixir: ").append(resource.getElixir()).append("\n");
        s.append("Score: ").append(World.sCurrentGame.getScore());
        villageStage.showInfo(s.toString());
    }

    public void showBuildingOverallInfo(Building building)
    {
        String s = String.format("Level: %d\nHealth: %d", building.getLevel(), building.getStrength());
        villageStage.showInfo(s);
    }

    public void showUpgradeInfo(Building building)
    {
        String s = String.format("Upgrade Cost: %s", building.getBuildingInfo().getBuildCost().toString(false));
        villageStage.showInfo(s);
    }


    public DialogResult showConstructDialog(String buildingName, Resource cost)
    {
        return villageStage.showSingleChoiceDialog(String.format("Do you want to build %s for %s?", buildingName, cost.toString(false)));
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
        return villageStage.showMapInputDialog(String.format("Where do you want to build %s", buildingName), map);
    }

    public DialogResult showUpgradeDialog(String buildingName, Resource cost)
    {
        return villageStage.showSingleChoiceDialog(String.format("Do you want to upgrade %s for %s?",
                buildingName,
                cost.toString(false)));
    }

    public void showConstructionsStatus(List<Construction> constructions)
    {
        StringBuilder s = new StringBuilder();
        constructions.stream()
                .sorted(Comparator.comparing(c -> c.getBuildingInfo().getName()))
                .forEach(c -> s.append(String.format("%s %d\n", c.getBuildingInfo().getName(), c.getRemainingTurns())));
        villageStage.showInfo(s.toString());
    }

    public DialogResult showSoldierTrainCountDialog(int availableCount)
    {
        return villageStage.showNumberInputDialog("How many of this soldier do you want to build?", availableCount);
    }

    public void showSoldierTrainingsStatus(Iterable<Recruit> recruits)
    {
        StringBuilder s = new StringBuilder();
        HashMap<String, Integer> hashMap = new HashMap<>();
        recruits.forEach(recruit ->
        {
            if (!hashMap.containsKey(recruit.getSoldierInfo().getName()))
                hashMap.put(recruit.getSoldierInfo().getName(), 1);
            else
                hashMap.put(recruit.getSoldierInfo().getName(), hashMap.get(recruit.getSoldierInfo().getName()) + 1);
        });
        hashMap.keySet().forEach(s1 ->
        {
            s.append(s1).append(" x").append(hashMap.get(s1).toString()).append("\n");
        });
        villageStage.showInfo(s.toString());
    }

    public void showCampsCapacityInfo()
    {
        String s = String.format("Your camp capacity is %d / %d\n", village.getAllSoldiers().count(), village.getCampsCapacity());
        villageStage.showInfo(s);
    }

    public void showAvailableSoldiers()
    {
        StringBuilder s = new StringBuilder();
        for (int i = 1; i <= SoldierValues.SOLDIER_TYPES_COUNT; i++)
            if (village.getSoldiers(i).size() > 0)
                s.append(String.format("%s x%d\n", SoldierValues.getSoldierInfo(i).getName(), village.getSoldiers(i).size()));
        villageStage.showInfo(s.toString());
    }

    public void showStorageSourceInfo(Storage storage)
    {
        if (storage instanceof GoldStorage)
            villageStage.showInfo(String.format("Your %s storage is %d / %d loaded.\n", "gold",
                    village.getResources().getGold(),
                    village.getTotalResourceCapacity().getGold()));
        else
            villageStage.showInfo(String.format("Your %s storage is %d / %d loaded.\n", "elixir",
                    village.getResources().getElixir(),
                    village.getTotalResourceCapacity().getElixir()));
    }

    public void showAttackInfo(DefensiveTower tower)
    {
        StringBuilder s = new StringBuilder();
        s.append("Target: ").append(tower.getDefenseType().getPrintName()).append("\n");
        s.append("Damage: ").append(tower.getDamagePower()).append("\n");
        s.append("Damage Range: ").append(tower.getRange());
        villageStage.showInfo(s.toString());
    }

    @Override
    public void showError(ConsoleException ex)
    {
        super.showError(ex);
        villageStage.showInfo(ex.getMessage());
    }

    @Override
    public void showError(ConsoleRuntimeException ex)
    {
        super.showError(ex);
        villageStage.showInfo(ex.getDatailedMessage());
    }


    private void showMapInfo(AttackMap map)
    {
        StringBuilder info = new StringBuilder("Resources: ").append("\n").append(map.getResources()).append("\n");
        HashMap<String, Integer> defensiveTowers = new HashMap<>();
        map.getAllDefensiveTowers().forEach(defensiveTower ->
        {
            if (!defensiveTowers.containsKey(defensiveTower.getName()))
                defensiveTowers.put(defensiveTower.getName(), 1);
            else
                defensiveTowers.replace(defensiveTower.getName(), defensiveTowers.get(defensiveTower.getName()) + 1);
        });
        if (defensiveTowers.size() != 0)
            info.append("Defensive towers:").append("\n");
        defensiveTowers.forEach((defensiveTower, integer) ->
        {
            info.append(defensiveTower).append(" x").append(integer.toString()).append("\n");
        });
        villageStage.showInfo(info.toString());
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
            theAttack = new Attack(map, isReal);
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

    private void trainSoldier(TrainSoldierItem item) throws NotEnoughResourceException, BuildingInConstructionException
    {
        Barracks barracks = (Barracks)((TrainSoldierSubmenu)getCurrentMenu()).getBuilding();
        if (barracks.getBuildStatus() == BuildStatus.IN_CONSTRUCTION)
            throw new BuildingInConstructionException(barracks);
        DialogResult result = showSoldierTrainCountDialog(item.getAvailableCount());
        if (result.getResultCode() != DialogResultCode.YES)
            return;

        barracks.trainSoldier(item.getSoldierType(), (int)result.getData(NumberInputDialog.KEY_NUMBER));
    }

    private void constructBuilding(BuildingInfo info) throws NotEnoughResourceException, NoAvailableBuilderException
    {
        if (showConstructDialog(info.getName(), info.getBuildCost()).getResultCode() != DialogResultCode.YES)
            return;
        Point location;
        while (true)
        {
            DialogResult mapResult = showConstructionMapDialog(info.getName(), village.getMap());
            if (mapResult.getResultCode() == DialogResultCode.CANCEL)
                return;

            Matcher m = (Matcher)mapResult.getData(TextInputDialog.KEY_MATCHER);
            location = new Point(Integer.parseInt(m.group("x")) - 1, Integer.parseInt(m.group("y")) - 1);
            if (village.getMap().isEmptyForBuilding(location))
                break;

            showText("You can't build this building here. Please choose another cell.");
        }
        village.construct(info.getType(), location);
    }

    private void upgradeBuilding(Building building) throws ConsoleException, ConsoleRuntimeException
    {
        if (building.getBuildStatus() == BuildStatus.IN_CONSTRUCTION)
            throw new BuildingInConstructionException(building);
        Resource cost = building.getBuildingInfo().getUpgradeCost();
        if (showUpgradeDialog(building.getName(), cost).getResultCode() != DialogResultCode.YES)
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
