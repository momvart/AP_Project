package graphics.gui.dialogs;

import graphics.layers.Layer;
import graphics.gui.GraphicView;
import views.dialogs.DialogResult;

public abstract class GraphicDialog extends GraphicView
{
    protected String message;

    public GraphicDialog(Layer layer, double width, double height, String message)
    {
        super(layer, width, height);
        this.message = message;
    }

    public void showMessage()
    {
        showInfoBar(message);
    }

    public abstract DialogResult showDialog();
}
