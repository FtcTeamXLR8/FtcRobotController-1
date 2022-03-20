package org.firstinspires.ftc.teamcode.MovementAlgorithms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.firstinspires.ftc.teamcode.Events.*;

public abstract class Movement<MoveAlg extends Movement<MoveAlg>> {
    protected ArrayList<Runnable> preMoveFunctionList = new ArrayList<>();
    protected ArrayList<Runnable> whileMoveFunctionList = new ArrayList<>();
    protected ArrayList<Runnable> postMoveFunctionList = new ArrayList<>();

    protected ArrayList<Event> eventList = new ArrayList<>();
    protected boolean endWithUntriggeredEvents = false;

    protected Callable<Boolean> endCondition = ()->true;
    protected Runnable ifEndedByCondition = ()-> {};
    protected Runnable ifNotEndedByCondition = () ->{};


    abstract void init();

    public void execute(LinearOpMode opMode) {
        init();
        for (Runnable runner : preMoveFunctionList) runner.run();


        try {
            while (!opMode.isStopRequested()) {

                for (Event event : eventList) {
                    event.test();
                }

                if (!endCondition.call()) {
                    ifEndedByCondition.run();
                    break;
                }
                if (moveMethod()) {
                    boolean check = true;

                    for(Event event : eventList){
                        if(event.getForceCompletion() && !event.isDisabled()){
                            check = false;
                            break;
                        }
                        
                    }

                    if (check) {
                        ifNotEndedByCondition.run();
                        break;
                    }
                }

                for (Runnable runner : whileMoveFunctionList) runner.run();
            }
            for (Runnable runner : postMoveFunctionList) runner.run();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    abstract boolean moveMethod();

    public Movement<MoveAlg> addPreMoveFunction(Runnable func){
        preMoveFunctionList.add(func);
        return this;
    }
    public Movement<MoveAlg> addMoveFunction(Runnable func){
        whileMoveFunctionList.add(func);
        return this;
    }
    public Movement<MoveAlg> addPostMoveFunction(Runnable func){
        postMoveFunctionList.add(func);
        return this;
    }

    public void removePreMoveFunction(Runnable func){
        preMoveFunctionList.remove(func);
    }
    public void removeMoveFunction(Runnable func){
        whileMoveFunctionList.remove(func);
    }
    public void removeProstMoveFunction(Runnable func){
        postMoveFunctionList.remove(func);
    }

    public void removeAllPreMoveFunctions(){
        preMoveFunctionList = new ArrayList<>();
    }
    public void removeAllPostMoveFunctions(){
        postMoveFunctionList = new ArrayList<>();
    }
    public void removeAllMoveFunctions(){
        whileMoveFunctionList = new ArrayList<>();
    }

    public Movement<MoveAlg> ifEndedByCondition(Runnable func){
        ifEndedByCondition = func;
        return this;
    }
    public Movement<MoveAlg> ifNotEndedByCondition(Runnable func){
        ifNotEndedByCondition = func;
        return this;
    }

    public Movement<MoveAlg> setEndCondition(Callable<Boolean> endCondition) {
        this.endCondition = endCondition;
        return this;
    }

    public Movement<MoveAlg> toggleEndWithUntriggeredEvents(){
        endWithUntriggeredEvents = !endWithUntriggeredEvents;
        return this;
    }

    public Movement<MoveAlg> addEvent(Event event){
        this.eventList.add(event);
        return this;
    }
    public Movement<MoveAlg> createEvent(Callable condition, Runnable callback){
        eventList.add(new Event(callback, condition));
        return this;
    }
    public Movement<MoveAlg> createTimedEvent(int milliseconds,Runnable callback){
        eventList.add(new TimedEvent(callback, milliseconds));
        return this;
    }
}
