package graphics.drawers;

import graphics.IFrameUpdatable;
import graphics.drawers.drawables.AnimationDrawable;
import graphics.drawers.drawables.Drawable;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashMap;

public class AnimationDrawer extends Drawer implements IFrameUpdatable
{
    protected HashMap<String, AnimationDrawable> animations = new HashMap<>();

    private Drawable baseDrawable;
    private AnimationDrawable currentAnim;

    public AnimationDrawer(Drawable baseDrawable)
    {
        super(baseDrawable);
        this.baseDrawable = baseDrawable;
    }

    public void addAnimation(String name, AnimationDrawable anim)
    {
        animations.put(name.toLowerCase(), anim);
    }

    public void playAnimation(String name)
    {
        currentAnim = animations.get(name.toLowerCase());
        if (currentAnim != null)
            setDrawable(currentAnim);
        else
            setDrawable(baseDrawable);
    }

    public AnimationDrawable getCurrentAnim()
    {
        return currentAnim;
    }

    public HashMap<String, AnimationDrawable> getAnimations()
    {
        return animations;
    }

    @Override
    public void update(double deltaT)
    {
        if (currentAnim != null)
            currentAnim.update(deltaT);
    }
}
