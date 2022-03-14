package org.firstinspires.ftc.teamcode.Events;

import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.Callable;

public class TimedEvent extends Event{
    ElapsedTime elapsedTime = new ElapsedTime();

    public TimedEvent(Runnable callback, int milliseconds, Callable<Boolean>... conditions) {
        super(callback, conditions);
        toggleRemoveOnceRun();
        addCondition(()->elapsedTime.milliseconds()<milliseconds);
        onInit(()->elapsedTime.reset());
    }
}
