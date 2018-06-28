package graphics.helpers;

import graphics.IFrameUpdatable;

public abstract class GraphicHelper implements IFrameUpdatable
{
    private double reloadStack;
    private double reloadDuration;
    private IOnReloadListener reloadListener;

    public void callOnReload()
    {
        if (reloadListener != null)
            reloadListener.onReload();
    }

    public void setReloadListener(IOnReloadListener reloadListener)
    {
        this.reloadListener = reloadListener;
    }

    public void setReloadDuration(double reloadDuration)
    {
        this.reloadDuration = reloadDuration;
    }

    @Override
    public void update(double deltaT)
    {
        if (reloadListener == null)
            return;
        reloadStack += deltaT;
        if (reloadStack >= reloadDuration)
        {
            reloadStack -= reloadDuration;
            callOnReload();
        }
    }
}
