package org.firstinspires.ftc.teamcode.Runnable;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.HardwareSystems.ToggleSwitch;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
//@Disabled
public class TeleOp extends BaseTele {

    ToggleSwitch speedswitch = new ToggleSwitch();

    @Override
    public void loop() {
        telemetry.update();

        // when speedswitch toggles change drive speed
        if(speedswitch.input(gamepad1.a)){
            if(!speedswitch.get()){
                driveTrain.rightspeed=0.7;
                driveTrain.forspeed = 0.7;
            }
            else {
                driveTrain.rightspeed=1;
                driveTrain.forspeed = 1;
            }
        }

        //set powers
        driveTrain.setSpeed(
                scaledInput(gamepad1.left_stick_y,-.7),
                scaledInput(gamepad1.left_stick_x,.7),
                scaledInput(gamepad1.right_stick_x,.7));

        //control carousel spinner
        if(gamepad2.b) {
            carouselSpinner.setPower(0.6+caroTimer.seconds()*0.1);
        }
        else if(gamepad2.x) {
            carouselSpinner.setPower(-0.6-caroTimer.seconds()*0.1);
        }
        else {
            caroTimer.reset();

            carouselSpinner.setPower(0);
        }

        //intake extender
        inExtension.setPower(-scaledInput(gamepad2.left_stick_y,0.6));

        //up extender
        upExtension.setPower(scaledInput(gamepad2.right_stick_y,1));

        //intake power
        intake.setPower(gamepad2.left_trigger - gamepad2.right_trigger);

        //dumper position
        if     (gamepad2.dpad_up)  dumper.toPosition(1);
        else if(gamepad2.dpad_left)dumper.toPosition(2);
        else                       dumper.toPosition(0);

        //intake flipper
        intakeFlipper.input(gamepad2.dpad_right);

        //team element grabber
        teGrabber.input(gamepad2.dpad_down);

        //team element lift
        teLift1.input(gamepad2.a);
        teLift2.input(gamepad2.a);

        telemetry.addData("StandardSpeed: ", !speedswitch.get());
        telemetry.addLine();
        telemetry.addData("Lift: ", upExtension.getPower());
        telemetry.addData("TeG", teGrabber.getPosition());
        telemetry.addLine("Distance: "+intakeScanner.getDistance(DistanceUnit.MM));
//        telemetry.addData("FR",FrontRight.getCurrentPosition());
//        telemetry.addData("FR",FrontLeft.getCurrentPosition());
    }
}
