package org.firstinspires.ftc.teamcode.MovementAlgorithms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.HardwareSystems.HardwareSystem;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public abstract class Movement<MoveAlg extends Movement<MoveAlg>> extends HardwareSystem {
    protected ArrayList<Runnable> preMoveFunctionList = new ArrayList<>();
    protected ArrayList<Runnable> whileMoveFunctionList = new ArrayList<>();
    protected ArrayList<Runnable> postMoveFunctionList = new ArrayList<>();

    protected Callable<Boolean> condition = ()->true;
    protected Runnable ifEndedByCondition = ()-> {};
    protected Runnable ifNotEndedByCondition = () ->{};


    abstract void init();

    public void execute(){
        init();
        for(Runnable runner : preMoveFunctionList)runner.run();

        try {
            while(true){
                if(!condition.call()){
                   for(Runnable runner : postMoveFunctionList)runner.run();
                   ifEndedByCondition.run();
                   break;
                }
                if(moveMethod()){
                    for(Runnable runner : postMoveFunctionList)runner.run();
                    ifNotEndedByCondition.run();
                    break;
                }
                for(Runnable runner : whileMoveFunctionList)runner.run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void execute(LinearOpMode opMode) {
        init();
        for (Runnable runner : preMoveFunctionList) runner.run();

        try {
            while (!opMode.isStopRequested()) {
                if (!condition.call()) {
                    ifEndedByCondition.run();
                    break;
                }
                if (moveMethod()){
                    ifNotEndedByCondition.run();
                    break;
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

    public Movement<MoveAlg> setCondition(Callable<Boolean> condition) {
        this.condition = condition;
        return this;
    }
}
