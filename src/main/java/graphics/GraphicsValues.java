package graphics;

import graphics.drawers.drawables.ImageDrawable;
import graphics.positioning.IsometricPositioningSystem;
import models.buildings.BuildingValues;
import models.soldiers.SoldierValues;
import utils.GraphicsUtilities;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GraphicsValues
{
    private static final String BUILDINGS_ASSETS_PATH = "assets/buildings";
    private static final String WALLS_ASSETS_PATH = "assets/buildings/wall";
    private static final String SOLDIERS_ASSETS_PATH = "assets/soldiers";
    public static final double PADDING = 10;

    private static double scale = 1;

    public static double getScale()
    {
        return scale;
    }

    public static void setScale(double scale)
    {
        GraphicsValues.scale = scale;
        Fonts.initialize();
    }


    private static ArrayList<HashMap<String, ImageDrawable[]>> soldierFrames;

    private static String getSoldierAssetsPath(int soldierType)
    {
        return SOLDIERS_ASSETS_PATH + "/" + SoldierValues.getSoldierInfo(soldierType).getName().toLowerCase();
    }

    public static ImageDrawable[] getSoldierFrames(int soldierType, int level, String animKey) throws URISyntaxException
    {
        if (soldierFrames == null)
            soldierFrames = new ArrayList<>(Collections.nCopies(SoldierValues.SOLDIER_TYPES_COUNT, null));


        if (soldierFrames.get(soldierType - 1) == null)
            soldierFrames.set(soldierType - 1, new HashMap<>());

        HashMap<String, ImageDrawable[]> frames = soldierFrames.get(soldierType - 1);
        if (!frames.containsKey(animKey + level))
        {
            frames.put(animKey + level, GraphicsUtilities.createFramesFrom(
                    getSoldierAssetsPath(soldierType) + "/" + (level + 1) + "/" + animKey.toLowerCase(),
                    50, 0, 0));
        }

        return frames.get(animKey + level);
    }

    private static HashMap<String, ImageDrawable> buildings;

    public static ImageDrawable getBuildingImage(int buildingType, int level)
    {
        if (buildings == null)
            buildings = new HashMap<>();

        String name = BuildingValues.getBuildingInfo(buildingType).getName().toLowerCase();
        if (!buildings.containsKey(name + level))
            try
            {
                buildings.put(name + level, GraphicsUtilities.createImageDrawable(String.format("%s/%s/%03d/001.png",
                        BUILDINGS_ASSETS_PATH, name, level),
                        IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_COS * 2,
                        IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_SIN * 2, false));
            }
            catch (URISyntaxException e) { e.printStackTrace(); }

        return buildings.get(name + level);
    }

    public enum WallStyle
    {
        UpRight,
        Right,
        Up,
        Static
    }

    private static HashMap<String, ImageDrawable> walls;

    public static ImageDrawable getWallImage(WallStyle style, int level)
    {
        if (walls == null)
            walls = new HashMap<>();

        if (walls.get(style.name() + level) == null)
            try
            {
                walls.put(style.name() + level, GraphicsUtilities.createImageDrawable(String.format("%s/%s/%03d/001.png",
                        WALLS_ASSETS_PATH, style.name().toLowerCase(), level),
                        IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_COS * 2,
                        IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_SIN * 2, true));
            }
            catch (URISyntaxException e)
            {
                e.printStackTrace();
            }

        return walls.get(style.name() + level);
    }
}
