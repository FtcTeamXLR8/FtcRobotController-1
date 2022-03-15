package org.firstinspires.ftc.teamcode.MovementAlgorithms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class MoveCycle extends java.util.ArrayList<Movement>{

    LinearOpMode opMode;

    public MoveCycle(LinearOpMode opMode){
        this.opMode = opMode;
    }

    public void executeSequence(){

    }
    public void addWhileMoveToEach(Runnable func){
        for(Movement movement : this){
            movement.addMoveFunction(func);
        }
    }
    public void addPostMoveToEach(Runnable func){
        for(Movement movement : this){
            movement.addPostMoveFunction(func);
        }
    }
    public void interrupt(){
        this.add(new Interrupt());
    }
    public void jumpToHere(){
        this.add(new JumpHere());
    }
    static class Interrupt extends Movement{

        @Override
        void init() {

        }

        @Override
        boolean moveMethod() {
            return false;
        }
    }
    static class JumpHere extends Movement{
        @Override
        void init() {

        }

        @Override
        boolean moveMethod() {
            return true;
        }
    }
}

