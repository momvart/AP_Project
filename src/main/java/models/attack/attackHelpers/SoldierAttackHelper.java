package models.attack.attackHelpers;


import graphics.helpers.IOnMoveFinishedListener;
import graphics.helpers.IOnReloadListener;
import graphics.helpers.SoldierGraphicHelper;
import models.attack.Attack;
import models.buildings.Building;
import models.soldiers.MoveType;
import models.soldiers.Soldier;
import models.soldiers.SoldierValues;
import utils.Point;
import utils.PointF;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class SoldierAttackHelper implements IOnReloadListener, IOnMoveFinishedListener
{
    private int health;
    protected Attack attack;
    protected Soldier soldier;
    private boolean isSoldierDeployed = false;

    protected boolean isDead = false;

    public SoldierAttackHelper(Attack attack, Soldier soldier)
    {
        this.attack = attack;
        this.soldier = soldier;
        this.health = getInitialHealth();
    }

    public int getHealth()
    {
        return health;
    }

    public void increaseHealth(int amount)
    {
        health = Math.min(this.getHealth() + amount, getInitialHealth());
    }

    public void decreaseHealth(int amount)
    {
        health = Math.max(health - amount, 0);
        if (health <= 0)
            setDead(true);
        if (getGraphicHelper() != null)
            getGraphicHelper().updateDrawer();
    }

    public int getInitialHealth()
    {
        return SoldierValues.getSoldierInfo(soldier.getType()).getInitialHealth() + (soldier.getLevel()) * 5;
    }

    public boolean isSoldierDeployed()
    {
        return isSoldierDeployed;
    }

    public void setSoldierIsDeployed(boolean isSoldierDeployed)
    {
        this.isSoldierDeployed = isSoldierDeployed;
    }

    public Point getSoldierLocation()
    {
        return soldier.getLocation();
    }

    public int getDamage()
    {
        return soldier.getDamage();
    }

    public int getRange()
    {
        return SoldierValues.getSoldierInfo(soldier.getType()).getRange();
    }

    public Attack getAttack()
    {
        return attack;
    }

    public void passTurn()
    {
        removeSoldierIfDead();
        if (soldier != null && !isDead && isSoldierDeployed)
        {
            setTarget();
            move();
            fire();
        }
    }

    protected void removeSoldierIfDead()
    {
        if (health <= 0)
        {
            setDead(true);
            soldier = null;
        }
    }

    public Point getPointToGo(Point destination)
    {
        List<Point> soldierPath = attack.getSoldierPath(getSoldierLocation(), destination, soldier.getMoveType() == MoveType.AIR);
        Point pointToGo = soldierPath.get(soldierPath.size() - 1);

        int i;
        for (i = soldierPath.size() - 1; i >= 0; i--)
        {
            if (i != soldierPath.size() - 1)
                pointToGo = soldierPath.get(i + 1);
            if (Point.euclideanDistance(soldierPath.get(i), getSoldierLocation()) > soldier.getSpeed())
                break;
        }
        return pointToGo;
    }

    public Point getLastPointOfStanding(int range, Point start, Point destination)
    {
        List<Point> soldierPath = attack.getSoldierPath(start, destination, soldier.getMoveType() == MoveType.AIR);
        if (soldierPath == null || soldierPath.size() <= 1)
            return null;
        Point lastPoint = soldierPath.get(1);

        int i;
        for (i = 1; i < soldierPath.size() - 1; i++)
        {
            lastPoint = soldierPath.get(i);
            if (Point.euclideanDistance(soldierPath.get(i + 1), destination) > range)
            {
                break;
            }
        }
        return lastPoint;
    }

    public Point getNextPathStraightReachablePoint(Point start, Point destination)
    {
        List<Point> soldierPath = attack.getSoldierPath(start, destination, soldier.getMoveType() == MoveType.AIR);
        if (soldierPath == null)
            return null;
        Point pointToGo = soldierPath.get(soldierPath.size() - 1);
        int i;
        for (i = soldierPath.size() - 2; i >= 0; i--)
        {
            if (isThereABuildingInPath(start, soldierPath.get(i + 1)))
            {
                return pointToGo;
            }
            pointToGo = soldierPath.get(i + 1);
        }
        return pointToGo;
    }

    private boolean isThereABuildingInPath(Point start, Point destination)
    {
        ArrayList<Point> pointsOnTheLine = getPointsOnLine(start, destination);
        ArrayList<Point> aliveBuildingPositions = getAliveBuildingsPositions();
        for (Point point : pointsOnTheLine)
        {
            if (aliveBuildingPositions.contains(point))
            {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Point> getAliveBuildingsPositions()
    {
        ArrayList<Point> positions = new ArrayList<>();
        getAliveBuildings().forEach(building -> positions.add(building.getLocation()));
        return positions;
    }

    public Stream<Building> getAliveBuildings()
    {
        return attack.getMap().getBuildings().stream().filter(building -> !building.getAttackHelper().isDestroyed()).filter(building -> building.getAttackHelper().getStrength() > 0);
    }


    public abstract void move();

    public abstract void fire();

    public abstract void setTarget();

    public boolean isDead()
    {
        return isDead;
    }

    public void setDead(boolean dead)
    {
        isDead = dead;
    }

    private IOnDecampListener decampListener;

    public void setDecampListener(IOnDecampListener decampListener)
    {
        this.decampListener = decampListener;
    }

    protected void callOnDecamp()
    {
        if (decampListener != null)
            decampListener.onDecamp();
    }

    private IOnSoldierDieListener soldierDieListener;

    public void setSoldierDieListener(IOnSoldierDieListener soldierDieListener)
    {
        this.soldierDieListener = soldierDieListener;
    }

    protected void callOnSoldierDie()
    {
        if (soldierDieListener != null)
            soldierDieListener.onSoldierDie();
    }

    //graphic
    private SoldierGraphicHelper graphicHelper;

    public SoldierGraphicHelper getGraphicHelper()
    {
        return graphicHelper;
    }

    public void setGraphicHelper(SoldierGraphicHelper graphicHelper)
    {
        this.graphicHelper = graphicHelper;

        graphicHelper.setMoveListener(this);
        graphicHelper.setReloadListener(this);
        this.setDecampListener(graphicHelper);
        this.setSoldierDieListener(graphicHelper);
    }

    public ArrayList<Point> getPointsOnLine(Point start, Point destination)
    {
        ArrayList<Point> pointsOnLine = new ArrayList<>();
        double stepLength = .2;
        PointF begin = new PointF(start.getX() + .5, start.getY() + .5);
        PointF end = new PointF(destination.getX() + .5, destination.getY() + .5);
        double distance = PointF.euclideanDistance(begin, end);
        double cos = (end.getX() - begin.getX()) / distance;
        double sin = (end.getY() - begin.getY()) / distance;
        PointF currentPoint = begin;
        double initialState = begin.getX() - end.getX();
        pointsOnLine.add(start);

        while (true)
        {
            if (initialState * (currentPoint.getX() - end.getX()) <= 0)
                break;
            currentPoint.setX(currentPoint.getX() + stepLength * cos);
            currentPoint.setY(currentPoint.getY() + stepLength * sin);
            Point veryCurrentPoint = new Point((int)Math.floor(currentPoint.getX()), (int)Math.floor(currentPoint.getY()));
            if (!pointsOnLine.contains(veryCurrentPoint))
                pointsOnLine.add(veryCurrentPoint);
        }
        return pointsOnLine;
    }
}
