package org.firstinspires.ftc.teamcode.Tutorials;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Auto Tutorial", group = "Tutorial")
@Disabled
public class AutoTutorial extends LinearOpMode {
    public void runOpMode(){
        waitForStart();
        while(opModeIsActive()){

            hardwareMap.get(DcMotor.class,"leftMotor").setPower(.4);

        }
    }
}
