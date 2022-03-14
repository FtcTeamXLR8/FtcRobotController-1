package org.firstinspires.ftc.teamcode.MovementAlgorithms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class MoveSequence extends java.util.ArrayList<Movement>{

    LinearOpMode opMode;
    Movement currentMovement;
    int currentMovementIndex;

    public MoveSequence(LinearOpMode opMode){
        this.opMode = opMode;
    }

    public void executeSequence(){
        currentMovementIndex = 0;
        int startMove=0;
        int i=0;
        for(Movement movement : this){
            if(movement.getClass().equals(JumpHere.class)) {
                startMove=i;
            }
            i++;
        }
        i=0;
        for (Movement movement : this) {
            if(i<startMove){
                i++;
                continue;
            }
            currentMovementIndex=i;
            currentMovement=movement;

            // if interrupted wait for robot to stop then end program
            if (movement.getClass().equals(Interrupt.class)) {

                ElapsedTime waitForEnd = new ElapsedTime();
                while (!opMode.isStopRequested() && waitForEnd.seconds() < 1.3) ;

                break;
            }

            // execute movement
            movement.execute(opMode);
            i++;
        }
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

    public int getCurrentMovementIndex() {
        return currentMovementIndex;
    }
    public Movement getCurrentMovement() {
        return currentMovement;
    }

    static class JumpHere extends Movement{
        void init(){}

        boolean moveMethod() {
            return true;
        }
    }
    static class Interrupt extends Movement {
        void init() {}

        boolean moveMethod() {
            return false;
        }
    }
}

