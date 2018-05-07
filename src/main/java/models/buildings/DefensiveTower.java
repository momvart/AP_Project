package models.buildings;

import exceptions.SoldierNotFoundException;
import menus.BuildingInfoSubmenu;
import menus.Menu;
import models.Attack;
import models.soldiers.Soldier;
import utils.Point;

import java.util.List;

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


    public void attack(Attack attack)
    {
        try
        {
            Point soldierPoint = attack.getNearestSoldier(location, range, getDefenseType().convertToMoveType());
            List<Soldier> soldiers = attack.getSoldiersOnLocations().getSoldiers(soldierPoint);
            soldiers.get(0).getAttackHelper().decreaseHealth(damagePower);
            showLog(soldiers.get(0));
        }
        catch (SoldierNotFoundException e)
        {
            //do nothing.
        }

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
