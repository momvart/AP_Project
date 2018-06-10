package utils;

import graphics.drawers.drawables.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Transform;

import java.io.*;
import java.net.URI;
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

    public static FrameAnimationDrawable createFrameAnimDrawableFrom(File directory, double duration, double minSideDimen)
    {
        List<File> files = Arrays.asList(directory.listFiles(File::isFile));
        files.sort(Comparator.comparing(File::getName));
        ImageDrawable[] frames = new ImageDrawable[files.size()];
        for (int i = 0; i < frames.length; i++)
            try
            {
                frames[i] = new ImageDrawable(new Image(Files.newInputStream(Paths.get(files.get(i).toURI()))), minSideDimen);
            }
            catch (IOException ignored) {}
        return new FrameAnimationDrawable(frames, duration);
    }
}
