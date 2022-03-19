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
        if(disable && reEnableCondition.call()){
            disable=false;
            onEnable.run();
        }

        if(disable)return false;

        for(Boolean check : conditionsCheck())if(!check){
            return false;
        }

        callback.run();
        if(disableOnceRun)disable();
        return true;
    }
    public boolean testEachCondition() throws Exception {
        if(disable && reEnableCondition.call()){
            this.disable=false;
            onEnable.run();
        }

        if(disable)return false;

        for(Boolean check : conditionsCheck())if(check){
            callback.run();
            if(disableOnceRun)disable();
            return true;
        }

        return false;
    }
    public boolean check() throws Exception {
        switch(EachOrAll.toLowerCase(Locale.ROOT)){
            case "all": return testAllConditions();
            default: return testEachCondition();
        }
    }
    public boolean check(String EachOrAllConditions) throws Exception {
        switch(EachOrAllConditions.toLowerCase(Locale.ROOT)){
            case "all conditions":
            case "allconditions":
            case "all": return testAllConditions();
            default: return testEachCondition();
        }
    }
    public ArrayList<Boolean> conditionsCheck() throws Exception {
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
