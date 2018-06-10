package graphics;

import javafx.scene.text.Font;

public class Fonts
{
    private static Font small;
    private static Font medium;
    private static Font large;

    public static void initialize()
    {
        small = Font.font(GraphicsValues.getScale() * 10);
        medium = Font.font(GraphicsValues.getScale() * 15);
        large = Font.font(GraphicsValues.getScale() * 20);
    }

    public static Font getSmall()
    {
        return small;
    }

    public static Font getMedium()
    {
        return medium;
    }

    public static Font getLarge()
    {
        return large;
    }
}
