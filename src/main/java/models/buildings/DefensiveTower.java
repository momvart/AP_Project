package models.buildings;

import menus.BuildingInfoSubmenu;
import menus.Menu;
import models.Attack;
import utils.Point;

public abstract class DefensiveTower extends Building
{
    private int damagePower;
    private int range;

    public DefensiveTower(Point location, int buildingNum)
    {
        super(location, buildingNum);
    }

    public int getRange()
    {
        return range;
    }

    public int getDamagePower()
    {
        return damagePower;
    }

    @Override
    public void ensureLevel()
    {
        super.ensureLevel();
        DefensiveTowerInfo info = (DefensiveTowerInfo)getBuildingInfo();
        damagePower = info.getInitialDamage() + info.getUpgradeDamageInc() * level;
        range = info.getAttackRange();
    }

    public abstract void attack(Attack attack);

    @Override
    public BuildingInfoSubmenu getInfoSubmenu()
    {
        return new BuildingInfoSubmenu(null)
                .insertItem(Menu.Id.DEFENSIVE_TARGET_INFO, "Attack Info");
    }
}
