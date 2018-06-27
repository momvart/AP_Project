package graphics.layers;

import graphics.GraphicHandler;
import graphics.drawers.drawables.animators.AlphaAnimator;
import graphics.drawers.drawables.animators.Animator;
import utils.RectF;

public class ToastLayer extends Layer
{
    private static final double SHOW_HIDE_DURATION = 1;
    private static final double DELAY_DURATION = 1;

    private AlphaAnimator shower;
    private Animator delayer;
    private AlphaAnimator hider;

    public ToastLayer(int order, RectF bounds, GraphicHandler gHandler)
    {
        super(order, bounds);

        shower = new AlphaAnimator(SHOW_HIDE_DURATION, 0, 1, this);
        shower.setOnFinish(() ->
        {
            gHandler.removeUpdatable(shower);
            delay(gHandler);
        });

        delayer = new Animator(DELAY_DURATION);
        delayer.setOnFinish(() ->
        {
            gHandler.removeUpdatable(delayer);
            hide(gHandler);
        });

        hider = new AlphaAnimator(SHOW_HIDE_DURATION, 1, 0, this);
        hider.setOnFinish(() ->
        {
            gHandler.removeUpdatable(hider);
            setVisible(false);
        });
    }

    public void show(GraphicHandler gHandler)
    {
        hider.stop();
        delayer.stop();
        setVisible(true);
        setAlpha(0);
        gHandler.addUpdatable(shower);
        shower.restart();
    }

    private void delay(GraphicHandler gHandler)
    {
        gHandler.addUpdatable(delayer);
        delayer.restart();
    }

    private void hide(GraphicHandler gHandler)
    {
        gHandler.addUpdatable(hider);
        hider.restart();
    }
}
