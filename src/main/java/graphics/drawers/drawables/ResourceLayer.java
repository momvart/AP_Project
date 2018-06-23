package graphics.drawers.drawables;

import graphics.GraphicsValues;
import graphics.Layer;
import graphics.drawers.Drawer;
import graphics.positioning.NormalPositioningSystem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import models.Village;
import utils.RectF;

public class ResourceLayer extends Layer
{
    private Village village;
    private HProgressbarDrawable goldbar;
    private HProgressbarDrawable elixirbar;

    public ResourceLayer(int order, RectF bounds, Village village)
    {
        super(order, bounds, new NormalPositioningSystem(1));
        this.village = village;
        initialize();
    }

    private void initialize()
    {
        goldbar = new HProgressbarDrawable(Color.GOLD);
        elixirbar = new HProgressbarDrawable(Color.PURPLE);
        goldbar.setStroke(Color.SILVER);
        elixirbar.setStroke(Color.SILVER);
        goldbar.setSize(100, 10);
        elixirbar.setSize(100, 10);
        goldbar.setPivot(1, 0);
        elixirbar.setPivot(1, 0);
        Drawer gDrawer = new Drawer(goldbar);
        Drawer eDrawer = new Drawer(elixirbar);
        gDrawer.setPosition(GraphicsValues.PADDING, GraphicsValues.PADDING);
        eDrawer.setPosition(GraphicsValues.PADDING, getBounds().getHeight() - GraphicsValues.PADDING);
        gDrawer.setLayer(this);
        eDrawer.setLayer(this);
    }

    @Override
    public void draw(GraphicsContext gc, RectF cameraBounds)
    {
        goldbar.setProgress(village.getResources().getGold() / (village.getTotalResourceCapacity().getGold()));
        elixirbar.setProgress(village.getResources().getElixir() * 1.0 / (village.getTotalResourceCapacity().getElixir()));
        super.draw(gc, cameraBounds);
    }
}
