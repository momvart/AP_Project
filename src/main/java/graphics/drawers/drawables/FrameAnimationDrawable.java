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
    }

    /**
     * Pivots of all frames will be set
     */
    @Override
    public void setPivot(double x, double y)
    {
        for (Drawable frame : frames)
            frame.setPivot(x, y);
    }

    public Drawable getFrame(int index)
    {
        return frames[index];
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
