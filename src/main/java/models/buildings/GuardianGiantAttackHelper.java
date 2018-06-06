package models.buildings;

import models.Attack;
import utils.Point;

import java.util.List;

public class GuardianGiantAttackHelper extends DefensiveTowerAttackHelper
{
    public GuardianGiantAttackHelper(Building building, Attack attack)
    {
        super(building, attack);
    }

    @Override
    public void passTurn()
    {
        if (!destroyed)
        {
            setTarget();
            if (mainTargets.size() != 0)
                attack();
            else
                move();
        }
    }


    private void move()
    {
        Point pointToGo = getPointToGo(mainTargets.get(0).getLocation());
        building.setLocation(pointToGo);
    }

    private Point getPointToGo(Point destination)
    {
        List<Point> soldierPath = attack.getSoldierPath(building.location, destination, false);
        Point pointToGo = soldierPath.get(soldierPath.size() - 1);

        int i;
        for (i = soldierPath.size() - 1; i >= 0; i--)
        {
            if (i != soldierPath.size() - 1)
                pointToGo = soldierPath.get(i + 1);
            if (Point.euclideanDistance(soldierPath.get(i), building.location) > GuardianGiant.GUARDIAN_GIANT_SPEED)
                break;
        }
        return pointToGo;
    }
}
