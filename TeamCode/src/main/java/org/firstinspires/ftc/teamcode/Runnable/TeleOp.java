package org.firstinspires.ftc.teamcode.Runnable;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
//@Disabled
public class TeleOp extends BaseTele {

    boolean lastspeed=true;
    boolean speedswitch=true;

    @Override
    public void loop() {
        telemetry.update();

        if(gamepad1.a&&lastspeed){//control speed switch on press of a
            lastspeed=false;
            speedswitch=!speedswitch;
        }
        else if(!gamepad1.a) speedswitch=true;

        //set powers
        if(speedswitch)driveTrain.setSpeed(scaledInput(gamepad1.left_stick_y,-.7),scaledInput(gamepad1.left_stick_x,.7),scaledInput(gamepad1.right_stick_x,.7));
        else driveTrain.setSpeed(scaledInput(gamepad1.left_stick_y,-.25),scaledInput(gamepad1.left_stick_x,.25),scaledInput(gamepad1.right_stick_x,.25));

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

        telemetry.addData("Slowmode: ", !speedswitch);
        telemetry.addLine();
        telemetry.addData("Lift: ", upExtension.getPower());
        telemetry.addData("TeG", teGrabber.getPosition());
//        telemetry.addData("FR",FrontRight.getCurrentPosition());
//        telemetry.addData("FR",FrontLeft.getCurrentPosition());
    }
}
