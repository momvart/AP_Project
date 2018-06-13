package graphics.gui;

import graphics.Layer;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.Drawable;
import graphics.drawers.drawables.ImageDrawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import models.World;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class GVillageView
{

    private Layer layer;

    private double width;
    private double height;


    public GVillageView(Layer layer, double width, double height)
    {
        this.layer = layer;
        this.height = height;
        this.width = width;
    }

    public void showTopBar()
    {
        Drawable topBar = new Drawable()
        {
            @Override
            protected void onDraw(GraphicsContext gc)
            {
                //region topBar
                gc.setFill(Color.rgb(0, 0, 0, 0.6));
                gc.fillRect(0, 0, width, height / 25);
                //endregion

                //region time
                gc.setFont(Font.font("Dyuthi", FontWeight.BOLD, 10));
                gc.setFill(Color.WHITE);
                gc.setTextAlign(TextAlignment.CENTER);
                DateFormat df = new SimpleDateFormat("HH:mm");
                Calendar calobj = Calendar.getInstance();
                gc.fillText(df.format(calobj.getTime()), width / 2, height / 40);
                //endregion

                //region resources
                gc.setTextAlign(TextAlignment.LEFT);
                gc.fillText(World.getVillage().getResources().toString(true), width / 20, height / 40);
                //endregion

                //region settings
                gc.setTextAlign(TextAlignment.RIGHT);
                gc.fillText("Settings", 9 * width / 10, height / 40);
                //endregion
            }
        };
        Drawer drawer = new Drawer(topBar);
        drawer.setLayer(layer);
    }

    public void showInfoBar(String text)
    {
        Drawable infoBar = new Drawable()
        {
            @Override
            protected void onDraw(GraphicsContext gc)
            {
                gc.setFill(Color.rgb(0, 0, 0, 0.6));
                String[] split = text.split("\n");
                int lineCount = split.length + 1;
                double boxWidth = 0.4 * width;
                double boxHeight = (lineCount + 1) * 0.03 * height;
                gc.fillRoundRect(width / 2 - boxWidth / 2, height - boxHeight, boxWidth, boxHeight, 10, 10);
                gc.setFont(Font.font("Lato", 15));
                gc.setTextAlign(TextAlignment.CENTER);
                gc.setFill(Color.WHITE);
                for (int i = 0; i < split.length; i++)
                    gc.fillText(split[i], width / 2, height - boxHeight + (i + 1) * 0.03 * height);
            }
        };
        Drawer drawer = new Drawer(infoBar);
        drawer.setLayer(layer);
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
        Drawable leftBar = new Drawable()
        {
            @Override
            protected void onDraw(GraphicsContext gc)
            {
                double boxWidth = 0.04 * width;
                gc.setFill(Color.rgb(0, 0, 0, 0.6));
                double boxHeight;
                if (buildingTypesCount <= 15)
                    boxHeight = buildingTypesCount * height / 15;
                else
                    boxHeight = height;
                gc.fillRoundRect(0, height / 2 - boxHeight / 2, boxWidth, boxHeight, 10, 10);
            }
        };
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
        Drawable rightBar = new Drawable()
        {
            @Override
            protected void onDraw(GraphicsContext gc)
            {
                double boxWidth = 0.04 * width;
                gc.setFill(Color.rgb(0, 0, 0, 0.6));
                double boxHeight;
                boxHeight = soldiersTypeCount * height / 15;
                gc.fillRoundRect(width - boxWidth, height / 2 - boxHeight / 2, boxWidth, boxHeight, 10, 10);
            }
        };
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
}
