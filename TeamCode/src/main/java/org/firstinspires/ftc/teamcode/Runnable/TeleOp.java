package org.firstinspires.ftc.teamcode.Runnable;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends BaseTele{
    @Override
    public void Loop() {
        driveTrain.setSpeed(gamepad1.left_stick_y,gamepad1.left_stick_x,gamepad1.right_stick_x);

        Lift.setPower(gamepad2.right_stick_y);
        telemetry.addData("",Lift.getPower());
        telemetry.update();

        LeftGrabber.setPower(gamepad2.left_trigger - gamepad2.right_trigger);
        RightGrabber.setPower(gamepad2.right_trigger - gamepad2.left_trigger);
    }
}
