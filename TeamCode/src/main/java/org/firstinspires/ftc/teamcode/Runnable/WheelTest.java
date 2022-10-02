package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
@Disabled
public class WheelTest extends BaseTele{
    @Override
    public void Loop() {
        if(gamepad1.a) FrontLeft.setPower(0.5);
        else FrontLeft.setPower(0);
        if(gamepad1.b) FrontRight.setPower(0.5);
        else FrontRight.setPower(0);
        if(gamepad1.x) BackRight.setPower(0.5);
        else BackRight.setPower(0);
        if(gamepad1.y) BackLeft.setPower(0.5);
        else BackLeft.setPower(0);
    }
}
