package org.firstinspires.ftc.teamcode.Movement;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Events.Event;
import org.firstinspires.ftc.teamcode.Events.TimedEvent;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public abstract class Movement<MoveAlg extends Movement<MoveAlg>> {
    protected ArrayList<Runnable> preMoveList = new ArrayList<>();
    protected ArrayList<Runnable> whileMoveList = new ArrayList<>();
    protected ArrayList<Runnable> postMoveList = new ArrayList<>();

    protected ArrayList<Event> eventList = new ArrayList<>();
    protected ArrayList<Event> forcedEventList = new ArrayList<>();

    protected Callable<Boolean> alternateEndCondition=()->false;
    protected Runnable ifEndedByAlternateEndCondition = ()->{}, ifNotEndedByAlternateEndCondition = ()->{};

    abstract void init();

    public void execute(LinearOpMode opMode) {
        init();
        for(Event event : eventList){
            event.reset();
        }
        for(Event event : forcedEventList){
            event.reset();
        }

        for (Runnable runner : preMoveList) runner.run();


        try {

            boolean movementComplete = false;
            boolean check = false;
            while (!opMode.isStopRequested()) {

                for (Event event : eventList) {
                    event.test();
                }
                for (Event event : forcedEventList) {
                    event.test();
                }

                if (!movementComplete) {
                    if (moveMethod()) {
                        movementComplete = true;
                    }
                }

                for (Runnable runner : whileMoveList) runner.run();

                for(Event event : forcedEventList){
                    if (!event.isFulfilled()) {
                        check = true;
                        break;
                    }
                }

                if(check&&movementComplete){
                    ifNotEndedByAlternateEndCondition.run();
                    break;
                }
                if(alternateEndCondition.call()){
                    ifEndedByAlternateEndCondition.run();
                    break;
                }
            }

            for (Runnable runner : postMoveList) runner.run();


        }catch(Exception e){
            e.printStackTrace();
        }
    }

    abstract boolean moveMethod();

    public MoveAlg beforeMoving(Runnable func){
        preMoveList.add(func);
        return (MoveAlg) this;
    }
    public MoveAlg whileMoving(Runnable func){
        whileMoveList.add(func);
        return (MoveAlg) this;
    }
    public MoveAlg afterMoving(Runnable func){
        postMoveList.add(func);
        return (MoveAlg) this;
    }

    public MoveAlg addEvent(Event event){
        this.eventList.add(event);
        return (MoveAlg) this;
    }
    public MoveAlg createEvent(Callable<Boolean> condition, Runnable callback){
        forcedEventList.add(new Event(condition, callback));
        return (MoveAlg) this;
    }
    public MoveAlg addForcedEvent(Event event){
        this.eventList.add(event);
        return (MoveAlg) this;
    }
    public MoveAlg createForcedEvent(Callable<Boolean> condition, Runnable callback){
        forcedEventList.add(new Event(condition, callback));
        return (MoveAlg) this;
    }

    public MoveAlg createTimedEvent(int milliseconds, Runnable callback){
        eventList.add(new TimedEvent(milliseconds,callback));
        return (MoveAlg) this;
    }
    public MoveAlg createForcedTimedEvent(int milliseconds, Runnable callback){
        forcedEventList.add(new TimedEvent(milliseconds,callback));
        return (MoveAlg) this;
    }

    public MoveAlg alterEndCondition(Callable<Boolean> condition){
        alternateEndCondition = condition;
        return (MoveAlg) this;
    }
    public MoveAlg alterEndCondition(Callable<Boolean> condition, Runnable ifEndedByAlternateEndCondition){
        alternateEndCondition = condition;
        this.ifEndedByAlternateEndCondition = ifEndedByAlternateEndCondition;
        return (MoveAlg) this;
    }
    public MoveAlg alterEndCondition(Callable<Boolean> condition, Runnable ifEndedByAlternateEndCondition, Runnable ifNotEndedByAlternateEndCondition){
        alternateEndCondition = condition;
        this.ifEndedByAlternateEndCondition = ifEndedByAlternateEndCondition;
        this.ifNotEndedByAlternateEndCondition = ifNotEndedByAlternateEndCondition;
        return (MoveAlg) this;
    }
}
