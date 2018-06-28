package graphics.drawers;

import graphics.GraphicsValues;
import graphics.drawers.drawables.FrameAnimationDrawable;
import javafx.scene.canvas.GraphicsContext;
import models.soldiers.Soldier;

public class SoldierDrawer extends LayerDrawer
{
    public static final String IDLE = "idle";
    public static final String RUN = "run";
    public static final String ATTACK = "attack";
    public static final String DIE = "die";

    private String lastAnim = IDLE;

    private Soldier soldier;

    private AnimationDrawer base;

    public SoldierDrawer(Soldier soldier)
    {
        super();

        this.soldier = soldier;
        base = new AnimationDrawer(null);
        getDrawers().add(base);
        setAnimations();
    }

    public void beeingHealedGlow()
    {
        //TODO
    }

    public enum Face
    {
        UP,
        DOWN,
        RIGHT,
        LEFT
    }

    private Face face = Face.DOWN;

    public Face getFace()
    {
        return face;
    }

    public void setFace(Face face)
    {
        this.face = face;
        setAnimations();

        System.out.println(face);
    }

    public void setFace(double deltaX, double deltaY)
    {
        if (deltaX < 0)
            setFace(Face.LEFT);
        else if (deltaX > 0)
            setFace(Face.RIGHT);
        else if (deltaY < 0)
            setFace(Face.UP);
        else if (deltaY > 0)
            setFace(Face.DOWN);
    }

    private void setAnimations()
    {
        String strFace = face == Face.UP || face == Face.RIGHT ? "right" : "down";
//        base.addAnimation(IDLE, new FrameAnimationDrawable(GraphicsValues.getSoldierFrames(soldier.getType(), soldier.getLevel(), IDLE, face), 1, 0.5, 1));
        base.addAnimation(RUN, new FrameAnimationDrawable(GraphicsValues.getSoldierFrames(soldier.getType(), soldier.getLevel(), RUN, strFace), 0.5, 0.5, 1));
        base.addAnimation(ATTACK, new FrameAnimationDrawable(GraphicsValues.getSoldierFrames(soldier.getType(), soldier.getLevel(), ATTACK, strFace), 0.5, 0.5, 1));
//        base.addAnimation(DIE, new FrameAnimationDrawable(GraphicsValues.getSoldierFrames(soldier.getType(), soldier.getLevel(), DIE, face), 1, 0.5, 1));

        playAnimation(lastAnim);

    }

    public void playAnimation(String key)
    {
        this.lastAnim = key;
        base.playAnimation(key);
    }

    @Override
    protected void onPreDraw(GraphicsContext gc)
    {
        super.onPreDraw(gc);
        gc.save();
        gc.scale(face == Face.UP || face == Face.LEFT ? -1 : 1, 1);
    }

    @Override
    protected void onPostDraw(GraphicsContext gc)
    {
        gc.restore();
        super.onPostDraw(gc);
    }
}
