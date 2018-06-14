package graphics.drawers;

import graphics.GraphicsValues;
import graphics.drawers.drawables.Drawable;
import graphics.drawers.drawables.ImageDrawable;
import models.buildings.Building;
import utils.GraphicsUtilities;

import java.net.URISyntaxException;

public class BuildingDrawer extends LayerDrawer
{
    public BuildingDrawer(Building building) throws URISyntaxException
    {
        setPosition(building.getLocation().getX(), building.getLocation().getY());
        ImageDrawable baseImg = GraphicsValues.getBuildingImage(building.getType(), building.getLevel());
        Drawer base = new Drawer(baseImg);
        getDrawers().add(base);
    }
}
