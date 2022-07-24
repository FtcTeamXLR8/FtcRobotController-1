package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Movement.*;
import org.firstinspires.ftc.teamcode.Systems.MecanumDriveTrain;

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

    MecanumDriveTrain driveTrain;
    
    
    
    //}}}


    public void initSystems(){
	    // Initalize hardware variables {{{
	    //    ex: FrontLeft = hardwaremap.dcmotor.get("frontleft");
	    
	    driveTrain = new MecanumDriveTrain(
            hardwareMap.dcMotor.get("frontLeft"),
            hardwareMap.dcMotor.get("frontRight"),
            hardwareMap.dcMotor.get("backLeft"),
            hardwareMap.dcMotor.get("backRight"),
            1
        );
	    
	    
	    
	    
	    
	    
	    
	    // }}}
    }

    public void interrupt(){
        moveSequence.interrupt();
    }

    public void waitForEnd(){
        while(!isStopRequested()&&opModeIsActive());
    }
}
