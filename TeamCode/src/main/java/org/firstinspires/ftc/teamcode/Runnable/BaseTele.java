package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Events.*;
import org.firstinspires.ftc.teamcode.Systems.MecanumDriveTrain;
import org.firstinspires.ftc.teamcode.Systems.SetPowerMotor;

import java.util.ArrayList;

public abstract class BaseTele extends OpMode {
    protected ElapsedTime elapsedTime;
    protected ArrayList<Event> eventList = new ArrayList<>();

    // Declare hardware variables{{{


    protected DcMotor FrontLeft, FrontRight, BackLeft, BackRight;
    protected DcMotor Lift;
    protected MecanumDriveTrain driveTrain;

    protected CRServo LeftGrabber, RightGrabber;
    
    
    // }}}


    public void init(){
        elapsedTime = new ElapsedTime();
	// initalize hardware variables{{{

        FrontLeft = hardwareMap.dcMotor.get("frontLeft");
        FrontRight= hardwareMap.dcMotor.get("frontRight");
        BackLeft  = hardwareMap.dcMotor.get("backLeft");
        BackRight = hardwareMap.dcMotor.get("backRight");


        driveTrain = new MecanumDriveTrain(
                hardwareMap.dcMotor.get("frontLeft"),
                hardwareMap.dcMotor.get("frontRight"),
                hardwareMap.dcMotor.get("backLeft"),
                hardwareMap.dcMotor.get("backRight"),
                1
        );


        Lift = hardwareMap.dcMotor.get("lift");

        LeftGrabber = hardwareMap.crservo.get("leftGrabber");
        RightGrabber = hardwareMap.crservo.get("rightGrabber");
	
	
	// }}}
    }
    public void loop(){
        Loop();

        for(Event event : eventList)event.test();
    }
    public abstract void Loop();
}
