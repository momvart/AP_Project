package graphics.drawers;

import graphics.drawers.drawables.ImageDrawable;
import javafx.scene.image.Image;
import models.soldiers.SoldierValues;
import utils.GraphicsUtilities;

import java.net.URISyntaxException;

public class SoldierDrawer extends AnimationDrawer
{
    public static final String IDLE = "idle";
    public static final String RUN = "run";
    public static final String ATTACK = "attack";
    public static final String DIE = "die";

    public SoldierDrawer(int soldierType) throws URISyntaxException
    {
        super(null);
        String folder = "assets/soldiers/" + SoldierValues.getSoldierInfo(soldierType).getName().toLowerCase() + "/" + "1";
        addAnimation(IDLE, GraphicsUtilities.createFrameAnimDrawableFrom(folder + "/idle", 1, 50, .5, 1));
        addAnimation(RUN, GraphicsUtilities.createFrameAnimDrawableFrom(folder + "/run", 0.5, 50, .5, 1));
        addAnimation(ATTACK, GraphicsUtilities.createFrameAnimDrawableFrom(folder + "/attack", 1, 50, .5, 1));
        addAnimation(DIE, GraphicsUtilities.createFrameAnimDrawableFrom(folder + "/die", 1, 50, .5, 1));
        setDrawable(new ImageDrawable(new Image(getClass().getClassLoader().getResourceAsStream(folder + "/idle/001.png")), 50));
    }


}
