package org.firstinspires.ftc.teamcode.Events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.Callable;

public class Event <Ev extends Event<Ev>> {
    ArrayList<Callable<Boolean>> conditions = new ArrayList();
    Callable<Boolean> reEnableCondition = ()->false;
    Runnable callback, onEnable = ()->{};

    boolean disableOnceRun = true;
    boolean disable = false;
    boolean forceCompletion = true;
    String EachOrAll = "each";

    public Event(Runnable callback, Callable<Boolean>... conditions){
        this.conditions.addAll(Arrays.asList(conditions));
        this.callback = callback;
    }
    public Event(Runnable onEnable, Runnable callback, Callable<Boolean>... conditions){
        this.conditions.addAll(Arrays.asList(conditions));
        this.callback = callback;
        this.onEnable = onEnable;
    }

    public Event<Ev> addCondition(Callable<Boolean> condition){
        conditions.add(condition);
        return this;
    }
    public void init(){
        onEnable.run();
    }

    public boolean testAllConditions() throws Exception {
        if(reEnableCondition.call()){
            this.disable=false;
            onEnable.run();
        }
        if(this.disable)return false;
        for(Boolean check : conditionCheck())if(!check){
            return false;
        }
        callback.run();
        return true;
    }
    public boolean testEachCondition() throws Exception {
        if(this.reEnableCondition.call())this.disable=false;
        if(this.disable)return false;
        for(Boolean check : conditionCheck())if(check){
            callback.run();
            return true;
        }
        return false;
    }
    public boolean testConditions() throws Exception {
        switch(EachOrAll.toLowerCase(Locale.ROOT)){
            case "all": return testAllConditions();
            default: return testEachCondition();
        }
    }
    public boolean testConditions(String EachOrAll) throws Exception {
        switch(EachOrAll.toLowerCase(Locale.ROOT)){
            case "all": return testAllConditions();
            default: return testEachCondition();
        }
    }
    public ArrayList<Boolean> conditionCheck() throws Exception {
        ArrayList<Boolean> checks = new ArrayList<>();
        for(Callable<Boolean> condition : conditions)checks.add(condition.call());
        return checks;
    }

    public boolean getDisableOnceRun(){
        return disableOnceRun;
    }
    public Event<Ev> toggleRemoveOnceRun(){
        disableOnceRun = !disableOnceRun;
        return this;
    }
    public Event<Ev> setDisableOnceRun(boolean setter){
        disableOnceRun = setter;
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

    public Event<Ev> reEnableOn(Callable<Boolean> callable){
        this.reEnableCondition = callable;
        return this;
    }

    public Event<Ev> testEachOrAllConditions(String eachOrAll) {
        EachOrAll = eachOrAll;
        return this;
    }

    public boolean isDisabled() {
        return disable;
    }
    public Event<Ev> disable(){
        this.disable = true;
        return this;
    }

    public boolean getForceCompletion(){
        return forceCompletion;
    }
}
