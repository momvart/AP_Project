package graphics.drawers;

import graphics.GraphicsValues;
import graphics.drawers.drawables.FrameAnimationDrawable;
import graphics.drawers.drawables.HealthHProgressbarDrawable;
import graphics.drawers.drawables.ImageDrawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import models.soldiers.Soldier;

public class SoldierDrawer extends LayerDrawer
{
    public static final String IDLE = "idle";
    public static final String RUN = "run";
    public static final String ATTACK = "attack";
    //public static final String DIE = "die";

    private String lastAnim = IDLE;

    private Soldier soldier;

    private AnimationDrawer base;

    private HealthHProgressbarDrawable healthbar;
    private Drawer healthbarDrawer;

    public SoldierDrawer(Soldier soldier)
    {
        super();
        this.soldier = soldier;

        initialize();
    }

    protected void initialize()
    {
        base = new AnimationDrawer(null);
        getDrawers().add(base);
        setAnimations();

        healthbar = new HealthHProgressbarDrawable(20, 5, Color.BLACK);
        healthbar.setPivot(0.5, 1);
        healthbarDrawer = new Drawer(healthbar);
//        healthbarDrawer.setPosition(0, base.getAnimations().values().stream().findFirst().get().getHeight() / 2);
        healthbarDrawer.setPosition(0, 0);
        healthbarDrawer.setVisible(false);
        getDrawers().add(healthbarDrawer);
    }

    public Soldier getSoldier()
    {
        return soldier;
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
        if (this.face == face)
            return;
        this.face = face;
        setAnimations();
    }

    public void setFace(double deltaX, double deltaY)
    {
        boolean updown = Math.abs(deltaY) > Math.abs(deltaX);

        if (updown)
            setFace(deltaY > 0 ? Face.DOWN : Face.UP);
        else
            setFace(deltaX > 0 ? Face.RIGHT : Face.LEFT);
    }

    private void setAnimations()
    {
        String strFace = face == Face.UP || face == Face.RIGHT ? "right" : "down";
        try
        {
            base.addAnimation(IDLE, new FrameAnimationDrawable(GraphicsValues.getSoldierFrames(soldier.getType(), soldier.getLevel(), IDLE, strFace), 1, 0, 0));
        }
        catch (Exception ignored) {}
        ImageDrawable[] frames = GraphicsValues.getSoldierFrames(soldier.getType(), soldier.getLevel(), RUN, strFace);
        base.addAnimation(RUN, new FrameAnimationDrawable(frames, frames.length * 0.1, 0, 0));
        frames = GraphicsValues.getSoldierFrames(soldier.getType(), soldier.getLevel(), ATTACK, strFace);
        base.addAnimation(ATTACK, new FrameAnimationDrawable(frames, frames.length * 0.1, 0, 0));
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

    public void updateDrawer()
    {
        if (!getSoldier().getAttackHelper().isDead() && getSoldier().getAttackHelper().getHealth() < getSoldier().getAttackHelper().getInitialHealth())
        {
            healthbarDrawer.setVisible(true);
            healthbar.setProgress((double)getSoldier().getAttackHelper().getHealth() / getSoldier().getAttackHelper().getInitialHealth());
        }

        if (getSoldier().getAttackHelper().isDead())
            setVisible(false);
    }
}
