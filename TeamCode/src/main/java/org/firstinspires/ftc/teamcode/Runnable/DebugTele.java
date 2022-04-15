package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Events.Event;
import org.firstinspires.ftc.teamcode.HardwareSystems.ToggleSwitch;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
//@Disabled
public class DebugTele extends BaseTele {

    ToggleSwitch speedswitch = new ToggleSwitch();
    ToggleSwitch liftInToggle = new ToggleSwitch();
    ToggleSwitch liftOutToggle = new ToggleSwitch();
    ToggleSwitch inInToggle = new ToggleSwitch();
    ToggleSwitch inOutToggle = new ToggleSwitch();
    ToggleSwitch RetractToggle = new ToggleSwitch();
    ToggleSwitch rumbleCheck = new ToggleSwitch();
    ElapsedTime ejectionTimer = new ElapsedTime();


    public void Init(){
        InFullIn = new Event(()->inExtension.setPower(0),()->inExtension.getCurrentPosition()<10);
        InFullIn.enableOn(inInToggle::getInputResult);
        InFullIn.onEnable(()->inExtension.setPower(-0.8));
        InFullIn.disable();

        LiftFullIn = new Event(()->upExtension.setPower(0),()->upExtension.getCurrentPosition()>-30);
        LiftFullIn.enableOn(liftInToggle::getInputResult);
        LiftFullIn.onEnable(()->upExtension.setPower(1));
        LiftFullIn.disable();

        InFullOut = new Event(()->inExtension.setPower(0),()->inExtension.getCurrentPosition()>310);
        InFullOut.enableOn(inOutToggle::getInputResult);
        InFullOut.onEnable(()->inExtension.setPower(0.8));
        InFullOut.disable();

        LiftFullOut = new Event(()->upExtension.setPower(0),()->upExtension.getCurrentPosition()<-1340);
        LiftFullOut.enableOn(liftOutToggle::getInputResult);
        LiftFullOut.onEnable(()->upExtension.setPower(-1));
        LiftFullOut.disable();

        eventList.add(InFullIn);
        eventList.add(LiftFullIn);
        eventList.add(InFullOut);
        eventList.add(LiftFullOut);

//        AutoRetract = new Event(()->{
//            if(inExtension.getCurrentPosition()>10)InFullIn.enable();
//            intakeFlipper.toPosition(1);
//            intake.setPower(-1);
//            if(upExtension.getCurrentPosition()>-1200)LiftFullIn.enable();
//        },
//            ()->(hasCube()&&gamepad2.left_stick_y==0 && intake.getPower()==0) || RetractToggle.getInputResult(),
////            ()->RetractToggle.getInputResult()&& !(gamepad1.back || gamepad2.back)) ,

//        eventList.add(AutoRetract);
    }

    public void Loop() {
        telemetry.update();
        inInToggle.input(gamepad2.left_bumper || gamepad1.left_bumper);
        liftInToggle.input(gamepad2.right_bumper || gamepad1.right_bumper);
        inOutToggle.input(gamepad2.left_stick_button || gamepad1.left_stick_button);
        liftOutToggle.input(gamepad2.right_stick_button || gamepad1.right_stick_button);
        RetractToggle.input(gamepad2.a);

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
                scaledInput(gamepad1.right_stick_x,.7)
        );

        //control carousel spinner
        if(gamepad1.x) {
            carouselSpinner.setPower(0.6+caroTimer.seconds()*0.1);
        }
        else if(gamepad1.b) {
            carouselSpinner.setPower(-0.6-caroTimer.seconds()*0.1);
        }
        else {
            caroTimer.reset();

            carouselSpinner.setPower(0);
        }

        //intake
        intake.setPower(Math.max(gamepad1.left_trigger,gamepad2.left_trigger)-Math.max(gamepad1.right_trigger,gamepad2.right_trigger));

        //intake flipper
        intakeFlipper.input(gamepad2.dpad_right || gamepad1.dpad_down);

        //dumper position
        if     (gamepad2.dpad_up || gamepad1.dpad_left)  dumper.toPosition(1);
        else if(gamepad2.dpad_left)dumper.toPosition(2);
        else                       dumper.toPosition(0);

        //team element grabber
        teGrabber.input(gamepad1.y || gamepad2.y);

        //team element lift
        teLift.input(gamepad2.a || gamepad1.dpad_up);
//        teLift1.input(gamepad2.a || gamepad1.dpad_up);
//        teLift2.input(gamepad2.a || gamepad1.dpad_up);



        //intake extender
        if(gamepad2.left_stick_y!=0){
            if     (!InFullIn.isDisabled()) {
                InFullIn.disable();
                InFullOut.disable();
            }
            if     (inExtension.getCurrentPosition() <  10 && Math.signum(gamepad2.left_stick_y) ==  1)inExtension.setPower(0);
            else if(inExtension.getCurrentPosition() > 310 && Math.signum(gamepad2.left_stick_y) == -1)inExtension.setPower(0);
            else    inExtension.setPower(-scaledInput(gamepad2.left_stick_y,1));
        }
        else if(InFullIn.isDisabled() && InFullOut.isDisabled())inExtension.setPower(0);

        if(inExtension.getCurrentPosition() < 100 && inExtension.getPower() < 0)inExtension.setPower(inExtension.getPower()*0.8);

        // rumble effect
        if(hasCube()){
            gamepad1.rumble(50);
            gamepad2.rumble(50);
        }

        //up extender
        if(gamepad2.right_stick_y!=0){
            if(!LiftFullIn.isDisabled()) {
                LiftFullIn.disable();
                LiftFullOut.disable();
            }
            if     (upExtension.getCurrentPosition() < -1340 && Math.signum(gamepad2.right_stick_y) == -1)upExtension.setPower(-0.1);
            else if(upExtension.getCurrentPosition() >   -30 && Math.signum(gamepad2.right_stick_y) ==  1)upExtension.setPower(0);
            else    upExtension.setPower(scaledInput(gamepad2.right_stick_y,1));
        }
        else if(LiftFullIn.isDisabled() && LiftFullOut.isDisabled())upExtension.setPower(0);

        if(upExtension.getCurrentPosition() > -200 && upExtension.getPower() > 0)upExtension.setPower(upExtension.getPower()*0.6);



        telemetry.addData("StandardSpeed", !speedswitch.get());
        telemetry.addData("Flipper",intakeFlipper.getPosition());
        telemetry.addLine();

        telemetry.addData("Up-Extend",upExtension.getCurrentPosition());
        telemetry.addData("Up-Power",upExtension.getPower());
        telemetry.addLine();

        telemetry.addData("In-Extend",inExtension.getCurrentPosition());
        telemetry.addData("In-Power",inExtension.getPower());
        telemetry.addLine();

        telemetry.addLine()
                .addData("TeLift",teLift.getPos())
                .addData("",teLift.getPosition());
//        telemetry.update();
        telemetry.addLine();

        telemetry.addLine("Drive: ")
                .addData("FL",FrontLeft.getPower())
                .addData("FR",FrontRight.getPower())
                .addData("BL",BackLeft.getPower())
                .addData("FR",BackRight.getPower());
        telemetry.addLine();

        telemetry.addLine("Retract:   ")
                .addData("up",  !LiftFullIn.isDisabled())
                .addData("out", !InFullIn.isDisabled());
        telemetry.addLine("Extend:   ")
                .addData("up",  !LiftFullOut.isDisabled())
                .addData("out", !InFullOut.isDisabled());
        telemetry.addLine();

        telemetry.addData("HasCube",hasCube());
        telemetry.addData("CubeDist",intakeScanner.getDistance(DistanceUnit.MM));
        telemetry.addData("G2 LSY",gamepad2.left_stick_y);


//        telemetry.addLine("Test: " + new Integer(1).equals(new Integer(1)));

    }
}
