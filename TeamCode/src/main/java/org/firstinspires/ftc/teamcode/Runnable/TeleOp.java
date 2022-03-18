package org.firstinspires.ftc.teamcode.Runnable;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Events.Event;
import org.firstinspires.ftc.teamcode.HardwareSystems.ToggleSwitch;

import java.security.cert.Extension;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
//@Disabled
public class TeleOp extends BaseTele {

    ToggleSwitch speedswitch = new ToggleSwitch();
    ToggleSwitch liftIn = new ToggleSwitch();
    ToggleSwitch inIn = new ToggleSwitch();


    public void Init(){
        InFullIn = new Event(()->inExtension.setPower(0),()->inExtension.getCurrentPosition()<50);
        InFullIn.reEnableOn(()->inIn.input(gamepad2.left_bumper));
        InFullIn.onEnable(()->inExtension.setPower(-0.8));
        InFullIn.disable();

        LiftFullIn = new Event(()->upExtension.setPower(0),()->upExtension.getCurrentPosition()<50);
        LiftFullIn.reEnableOn(()->liftIn.input(gamepad2.right_bumper));
        LiftFullIn.onEnable(()->upExtension.setPower(-0.8));
        LiftFullIn.disable();
    }
    @Override
    public void Loop() {
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
        if(gamepad2.right_stick_y!=0){
            if(!InFullIn.isDisabled())InFullIn.disable();
            if     (inExtension.getCurrentPosition() <  60 && Math.signum(gamepad2.right_stick_y) ==  1)inExtension.setPower(0);
            else if(inExtension.getCurrentPosition() > 310 && Math.signum(gamepad2.right_stick_y) == -1)inExtension.setPower(0);
            else    inExtension.setPower(-scaledInput(gamepad2.right_stick_y,1));
        }
        else if(InFullIn.isDisabled())inExtension.setPower(0);


        //up extender
        if(gamepad2.left_stick_y!=0){
            if(!LiftFullIn.isDisabled())LiftFullIn.disable();
            if     (upExtension.getCurrentPosition() < -1260 && Math.signum(gamepad2.left_stick_y) == -1)upExtension.setPower(-0.1);
            else if(upExtension.getCurrentPosition() >   -50 && Math.signum(gamepad2.left_stick_y) ==  1)upExtension.setPower(0);
            else    upExtension.setPower(scaledInput(gamepad2.left_stick_y,1));
        }
        else if(LiftFullIn.isDisabled())upExtension.setPower(0);


        //intake power
        intake.setPower(gamepad2.left_trigger + gamepad1.left_trigger - gamepad2.right_trigger - gamepad1.right_trigger);

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
        telemetry.addLine("Flipper: "+intakeFlipper.getPosition());
        telemetry.addLine();
        telemetry.addLine("Up-Extend: "+upExtension.getCurrentPosition());
        telemetry.addLine("Up-Power: "+upExtension.getPower());
        telemetry.addLine();
        telemetry.addLine("In-Extend: "+inExtension.getCurrentPosition());
        telemetry.addLine("In-Power: "+inExtension.getPower());
    }
}
