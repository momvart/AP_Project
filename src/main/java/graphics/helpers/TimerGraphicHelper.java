package graphics.helpers;

import graphics.drawers.drawables.TextDrawable;
import utils.TimeSpan;

public class TimerGraphicHelper extends GraphicHelper
{
    private TimeSpan time;

    private boolean reversed;

    private Runnable onTimeFinished;

    public TimerGraphicHelper(TimeSpan time, boolean reversed)
    {
        this.setReloadDuration(1);
        this.setReloadListener(this::onClockTick);
        this.time = time;
        this.reversed = reversed;
    }

    private void onClockTick()
    {
        time.addSeconds(reversed ? -1 : 1);
        if (time.getTotalSeconds() == 0)
            callOnTimeFinished();
    }

    private void callOnTimeFinished()
    {
        if (onTimeFinished != null)
            onTimeFinished.run();
    }

    public void setOnTimeFinished(Runnable onTimeFinished)
    {
        this.onTimeFinished = onTimeFinished;
    }

}
