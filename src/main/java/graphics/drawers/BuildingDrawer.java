package graphics.drawers;

import graphics.GraphicsValues;
import graphics.drawers.drawables.ImageDrawable;
import models.Map;
import models.buildings.BuildStatus;
import models.buildings.Building;
import models.buildings.Wall;

public class BuildingDrawer extends LayerDrawer
{
    private Map containingMap;
    private Building building;

    protected Drawer base;

    public BuildingDrawer(Building building, Map map)
    {
        this.building = building;
        this.containingMap = map;

        initialize();
    }

    protected void initialize()
    {
        setPosition(building.getLocation().getX(), building.getLocation().getY());
        base = new Drawer(fetchBaseImage());
        getDrawers().add(base);
    }

    public Building getBuilding()
    {
        return building;
    }


    protected ImageDrawable fetchBaseImage()
    {
        if (building instanceof Wall)
        {
            GraphicsValues.WallStyle style;
            if (containingMap.getBuildingAt(getBuilding().getLocation().getX() + 1, getBuilding().getLocation().getY()) instanceof Wall && containingMap.getBuildingAt(getBuilding().getLocation().getX(), getBuilding().getLocation().getY() - 1) instanceof Wall)
                style = GraphicsValues.WallStyle.UpRight;
            else if (containingMap.getBuildingAt(getBuilding().getLocation().getX(), getBuilding().getLocation().getY() - 1) instanceof Wall)
                style = GraphicsValues.WallStyle.Up;
            else if (containingMap.getBuildingAt(getBuilding().getLocation().getX() + 1, getBuilding().getLocation().getY()) instanceof Wall)
                style = GraphicsValues.WallStyle.Right;
            else
                style = GraphicsValues.WallStyle.Static;

            return GraphicsValues.getWallImage(style, getBuilding().getLevel());
        }
        else
            return GraphicsValues.getBuildingImage(getBuilding().getType(), getBuilding().getLevel());
    }

    public void updateDrawer()
    {
        System.err.println("Update requested !");
        base.setDrawable(fetchBaseImage());
    }
}
