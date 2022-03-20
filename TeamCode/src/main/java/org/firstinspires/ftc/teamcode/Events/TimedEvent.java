package org.firstinspires.ftc.teamcode.Events;

import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.Callable;

public class TimedEvent extends Event{
    ElapsedTime elapsedTime = new ElapsedTime();

    public TimedEvent(Runnable callback, int milliseconds) {
        super(callback, ()->false);
        setCondition(()->elapsedTime.milliseconds()<milliseconds);
        onEnable(()->elapsedTime.reset());
    }
}
