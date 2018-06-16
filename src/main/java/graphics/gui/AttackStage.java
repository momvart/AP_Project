package graphics.gui;

import graphics.helpers.SoldierGraphicHelper;
import models.attack.Attack;
import models.soldiers.Soldier;

public class AttackStage extends MapStage
{
    private Attack attack;

    public AttackStage(Attack attack, double width, double height)
    {
        super(attack.getMap(), width, height);
        this.attack = attack;
    }


    public void setUpAndShow()
    {
        attack.setSoldierPutListener(this::addSoldier);

        super.setUpAndShow();
    }

    private void addSoldier(Soldier soldier)
    {
        SoldierGraphicHelper helper = new SoldierGraphicHelper(soldier, getObjectsLayer());
        helper.makeIdle();
        soldier.getAttackHelper().setGraphicHelper(helper);
        helper.setUpListeners();
        gHandler.addUpdatable(helper);
    }
}
