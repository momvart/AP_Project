package graphics.gui;

import graphics.layers.Layer;
import graphics.layers.MenuLayer;
import graphics.drawers.Drawer;
import graphics.drawers.LayerDrawer;
import graphics.drawers.drawables.Drawable;
import graphics.drawers.drawables.ImageDrawable;
import graphics.drawers.drawables.RoundRectDrawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import menus.Menu;
import menus.ParentMenu;
import models.World;
import models.buildings.Building;
import utils.GraphicsUtilities;
import utils.RectF;

import java.net.URISyntaxException;
import java.util.ArrayList;


public class GraphicView
{

    private Layer layer;
    private double width;
    private double height;
    private MenuLayer lmenu;

    public GraphicView(Layer layer, double width, double height)
    {
        this.layer = layer;
        this.width = width;
        this.height = height;
        lmenu = new MenuLayer(5, new RectF(0, height - 100, width, 100), MenuLayer.Orientation.HORIZONTAL);
    }

    public MenuLayer getLmenu()
    {
        return lmenu;
    }

    public double getWidth()
    {
        return width;
    }

    public double getHeight()
    {
        return height;
    }

    public Layer getLayer()
    {
        return layer;
    }

    public void showTopBar()
    {
        Drawable resources = new Drawable()
        {
            @Override
            protected void onDraw(GraphicsContext gc)
            {
                gc.setFill(Color.WHITE);
                gc.setTextAlign(TextAlignment.LEFT);
                gc.fillText(World.getVillage().getResources().toString(true), width / 20, height / 40);
            }
        };
        Drawable settings = new Drawable()
        {
            @Override
            protected void onDraw(GraphicsContext gc)
            {
                gc.setFill(Color.WHITE);
                gc.setTextAlign(TextAlignment.RIGHT);
                gc.fillText("Settings", 9 * width / 10, height / 40);
            }
        };
        Drawer resDrawer = new Drawer(resources);
        Drawer setDrawer = new Drawer(settings);
        resDrawer.setLayer(layer);
        setDrawer.setLayer(layer);
        setDrawer.setClickListener((sender, mouseEvent) ->
        {
            System.err.println("setting");
        });
    }

    public void showBottomBar(ParentMenu menu)
    {
        lmenu.setCurrentMenu(menu);
    }

    public void showBottomBar(Building building)
    {
        showBottomBar(building.getMenu(new ParentMenu(Menu.Id.VILLAGE_MAIN_MENU, "")));
    }

    public void showInfoBar(String text)
    {

    }

    public void showLeftBar()
    {
        final double cellSize = 50;
        final double padding = 5;
        ArrayList<Integer> buildingTypes = new ArrayList<>();
        World.getVillage().getBuildings().forEach(building ->
        {
            if (!buildingTypes.contains(building.getType()))
                buildingTypes.add(building.getType());
        });
        int buildingTypesCount = buildingTypes.size();

        Drawable bg = new RoundRectDrawable(cellSize + 2 * padding, (cellSize + padding) * buildingTypesCount + padding, 10, Color.rgb(0, 0, 0, .6));
        bg.setPivot(1, 0);
        LayerDrawer drawer = new LayerDrawer();
        drawer.getDrawers().add(new Drawer(bg));

        for (int i = 0; i < buildingTypesCount; i++)
        {
            try
            {
                ImageDrawable img = GraphicsUtilities.createImageDrawable("assets/soldiers/guardian/1/idle/001.png", cellSize, cellSize, false);
                img.setPivot(0.5, 0.5);
                Drawer d = new Drawer(img);
                Building building;
                building = World.getVillage().getMap().getBuildings(buildingTypes.get(i)).getMin();
                System.err.println(building.getName());
                d.setPosition(bg.getWidth() / 2, (i) * (cellSize + padding) + (cellSize / 2 + padding) + bg.getHeight());
                d.setClickListener((sender, event) ->
                        showBottomBar(building));
                d.setLayer(layer);
            }
            catch (URISyntaxException ignored) {}
        }

        drawer.setPosition(cellSize + 2 * padding, (height - bg.getHeight()) / 2);

        drawer.setLayer(layer);
    }


    public void showRightBar()
    {
        final double cellSize = 50;
        final double padding = 5;
        int soldiersTypeCount = 5;
        Drawable bg = new RoundRectDrawable(cellSize + 2 * padding, (cellSize + padding) * soldiersTypeCount + padding, 10, Color.rgb(0, 0, 0, .6));

        LayerDrawer drawer = new LayerDrawer();

        drawer.getDrawers().add(new Drawer(bg));

        for (int i = 0; i < soldiersTypeCount; i++)
        {
            try
            {
                ImageDrawable img = GraphicsUtilities.createImageDrawable("assets/soldiers/guardian/1/idle/001.png", cellSize, cellSize, false);
                img.setPivot(.5, .5);
                Drawer d = new Drawer(img);
                d.setPosition(bg.getWidth() / 2, (i) * (cellSize + padding) + (cellSize / 2 + padding));
                drawer.getDrawers().add(d);
            }
            catch (URISyntaxException ignored) {}
        }

        drawer.setPosition(width - cellSize - 2 * padding, (height - bg.getHeight()) / 2);
        drawer.setLayer(layer);
    }


}
