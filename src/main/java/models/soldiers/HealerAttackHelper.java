package models.soldiers;

import models.Attack;
import utils.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HealerAttackHelper extends AttackHelper
{
    private ArrayList<Soldier> targets;
    private final int width;
    private final int height;
    private final Point point1;
    private final Point point2;
    private final Point point3;
    private final Point point4;
    private final Point point5;
    private final Point point6;
    private final Point point7;
    private final Point point8;
    private final Point point9;
    private final ArrayList<Point> points;
    private Point destination;

    private int timeTillDie = 10;

    public void ageOneDeltaT()
    {
        if (timeTillDie > 0)
        {
            timeTillDie--;
        }
    }

    public int getTimeTillDie()
    {
        return timeTillDie;
    }


    public HealerAttackHelper(Attack attack, Healer healer)
    {
        super(attack, healer);
        width = attack.getMap().getWidth();
        height = attack.getMap().getHeight();
        point1 = new Point(width / 6, height / 6);
        point2 = new Point(width / 2, height / 6);
        point3 = new Point(5 * width / 6, height / 6);
        point4 = new Point(width / 6, height / 2);
        point5 = new Point(width / 2, height / 2);
        point6 = new Point(5 * width / 6, height / 2);
        point7 = new Point(width / 6, 5 * height / 6);
        point8 = new Point(width / 2, 5 * height / 6);
        point9 = new Point(5 * width / 6, 5 * height / 6);
        points = new ArrayList<>(Arrays.asList(point1, point2, point3, point4, point5, point6, point7, point8, point9));
    }

    @Override
    public void passTurn()
    {
        ageOneDeltaT();
        super.passTurn();
    }

    @Override
    public void move()
    {
        System.err.println("destination x:" + destination.getX() + "y:" + destination.getY());
        if (soldier != null && isSoldierDeployed() && !soldier.getAttackHelper().isDead())
        {
            if (destination != null)
            {
                Point pointToGo = getPointToGo(destination);
                attack.moveOnLocation(soldier, getSoldierLocation(), pointToGo);
                soldier.setLocation(pointToGo);
            }
        }
    }

    @Override
    public void fire()
    {
        if (soldier != null && !soldier.getAttackHelper().isDead())
        {
            if (targets != null && targets.size() != 0)
            {
                for (Soldier target : targets)
                {
                    System.out.println("healer healing building type:" + target.getType() + "in amount of" + getDamage());
                    target.getAttackHelper().increaseHealth(getDamage());
                }
            }
        }
    }

    @Override
    public void setTarget()
    {
        targets = getSoldiersInRange();
        changeDestinationIfNeeded();

    }

    private Point getSoldiersConcentrationPoint()
    {
        Point output = points.get(0);
        int outputCountOfSoldiers = countNumberOfSoldiersAround(output);
        for (int i = 1; i < 9; i++)
        {
            int soldierNumbers = countNumberOfSoldiersAround(points.get(i));
            if (soldierNumbers > outputCountOfSoldiers)
            {
                output = points.get(i);
                outputCountOfSoldiers = soldierNumbers;
            }
        }
        return output;
    }

    private void changeDestinationIfNeeded()
    {
        Point shouldISwitchTo = getSoldiersConcentrationPoint();
        int shouldISwitchToNumber = countNumberOfSoldiersAround(shouldISwitchTo);
        if (destination != null)
        {
            if (countNumberOfSoldiersAround(destination) <= (0.7 * shouldISwitchToNumber))
            {
                destination = shouldISwitchTo;
            }
        }
        else
        {
            destination = shouldISwitchTo;
        }
    }

    private int countNumberOfSoldiersAround(Point point)
    {
        int numberOfSoldiersInRange = 0;
        List<Soldier> soldiers = attack.getDeployedAliveUnits().collect(Collectors.toList());
        if (soldiers != null && soldiers.size() != 0)
        {
            for (Soldier soldier : soldiers)
            {
                if (soldier != null && isSoldierDeployed() && !soldier.getAttackHelper().isDead() && soldier.getMoveType() == MoveType.GROUND)
                {
                    if (soldier.getLocation() != null && soldier.getLocation().getX() >= 0 && soldier.getLocation().getY() >= 0)
                    {
                        if (Point.euclideanDistance(soldier.getLocation(), point) - (attack.getMap().getWidth() + attack.getMap().getHeight()) / 2 / 3 < 0.01)
                        {
                            numberOfSoldiersInRange++;
                        }
                    }

                }

            }
        }
        return numberOfSoldiersInRange;
    }


    private ArrayList<Soldier> getSoldiersInRange()
    {
        ArrayList<Soldier> soldiersInRange = new ArrayList<>();
        List<Soldier> soldiers = attack.getDeployedAliveUnits().collect(Collectors.toList());
        if (soldiers != null && soldiers.size() != 0)
        {
            for (Soldier soldier : soldiers)
            {
                if (soldier != null && !soldier.getAttackHelper().isDead() && isSoldierDeployed() && soldier.getMoveType() == MoveType.GROUND)
                {
                    if (Point.euclideanDistance(soldier.getLocation(), getSoldierLocation()) - getRange() < 0.01)
                    {
                        soldiersInRange.add(soldier);
                    }
                }
            }
        }
        return soldiersInRange;
    }
}
