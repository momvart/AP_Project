package graphics.drawers;

import graphics.GraphicsValues;
import graphics.drawers.drawables.FrameAnimationDrawable;
import models.soldiers.Soldier;

import java.net.URISyntaxException;

public class SoldierDrawer extends AnimationDrawer
{
    public static final String IDLE = "idle";
    public static final String RUN = "run";
    public static final String ATTACK = "attack";
    public static final String DIE = "die";

    public SoldierDrawer(Soldier soldier) throws URISyntaxException
    {
        super(null);
        addAnimation(IDLE, new FrameAnimationDrawable(GraphicsValues.getSoldierFrames(soldier.getType(), soldier.getLevel(), IDLE), 1, 0.5, 1));
        addAnimation(RUN, new FrameAnimationDrawable(GraphicsValues.getSoldierFrames(soldier.getType(), soldier.getLevel(), RUN), 0.5, 0.5, 1));
        addAnimation(ATTACK, new FrameAnimationDrawable(GraphicsValues.getSoldierFrames(soldier.getType(), soldier.getLevel(), ATTACK), 0.5, 0.5, 1));
        addAnimation(DIE, new FrameAnimationDrawable(GraphicsValues.getSoldierFrames(soldier.getType(), soldier.getLevel(), DIE), 1, 0.5, 1));
        setDrawable(((FrameAnimationDrawable)animations.get(IDLE)).getFrame(0));
    }

    public void beeingHealedGlow()
    {
        //TODO
    }
}
