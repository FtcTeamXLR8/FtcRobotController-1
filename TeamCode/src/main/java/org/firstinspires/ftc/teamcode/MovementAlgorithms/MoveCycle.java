package org.firstinspires.ftc.teamcode.MovementAlgorithms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;

public class MoveCycle {

    LinearOpMode opMode;

    public ArrayList<Movement> MoveSequence = new ArrayList<>();

    public MoveCycle(LinearOpMode opMode){
        this.opMode = opMode;
    }

    public void add(Movement move){
        MoveSequence.add(move);
    }
    public void executeSequence(){
        for (Movement movement : MoveSequence) {

            // if interrupted wait for robot to stop then end program
            if (movement.getClass().equals(Interrupt.class)) {

                ElapsedTime waitForEnd = new ElapsedTime();
                while (!opMode.isStopRequested() && waitForEnd.seconds() < 1.3) ;

                break;
            }

            // execute movement
            movement.execute();
        }
    }
    public void addWhileMoveToEach(Runnable func){
        for(Movement movement : MoveSequence){
            movement.addMoveFunction(func);
        }
    }
}

