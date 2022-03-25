package org.firstinspires.ftc.teamcode.Events;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class Event <Ev extends Event<Ev>> {
    Callable<Boolean> condition, reEnableCondition = ()->false;
    Runnable callback, onEnable = ()->{};

    boolean disableOnceRun = true;
    boolean disabled = false;
    boolean forceCompletion = true;

    public Event(Runnable callback, Callable<Boolean> condition){
        this.condition = condition;
        this.callback = callback;
    }
    public Event(Runnable callback, Callable<Boolean> condition, Callable<Boolean> enableOn){
        this.condition = condition;
        this.callback = callback;
        this.reEnableCondition = enableOn;
    }
    public Event(Runnable callback, Callable<Boolean> condition, Callable<Boolean> enableOn, Runnable onEnable){
        this.condition = condition;
        this.callback = callback;
        this.onEnable = onEnable;
        this.reEnableCondition = enableOn;
    }


    public void init(){
        onEnable.run();
    }

    public void test() {


        try {
            if (disabled && reEnableCondition.call()) {
                disabled = false;
                onEnable.run();
            }

            if (disabled || !condition.call())return;

        }
        catch(Exception e){
            e.printStackTrace();
        }

        callback.run();
        if(disableOnceRun)disable();
    }

    public boolean getIfDisabledOnceRun(){
        return disableOnceRun;
    }
    public Event<Ev> toggleRemoveOnceRun(){
        disableOnceRun = !disableOnceRun;
        return this;
    }
    public Event<Ev> setDisableOnceRun(boolean setter){
        disableOnceRun = setter;
        forceCompletion = setter;
        return this;
    }

    public Event<Ev> onEnable(Runnable runnable){
        this.onEnable = runnable;
        return this;
    }

    public Event<Ev> enableOn(Callable<Boolean> callable){
        this.reEnableCondition = callable;
        return this;
    }

    public boolean isDisabled() {
        return disabled;
    }
    public Event<Ev> disable(){
        this.disabled = true;
        return this;
    }

    public void setCondition(Callable<Boolean> condition) {
        this.condition = condition;
    }

    public boolean getForceCompletion(){
        return forceCompletion;
    }
    public void trigger(){
        if(disableOnceRun)disable();
        callback.run();
    }
    public void enable(){
        disabled = false;
        onEnable.run();
    }

}
