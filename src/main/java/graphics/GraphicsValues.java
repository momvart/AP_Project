package graphics;

public class GraphicsValues
{
    private static double scale = 1;

    public static double getScale()
    {
        return scale;
    }

    public static void setScale(float scale)
    {
        GraphicsValues.scale = scale;
        Fonts.initialize();
    }
}
