package graphics.drawers.drawables.bars;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TopBarDrawable extends Bar
{
    public static final double HEIGHT_SCALE = 0.04;

    public TopBarDrawable(double width, double height)
    {
        super(width, height);
    }

    @Override
    protected void onDraw(GraphicsContext gc)
    {
        //region topBar
        gc.setFill(Color.rgb(0, 0, 0, 0.6));
        gc.fillRect(0, 0, width, HEIGHT_SCALE * height);
        //endregion

        //region time
        gc.setFont(Font.font("Dyuthi", FontWeight.BOLD, 10));
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);
        DateFormat df = new SimpleDateFormat("HH:mm");
        Calendar calobj = Calendar.getInstance();
        gc.fillText(df.format(calobj.getTime()), width / 2, height / 40);
        //endregion

    }
}
