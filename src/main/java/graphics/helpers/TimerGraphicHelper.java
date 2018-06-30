package graphics.helpers;

import graphics.drawers.drawables.TextDrawable;
import utils.TimeSpan;

public class TimerGraphicHelper extends GraphicHelper
{
    private TextDrawable text;

    private TimeSpan time;

    private boolean reversed;

    private Runnable onTimeFinished;

    public TimerGraphicHelper(TextDrawable text, TimeSpan time, boolean reversed)
    {
        this.text = text;
        this.setReloadDuration(1);
        this.setReloadListener(this::onClockTick);
        this.time = time;
        this.reversed = reversed;
    }

    private void onClockTick()
    {
        time.addSeconds(reversed ? -1 : 1);
        updateText();
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

    private void updateText()
    {
        text.setText(String.format("%02d:%02d", time.getTotalMinutes(), time.getSeconds()));
    }
}
