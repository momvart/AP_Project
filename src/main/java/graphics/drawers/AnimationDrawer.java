package graphics.drawers;

import graphics.IFrameUpdatable;
import graphics.drawers.drawables.AnimationDrawable;
import graphics.drawers.drawables.Drawable;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashMap;

public class AnimationDrawer extends Drawer implements IFrameUpdatable
{
    private HashMap<String, AnimationDrawable> animations = new HashMap<>();

    private AnimationDrawable currentAnim;

    public AnimationDrawer(Drawable baseDrawer)
    {
        super(baseDrawer);
    }

    public void addAnimation(String name, AnimationDrawable anim)
    {
        animations.put(name.toLowerCase(), anim);
    }

    public void playAnimation(String name)
    {
        name = name.toLowerCase();
        if (!animations.containsKey(name))
            throw new IllegalArgumentException("Name is not valid.");
        currentAnim = animations.get(name);
    }

    @Override
    public void draw(GraphicsContext gc)
    {
        if (currentAnim == null)
            super.draw(gc);

        gc.save();
        if (getLayer() == null)
            gc.translate(getPosition().getX(), getPosition().getY());
        else
            gc.translate(getLayer().getPosSys().convertX(getPosition()), getLayer().getPosSys().convertY(getPosition()));
        currentAnim.draw(gc);
        gc.restore();
    }

    @Override
    public void update(double deltaT)
    {
        if (currentAnim != null)
            currentAnim.update(deltaT);
    }
}
