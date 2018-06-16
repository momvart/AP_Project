package graphics;

import javafx.scene.text.Font;

public class Fonts
{
    private static final String FONT_PATH = "assets/fonts/Supercell.ttf";
    private static Font tiny;
    private static Font small;
    private static Font medium;
    private static Font large;

    public static void initialize()
    {
        tiny = Font.loadFont(Fonts.class.getClassLoader().getResourceAsStream(FONT_PATH), 5);
        small = Font.loadFont(Fonts.class.getClassLoader().getResourceAsStream(FONT_PATH), 10);
        medium = Font.loadFont(Fonts.class.getClassLoader().getResourceAsStream(FONT_PATH), 15);
        large = Font.loadFont(Fonts.class.getClassLoader().getResourceAsStream(FONT_PATH), 20);
    }

    public static Font getTiny()
    {
        return tiny;
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
