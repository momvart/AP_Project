package utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Transform;

public class GraphicsUtilities
{
    public static void GCTransform(GraphicsContext gc, Transform transform)
    {
        gc.transform(transform.getMxx(), transform.getMyx(), transform.getMxy(), transform.getMyy(), transform.getTx(), transform.getTy());
    }
}
