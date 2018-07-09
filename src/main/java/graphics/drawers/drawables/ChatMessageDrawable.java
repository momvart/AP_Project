package graphics.drawers.drawables;

import graphics.Fonts;
import graphics.GraphicsValues;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import network.Message;
import utils.GraphicsUtilities;
import utils.SizeF;

public class ChatMessageDrawable extends Drawable
{
    private static final double InsidePadding = GraphicsValues.PADDING / 2;


    private RoundRectDrawable background;
    private TextDrawable txtTitle;
    private TextDrawable txtMessage;

    private Message message;

    public ChatMessageDrawable(Pair<String, String> message, double width)
    {
        this.txtTitle = new TextDrawable(message.getKey() + ":", Color.GOLD, Fonts.getTiny());
        this.txtTitle.setPivot(0, 0);
        this.txtTitle.setMaxWidth(width - 2 * InsidePadding);

        this.txtMessage = new TextDrawable(message.getValue(), Color.WHITE, Fonts.getSmaller());
        this.txtMessage.setPivot(0, 0);
        this.txtMessage.setMaxWidth(width - 2 * InsidePadding);
        //TODO: text must be multiline.
        setSize(width, txtTitle.getHeight() + InsidePadding + txtMessage.getHeight() + InsidePadding * 2);
    }

    @Override
    protected void onDraw(GraphicsContext gc)
    {
        gc.save();
        gc.translate(InsidePadding, InsidePadding);

        txtTitle.draw(gc);

        gc.save();
        gc.translate(0, txtTitle.getHeight() + InsidePadding);
        txtMessage.draw(gc);
        gc.restore();

        gc.restore();
    }
}
