package graphics.drawers.drawables;

import graphics.Fonts;
import graphics.GraphicsValues;
import graphics.layers.Layer;
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
    private Drawer goldTextDrawer;
    private Drawer elixirTextDrawer;

    public ResourceLayer(int order, RectF bounds, Village village)
    {
        super(order, bounds, new NormalPositioningSystem(1));
        this.village = village;
        initialize();
    }

    private void initialize()
    {
        goldbar = new HProgressbarDrawable(Color.rgb(255, 215, 0, 1));
        elixirbar = new HProgressbarDrawable(Color.rgb(138, 43, 226, 1));
        goldbar.setStroke(Color.rgb(128, 128, 128, 1));
        elixirbar.setStroke(Color.rgb(128, 128, 128, 1));
        goldbar.setSize(200, 25);
        elixirbar.setSize(200, 25);
        goldbar.setPivot(1, 0);
        elixirbar.setPivot(1, 0);
        Drawer gDrawer = new Drawer(goldbar);
        Drawer eDrawer = new Drawer(elixirbar);
        gDrawer.setPosition(GraphicsValues.PADDING, GraphicsValues.PADDING);
        eDrawer.setPosition(GraphicsValues.PADDING, getBounds().getHeight() - GraphicsValues.PADDING);
        gDrawer.setLayer(this);
        eDrawer.setLayer(this);

        CircleDrawable gold = new CircleDrawable(goldbar.getHeight(), Color.BROWN);
        CircleDrawable elixir = new CircleDrawable(elixirbar.getHeight(), Color.PINK);
        Drawer goldD = new Drawer(gold);
        Drawer elixirD = new Drawer(elixir);
        gold.setPivot(0.5, 0.5);
        elixir.setPivot(0.5, 0.5);
        goldD.setPosition(-goldbar.getWidth(), goldbar.getHeight() / 2 + GraphicsValues.PADDING);
        elixirD.setPosition(-elixirbar.getWidth(), getBounds().getHeight() + elixirbar.getHeight() / 2 - GraphicsValues.PADDING);
        goldD.setLayer(this);
        elixirD.setLayer(this);
    }

    @Override
    public void draw(GraphicsContext gc, RectF cameraBounds)
    {
        removeObject(goldTextDrawer);
        removeObject(elixirTextDrawer);
        TextDrawable gold = new TextDrawable(String.format("%d", village.getResources().getGold()), Color.BLACK, Fonts.getMedium());
        TextDrawable elixir = new TextDrawable(String.format("%d", village.getResources().getElixir()), Color.WHITE, Fonts.getMedium());
        goldTextDrawer = new Drawer(gold);
        elixirTextDrawer = new Drawer(elixir);
        goldbar.setProgress(village.getResources().getGold() * 1.0 / (village.getTotalResourceCapacity().getGold()));
        elixirbar.setProgress(village.getResources().getElixir() * 1.0 / (village.getTotalResourceCapacity().getElixir()));
        gold.setPivot(0.5, 0);
        elixir.setPivot(0.5, 0);
        goldTextDrawer.setPosition(-goldbar.getWidth() / 2, goldbar.getHeight() / 2 + gc.getLineWidth());
        elixirTextDrawer.setPosition(-elixirbar.getWidth() / 2, getBounds().getHeight() - GraphicsValues.PADDING / 2);
        goldTextDrawer.setLayer(this);
        elixirTextDrawer.setLayer(this);
        super.draw(gc, cameraBounds);
    }
}
