package graphics.drawers.drawables;

import graphics.GraphicsValues;
import javafx.scene.canvas.GraphicsContext;
import network.Message;

public class ChatMessageDrawable extends Drawable
{
    private static final double InsidePadding = GraphicsValues.PADDING / 2;


    private RoundRectDrawable background;
    private Message message;
    private TextDrawable messageText;

    public ChatMessageDrawable(Message message)
    {
        //todo : implement this part
    }

    @Override
    protected void onDraw(GraphicsContext gc)
    {
        //todo : implement this part
    }
}
