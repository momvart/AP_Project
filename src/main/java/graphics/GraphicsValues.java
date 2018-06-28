package graphics;

import graphics.drawers.SoldierDrawer;
import graphics.drawers.drawables.ImageDrawable;
import graphics.positioning.IsometricPositioningSystem;
import models.buildings.BuildingValues;
import models.soldiers.SoldierValues;
import utils.GraphicsUtilities;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class GraphicsValues
{
    public static final String BUILDINGS_ASSETS_PATH = "assets/buildings";
    public static final String BUILDINGS_DESTRUCTED_ASSETS_PATH = "assets/buildings/destructed";
    public static final String WALLS_ASSETS_PATH = "assets/buildings/wall";
    public static final String SOLDIERS_ASSETS_PATH = "assets/soldiers";
    public static final String UI_ASSETS_PATH = "assets/ui";
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

    public static String getSoldierAssetsPath(int soldierType)
    {
        return SOLDIERS_ASSETS_PATH + "/" + SoldierValues.getSoldierInfo(soldierType).getName().toLowerCase().replace(" ", "");
    }

    public static ImageDrawable[] getSoldierFrames(int soldierType, int level, String animKey, String face)
    {
        if (soldierFrames == null)
            soldierFrames = new ArrayList<>(Collections.nCopies(SoldierValues.SOLDIER_TYPES_COUNT, null));


        if (soldierFrames.get(soldierType - 1) == null)
            soldierFrames.set(soldierType - 1, new HashMap<>());

        HashMap<String, ImageDrawable[]> frames = soldierFrames.get(soldierType - 1);
        final String key = animKey + level + face;
        if (!frames.containsKey(key))
            try
            {
                frames.put(key, GraphicsUtilities.createFramesFrom(
                        Paths.get(getSoldierAssetsPath(soldierType), Integer.toString(level + 1), animKey.toLowerCase(), face.toLowerCase()).toString(),
                        50, 0, 0));
            }
            catch (Exception ex) { ex.printStackTrace();}

        return frames.get(key);
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
                        BUILDINGS_ASSETS_PATH, name.toLowerCase(), level),
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

    private static ImageDrawable imgConstruction;

    public static ImageDrawable getConstructionImage()
    {
        if (imgConstruction == null)
        {
            try
            {
                imgConstruction = GraphicsUtilities.createImageDrawable(BUILDINGS_ASSETS_PATH + "/construction1.png",
                        IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_COS * 2,
                        IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_SIN * 2, false);
            }
            catch (URISyntaxException ignored) { }
        }

        return imgConstruction;
    }

    private static ImageDrawable imgDestructed;

    public static ImageDrawable getDestructedImage()
    {
        if (imgDestructed == null)
            try
            {
                imgDestructed = GraphicsUtilities.createImageDrawable(BUILDINGS_DESTRUCTED_ASSETS_PATH + "/general.png",
                        IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_COS * 2,
                        IsometricPositioningSystem.sScale * IsometricPositioningSystem.ANG_SIN * 2, false);
            }
            catch (URISyntaxException ignored) { }

        return imgDestructed;
    }
}
