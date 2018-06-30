package graphics.gui;

import graphics.*;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.ButtonDrawable;
import graphics.drawers.drawables.RoundRectDrawable;
import graphics.drawers.drawables.TextDrawable;
import graphics.gui.dialogs.NumberInputDialog;
import graphics.gui.dialogs.SingleChoiceDialog;
import graphics.layers.Layer;
import graphics.layers.MenuLayer;
import graphics.layers.ToastLayer;
import graphics.positioning.NormalPositioningSystem;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import models.Map;
import utils.RectF;
import views.dialogs.DialogResult;

public abstract class GUIMapStage extends MapStage
{
    protected final double CELL_SIZE = height / 10;
    protected final double LINE_SIZE = height / 20;
    protected final double CHARACTER_SPACING = width / 100;

    private Canvas guiCanvas;
    private GameScene guiScene;
    private GraphicHandler guiHandler;
    private GameLooper guiLooper;

    private MenuLayer lMenu;
    private ToastLayer lInfo;
    private Layer lStuffs;


    public GUIMapStage(Map map, double width, double height)
    {
        super(map, width, height);

        lMenu = new MenuLayer(6, new RectF(0, height - CELL_SIZE - GraphicsValues.PADDING * 2, width, CELL_SIZE + 2 * GraphicsValues.PADDING), MenuLayer.Orientation.HORIZONTAL);
        lMenu.setItemCellSize(CELL_SIZE);

        lStuffs = new Layer(10, new RectF(GraphicsValues.PADDING, GraphicsValues.PADDING, width - 2 * GraphicsValues.PADDING, height - 2 * GraphicsValues.PADDING));
        lStuffs.setPosSys(new NormalPositioningSystem(CELL_SIZE + 2 * GraphicsValues.PADDING));
    }

    @Override
    protected void preShow(Group group)
    {
        super.preShow(group);

        guiCanvas = new Canvas(width * GraphicsValues.getScale(), height * GraphicsValues.getScale());
        group.getChildren().add(guiCanvas);

        guiHandler = new GraphicHandler(guiCanvas.getGraphicsContext2D(), new RectF(0, 0, guiCanvas.getWidth(), guiCanvas.getHeight()));
        guiScene = new GameScene(width, height);

        lInfo = new ToastLayer(7, new RectF(0, GraphicsValues.PADDING, width, height - 2 * GraphicsValues.PADDING), guiHandler);

        guiScene.addLayer(lMenu);
        guiScene.addLayer(lInfo);
        guiScene.addLayer(lStuffs);

        guiHandler.setScene(guiScene);

        guiLooper = new GameLooper(guiHandler);
        guiLooper.start();

        graphicHandlers.add(guiHandler);
    }


    public GameScene getGuiScene()
    {
        return guiScene;
    }

    public GraphicHandler getGuiHandler()
    {
        return guiHandler;
    }

    public MenuLayer getMenuLayer()
    {
        return lMenu;
    }

    public ToastLayer getInfoLayer()
    {
        return lInfo;
    }

    public Layer getStuffsLayer()
    {
        return lStuffs;
    }


    public void showInfo(String info)
    {
        lInfo.showText(info, guiHandler);
    }

    public DialogResult showSingleChoiceDialog(String message)
    {
        SingleChoiceDialog dialog = new SingleChoiceDialog(lStuffs, width, height, message);
        return dialog.showDialog();
    }

    public DialogResult showNumberInputDialog(String message, int count)
    {
        NumberInputDialog dialog = new NumberInputDialog(getStuffsLayer(), width, height, message, count);
        return dialog.showDialog();
    }

    @Override
    protected void onClose()
    {
        super.onClose();
        guiLooper.stop();
    }
}
