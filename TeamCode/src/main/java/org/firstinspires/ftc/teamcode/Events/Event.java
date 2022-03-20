package org.firstinspires.ftc.teamcode.Events;

import java.util.concurrent.Callable;

public class Event <Ev extends Event<Ev>> {
    Callable<Boolean> condition, reEnableCondition = ()->false;
    Runnable callback, onEnable = ()->{};

    boolean disableOnceRun = true;
    boolean disable = false;
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
            if (disable && reEnableCondition.call()) {
                disable = false;
                onEnable.run();
            }

            if (disable || !condition.call())return;

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

    public Event<Ev> toggleForceCompletion(){
        forceCompletion = !forceCompletion;
        return this;
    }

    public Event<Ev> enableOn(Callable<Boolean> callable){
        this.reEnableCondition = callable;
        return this;
    }

    public boolean isDisabled() {
        return disable;
    }
    public Event<Ev> disable(){
        this.disable = true;
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
}
