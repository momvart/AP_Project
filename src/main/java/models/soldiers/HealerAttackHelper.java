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
    private final Point point1 = new Point(5, 5);
    private final Point point2 = new Point(15, 5);
    private final Point point3 = new Point(25, 5);
    private final Point point4 = new Point(5, 15);
    private final Point point5 = new Point(15, 15);
    private final Point point6 = new Point(25, 15);
    private final Point point7 = new Point(5, 25);
    private final Point point8 = new Point(15, 25);
    private final Point point9 = new Point(25, 25);
    private final ArrayList<Point> points = new ArrayList<>(Arrays.asList(point1, point2, point3, point4, point5, point6, point7, point8, point9));
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
    }

    @Override
    public void passTurn()
    {
        ageOneDeltaT();
        if (timeTillDie <= 0)
            setDead(true);
        super.passTurn();
    }

    @Override
    public void move()
    {
        if (soldier != null && isSoldierDeployed() && !soldier.getAttackHelper().isDead())
        {
            changeDestinationIfNeeded();
            Point pointToGo = getPointToGo(destination);
            soldier.setLocation(pointToGo);
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
                    target.increaseHealth(getDamage());
                }
            }
        }
    }

    @Override
    public void setTarget()
    {
        targets = getSoldiersInRange();
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
        if (countNumberOfSoldiersAround(destination) <= (0.7 * shouldISwitchToNumber))
        {
            destination = shouldISwitchTo;
        }
    }

    private int countNumberOfSoldiersAround(Point point)
    {
        int numberOfSoldiersInRange = 0;
        List<Soldier> soldiers = attack.getAllDeployedUnits().collect(Collectors.toList());
        if (soldiers != null && soldiers.size() != 0)
        {
            for (Soldier soldier : soldiers)
            {
                if (soldier != null && isSoldierDeployed() && !soldier.getAttackHelper().isDead())
                {
                    if (Point.euclideanDistance(soldier.getLocation(), point) - getRange() < 0.01)
                    {
                        numberOfSoldiersInRange++;
                    }
                }

            }
        }
        return numberOfSoldiersInRange;
    }


    private ArrayList<Soldier> getSoldiersInRange()
    {
        ArrayList<Soldier> soldiersInRange = new ArrayList<>();
        List<Soldier> soldiers = attack.getAllDeployedUnits().collect(Collectors.toList());
        if (soldiers != null && soldiers.size() != 0)
        {
            for (Soldier soldier : soldiers)
            {
                if (soldier != null && !soldier.getAttackHelper().isDead())
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
