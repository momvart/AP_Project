package graphics.drawers;

import graphics.GraphicsValues;
import graphics.drawers.drawables.ImageDrawable;
import models.Map;
import models.buildings.BuildStatus;
import models.buildings.Building;

public class VillageBuildingDrawer extends BuildingDrawer
{
    private Drawer construction;

    public VillageBuildingDrawer(Building building, Map map)
    {
        super(building, map);

        ImageDrawable imgConstruction = GraphicsValues.getConstructionImage();
        imgConstruction.setPivot(0.5, 0.5);
        construction = new Drawer(imgConstruction);
        getDrawers().add(construction);
    }

    @Override
    public void updateDrawer()
    {
        super.updateDrawer();

        if (getBuilding().getBuildStatus() == BuildStatus.IN_CONSTRUCTION)
            construction.setVisible(true);
        else
            construction.setVisible(false);
    }
}
