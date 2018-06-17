package graphics.drawers;

import graphics.GraphicsValues;
import graphics.GraphicsValues.WallStyle;
import graphics.drawers.drawables.ImageDrawable;
import models.Map;
import models.buildings.Wall;

public class WallDrawer extends BuildingDrawer
{
    private Map containigMap;

    public WallDrawer(Wall building, Map container)
    {
        super(building);
        this.containigMap = container;
    }

    @Override
    public void setUpDrawable()
    {
        WallStyle style;
        if (containigMap.getBuildingAt(getBuilding().getLocation().getX() + 1, getBuilding().getLocation().getY()) instanceof Wall && containigMap.getBuildingAt(getBuilding().getLocation().getX(), getBuilding().getLocation().getY() - 1) instanceof Wall)
            style = WallStyle.UpRight;
        else if (containigMap.getBuildingAt(getBuilding().getLocation().getX(), getBuilding().getLocation().getY() - 1) instanceof Wall)
            style = WallStyle.Up;
        else if (containigMap.getBuildingAt(getBuilding().getLocation().getX() + 1, getBuilding().getLocation().getY()) instanceof Wall)
            style = WallStyle.Right;
        else
            style = WallStyle.Static;

        ImageDrawable baseImg = GraphicsValues.getWallImage(style, getBuilding().getLevel());
        Drawer base = new Drawer(baseImg);
        getDrawers().add(base);
    }
}
