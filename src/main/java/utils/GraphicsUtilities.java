package utils;

import graphics.drawers.drawables.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Transform;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;

import java.util.*;

public class GraphicsUtilities
{
    public static void GCTransform(GraphicsContext gc, Transform transform)
    {
        gc.transform(transform.getMxx(), transform.getMyx(), transform.getMxy(), transform.getMyy(), transform.getTx(), transform.getTy());
    }

    public static FrameAnimationDrawable createFrameAnimDrawableFrom(URI directory, double duration, double minSideDimen)
    {
        return createFrameAnimDrawableFrom(new File(directory), duration, minSideDimen);
    }

    public static FrameAnimationDrawable createFrameAnimDrawableFrom(String directoryPath, double duration, double minSideDimen) throws URISyntaxException
    {
        return createFrameAnimDrawableFrom(new File(GraphicsUtilities.class.getClassLoader().getResource(directoryPath).toURI()), duration, minSideDimen);
    }

    public static FrameAnimationDrawable createFrameAnimDrawableFrom(String directoryPath, double duration, double minSideDimen, double pivotX, double pivotY) throws URISyntaxException
    {
        return createFrameAnimDrawableFrom(new File(GraphicsUtilities.class.getClassLoader().getResource(directoryPath).toURI()),
                duration, minSideDimen, pivotX, pivotY);
    }

    public static FrameAnimationDrawable createFrameAnimDrawableFrom(File directory, double duration, double minSideDimen)
    {
        return createFrameAnimDrawableFrom(directory, duration, minSideDimen, 0, 0);
    }

    public static FrameAnimationDrawable createFrameAnimDrawableFrom(File directory, double duration, double minSideDimen, double pivotX, double pivotY)
    {
        List<File> files = Arrays.asList(directory.listFiles(File::isFile));
        files.sort(Comparator.comparing(File::getName));
        ImageDrawable[] frames = new ImageDrawable[files.size()];
        for (int i = 0; i < frames.length; i++)
            try
            {
                frames[i] = new ImageDrawable(new Image(Files.newInputStream(Paths.get(files.get(i).toURI()))), minSideDimen);
//                frames[i].setPivot(pivotX, pivotY);
            }
            catch (IOException ignored) {}
        FrameAnimationDrawable anim = new FrameAnimationDrawable(frames, duration);
        anim.setPivot(pivotX, pivotY);
        return anim;
    }
}
