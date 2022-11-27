package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.hardware.DcMotor;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class LogEncoders extends BaseTele{
    @Override
    public void Loop(){
        telemetry.addLine("FL: "+FrontLeft.getCurrentPosition());
        telemetry.addLine("FR: "+FrontRight.getCurrentPosition());
        telemetry.addLine("BL: "+BackLeft.getCurrentPosition());
        telemetry.addLine("BR: "+BackRight.getCurrentPosition());
        telemetry.update();
    }
}
