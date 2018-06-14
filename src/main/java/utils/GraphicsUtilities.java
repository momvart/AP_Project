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

import com.google.gson.*;

public class GraphicsUtilities
{
    public static final String META_DATA_FILE_NAME = "METAD.json";

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
        FrameAnimationDrawable anim = new FrameAnimationDrawable(createFramesFrom(directory, minSideDimen, pivotX, pivotY), duration);
        anim.setPivot(pivotX, pivotY);
        return anim;
    }

    public static ImageDrawable[] createFramesFrom(String directoryPath, double minSideDimen, double pivotX, double pivotY) throws URISyntaxException
    {
        return createFramesFrom(new File(GraphicsUtilities.class.getClassLoader().getResource(directoryPath).toURI()), minSideDimen, pivotX, pivotY);
    }

    public static ImageDrawable[] createFramesFrom(File directory, double minSideDimen, double pivotX, double pivotY)
    {
        List<File> files = Arrays.asList(directory.listFiles(File::isFile));
        files.sort(Comparator.comparing(File::getName));
        ImageDrawable[] frames = new ImageDrawable[files.size()];
        for (int i = 0; i < frames.length; i++)
            try
            {
                frames[i] = new ImageDrawable(new Image(new FileInputStream(files.get(i))), minSideDimen);
                frames[i].setPivot(pivotX, pivotY);
            }
            catch (IOException ignored) {}
        return frames;
    }

    public static JsonObject fetchMetadata(File directory)
    {
        File metaFile = new File(directory.getAbsolutePath(), META_DATA_FILE_NAME);
        if (metaFile.exists())
        {
            try (FileReader reader = new FileReader(metaFile))
            {
                return new JsonParser().parse(reader).getAsJsonObject();
            }
            catch (Exception e) { return null; }
        }
        else if (directory.getParent() != null)
            return fetchMetadata(directory.getParentFile());
        else
            return null;
    }

    public static ImageDrawable createImageDrawable(String filePath, double width, double height) throws URISyntaxException
    {
        return createImageDrawable(new File(GraphicsUtilities.class.getClassLoader().getResource(filePath).toURI()), width, height);
    }

    public static ImageDrawable createImageDrawable(File file, double width, double height)
    {
        JsonObject meta = fetchMetadata(file.getParentFile());
        double pivotX = 0, pivotY = 0;
        if (meta != null)
        {
            if (meta.has("pivotX"))
                pivotX = meta.get("pivotX").getAsDouble();
            if (meta.has("pivotY"))
                pivotY = meta.get("pivotY").getAsDouble();
        }

        try (FileInputStream reader = new FileInputStream(file))
        {
            ImageDrawable img = new ImageDrawable(new Image(reader), width, height);
            img.setPivot(pivotX, pivotY);
            return img;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

}
