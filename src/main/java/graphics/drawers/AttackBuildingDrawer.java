package graphics.drawers;

import graphics.GraphicsValues;
import graphics.drawers.drawables.HealthHProgressbarDrawable;
import graphics.drawers.drawables.animators.AlphaAnimator;
import graphics.drawers.drawables.animators.ScaleAnimator;
import graphics.gui.MapStage;
import javafx.scene.paint.Color;
import models.Map;
import models.buildings.Building;

public class AttackBuildingDrawer extends BuildingDrawer
{
    private HealthHProgressbarDrawable strengthbar;
    private Drawer strengthbarDrawer;

    public AttackBuildingDrawer(Building building, Map map)
    {
        super(building, map);
    }

    @Override
    protected void initialize()
    {
        super.initialize();

        strengthbar = new HealthHProgressbarDrawable(20, 5, Color.BLACK);
        strengthbar.setPivot(0.5, 1);
        strengthbarDrawer = new Drawer(strengthbar);
        strengthbarDrawer.setPosition(0, 0);
        strengthbarDrawer.setVisible(false);
        getDrawers().add(strengthbarDrawer);
    }

    public void healthDecreseBarLoading(int initialHealth, int finalHealth)
    {
        //TODO
    }

    public void destroyBuilding()
    {
        System.out.println("destroyed" + getBuilding().toString());
        base.setDrawable(GraphicsValues.getDestructedImage());
        strengthbarDrawer.setVisible(false);

        Drawer dustDrawer = new Drawer(GraphicsValues.getDustImage());
        getDrawers().add(dustDrawer);
        ScaleAnimator scaler = new ScaleAnimator(0.5, 0, 1, dustDrawer.getDrawable());
        AlphaAnimator alpha = new AlphaAnimator(0.5, 1, 0, dustDrawer.getDrawable());
        AlphaAnimator baseAlpha = new AlphaAnimator(0.5, 0, 1, base.getDrawable());
        scaler.start();
        alpha.start();
        baseAlpha.start();
        addUpdatable(scaler);
        addUpdatable(alpha);
        addUpdatable(baseAlpha);
        alpha.setOnFinish(() ->
        {
            getDrawers().remove(dustDrawer);
            removeUpdatable(scaler);
            removeUpdatable(alpha);
            removeUpdatable(baseAlpha);
            getLayer().removeObject(this);
            setLayer(getLayer().getScene().getLayers().get(MapStage.FLOOR_LAYER_ORDER));
        });
    }

    @Override
    public void updateDrawer()
    {
        super.updateDrawer();
        if (getBuilding().getAttackHelper().getStrength() < getBuilding().getAttackHelper().getInitialStrength() && !getBuilding().getAttackHelper().isDestroyed())
        {
            strengthbarDrawer.setVisible(true);
            strengthbar.setProgress((double)getBuilding().getAttackHelper().getStrength() / getBuilding().getAttackHelper().getInitialStrength());
        }
    }
}
