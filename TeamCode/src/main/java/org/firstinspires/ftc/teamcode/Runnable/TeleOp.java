package org.firstinspires.ftc.teamcode.Runnable;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends BaseTele{
    @Override
    public void Loop() {
        driveTrain.setSpeed(gamepad1.left_stick_y,gamepad1.left_stick_x,gamepad1.right_stick_x);
    }
}
