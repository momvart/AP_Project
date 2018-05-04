package models.soldiers;

import models.Attack;
import models.buildings.Building;
import utils.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.min;

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
            if (isTargetInRange())
            {
                target.decreaseStrength(getDamage());
            }
        }
    }

    private boolean isTargetInRange()
    {
        if (target != null)
        {
            return euclidianDistance(target.getLocation(), getSoldierLocation()) <= getRange();
        }
        return true;//TODO‌ bad smell of redandent code of escaping compile error.in fact the code shouldn't reach here because we set the target first then we come up to move or fight
    }

    @Override
    public void setTarget()
    {
        if (soldier != null && !soldier.getAttackHelper().isDead())
        {
            if (target == null || target.getStrength() <= 0)
            {
                if (getBestFavouriteTarget() != null)
                {
                    target = getBestFavouriteTarget();
                }
                else
                {
                    target = getNearestBuilding();
                }
            }
        }
    }

    private Building getNearestBuilding()
    {
//        if (attack.getMap().getBuildings() != null)
//        {
//            ArrayList<Building> buildings = attack.getMap().getBuildings();
//            ArrayList<Point> locations = new ArrayList<>();
//            for (Building building : buildings)
//            {
//                locations.add(building.getLocation());
//            }
//            Point startPoint = getSoldierLocation();
//            Point point = getSoldierLocation();
//            HashMap<Point , Double> buildingLocationsInRange = new HashMap<>();
//            for (int i = 1; i < getRange() ; i += 2)
//            {
//                if (Math.abs(point.getX() - startPoint.getX()) > getRange() || point.getY() - startPoint.getY() > getRange())
//                {
//                    break;
//                }
//                double radius;
//                for (int j = 0; j < i ; j++)
//                {
//                    up(point);
//                    radius = euclidianDistance(startPoint , point);
//                    if (locations.contains(point))
//                    {
//                        buildingLocationsInRange.put(point , radius);
//                    }
//                }
//                for (int j = 0; j < i; j++)
//                {
//                    right(point);
//                    radius = euclidianDistance(startPoint , point);
//                    if (locations.contains(point))
//                    {
//                        buildingLocationsInRange.put(point , radius);
//                    }
//                }
//                for (int j = 0; j < i + 1 ; j++)
//                {
//                    down(point);
//                    radius = euclidianDistance(startPoint , point);
//                    if (locations.contains(point))
//                    {
//                        buildingLocationsInRange.put(point , radius);
//                    }
//                }
//                for (int j = 0; j < i + 1 ; j++)
//                {
//                    left(point);
//                    radius = euclidianDistance(startPoint , point);
//                    if (locations.contains(point))
//                    {
//                        buildingLocationsInRange.put(point , radius);
//                    }
//                }
//            }
//
//
//        }
        ArrayList<Building> buildings = super.attack.getMap().getBuildings();
        ArrayList<Double> distances = new ArrayList<>();
        for (Building building : buildings)
        {
            distances.add(euclidianDistance(building.getLocation(), getSoldierLocation()));
        }
        double minimumDistance = min(distances);
        for (Building building : buildings)
        {
            if (euclidianDistance(building.getLocation(), getSoldierLocation()) == minimumDistance)
            {
                return building;
            }
        }
        return null;
    }

    private void left(Point point)
    {
        point.setX(point.getX() - 1);
    }

    private void down(Point point)
    {
        point.setY(point.getY() + 1);
    }

    private void right(Point point)
    {
        point.setX(point.getX() + 1);
    }

    private void up(Point point)
    {
        point.setY(point.getY() - 1);
    }

    public Building getBestFavouriteTarget()
    {
        ArrayList<Building> favoutriteTargets = new ArrayList<>();
        ArrayList<Building> towers = super.attack.getMap().getBuildings();
        for (Building tower : towers)
        {
            if (Arrays.stream(soldier.getSoldierInfo().getFavouriteTargets()).anyMatch(c -> c.isInstance(tower)))
            {
                favoutriteTargets.add(tower);
            }
        }
        ArrayList<Integer> manhatanianDistances = new ArrayList<>();
        if (favoutriteTargets.size() != 0)
        {
            for (Building favoutriteTarget : favoutriteTargets)
            {
                manhatanianDistances.add(manhatanianDistance(favoutriteTarget.getLocation(), super.getSoldierLocation()));
            }
            Collections.sort(manhatanianDistances);
            for (Building favoutriteTarget : favoutriteTargets)
            {
                for (int i = 0; i < manhatanianDistances.size(); i++)
                {
                    if (manhatanianDistance(favoutriteTarget.getLocation(), super.getSoldierLocation()).equals(manhatanianDistances.get(i)))
                    {
                        if (isTargetReachable(favoutriteTarget) && isntTargetTooFar(favoutriteTarget))
                        {
                            return favoutriteTarget;
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean isntTargetTooFar(Building favoutriteTarget)
    {
        return manhatanianDistance(getSoldierLocation(), favoutriteTarget.getLocation()) < 24;//TODO to be manipulated for increasing the performance
    }

    private boolean isTargetReachable(Building favouriteTarget)
    {
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
                Point pointToGo = soldierPath.get(0);
                int i;
                for (i = 0; i < soldierPath.size(); i++)
                {
                    if (i != 0)
                    {
                        pointToGo = soldierPath.get(i - 1);
                    }
                    if (euclidianDistance(soldierPath.get(i), getSoldierLocation()) > soldier.getSpeed())
                    {
                        break;
                    }
                }
                soldier.setLocation(pointToGo);
            }
        }
    }
}
