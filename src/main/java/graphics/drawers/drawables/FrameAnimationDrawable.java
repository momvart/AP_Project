package graphics.drawers.drawables;

import javafx.scene.canvas.GraphicsContext;
import utils.PointF;

public class FrameAnimationDrawable extends AnimationDrawable
{
    private Drawable[] frames;

    public FrameAnimationDrawable(Drawable[] frames, double duration)
    {
        super(duration);
        if (frames.length == 0)
            throw new IllegalArgumentException("Frames cannot be empty.");
        this.frames = frames;
        setPivot(0, 0);
    }

    /**
     * Pivots of all frames will be set
     */
    @Override
    public void setPivot(double x, double y)
    {
        super.setPivot(x, y);
        for (Drawable frame : frames)
            frame.setPivot(x, y);
    }

    /**
     * Rotation of all frames will be set
     */
    @Override
    public void setRotation(double angle)
    {
        super.setRotation(angle);
        for (Drawable frame : frames)
            frame.setRotation(angle);
    }

    /**
     * Rotation of all frames will be set
     */
    @Override
    public void setScale(double x, double y)
    {
        super.setScale(x, y);
        for (Drawable frame : frames)
            frame.setScale(x, y);
    }

    private int frameNum;

    @Override
    public void update(double deltaT)
    {
        if (isPaused())
            return;

        super.update(deltaT);

        if (timeStack >= getDuration())
            timeStack = 0;

        frameNum = (int)(timeStack / getDuration() * frames.length);
    }

    @Override
    public void onDraw(GraphicsContext gc)
    {
        frames[frameNum].draw(gc);
    }
}
