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
        for (Movement movement : this) {

            // if interrupted wait for robot to stop then end program
            if (movement.getClass().equals(Interrupt.class)) {

                ElapsedTime waitForEnd = new ElapsedTime();
                while (!opMode.isStopRequested() && waitForEnd.seconds() < 1.3) ;

                break;
            }

            // execute movement
            movement.execute(opMode);
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
}

