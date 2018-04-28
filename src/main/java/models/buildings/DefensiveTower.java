package models.buildings;

import menus.BuildingInfoSubmenu;
import menus.Menu;
import models.Attack;
import utils.Point;

public abstract class DefensiveTower extends Building
{
    private int attackPower;
    private int range;

    public DefensiveTower(Point location, int buildingNum)
    {
        super(location, buildingNum);
    }

    public int getRange()
    {
        return range;
    }

    public int getAttackPower()
    {
        return attackPower;
    }

    public abstract void attack(Attack attack);

    @Override
    public BuildingInfoSubmenu getInfoSubmenu()
    {
        return (BuildingInfoSubmenu)new BuildingInfoSubmenu(null)
                .insertItem(Menu.Id.DEFENSIVE_TARGET_INFO, "Attack Info");
    }
}
