package models.attack.attackHelpers;

import exceptions.BuildingNotFoundException;
import exceptions.ConsoleException;
import exceptions.SoldierNotFoundException;
import graphics.helpers.SoldierGraphicHelper;
import models.attack.Attack;
import models.buildings.Building;
import models.soldiers.Soldier;
import utils.Point;
import utils.PointF;

public class NetworkDecode
{
    private static NetworkDecode instance;
    private Attack theAttack;

    public NetworkDecode(Attack theAttack)
    {
        this.theAttack = theAttack;
    }

    public static NetworkDecode getInstance(Attack attack)
    {
        if (instance == null)
            instance = new NetworkDecode(attack);
        return instance;
    }

    public void putUnits(int unitType, int count, Point location)
    {
        try
        {
            theAttack.putUnits(unitType, count, location, true);
        }
        catch (ConsoleException ignored) {}
    }

    public void soldrStJogTowd(long soldierId, PointF dest)
    {
        getSoldier(soldierId).getAttackHelper().getGraphicHelper().startJoggingToward(dest, true);
    }

    public void setSoldrPos(long soldierId, PointF pos)
    {
        Soldier soldier = getSoldier(soldierId);
        SoldierGraphicHelper graphicHelper = soldier.getAttackHelper().getGraphicHelper();
        graphicHelper.onMoveFinished();
        graphicHelper.getDrawer().setPosition(pos.getX(), pos.getY());
    }

    public void soldierDie(long id)
    {
        getSoldier(id).getAttackHelper().callOnSoldierDie();
    }

    public void buildingDestroy(long id)
    {
        getBuilding(id).getAttackHelper().callOnDestroyed();
    }

    public void soldierDecreaseHealth(long id, int amount)
    {
        getSoldier(id).getAttackHelper().decreaseHealth(amount, true);
    }

    public void soldierIncreaseHealth(long id, int amount)
    {
        getSoldier(id).getAttackHelper().increaseHealth(amount, true);
    }

    public void buildingDecreaseStrength(long id, int amount)
    {
        getBuilding(id).getAttackHelper().decreaseStrength(amount, true);
    }

    //
    private Soldier getSoldier(long soldierId)
    {
        try
        {
            return theAttack.getSoldierById(soldierId);
        }
        catch (SoldierNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private Building getBuilding(long buildingId)
    {
        try
        {
            return theAttack.getBuildingById(buildingId);
        }
        catch (BuildingNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
