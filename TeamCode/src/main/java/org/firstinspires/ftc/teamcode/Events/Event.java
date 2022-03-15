package org.firstinspires.ftc.teamcode.Events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class Event <Ev extends Event<Ev>> {
    ArrayList<Callable<Boolean>> conditions = new ArrayList();
    Runnable callback, onInit = ()->{};

    boolean removeOnceRun = false;
    boolean forceCompletion = true;

    public Event(Runnable callback, Callable<Boolean>... conditions){
        this.conditions.addAll(Arrays.asList(conditions));
        this.callback = callback;
    }
    public Event(Runnable onInit,Runnable callback, Callable<Boolean>... conditions){
        this.conditions.addAll(Arrays.asList(conditions));
        this.callback = callback;
        this.onInit = onInit;
    }

    public Event<Ev> addCondition(Callable<Boolean> condition){
        conditions.add(condition);
        return this;
    }
    public void init(){
        onInit.run();
    }

    public boolean testAllConditions() throws Exception {
        for(Boolean check : conditionCheck())if(!check){
            return false;
        }
        callback.run();
        return true;
    }
    public boolean testEachCondition() throws Exception {
        for(Boolean check : conditionCheck())if(check){
            callback.run();
            return true;
        }
        return false;
    }
    public ArrayList<Boolean> conditionCheck() throws Exception {
        ArrayList<Boolean> checks = new ArrayList<>();
        for(Callable<Boolean> condition : conditions)checks.add(condition.call());
        return checks;
    }

    public boolean getRemoveOnceRun(){
        return removeOnceRun;
    }
    public Event<Ev> toggleRemoveOnceRun(){
        removeOnceRun = !removeOnceRun;
        return this;
    }
    public Event<Ev> setRemoveOnceRun(boolean setter){
        removeOnceRun = setter;
        return this;
    }

    public Event<Ev> onInit(Runnable init){
        this.onInit = init;
        return this;
    }

    public Event<Ev> toggleForceCompletion(){
        forceCompletion = !forceCompletion;
        return this;
    }
    public boolean getForceCompletion(){
        return forceCompletion;
    }
}
