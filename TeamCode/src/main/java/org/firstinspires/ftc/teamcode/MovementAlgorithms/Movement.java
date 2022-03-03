package org.firstinspires.ftc.teamcode.MovementAlgorithms;

import org.firstinspires.ftc.teamcode.HardwareSystems.HardwareSystem;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public abstract class Movement<MoveAlg extends Movement<MoveAlg>> extends HardwareSystem {
    protected ArrayList<Runnable> preMoveFunctionList = new ArrayList<>();
    protected ArrayList<Runnable> whileMoveFunctionList = new ArrayList<>();
    protected ArrayList<Runnable> postMoveFunctionList = new ArrayList<>();

    Callable<Boolean> condition = ()->true;



    abstract void init();

    public void execute(){
        init();
        for(Runnable runner : preMoveFunctionList)runner.run();

        try {
            while((!moveMethod() || !condition.call())){
                for(Runnable runner : whileMoveFunctionList)runner.run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(Runnable runner : postMoveFunctionList)runner.run();
    }
    abstract boolean moveMethod();

    public MoveAlg addPreMoveFunction(Runnable func){
        preMoveFunctionList.add(func);
        return (MoveAlg) this;
    }
    public MoveAlg addMoveFunction(Runnable func){
        whileMoveFunctionList.add(func);
        return (MoveAlg) this;
    }
    public MoveAlg addPostMoveFunction(Runnable func){
        postMoveFunctionList.add(func);
        return (MoveAlg) this;
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

    public MoveAlg setCondition(Callable<Boolean> condition) {
        this.condition = condition;
        return (MoveAlg) this;
    }
}
