package org.firstinspires.ftc.teamcode.MovementAlgorithms;

import org.firstinspires.ftc.teamcode.HardwareSystems.HardwareSystem;

import java.util.ArrayList;

public abstract class Movement<MoveAlg extends Movement<MoveAlg>> extends HardwareSystem {
    protected ArrayList<Runnable> preMoveFunctionList = new ArrayList<>();
    protected ArrayList<Runnable> whileMoveFunctionList = new ArrayList<>();
    protected ArrayList<Runnable> postMoveFunctionList = new ArrayList<>();

    boolean firstMove=true;
    abstract void init();

    public boolean execute(){//will return true as long as the movement is unfinished
        if(firstMove){
            firstMove=false;
            init();
            for(Runnable runner : preMoveFunctionList)runner.run();
        }
        for(Runnable runner : whileMoveFunctionList)runner.run();

        if(moveMethod()){
            for(Runnable runner : postMoveFunctionList)runner.run();
            return true;
        }
        return false;
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

}
