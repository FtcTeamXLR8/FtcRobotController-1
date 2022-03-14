package org.firstinspires.ftc.teamcode;

public class Event <Ev extends Event<Ev>> {}
    ArrayList<Callable> conditions = new ArrayList();
    Runnable callback = ()->{};

    boolean removeOnceRun = true;

    public Event(Callable... conditions, Runnable callback){
        this.conditions = conditions;
        this.callback = callback;
    }

    public Event<Ev> addCondition(Callable condition){
        conditions.add(condition);
        return this;
    }

    public boolean testAllConditions(){
        for(Boolean check : conditionCheck)if(!check){
            return false;
        }
        callback.run();
        return true;
    }
    public boolean testEachCondition(){
        for(Boolean check : conditionCheck)if(check){
            callback.run();
            return true;
        }
        return false;
    }
    public ArrayList<Boolean> conditionCheck(){
        ArrayList<Boolean> checks = new ArrayList();
        for(Callable condition : conditions)checks.add(condition.call());
        return checks;
    }

    public boolean getRemoveOnceRun(){
        return removeOnceRun;
    }
    public void toggleRemoveOnceRun(){
        removeOnceRun = !removeOnceRun;
    }
    public void setRemoveOnceRun(boolean setter){
        removeOnceRun = setter;
    }
}
