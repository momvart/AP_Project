package graphics.drawers.drawables;

import javafx.scene.canvas.GraphicsContext;

public class FrameAnimationDrawable extends AnimationDrawable
{
    private Drawable[] frames;
    private double frameDuration;

    public FrameAnimationDrawable(Drawable[] frames, double duration)
    {
        super(duration);
        if (frames.length == 0)
            throw new IllegalArgumentException("Frames cannot be empty.");
        this.frames = frames;
        this.frameDuration = duration / frames.length;
    }

    public void setFrameDuration(double frameDuration)
    {
        this.frameDuration = frameDuration;
    }

    private int frameNum;

    @Override
    public void update(double deltaT)
    {
        if (isPaused())
            return;

        super.update(deltaT);

        if (timeStack >= frameDuration)
        {
            frameNum++;
            timeStack = 0;
        }
        if (frameNum == frames.length)
            frameNum = 0;
    }

    @Override
    public void draw(GraphicsContext gc)
    {
        frames[frameNum].draw(gc);
    }
}
