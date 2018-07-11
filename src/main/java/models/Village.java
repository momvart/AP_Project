package models;

import exceptions.*;
import graphics.gui.VillageStage;
import graphics.helpers.VillageBuildingGraphicHelper;
import models.attack.Attack;
import models.buildings.*;
import models.soldiers.Soldier;
import models.soldiers.SoldierCollection;
import utils.Point;
import utils.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Village
{
    private Map map;
    private ConstructionManager constructionManager;
    private VillageStatus villageStatus = VillageStatus.NORMAL;
    private int turn = 0;
    private SoldierCollection soldiers;
    private transient VillageStage villageStage;

    public void initialize()
    {
        map = new Map(new Size(30, 30));
        map.initialize();
        constructionManager = new ConstructionManager();
        soldiers = new SoldierCollection();
    }

    public VillageStatus getVillageStatus()
    {
        return villageStatus;
    }

    public void setVillageStatus(VillageStatus villageStatus)
    {
        this.villageStatus = villageStatus;
    }

    public void setVillageStage(VillageStage villageStage)
    {
        this.villageStage = villageStage;
    }

    public Map getMap()
    {
        return map;
    }

    public ArrayList<Building> getBuildings()
    {
        return map.getBuildings();
    }

    ///region Resources
    public Resource getResources()
    {
        return map.getResources();
    }

    public Resource getTotalResourceCapacity()
    {
        int gold = 0, elixir = 0;
        for (Building storage : map.getBuildings(GoldStorage.BUILDING_TYPE))
            gold += ((GoldStorage)storage).getCapacity();
        for (Building storage : map.getBuildings(ElixirStorage.BUILDING_TYPE))
            elixir += ((ElixirStorage)storage).getCapacity();
        return new Resource(gold, elixir);
    }

    public Resource getResourceFreeCapacity()
    {
        return Resource.subtract(getTotalResourceCapacity(), getResources());
    }

    public void spendResource(Resource toSpend) throws NotEnoughResourceException
    {
        try
        {
            getResources().decrease(toSpend);
        }
        catch (IllegalArgumentException ex)
        {
            throw new NotEnoughResourceException(getResources(), toSpend);
        }
    }

    public boolean spendResource(Resource toSpend, boolean forced)
    {
        try
        {
            getResources().decrease(toSpend);
            return true;
        }
        catch (IllegalArgumentException ex)
        {
            if (forced)
            {
                getResources().decrease(getResources());
                return true;
            }
            else return false;
        }
    }

    /**
     * Adds a certain amount of resource to the village.
     * If there is not enough space the remaining will be ignored.
     *
     * @param toAdd
     */
    public Resource addResource(Resource toAdd)
    {
        Resource free = getResourceFreeCapacity();
        Resource added = new Resource(Math.min(free.getGold(), toAdd.getGold()), Math.min(free.getElixir(), toAdd.getElixir()));
        getResources().increase(added);
        return added;
    }
    ///endregion

    ///region Constructions

    public ConstructionManager getConstructionManager()
    {
        return constructionManager;
    }

    public Builder getAvailableBuilder() throws NoAvailableBuilderException
    {
        return map.getTownHall().getAvailableBuilder();
    }

    public void construct(int buildingType, Point location) throws NotEnoughResourceException, NoAvailableBuilderException
    {
        Resource cost = BuildingValues.getBuildingInfo(buildingType).getBuildCost();
        Resource available = getResources();
        if (available.isLessThan(cost))
            throw new NotEnoughResourceException(available, cost);

        Building building = constructionManager.construct(buildingType, location);
        VillageBuildingGraphicHelper graphicHelper = (VillageBuildingGraphicHelper)villageStage.addBuilding(building);
        constructionManager.getLastConstruction().setFinishListener(graphicHelper);
        graphicHelper.getBuildingDrawer().updateDrawer();

        spendResource(cost);
    }

    public void upgradeBuilding(Building building) throws NotEnoughResourceException, NoAvailableBuilderException, UnavailableUpgradeException
    {
        Resource cost = BuildingValues.getBuildingInfo(building.getType()).getBuildCost();
        Resource available = getResources();
        if (available.isLessThan(cost))
            throw new NotEnoughResourceException(available, cost);

        if (building.getLevel() == getMap().getTownHall().getLevel() && building.getType() != TownHall.BUILDING_TYPE)
            throw new UnavailableUpgradeException(building, UnavailableUpgradeException.Reason.REACHED_TH);
        if (building.getType() == Camp.BUILDING_TYPE)
            throw new UnavailableUpgradeException(building, UnavailableUpgradeException.Reason.IMPOSSIBLE);

        VillageBuildingGraphicHelper graphicHelper = (VillageBuildingGraphicHelper)building.getVillageHelper().getGraphicHelper();
        constructionManager.upgrade(building);
        constructionManager.getLastConstruction().setFinishListener(graphicHelper);
        graphicHelper.getBuildingDrawer().updateDrawer();

        spendResource(cost);
    }

    ///endregion

    //region Turns
    public int getTurn()
    {
        return turn;
    }

    public void passTurn()
    {
        if (villageStatus.equals(VillageStatus.NORMAL))
        {
            turn++;
            getMap().forEachBuilding(Mine.class, Mine::passTurn);
            getMap().forEachBuilding(Barracks.class, Barracks::passTurn);
            getMap().getTownHall().passTurn();
        }
    }
    //endregion

    //region Soldiers
    public void setupSoldiers(Iterable<Soldier> soldierList)
    {
        soldiers = new SoldierCollection();
        soldierList.forEach(this::addSoldier);
    }

    public void addSoldier(Soldier soldier) throws NotEnoughCampCapacityException
    {
        if (getCampsCapacity() <= getSoldiersCount())
            throw new NotEnoughCampCapacityException();
        soldiers.addSoldier(soldier);
    }

    public SoldierCollection getSoldiers()
    {
        return soldiers;
    }

    public ArrayList<Soldier> getSoldiers(int soldierType)
    {
        return soldiers.getSoldiers(soldierType);
    }

    public Stream<Soldier> getAllSoldiers()
    {
        return soldiers.getAllSoldiers();
    }

    public List<Soldier> selectUnit(int soldierType, int count, Attack attack) throws NotEnoughSoldierException
    {
        List<Soldier> freeSoldiers = getSoldiers(soldierType).stream()
                .filter(soldier -> !soldier.isParticipating(attack))
                .limit(count).collect(Collectors.toList());

        if (count > freeSoldiers.size())
            throw new NotEnoughSoldierException(soldierType, freeSoldiers.size(), count);
        return freeSoldiers;
    }

    public int getCampsCapacity()
    {
        int sum = 0;
        for (Building camp : getMap().getBuildings(Camp.BUILDING_TYPE).getValues())
            sum += ((Camp)camp).getCapacity();
        return sum;
    }

    public int getSoldiersCount()
    {
        return (int)getAllSoldiers().count();
    }
    //endregion
}
