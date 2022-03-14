package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MovementAlgorithms.*;

public abstract class BaseAuto extends LinearOpMode {
    public abstract void initializeMovements();

    public void runOpMode(){
        initSystems();
        initializeMovements();
        waitForStart();
        moveSequence.executeSequence();
        waitForEnd();
    }

    MoveSequence moveSequence = new MoveSequence(this);

    //Declare hardware variables {{{
    
    
    
    
    //}}}


    public void initSystems(){
	    // Initalize hardware variables {{{
	    //    ex: FrontLeft = hardwaremap.dcmotor.get("frontleft");
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    // }}}
    }

    public void interrupt(){
        moveSequence.interrupt();
    }

    public void waitForEnd(){
        while(!isStopRequested()&&opModeIsActive());
    }
}
