package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.HardwareSystems.*;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.*;

import java.util.ArrayList;

public abstract class BaseAuto extends LinearOpMode {
    public abstract void initializeMovements();

    public void runOpMode(){
        initSystems();
        initializeMovements();
        waitForStart();
        executeMoveSequence();
        waitForEnd();
    }

    ArrayList<Movement> MoveSequence = new ArrayList<>();

    //Declare hardware variables {{{
    
    
    
    
    //}}}


    public void initSystems(){
	    // Initalize hardware variables {{{
	    //    ex: FrontLeft = hardwaremap.dcmotor.get("frontleft");
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    // }}}
    }

    public void executeMoveSequence(){
        for(Movement movement : MoveSequence) {

            // if interrupted wait for robot to stop then end program
            if(movement.getClass().equals(Interrupt.class)){
                driveTrain.stope();

                ElapsedTime waitForEnd = new ElapsedTime();
                while(!isStopRequested() && waitForEnd.seconds() < 1.3);

                return;
            }

            // execute all movements
            while(!isStopRequested() && !movement.execute());
        }
    }

    public void interrupt(){
        MoveSequence.add(new Interrupt());
    }

    public void waitForEnd(){
        while(!isStopRequested()&&opModeIsActive());
    }

    public void RemoveMoveFunctions(){
	//Removes all of the pre/while/post move functions from MoveSequence
	//add back all of the stop postMoveFunctions
        for(Movement movement : MoveSequence){
            movement.removeAllPostMoveFunctions();
            movement.removeAllPreMoveFunctions();
            movement.removeAllMoveFunctions();
            movement.addPostMoveFunction(()->driveTrain.stope());
        }
    }
}
