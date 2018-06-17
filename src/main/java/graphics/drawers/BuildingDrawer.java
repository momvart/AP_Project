package graphics.drawers;

import graphics.GraphicsValues;
import graphics.drawers.drawables.ImageDrawable;
import models.buildings.Building;

public class BuildingDrawer extends LayerDrawer
{
    private Building building;

    public BuildingDrawer(Building building)
    {
        this.building = building;
        setPosition(building.getLocation().getX(), building.getLocation().getY());
    }

    public void setUpDrawable()
    {
        ImageDrawable baseImg = GraphicsValues.getBuildingImage(getBuilding().getType(), getBuilding().getLevel());
        Drawer base = new Drawer(baseImg);
        getDrawers().add(base);
    }

    public Building getBuilding()
    {
        return building;
    }


    public void playDestroyAnimation()
    {
        //TODO
    }

    public void healthDecreseBarLoading(int initialHealth, int finalHealth)
    {
        //TODO
    }
}
