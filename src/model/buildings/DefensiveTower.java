package model.buildings;

public abstract class DefensiveTower extends Building
{
    private int attackPower;

    public int getAttackPower()
    {
        return attackPower;
    }

    public abstract void attack(Attack attack);

}
