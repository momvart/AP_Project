package models.attack.attackHelpers;

import exceptions.BuildingNotFoundException;
import exceptions.ConsoleException;
import exceptions.CouldNotFetchNetworkDataException;
import exceptions.SoldierNotFoundException;
import graphics.helpers.DefensiveTowerGraphicHelper;
import graphics.helpers.GuardianGiantGraphicHelper;
import graphics.helpers.SoldierGraphicHelper;
import models.attack.Attack;
import models.buildings.Building;
import models.buildings.DefensiveTower;
import models.buildings.GuardianGiant;
import models.soldiers.Soldier;
import network.AttackMessage;
import utils.Point;
import utils.PointF;

public class NetworkDecode
{
    private static NetworkDecode instance;
    private Attack theAttack;

    private NetworkDecode(Attack theAttack)
    {
        this.theAttack = theAttack;
    }

    public static NetworkDecode getInstance(Attack attack)
    {
        if (instance == null)
            instance = new NetworkDecode(attack);
        return instance;
    }

    private void onAttackMessageReceived(AttackMessage message)
    {
        switch (message.getType())
        {
            case AttackMessage.Types.PutUnit:

                break;
            case AttackMessage.Types.StartJogging:

                break;
            case AttackMessage.Types.SoldierSetPos:

                break;
            case AttackMessage.Types.SoldierDie:

                break;
            case AttackMessage.Types.BuildingDestroy:

                break;
            case AttackMessage.Types.SoldierSetHealth:

                break;
            case AttackMessage.Types.BuildingSetStrength:

                break;
            case AttackMessage.Types.BuildingStartNewWave:

                break;
        }
    }

    public void putUnits(int unitType, int count, Point location)
    {
        try
        {
            theAttack.putUnits(unitType, count, location, true);
        }
        catch (ConsoleException ignored) {}
    }

    public void sldrStJogTowd(long soldierId, PointF dest) throws CouldNotFetchNetworkDataException
    {
        Soldier soldier = getSoldier(soldierId);
        if (soldier == null)
            throw new CouldNotFetchNetworkDataException();
        soldier.getAttackHelper().getGraphicHelper().startJoggingToward(dest, true);
    }

    public void grdnGntStJojTow(long id, Soldier soldier) throws CouldNotFetchNetworkDataException
    {
        Building building = getBuilding(id);
        if (building == null)
            throw new CouldNotFetchNetworkDataException();
        GuardianGiantGraphicHelper graphicHelper = (GuardianGiantGraphicHelper)building.getAttackHelper().getGraphicHelper();
        graphicHelper.startJoggingToward(soldier, true);
    }

    public void setSoldrPos(long soldierId, PointF pos) throws CouldNotFetchNetworkDataException
    {
        Soldier soldier = getSoldier(soldierId);
        if (soldier == null)
            throw new CouldNotFetchNetworkDataException();
        SoldierGraphicHelper graphicHelper = soldier.getAttackHelper().getGraphicHelper();
        graphicHelper.onMoveFinished();
        graphicHelper.getDrawer().setPosition(pos.getX(), pos.getY());
        graphicHelper.syncLogicWithGraphic();
    }


    public void setGrdnGntPos(long id, PointF position) throws CouldNotFetchNetworkDataException
    {
        GuardianGiant guardianGiant = (GuardianGiant)getBuilding(id);
        if (guardianGiant == null)
            throw new CouldNotFetchNetworkDataException();
        GuardianGiantGraphicHelper graphicHelper = (GuardianGiantGraphicHelper)guardianGiant.getAttackHelper().getGraphicHelper();
        graphicHelper.onMoveFinished();
        graphicHelper.getBuildingDrawer().setPosition(position.getX(), position.getY());
        graphicHelper.syncLogicWithGraphic();
    }


    public void soldierDie(long id) throws CouldNotFetchNetworkDataException
    {
        Soldier soldier = getSoldier(id);
        if (soldier == null)
            throw new CouldNotFetchNetworkDataException();
        soldier.getAttackHelper().callOnSoldierDie();

    }

    public void buildingDestroy(long id) throws CouldNotFetchNetworkDataException
    {
        Building building = getBuilding(id);
        if (building == null)
            throw new CouldNotFetchNetworkDataException();
        building.getAttackHelper().callOnDestroyed();
    }


    public void soldierSetHealth(long id, int health) throws CouldNotFetchNetworkDataException
    {
        Soldier soldier = getSoldier(id);
        if (soldier == null)
            throw new CouldNotFetchNetworkDataException();
        soldier.getAttackHelper().setHealth(health, true);
    }

    public void buildingSetStrength(long id, int strength) throws CouldNotFetchNetworkDataException
    {
        Building building = getBuilding(id);
        if (building == null)
            throw new CouldNotFetchNetworkDataException();
        building.getAttackHelper().setStrength(strength, true);
    }

    public void bulletStartNewWave(long id, PointF start, PointF end, Soldier soldier) throws CouldNotFetchNetworkDataException
    {
        Building building = getBuilding(id);
        if (building == null)
            throw new CouldNotFetchNetworkDataException();
        DefensiveTowerGraphicHelper graphicHelper = (DefensiveTowerGraphicHelper)building.getAttackHelper().getGraphicHelper();
        graphicHelper.getBullet().startNewWave(start, end, soldier, true);
    }

    public void bulletSetPos(long id, PointF position) throws CouldNotFetchNetworkDataException
    {
        DefensiveTower tower = (DefensiveTower)getBuilding(id);
        if (tower == null)
            throw new CouldNotFetchNetworkDataException();
        DefensiveTowerGraphicHelper graphicHelper = (DefensiveTowerGraphicHelper)tower.getAttackHelper().getGraphicHelper();
        graphicHelper.getBullet().getDrawer().setPosition(position.getX(), position.getY());
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
