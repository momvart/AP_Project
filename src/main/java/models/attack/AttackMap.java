package models.attack;

import models.Map;
import models.Resource;
import models.buildings.Building;
import models.buildings.DefensiveTower;
import models.buildings.Trap;
import utils.MySortedList;
import utils.Point;
import utils.Size;

import java.util.List;

public class AttackMap extends Map
{
    private MySortedList<Long, DefensiveTower> defensiveTowers;

    public AttackMap(Size size)
    {
        super(size);
        defensiveTowers = new MySortedList<>(DefensiveTower::getId);
    }

    public AttackMap(Size size, Resource resource)
    {
        this(size);
        this.resources.setGold(resource.getGold());
        this.resources.setElixir(resource.getElixir());
    }

    @Override
    public void addBuilding(Building building)
    {
        if (building.getBuildingNum() == 0)
            building.setBuildingNum(getBuildings(building.getType()).size() + 1);
        super.addBuilding(building);
        if (building instanceof DefensiveTower)
            defensiveTowers.addValue((DefensiveTower)building);
    }

    public List<DefensiveTower> getAllDefensiveTowers()
    {
        return defensiveTowers.getValues();
    }

    public List<DefensiveTower> getDefensiveTowers(int towerType)
    {
        return defensiveTowers.getRange((long)towerType << Integer.SIZE, (e1, e2) ->
        {
            if (e1 >>> Integer.SIZE == e2 >>> Integer.SIZE)
                return 0;
            return e1.compareTo(e2);
        }).getValues();
    }


    public boolean isEmptyOrDestroyed(int x, int y)
    {
        if (!isValid(x, y))
            return false;
        if (isEmpty(x, y))
            return true;
        else if (getBuildingAt(x, y).getAttackHelper().isDestroyed() || getBuildingAt(x, y).getType() == Trap.BUILDING_TYPE)
            return true;
        return false;
    }
}
