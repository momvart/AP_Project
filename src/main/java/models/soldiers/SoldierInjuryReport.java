package models.soldiers;

public class SoldierInjuryReport
{
    Soldier soldier;
    int initialHealth;
    int finalHealth;

    public SoldierInjuryReport(Soldier soldier, int initialHealth, int finalHealth)
    {
        this.soldier = soldier;
        this.initialHealth = initialHealth;
        this.finalHealth = finalHealth;
    }

    public Soldier getSoldier()
    {
        return soldier;
    }

    public int getInitialHealth()
    {
        return initialHealth;
    }

    public int getFinalHealth()
    {
        return finalHealth;
    }
}
