package graphics.layers;

import graphics.GraphicsValues;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.ChatMessageDrawable;
import graphics.drawers.drawables.RoundRectDrawable;
import graphics.positioning.NormalPositioningSystem;
import graphics.positioning.PercentPositioningSystem;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import network.Message;
import utils.RectF;

import java.util.ArrayList;

public class ChatLayer extends Layer
{
    private ArrayList<Pair<String, String>> messages;

    public ChatLayer(int order, RectF bounds)
    {
        super(order, bounds);
        setPosSys(new NormalPositioningSystem(1));

        setHardBound(true);
        messages = new ArrayList<>();
        setVerticallyScrollable(true);
    }

    private Drawer lastMessage;

    public void newMessage(Pair<String, String> message)
    {
        messages.add(message);

        ChatMessageDrawable chatItem = new ChatMessageDrawable(message, getWidth() - 2 * GraphicsValues.PADDING);
        chatItem.setPivot(0, 0);
        Drawer dMessage = new Drawer(chatItem);
        if (lastMessage != null)
            dMessage.setPosition(0, lastMessage.getPosition().getY() + lastMessage.getDrawable().getHeight() + GraphicsValues.PADDING);
        else
            dMessage.setPosition(0, getHeight());
        dMessage.setLayer(this);

        lastMessage = dMessage;

        setScroll(0, Double.NEGATIVE_INFINITY);
    }

    public void redrawAll()
    {
        removeAllObjects();

        double lastY = getHeight();

        for (int i = 0; i < messages.size(); i++)
        {
            ChatMessageDrawable chatItem = new ChatMessageDrawable(messages.get(i), getWidth() - 2 * GraphicsValues.PADDING);
            chatItem.setPivot(0, 0);
            Drawer dMessage = new Drawer(chatItem);

            dMessage.setPosition(0, lastY);

            dMessage.setLayer(this);
            lastY += chatItem.getHeight() + GraphicsValues.PADDING;
        }
    }
}
