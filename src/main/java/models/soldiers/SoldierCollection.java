package models.soldiers;

import exceptions.SoldierNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

public class SoldierCollection
{
    private ArrayList<ArrayList<Soldier>> lists;

    public SoldierCollection()
    {
        lists = new ArrayList<>(getListsCount());
        for (int i = 0; i < getListsCount(); i++)
            lists.add(new ArrayList<>());
    }

    public int getListsCount()
    {
        return SoldierValues.SOLDIER_TYPES_COUNT;
    }

    public ArrayList<Soldier> getSoldiers(int soldierType)
    {
        return lists.get(soldierType - 1);
    }

    public Stream<Soldier> getAllSoldiers()
    {
        return lists.stream().flatMap(Collection::stream);
    }

    public void addSoldier(Soldier soldier)
    {
        lists.get(soldier.getType() - 1).add(soldier);
    }

    public Soldier getSoldierById(long id) throws SoldierNotFoundException
    {
        for (ArrayList<Soldier> list : lists)
            for (Soldier soldier : list)
                if (soldier.getId() == id)
                    return soldier;
        throw new SoldierNotFoundException("soldier not found ", "");
    }
}
