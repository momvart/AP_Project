package graphics.drawers;

import graphics.drawers.drawables.Drawable;
import graphics.drawers.drawables.bars.BottomBarDrawable;
import graphics.drawers.drawables.bars.MenuDrawable;
import javafx.scene.canvas.GraphicsContext;

public class MenuDrawer extends Drawer
{
    MenuDrawable menuDrawable;

    public MenuDrawer(MenuDrawable menuDrawable)
    {
        this.menuDrawable = menuDrawable;
    }

    @Override
    protected void onDraw(GraphicsContext gc)
    {
        Drawable[] drawables = menuDrawable.getItems();
        int size = drawables.length;
        double width = menuDrawable.getScreenWidth();
        double height = menuDrawable.getScreenHeight();
        double columnsSpacing = BottomBarDrawable.COLUMNS_SPACING_SCALE * width;
        double boxHeight = BottomBarDrawable.BOX_HEIGHT_SCALE * height;
        Drawer[] drawers = new Drawer[size];
        for (int i = 0; i < size; i++)
        {
            drawers[i].setPosition(width / 2 + (i - size / 2 + (size % 2 == 1 ? 0 : 0.5)) * columnsSpacing, height - boxHeight / 2);
            drawers[i].setLayer(getLayer());
        }

    }
}
