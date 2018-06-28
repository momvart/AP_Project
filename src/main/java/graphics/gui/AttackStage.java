package graphics.gui;

import graphics.helpers.*;
import models.attack.Attack;
import models.attack.attackHelpers.SingleTargetAttackHelper;
import models.buildings.Building;
import models.buildings.DefensiveTower;
import models.soldiers.Healer;
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

    @Override
    public BuildingGraphicHelper addBuilding(Building building)
    {
        AttackBuildingGraphicHelper graphicHelper;

        if (building instanceof DefensiveTower)
        {
            if (building.getAttackHelper() instanceof SingleTargetAttackHelper)
                graphicHelper = new SingleTDefenseGraphicHelper(building, getObjectsLayer(), getMap());
            else
                graphicHelper = new AreaSplashDefenseGraphicHelper(building, getObjectsLayer(), getMap());
        }
        else
            graphicHelper = new AttackBuildingGraphicHelper(building, getObjectsLayer(), getMap());

        building.getAttackHelper().setGraphicHelper(graphicHelper);
        setUpBuildingDrawer(graphicHelper.getBuildingDrawer());
        graphicHelper.setUpListeners();
        return graphicHelper;
    }

    private void addSoldier(Soldier soldier)
    {
        SoldierGraphicHelper helper;
        if (soldier.getType() == Healer.SOLDIER_TYPE)
        {
            helper = new HealerGraphicHelper(soldier, getObjectsLayer());
        }
        else
        {
            helper = new GeneralSoldierGraphicHelper(soldier, getObjectsLayer());
        }

        soldier.getAttackHelper().setGraphicHelper(helper);
        helper.setUpListeners();
        gHandler.addUpdatable(helper);
    }

}
