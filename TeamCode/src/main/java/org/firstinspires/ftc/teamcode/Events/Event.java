package org.firstinspires.ftc.teamcode.Events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class Event <Ev extends Event<Ev>> {
    Callable<Boolean> condition;
    Runnable callback, initCallback = ()->{};
    Runnable pInit = ()->{};

    boolean fulfilled = false;
    String name = null;

    public Event(Callable<Boolean> condition, Runnable callback){
       this.condition = condition;
       this.callback = callback;
    }
    public Event(Callable<Boolean> condition, Runnable callback, Runnable initCallback){
        this.condition = condition;
        this.callback = callback;
        this.initCallback = initCallback;
    }
    public Event(String name, Callable<Boolean> condition, Runnable callback){
        this.condition = condition;
        this.callback = callback;
        this.name = name;
    }
    public Event(String name, Callable<Boolean> condition, Runnable callback, Runnable initCallback){
        this.condition = condition;
        this.callback = callback;
        this.initCallback = initCallback;
        this.name = name;
    }

    public boolean firstTest = true;
    public void reset(){
        this.fulfilled = false;
    }
    public void test(){
        if(fulfilled)return;

        if (firstTest){
            pInit.run();
            initCallback.run();
        }
        try{
            if(condition.call())callback.run();
            fulfilled=true;
        }
        catch (Exception e){e.printStackTrace();}
    }

    public boolean isFulfilled(){
        return fulfilled;
    }
    public String getName(){
        return name;
    }

    public void setCondition(Callable<Boolean> condition){
        this.condition = condition;
    }
    public void setCallback(Runnable callback){
        this.callback = callback;
    }
    public void setInitCallback(Runnable initCallback){
        this.initCallback = initCallback;
    }
    void setPInit(Runnable pInit){
        this.pInit = pInit;
    }
}
