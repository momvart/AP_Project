package graphics.drawers;

import graphics.GraphicsValues;
import graphics.drawers.drawables.HProgressbarDrawable;
import javafx.scene.paint.Color;
import models.Map;
import models.buildings.Building;

public class AttackBuildingDrawer extends BuildingDrawer
{
    private Color[] strengthColors = new Color[] { Color.RED, Color.ORANGE, Color.YELLOW, Color.FORESTGREEN, Color.FORESTGREEN };
    private HProgressbarDrawable strengthbar;
    private Drawer strengthbarDrawer;

    public AttackBuildingDrawer(Building building, Map map)
    {
        super(building, map);
    }

    @Override
    protected void initialize()
    {
        super.initialize();

        strengthbar = new HProgressbarDrawable(20, 5, Color.BLACK);
        strengthbar.setPivot(0.5, 1);
        strengthbarDrawer = new Drawer(strengthbar);
        strengthbarDrawer.setPosition(0, base.getDrawable().getHeight() / 2);
        strengthbarDrawer.setVisible(false);
        getDrawers().add(strengthbarDrawer);
    }

    public void playDestroyAnimation()
    {
        //TODO
    }

    public void healthDecreseBarLoading(int initialHealth, int finalHealth)
    {
        //TODO
    }

    @Override
    public void updateDrawer()
    {
        super.updateDrawer();

        if (getBuilding().getAttackHelper().isDestroyed())
        {
            base.setDrawable(GraphicsValues.getDestructedImage());
            strengthbarDrawer.setVisible(false);
        }

        if (getBuilding().getAttackHelper().getStrength() < getBuilding().getAttackHelper().getStartStrength() && !getBuilding().getAttackHelper().isDestroyed())
        {
            strengthbarDrawer.setVisible(true);
            strengthbar.setProgress((double)getBuilding().getAttackHelper().getStrength() / getBuilding().getAttackHelper().getStartStrength());
            strengthbar.setFill(strengthColors[(int)(strengthbar.getProgress() / 0.25)]);
        }
    }
}
