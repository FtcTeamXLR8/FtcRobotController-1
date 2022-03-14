package org.firstinspires.ftc.teamcode.MovementAlgorithms;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Events.*;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void execute(LinearOpMode opMode) {
        init();
        for (Runnable runner : preMoveFunctionList) runner.run();


        try {
            while (!opMode.isStopRequested()) {

                ArrayList<Event> newEventList = eventList;
                for (Event event : eventList) {
                    if(!event.testEachCondition())newEventList.add(event);
                    else if(!event.getRemoveOnceRun())newEventList.add(event);
                }

                if (!endCondition.call()) {
                    ifEndedByCondition.run();
                    break;
                }
                if (moveMethod()){
                    if(endWithUntriggeredEvents || newEventList.stream().noneMatch(Event::getRemoveOnceRun)) {
                        ifNotEndedByCondition.run();
                        break;
                    }
                }

                for (Runnable runner : whileMoveFunctionList) runner.run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Runnable runner : postMoveFunctionList) runner.run();
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
}
