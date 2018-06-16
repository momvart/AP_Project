package graphics.gui;

import graphics.GraphicsValues;
import graphics.Layer;
import graphics.drawers.Drawer;
import graphics.drawers.LayerDrawer;
import graphics.drawers.MenuDrawer;
import graphics.drawers.drawables.Drawable;
import graphics.drawers.drawables.ImageDrawable;
import graphics.drawers.drawables.RoundRectDrawable;
import graphics.drawers.drawables.bars.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import menus.BuildingSubmenu;
import menus.Menu;
import menus.ParentMenu;
import models.World;
import models.buildings.Building;
import utils.GraphicsUtilities;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class GraphicView
{

    private Layer layer;
    private static Drawer lastInfoBar;
    private static Drawer lastTopBar;
    private static Drawer lastRightBar;
    private static Drawer lastLeftBar;
    private static Drawer[] lastDownDrawer;
    private double width;
    private double height;

    public GraphicView(Layer layer, double width, double height)
    {
        this.layer = layer;
        this.width = width;
        this.height = height;
    }

    public void showTopBar()
    {
        Drawable topBar = new TopBarDrawable(width, height);
        Drawer drawer = new Drawer(topBar);
        drawer.setLayer(layer);
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
        Drawable bottomBarDrawable = new BottomBarDrawable(width, height, menu.getItems().size());
        Drawer drawer = new Drawer(bottomBarDrawable);
        MenuDrawable menuDrawable = new MenuDrawable(width, height, menu);
        MenuDrawer menuDrawer = new MenuDrawer(menuDrawable);
        menuDrawer.setLayer(layer);
        drawer.setLayer(layer);
    }

    public void showBottomBar(Building building)
    {
        BuildingSubmenu menu = building.getMenu(new ParentMenu(Menu.Id.VILLAGE_MAIN_MENU, ""));
        showBottomBar(menu);
    }

    public void showInfoBar(String text)
    {
        String[] split = text.split("\n");
        System.err.println(split.length);
        Drawable infoBar = new InfoBarDrawable(width, height, split.length);
        Drawer infoBarDrawer = new Drawer(infoBar);
        infoBarDrawer.setLayer(layer);
        System.out.println(infoBarDrawer.isVisible());
        for (int i = 0; i < split.length; i++)
        {
            String line = split[i];
            double textHeight = TopBarDrawable.HEIGHT_SCALE * height + InfoBarDrawable.LINE_SPACING_SCALE * height * (i + 0.5);
            Drawable textDrawable = new Drawable()
            {
                @Override
                protected void onDraw(GraphicsContext gc)
                {
                    gc.setFill(Color.WHITE);
                    gc.setTextAlign(TextAlignment.CENTER);
                    gc.setFont(Font.font("Dyuthi", 15));
                    gc.fillText(line, width / 2, textHeight);
                }
            };
            Drawer drawer = new Drawer(textDrawable);
            drawer.setLayer(layer);
        }
    }

    public void showLeftBar()
    {
        ArrayList<Integer> buildingTypes = new ArrayList<>();
        World.getVillage().getBuildings().forEach(building ->
        {
            if (!buildingTypes.contains(building.getType()))
                buildingTypes.add(building.getType());
        });
        int buildingTypesCount = buildingTypes.size();
        double tileSize;
        Drawable leftBar = new LeftBarDrawable(width, height, buildingTypesCount);
        double boxWidth = 0.04 * width;
        Drawer drawer = new Drawer(leftBar);
        drawer.setLayer(layer);
        if (buildingTypesCount <= 15)
            tileSize = height / 20;
        else
            tileSize = height / buildingTypesCount * 0.8;
        Drawable[] drawables = new Drawable[buildingTypesCount];
        Drawer[] drawers = new Drawer[buildingTypesCount];
        for (int i = 0; i < buildingTypesCount; i++)
        {
            drawables[i] = new ImageDrawable(new Image("assets/soldiers/guardian/1/idle/001.png"), tileSize, tileSize);
            drawables[i].setPivot(0.5, 0.5);
            drawers[i] = new Drawer(drawables[i]);
            drawers[i].setPosition(boxWidth / 2, height / 2 + (i - buildingTypesCount / 2 + (buildingTypesCount % 2 == 1 ? 0 : 0.5)) * height / 15);
            Building building;
            building = World.getVillage().getMap().getBuildings(buildingTypes.get(i)).getMin();
            drawers[i].setClickListener((sender, mouseEvent) ->
                    showBottomBar(building));
            drawers[i].setLayer(layer);
        }
    }

    public void showRightBar()
    {
        ArrayList<Integer> soldiersTypeList = new ArrayList<>();
        for (int i = 1; i <= World.getVillage().getSoldiers().getListsCount(); i++)
            if (World.getVillage().getSoldiers(i).size() > 0)
                soldiersTypeList.add(i);
        int soldiersTypeCount = soldiersTypeList.size() + 8;
        Drawable rightBar = new RightBarDrawable(width, height, soldiersTypeCount);
        double boxWidth = 0.04 * width;
        Drawer drawer = new Drawer(rightBar);
        drawer.setLayer(layer);
        double tileSize = height / 20;
        Drawable[] drawables = new Drawable[soldiersTypeCount];
        Drawer[] drawers = new Drawer[soldiersTypeCount];
        for (int i = 0; i < soldiersTypeCount; i++)
        {
            drawables[i] = new ImageDrawable(new Image("assets/soldiers/guardian/1/idle/00" + (i + 1) + ".png"), tileSize, tileSize);
            drawables[i].setPivot(0.5, 0.5);
            drawers[i] = new Drawer(drawables[i]);
            drawers[i].setPosition(width - boxWidth / 2, height / 2 + (i - soldiersTypeCount / 2 + (soldiersTypeCount % 2 == 1 ? 0 : 0.5)) * height / 15);
            drawers[i].setLayer(layer);
        }
    }

    public void showRightBar2()
    {
        final double cellSize = 50;
        final double padding = 5;

        int soldiersTypeCount = 5;

        LayerDrawer drawer = new LayerDrawer();

        Drawable bg = new RoundRectDrawable(cellSize + 2 * padding, (cellSize + padding) * soldiersTypeCount + padding, 10, Color.rgb(0, 0, 0, .6));
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
            catch (URISyntaxException ex) {}
        }

        drawer.setPosition(width - cellSize - 2 * padding, (height - bg.getHeight()) / 2);
        drawer.setLayer(layer);
    }


}
