package graphics;

import javafx.scene.text.Font;

public class Fonts
{
    private static final String FONT_PATH = "assets/fonts/Supercell.ttf";
    private static final String BB_FONT_PATH = "assets/fonts/BackBeat.ttf";

    private static Font tiny;
    private static Font smaller;
    private static Font small;
    private static Font medium;
    private static Font large;

    private static Font[] defFonts;
    private static Font[] bbFonts;

    public static void initialize()
    {
        defFonts = new Font[] { load(FONT_PATH, 5), load(FONT_PATH, 7.5), load(FONT_PATH, 10), load(FONT_PATH, 15), load(FONT_PATH, 20) };
        bbFonts = new Font[] { load(BB_FONT_PATH, 5), load(BB_FONT_PATH, 7.5), load(BB_FONT_PATH, 10), load(BB_FONT_PATH, 15), load(BB_FONT_PATH, 20) };
    }

    public static Font getTiny()
    {
        return defFonts[0];
    }

    public static Font getSmaller()
    {
        return defFonts[1];
    }

    public static Font getSmall()
    {
        return defFonts[2];
    }

    public static Font getMedium()
    {
        return defFonts[3];
    }

    public static Font getLarge()
    {
        return defFonts[4];
    }

    public static Font getBBTiny()
    {
        return bbFonts[0];
    }

    public static Font getBBSmaller()
    {
        return bbFonts[1];
    }

    public static Font getBBSmall()
    {
        return bbFonts[2];
    }

    public static Font getBBMedium()
    {
        return bbFonts[3];
    }

    public static Font getBBLarge()
    {
        return bbFonts[4];
    }

    private static Font load(String path, double size)
    {
        return Font.loadFont(Fonts.class.getClassLoader().getResourceAsStream(path), size);
    }
}
