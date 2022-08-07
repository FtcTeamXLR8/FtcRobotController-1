package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Events.*;
import org.firstinspires.ftc.teamcode.Systems.MecanumDriveTrain;

import java.util.ArrayList;

public abstract class BaseTele extends OpMode {
    ElapsedTime elapsedTime;
    ArrayList<Event> eventList = new ArrayList<>();

    // Declare hardware variables{{{


    DcMotor FrontLeft, FrontRight, BackLeft, BackRight;
    MecanumDriveTrain driveTrain;
    
    
    
    // }}}


    public void init(){
        elapsedTime = new ElapsedTime();
	// initalize hardware variables{{{

        FrontLeft =        hardwareMap.dcMotor.get("frontLeft");
        FrontRight=        hardwareMap.dcMotor.get("frontRight");
        BackLeft  =        hardwareMap.dcMotor.get("backLeft");
        BackRight =        hardwareMap.dcMotor.get("backRight");


        driveTrain = new MecanumDriveTrain(
                hardwareMap.dcMotor.get("frontLeft"),
                hardwareMap.dcMotor.get("frontRight"),
                hardwareMap.dcMotor.get("backLeft"),
                hardwareMap.dcMotor.get("backRight"),
                1
        );
	
	
	
	// }}}
    }
    public void loop(){
        Loop();

        for(Event event : eventList)event.test();
    }
    public abstract void Loop();
}
