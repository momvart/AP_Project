package graphics.drawers.drawables;

import graphics.IFrameUpdatable;
import javafx.scene.canvas.GraphicsContext;

public abstract class AnimationDrawable extends Drawable implements IFrameUpdatable
{
    private boolean paused;

    protected double timeStack;
    private double duration;

    public AnimationDrawable(double duration)
    {
        this.duration = duration;
    }

    public boolean isPaused()
    {
        return paused;
    }

    public void pause() { this.paused = true; }

    public void start() { this.paused = false; }

    public void restart()
    {
        this.paused = false;
        timeStack = 0;
    }


    public double getDuration()
    {
        return duration;
    }

    public void setDuration(double duration)
    {
        this.duration = duration;
    }

    public void update(double deltaT)
    {
        if (isPaused())
            return;

        timeStack += deltaT;
    }
}
