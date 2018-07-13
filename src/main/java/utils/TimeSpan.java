package utils;

import java.util.function.Consumer;

public class TimeSpan
{
    public static final int SEC_IN_MIN = 60;
    private long seconds = 0;
    private Consumer<TimeSpan> timeChangeListener;


    public TimeSpan(long seconds)
    {
        this.seconds = seconds;
    }

    public TimeSpan(long minutes, long seconds)
    {
        this.seconds += seconds;
        this.seconds += minutes * SEC_IN_MIN;
    }

    public long getSeconds()
    {
        return getTotalSeconds() % SEC_IN_MIN;
    }

    public long getTotalSeconds()
    {
        return seconds;
    }

    public TimeSpan addSeconds(long seconds)
    {
        this.seconds += seconds;
        if (seconds != 0)
            callTimeChanged();
        return this;
    }

    public long getMinutes()
    {
        return getTotalMinutes() % 60;
    }

    public long getTotalMinutes()
    {
        return getTotalSeconds() / 60;
    }

    public long getHours()
    {
        return getTotalHours() % 24;
    }

    public long getTotalHours()
    {
        return getTotalMinutes() / 60;
    }

    public long getDays()
    {
        return getTotalHours() / 24;
    }

    private void callTimeChanged()
    {
        if (timeChangeListener != null)
            timeChangeListener.accept(this);
    }

    public void setTimeChangeListener(Consumer<TimeSpan> timeChangeListener)
    {
        this.timeChangeListener = timeChangeListener;
    }
}
