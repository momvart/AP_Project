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
import network.AttackUDPReceiver;
import utils.Point;
import utils.PointF;

public class NetworkDecoder
{
    private Attack theAttack;

    public NetworkDecoder(Attack theAttack, AttackUDPReceiver receiver)
    {
        this.theAttack = theAttack;
        receiver.setAttackMessageReceiver(this::onAttackMessageReceived);
    }

    private void onAttackMessageReceived(AttackMessage message)
    {
        System.out.println(message);
        try
        {
            switch (message.getType())
            {
                case AttackMessage.Types.PutUnit:
                    putUnits(message.getIntData(NetworkHelper.UNIT_TYPE_FIELD),
                            message.getIntData("count"),
                            new Point(message.getIntData("x"), message.getIntData("y")));
                    break;
                case AttackMessage.Types.StartJogging:
                    sldrStJogTowd(message.getIdData(), message.getPointFData());
                    break;
                case AttackMessage.Types.GCStartJogging:
                    grdnGntStJojTow(message.getIdData(), message.getLongData(NetworkHelper.SOLDIER_ID_FIELD));
                    break;
                case AttackMessage.Types.SoldierSetPos:
                    setSoldrPos(message.getIdData(), message.getPointFData());
                    break;
                case AttackMessage.Types.GCSetPos:
                    setGrdnGntPos(message.getIdData(), message.getPointFData());
                    break;
                case AttackMessage.Types.SoldierDie:
                    soldierDie(message.getIdData());
                    break;
                case AttackMessage.Types.BuildingDestroy:
                    buildingDestroy(message.getIdData());
                    break;
                case AttackMessage.Types.SoldierSetHealth:
                    soldierSetHealth(message.getIdData(), message.getIntData(NetworkHelper.HEALTH_STRENGTH));
                    break;
                case AttackMessage.Types.BuildingSetStrength:
                    buildingSetStrength(message.getIdData(), message.getIntData(NetworkHelper.HEALTH_STRENGTH));
                    break;
                case AttackMessage.Types.BulletStartNewWave:
                    bulletStartNewWave(message.getIdData(),
                            new PointF(message.getDoubleData("x1"), message.getDoubleData("y1")),
                            new PointF(message.getDoubleData("x2"), message.getDoubleData("y2")),
                            message.getLongData(NetworkHelper.SOLDIER_ID_FIELD));
                    break;
            }
        }
        catch (CouldNotFetchNetworkDataException ex)
        {
            ex.printStackTrace();
        }
    }

    public void putUnits(int unitType, int count, Point location)
    {
        try
        {
            theAttack.putUnits(unitType, count, location, true);
        }
        catch (ConsoleException ex)
        {
            ex.printStackTrace();
        }
    }

    public void sldrStJogTowd(long soldierId, PointF dest) throws CouldNotFetchNetworkDataException
    {
        Soldier soldier = getSoldier(soldierId);
        soldier.getAttackHelper().getGraphicHelper().startJoggingToward(dest, true);
    }

    public void grdnGntStJojTow(long id, long soldierId) throws CouldNotFetchNetworkDataException
    {
        Building building = getBuilding(id);
        GuardianGiantGraphicHelper graphicHelper = (GuardianGiantGraphicHelper)building.getAttackHelper().getGraphicHelper();
        graphicHelper.startJoggingToward(getSoldier(soldierId), true);
    }

    public void setSoldrPos(long soldierId, PointF pos) throws CouldNotFetchNetworkDataException
    {
        Soldier soldier = getSoldier(soldierId);
        SoldierGraphicHelper graphicHelper = soldier.getAttackHelper().getGraphicHelper();
        graphicHelper.onMoveFinished();
        graphicHelper.getDrawer().setPosition(pos.getX(), pos.getY());
        graphicHelper.syncLogicWithGraphic();
    }


    public void setGrdnGntPos(long id, PointF position) throws CouldNotFetchNetworkDataException
    {
        GuardianGiant guardianGiant = (GuardianGiant)getBuilding(id);
        GuardianGiantGraphicHelper graphicHelper = (GuardianGiantGraphicHelper)guardianGiant.getAttackHelper().getGraphicHelper();
        graphicHelper.onMoveFinished();
        graphicHelper.getBuildingDrawer().setPosition(position.getX(), position.getY());
        graphicHelper.syncLogicWithGraphic();
    }


    public void soldierDie(long id) throws CouldNotFetchNetworkDataException
    {
        Soldier soldier = getSoldier(id);
        soldier.getAttackHelper().callOnSoldierDie();

    }

    public void buildingDestroy(long id) throws CouldNotFetchNetworkDataException
    {
        Building building = getBuilding(id);
        building.getAttackHelper().callOnDestroyed();
    }


    public void soldierSetHealth(long id, int health) throws CouldNotFetchNetworkDataException
    {
        Soldier soldier = getSoldier(id);
        SoldierAttackHelper attackHelper = soldier.getAttackHelper();
        int previousHealth = attackHelper.getHealth();
        if (health > previousHealth)
            attackHelper.increaseHealth(health - previousHealth, true);
        else
            attackHelper.decreaseHealth(previousHealth - health, true);
    }

    public void buildingSetStrength(long id, int strength) throws CouldNotFetchNetworkDataException
    {
        Building building = getBuilding(id);
        BuildingAttackHelper attackHelper = building.getAttackHelper();
        attackHelper.decreaseStrength(attackHelper.getStrength() - strength, true);
    }

    public void bulletStartNewWave(long id, PointF start, PointF end, long soldierId) throws CouldNotFetchNetworkDataException
    {
        Building building = getBuilding(id);
        DefensiveTowerGraphicHelper graphicHelper = (DefensiveTowerGraphicHelper)building.getAttackHelper().getGraphicHelper();
        graphicHelper.getBullet().startNewWave(start, end, getSoldier(soldierId), true);
    }

    public void bulletSetPos(long id, PointF position) throws CouldNotFetchNetworkDataException
    {
        DefensiveTower tower = (DefensiveTower)getBuilding(id);
        DefensiveTowerGraphicHelper graphicHelper = (DefensiveTowerGraphicHelper)tower.getAttackHelper().getGraphicHelper();
        graphicHelper.getBullet().getDrawer().setPosition(position.getX(), position.getY());
    }

    private Soldier getSoldier(long soldierId) throws CouldNotFetchNetworkDataException
    {
        try
        {
            return theAttack.getSoldierById(soldierId);
        }
        catch (SoldierNotFoundException e)
        {
            throw new CouldNotFetchNetworkDataException();
        }
    }

    private Building getBuilding(long buildingId) throws CouldNotFetchNetworkDataException
    {
        try
        {
            return theAttack.getBuildingById(buildingId);
        }
        catch (BuildingNotFoundException e)
        {
            throw new CouldNotFetchNetworkDataException();
        }
    }

}
