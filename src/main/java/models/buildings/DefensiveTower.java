package models.buildings;

import menus.BuildingInfoSubmenu;
import menus.Menu;
import models.soldiers.Soldier;
import utils.Point;

public abstract class DefensiveTower extends Building
{
    private transient int damagePower;
    private transient int range;

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


    public DefenseType getDefenseType()
    {
        return ((DefensiveTowerInfo)getBuildingInfo()).getTargetType();
    }
    @Override
    public BuildingInfoSubmenu getInfoSubmenu()
    {
        return new BuildingInfoSubmenu(null)
                .insertItem(Menu.Id.DEFENSIVE_TARGET_INFO, "Attack Info");
    }

    public void showLog(Soldier soldier)
    {
        System.err.format("%s attacked by %s with damage power of %d\n", soldier.getSoldierInfo().getName(), getName(), getDamagePower());
    }
}
