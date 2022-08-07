package org.firstinspires.ftc.teamcode.Events;

import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.Callable;

public class TimedEvent extends Event{
    ElapsedTime elapsedTime = new ElapsedTime();

    public TimedEvent(int milliseconds,Runnable callback) {
        super(()->false, callback);
        setPInit(()->elapsedTime.reset());
        setCondition(()->elapsedTime.milliseconds()>milliseconds);
    }
    public TimedEvent(int milliseconds,Runnable callback, Runnable initCallback) {
        super(()->false, callback, initCallback);
        setPInit(()->elapsedTime.reset());
        setCondition(()->elapsedTime.milliseconds()>milliseconds);
    }
    public TimedEvent(String name, int milliseconds,Runnable callback) {
        super(name, ()->false, callback);
        setPInit(()->elapsedTime.reset());
        setCondition(()->elapsedTime.milliseconds()>milliseconds);
    }
    public TimedEvent(String name, int milliseconds,Runnable callback, Runnable initCallback) {
        super(name, ()->false, callback, initCallback);
        setPInit(()->elapsedTime.reset());
        setCondition(()->elapsedTime.milliseconds()>milliseconds);
    }
}
