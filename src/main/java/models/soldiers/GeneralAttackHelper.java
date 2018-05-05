package models.soldiers;

import models.Attack;
import models.buildings.Building;
import utils.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneralAttackHelper extends AttackHelper

{
    private Building target;

    public GeneralAttackHelper(Attack attack, Soldier soldier)
    {
        super(attack, soldier);
    }

    @Override
    public void fire()
    {
        if (soldier != null && !soldier.getAttackHelper().isDead())
        {
            if (target != null)
            {
                if (isTargetInRange())
                {
                    if (target.getStrength() > 0 && !target.isDestroyed())
                    {
                        target.decreaseStrength(getDamage());
                    }
                    if (target.getStrength() <= 0)
                    {
                        target.setDestroyed(true);
                        attack.addToClaimedResource(target.getBuildingInfo().getDestroyResource());
                    }
                }
            }
        }
    }

    private boolean isTargetInRange()
    {
        if (target != null)
        {
            double distance2nd = Point.euclideanDistance2nd(target.getLocation(), getSoldierLocation());
            return distance2nd == 2 || Math.sqrt(distance2nd) <= getRange();
        }
        return true;
        //TODO‌ bad smell of redundant code of escaping compile error.in fact the code shouldn't reach here because we set the target first then we come up to move or fight
    }

    @Override
    public void setTarget()
    {
        if (soldier != null && !soldier.getAttackHelper().isDead())
        {
            if (target == null || target.getStrength() <= 0 || target.isDestroyed())
            {
                target = getNearestBuilding();
                if (target != null)
                    System.err.println("new target: " + target.getName() + " at: " + target.getLocation().toString());
                else
                    System.err.println("no target found.");
            }
        }
    }

    private Building getNearestBuilding()
    {

        ArrayList<Building> aliveBuildings = getAliveBuildings()
                .sorted(Comparator.comparingDouble(building -> Point.euclideanDistance2nd(building.getLocation(), getSoldierLocation())))
                .collect(Collectors.toCollection(ArrayList::new));
        try
        {
            if (soldier.getSoldierInfo().getFavouriteTargets().length == 0)
                throw new Exception();

            return aliveBuildings.stream()
                    .filter(building -> Arrays.stream(soldier.getSoldierInfo().getFavouriteTargets()).anyMatch(t -> t.isInstance(building)))
                    .filter(this::isTargetReachable)
                    .findFirst().orElseThrow(Exception::new);

        }
        catch (Exception ex)
        {
            return aliveBuildings.stream()
                    .filter(this::isTargetReachable)
                    .findFirst()
                    .orElse(null);
        }
    }


    public Stream<Building> getAliveBuildings()
    {
        return attack.getMap().getBuildings().stream().filter(building -> !building.isDestroyed());
    }

    private boolean isTargetReachable(Building favouriteTarget)
    {
        // TODO: 5/6/18 must be done as soon as possible
        //attack.getArrayMap().isReachable();
        return true;
    }

    @Override
    public void move()
    {
        if (soldier != null && !soldier.getAttackHelper().isDead())
        {
            if (!isTargetInRange())
            {
                List<Point> soldierPath = attack.getSoldierPath(getSoldierLocation(), target.getLocation());
                Point pointToGo = soldierPath.get(soldierPath.size() - 1);

                int i;
                for (i = soldierPath.size() - 1; i >= 0; i--)
                {
                    if (i != soldierPath.size() - 1)
                    {
                        pointToGo = soldierPath.get(i + 1);
                    }
                    if (Point.euclideanDistance(soldierPath.get(i), getSoldierLocation()) > soldier.getSpeed())
                    {
                        break;
                    }
                }
                attack.displayMove(soldier, getSoldierLocation(), pointToGo);
                soldier.setLocation(pointToGo);
            }
        }
    }
}
