package graphics.layers;

import graphics.Fonts;
import graphics.GraphicsValues;
import graphics.drawers.LayerDrawer;
import graphics.drawers.drawables.CircleDrawable;
import graphics.drawers.drawables.HProgressbarDrawable;
import graphics.drawers.drawables.ImageDrawable;
import graphics.drawers.drawables.TextDrawable;
import graphics.layers.Layer;
import graphics.drawers.Drawer;
import graphics.positioning.NormalPositioningSystem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import models.Resource;
import models.Village;
import utils.GraphicsUtilities;
import utils.RectF;

import java.util.function.Supplier;

public class ResourceLayer extends Layer
{
    private HProgressbarDrawable goldbar;
    private HProgressbarDrawable elixirbar;

    private TextDrawable lblGold;
    private TextDrawable lblElixir;

    private Supplier<Resource> totalCalculator;
    private Supplier<Resource> currentCalculator;

    public ResourceLayer(int order, RectF bounds, Supplier<Resource> currentCalculator, Supplier<Resource> totalCalculator)
    {
        super(order, bounds, new NormalPositioningSystem(1));
        this.totalCalculator = totalCalculator;
        this.currentCalculator = currentCalculator;
        initialize();
    }

    private void initialize()
    {

        //Gold
        try
        {
            goldbar = new HProgressbarDrawable(200, 25, Color.rgb(255, 215, 0, 1), true);
            goldbar.setStroke(Color.BLACK);
            goldbar.setBackground(Color.rgb(0, 0, 0, 0.5));
            goldbar.setPivot(1, 0.5);
            Drawer dProgress = new Drawer(goldbar);
            dProgress.setPosition(goldbar.getWidth(), goldbar.getHeight() / 2);

            ImageDrawable imgGold = GraphicsUtilities.createImageDrawable(GraphicsValues.UI_ASSETS_PATH + "/GoldCoin.png", goldbar.getHeight(), goldbar.getHeight(), true);
            imgGold.setPivot(1, 0.5);
            Drawer dIcon = new Drawer(imgGold);
            dIcon.setPosition(goldbar.getWidth(), dProgress.getPosition().getY());

            lblGold = new TextDrawable("", Color.WHITE, Fonts.getMedium());
            lblGold.setPivot(0.5, 0.5);
            lblGold.setHasShadow(true);
            Drawer dLabel = new Drawer(lblGold);
            dLabel.setPosition(goldbar.getWidth() / 2, goldbar.getHeight() / 2);

            LayerDrawer goldDrawer = new LayerDrawer(dProgress, dIcon, dLabel);
            goldDrawer.setPosition(GraphicsValues.PADDING, GraphicsValues.PADDING);
            goldDrawer.setLayer(this);
        }
        catch (Exception ignored) {}

        //Elixir
        try
        {
            elixirbar = new HProgressbarDrawable(200, 25, Color.web("#cf00dc"), true);
            elixirbar.setStroke(Color.BLACK);
            elixirbar.setBackground(Color.rgb(0, 0, 0, 0.5));
            elixirbar.setPivot(1, 0.5);
            Drawer dProgress = new Drawer(elixirbar);
            dProgress.setPosition(elixirbar.getWidth(), elixirbar.getHeight() / 2);

            ImageDrawable imgElixir = GraphicsUtilities.createImageDrawable(GraphicsValues.UI_ASSETS_PATH + "/ElixirDrop.png", elixirbar.getHeight(), elixirbar.getHeight(), true);
            imgElixir.setPivot(1, 0.5);
            Drawer dIcon = new Drawer(imgElixir);
            dIcon.setPosition(elixirbar.getWidth(), dProgress.getPosition().getY());

            lblElixir = new TextDrawable("", Color.WHITE, Fonts.getMedium());
            lblElixir.setPivot(0.5, 0.5);
            lblElixir.setHasShadow(true);
            Drawer dLabel = new Drawer(lblElixir);
            dLabel.setPosition(elixirbar.getWidth() / 2, elixirbar.getHeight() / 2);

            LayerDrawer elixirDrawer = new LayerDrawer(dProgress, dIcon, dLabel);
            elixirDrawer.setPosition(GraphicsValues.PADDING, GraphicsValues.PADDING + elixirbar.getHeight() + GraphicsValues.PADDING);
            elixirDrawer.setLayer(this);
        }
        catch (Exception ignored) {}
    }

    @Override
    public void draw(GraphicsContext gc, RectF cameraBounds)
    {
        Resource current = currentCalculator.get();
        lblGold.setText(String.format("%d", current.getGold()));
        lblElixir.setText(String.format("%d", current.getElixir()));
        Resource total = totalCalculator.get();
        goldbar.setProgress((double)current.getGold() / total.getGold());
        elixirbar.setProgress((double)current.getElixir() / total.getElixir());
        super.draw(gc, cameraBounds);
    }
}
