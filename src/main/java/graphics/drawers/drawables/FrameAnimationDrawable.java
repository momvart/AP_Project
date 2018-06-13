package graphics.drawers.drawables;

import javafx.scene.canvas.GraphicsContext;

public class FrameAnimationDrawable extends AnimationDrawable
{
    private Drawable[] frames;

    public FrameAnimationDrawable(Drawable[] frames, double duration)
    {
        this(frames, duration, 0, 0);
    }

    public FrameAnimationDrawable(Drawable[] frames, double duration, double pivotX, double pivotY)
    {
        super(duration);
        if (frames.length == 0)
            throw new IllegalArgumentException("Frames cannot be empty.");
        this.frames = frames;
        this.setSize(frames[0].getWidth(), frames[0].getHeight());
        setPivot(pivotX, pivotY);
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

        frameNum = (int)(timeStack / getDuration() * frames.length);
    }

    @Override
    protected void onPreDraw(GraphicsContext gc)
    {
        frames[frameNum].onPreDraw(gc);
        super.onPreDraw(gc);
    }

    @Override
    public void onDraw(GraphicsContext gc)
    {
        frames[frameNum].onDraw(gc);
    }

    @Override
    protected void onPostDraw(GraphicsContext gc)
    {
        super.onPostDraw(gc);
        frames[frameNum].onPostDraw(gc);
    }
}
