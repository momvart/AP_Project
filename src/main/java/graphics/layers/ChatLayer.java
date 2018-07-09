package graphics.layers;

import graphics.GraphicsValues;
import graphics.drawers.Drawer;
import graphics.drawers.drawables.ChatMessageDrawable;
import graphics.drawers.drawables.RoundRectDrawable;
import graphics.positioning.PercentPositioningSystem;
import javafx.scene.paint.Color;
import network.Message;
import utils.RectF;

import java.util.ArrayList;

public class ChatLayer extends Layer
{
    private RoundRectDrawable background;
    private ArrayList<Message> messages;
    private ArrayList<ChatMessageDrawable> messageDrawables;
    private static int MAX_MESSAGE_COUNT = 10;

    public ChatLayer(int order, RectF bounds)
    {
        super(order, bounds);
        setPosSys(new PercentPositioningSystem(this));

        background = new RoundRectDrawable(0, 0, GraphicsValues.PADDING, Color.rgb(0, 0, 0, 0.6));
        background.setPivot(0, 0.5);
        Drawer dBackground = new Drawer(background);
        dBackground.setPosition(0, 0);
        dBackground.setLayer(this);

        messages = new ArrayList<>();
        messageDrawables = new ArrayList<>();

    }

    public void newMessage(Message message)
    {
        messages.add(message);
        update();
    }

    public void update()
    {
        removeAllObjects();
        messageDrawables = new ArrayList<>();
        background.setSize(getWidth(), getHeight());
        Drawer dbg = new Drawer(background);
        dbg.setPosition(0, 0);
        dbg.setLayer(this);

        messages.forEach(message ->
        {
            ChatMessageDrawable chatMessageDrawable = new ChatMessageDrawable(message);
            chatMessageDrawable.setPivot(0, 0);
            messageDrawables.add(chatMessageDrawable);
        });

        double size = 0;
        for (int i = 0; i < messageDrawables.size(); i++)
        {
            size += messageDrawables.get(messageDrawables.size() - i - 1).getHeight() + GraphicsValues.PADDING;
            if (size < getHeight())
            {
                Drawer drawer = new Drawer(messageDrawables.get(messageDrawables.size() - i - 1));
                drawer.setPosition(GraphicsValues.PADDING / getWidth(), 0.5 - (i + 1) * ((messageDrawables.get(messageDrawables.size() - 1).getSize().getHeight() + GraphicsValues.PADDING) / getHeight()));
                drawer.setLayer(this);
            }
            else
                break;
        }
    }
}
