package graphics;

import javafx.animation.AnimationTimer;
import models.World;

public class GameLooper extends AnimationTimer
{
    private IFrameUpdatable handler;

    private long previousNow;

    public GameLooper(IFrameUpdatable handler)
    {
        this.handler = handler;
    }

    @Override
    public void handle(long now)
    {
        double deltaT = (now - previousNow) * World.sSettings.getGameSpeed() / (double)1e9;
        previousNow = now;
        if (deltaT > 1)
            return;
        handler.update(deltaT);
    }

    @Override
    public void start()
    {
        previousNow = System.currentTimeMillis();
        super.start();
    }
}
