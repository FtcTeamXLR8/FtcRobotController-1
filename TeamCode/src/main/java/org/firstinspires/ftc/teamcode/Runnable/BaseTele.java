package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import java.lang.annotation.Target;
import com.qualcomm.robotcore.hardware.*;
import java.util.Set;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.Targets;

import org.firstinspires.ftc.teamcode.Misc.PositionVar;
import org.firstinspires.ftc.teamcode.Systems.*;
import org.firstinspires.ftc.teamcode.Toggles.ToggleSwitch;
import org.firstinspires.ftc.teamcode.Toggles.ToggleUpdater;

public abstract class BaseTele extends OpMode {
    SystemUpdater su = new SystemUpdater();
    ToggleUpdater tu = new ToggleUpdater();
    
    DcMotor FrontLeft, FrontRight, BackLeft, BackRight, Intake, Conveyor;
    DcMotorEx LauncherLeft, LauncherRight;
    CRServo Splitter, GrabberLift, TopLift, Srv;//Evan, i made a seccond grabberlift and Srv. servo so we could run the lift but I couldne figure out how to make a new one with your system.
    Servo Hook, Stopper, Grabber;
    
    HolonomicDriveTrain driveTrain;
    public Odometry odom;
    Launcher launcher;
    Grabber grabber;
    Grabber hook;
    ServoLift grabberLift;
    Intake intake;
    Conveyor conveyor;
    
    String OdomMode = "";
    
        
    ToggleSwitch a1Toggle = new ToggleSwitch(gamepad1);//construct controller 1 toggle switches
    ToggleSwitch b1Toggle = new ToggleSwitch(gamepad1);
    ToggleSwitch x1Toggle = new ToggleSwitch(gamepad1);
    ToggleSwitch y1Toggle = new ToggleSwitch(gamepad1);
    ToggleSwitch lb1Toggle = new ToggleSwitch(gamepad1);
    ToggleSwitch rb1Toggle = new ToggleSwitch(gamepad1);
    ToggleSwitch lsb1Toggle = new ToggleSwitch(gamepad1);
    ToggleSwitch rsb1Toggle = new ToggleSwitch(gamepad1);

    ToggleSwitch a2Toggle = new ToggleSwitch(gamepad2);//construct controller 2 toggle switches
    ToggleSwitch b2Toggle = new ToggleSwitch(gamepad2);
    ToggleSwitch x2Toggle = new ToggleSwitch(gamepad2);
    ToggleSwitch y2Toggle = new ToggleSwitch(gamepad2);
    ToggleSwitch lb2Toggle = new ToggleSwitch(gamepad2);
    ToggleSwitch rb2Toggle = new ToggleSwitch(gamepad2);
    ToggleSwitch lsb2Toggle = new ToggleSwitch(gamepad2);
    ToggleSwitch rsb2Toggle = new ToggleSwitch(gamepad2);

    final int NumberOfSaves = 3;
    PositionVar[] posSaves = new PositionVar[NumberOfSaves];
    Integer[] posSavesPrec = new Integer[NumberOfSaves];
    int saveSlot = 0;
    
    Targets targets = new Targets();
    
    final PositionVar[] targetSaves = {targets.Tower,targets.BlueTower,targets.PowerShot1,targets.PowerShot2,targets.PowerShot3};
    final int NumberOfTargets = 5;
    int targetSlot = 0;
    
    int stability = 0;
    int shotCount = 0;

    public void initToggles() {

        {
            tu.addToggle(a1Toggle);
            tu.addToggle(b1Toggle);
            tu.addToggle(x1Toggle);
            tu.addToggle(y1Toggle);
            tu.addToggle(lb1Toggle);
            tu.addToggle(rb1Toggle);
            tu.addToggle(lsb1Toggle);
            tu.addToggle(rsb1Toggle);
        }//init controller 1 toggle switches
        {
            tu.addToggle(a2Toggle);
            tu.addToggle(b2Toggle);
            tu.addToggle(x2Toggle);
            tu.addToggle(y2Toggle);
            tu.addToggle(lb2Toggle);
            tu.addToggle(rb2Toggle);
            tu.addToggle(lsb2Toggle);
            tu.addToggle(rsb2Toggle);
        }//init controller 2 toggle switches
        rsb2Toggle.setInput(false);
    }
    public void updateToggles(){
            a1Toggle.setInput(gamepad1.a);
            b1Toggle.setInput(gamepad1.b);
            x1Toggle.setInput(gamepad1.x);
            y1Toggle.setInput(gamepad1.y);
            lb1Toggle.setInput(gamepad1.left_bumper);
            rb1Toggle.setInput(gamepad1.right_bumper);
            lsb1Toggle.setInput(gamepad1.left_stick_button);
            rsb1Toggle.setInput(gamepad1.right_stick_button);
            a2Toggle.setInput(gamepad2.a);
            b2Toggle.setInput(gamepad2.b);
            x2Toggle.setInput(gamepad2.x);
            y2Toggle.setInput(gamepad2.y);
            lb2Toggle.setInput(gamepad2.left_bumper);
            rb2Toggle.setInput(gamepad2.right_bumper);
            lsb2Toggle.setInput(gamepad2.left_stick_button);
            rsb2Toggle.setInput(gamepad2.right_stick_button);
            
            tu.update();
    }
    public void initSystems(){
        // telemetry.setAutoClear(false);
        
        FrontLeft =     hardwareMap.get(DcMotor.class,"frontLeft");
        FrontRight =    hardwareMap.get(DcMotor.class,"frontRight");
        BackLeft =      hardwareMap.get(DcMotor.class,"backLeft");
        BackRight =     hardwareMap.get(DcMotor.class,"backRight");
        Intake =        hardwareMap.get(DcMotor.class,"intake");
        Conveyor =      hardwareMap.get(DcMotor.class,"conveyor");
        LauncherRight = hardwareMap.get(DcMotorEx.class,"launcherRight");
        LauncherLeft =  hardwareMap.get(DcMotorEx.class,"launcherLeft");
    
        Splitter =      hardwareMap.crservo.get("splitter");
        GrabberLift =   hardwareMap.crservo.get("prankt");// needs to be "bottomServo" but changes so it would work
        Srv =           hardwareMap.crservo.get("2"); //sdded to make the servos work can be deleted later
        TopLift =       hardwareMap.crservo.get("topServo");
        
        // S0 = hardwareMap.servo.get("wobbleUp");
        Hook = hardwareMap.servo.get("hook");
        Stopper = hardwareMap.servo.get("stopper");
        Grabber = hardwareMap.servo.get("grabber");
        
        resetEncoder(FrontLeft);
        resetEncoder(FrontRight);
        resetEncoder(BackLeft);
        resetEncoder(BackRight);
        resetEncoder(Intake);
        resetEncoder(Conveyor);
        resetEncoder(LauncherRight);
        resetEncoder(LauncherLeft);
        
        odom = new Odometry(BackRight,FrontRight,BackLeft,FrontLeft,FrontRight,BackLeft,BackRight);
        driveTrain = new HolonomicDriveTrain(FrontLeft,FrontRight,BackLeft,BackRight);
        launcher = new Launcher(LauncherLeft, LauncherRight,Stopper,Splitter, 1900);
        grabber = new Grabber(Grabber,.5,.8);
        hook = new Grabber(Hook,0,.52);
        grabberLift = new ServoLift(GrabberLift); //commented out because it was causing errors with things we wernt useing
        intake = new Intake(Intake);
        conveyor = new Conveyor(Conveyor);
        
        // launcher.runWithVelocity();
        
        
        su.addSystem(driveTrain);
        su.addSystem(odom);
        su.addSystem(launcher);
        su.addSystem(grabberLift);
        su.addSystem(intake);
        su.addSystem(conveyor);
        
        driveTrain.setSpeedScalar(.7);
        
        driveTrain.enable();
        odom.disable();
        
        launcher.setLauncherSpeed(.88);
    }
    public void resetEncoder(DcMotor c){
        c.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        c.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void initSaves(){
        for(int i=0;i<NumberOfSaves;i++){
            posSaves[i] = odom.getPos();
            posSavesPrec[i] = 15;
        }
    }
    int x1presses=0;
    public void onX1Press(){
        if(x1Toggle.onOnce()){
            x1presses++;
            posSaves[saveSlot] = new PositionVar(odom.getPos().getX(),odom.getPos().getY(),odom.getPos().getR());
        }
    }
    int b1presses=0;
    public void onB1Press(){
        if(b1Toggle.onOnce()){
            if(OdomMode=="Driving"){
                OdomMode = "Moving";
            }
            else{
                OdomMode = "Driving";
            }
            autoMove();
            b1presses++;
        }
    }
    int y1presses=0;
    
    int rb1presses=0;
    public void onRB1Press(){
        if(rb1Toggle.onOnce()){
            rb1presses++;
            // if(lsb1Toggle.get()){
                saveSlot++;
            // }
            // else{
                targetSlot++;
            // }
            if(saveSlot>=NumberOfSaves){
                saveSlot = 0;
            }
            if(targetSlot>=NumberOfTargets){
                targetSlot=0;
            }
        }
    }
    int lb1presses=0;
    public void onLB1Press(){
        if(lb1Toggle.onOnce()){
            lb1presses++;
            // if(lsb1Toggle.get()){
                saveSlot--;
            // }
            // else{
                targetSlot--;
            // }
            if(saveSlot<0){
                saveSlot = NumberOfSaves-1;
            }
            if(targetSlot<0){
                targetSlot=NumberOfTargets-1;
            }
        }
    }
    int a2presses=0;
    public void onA2Press(){
        if(a2Toggle.onOnce()){
            a2presses++;
            
        }
    }
    int rsb2Presses=0;
    public void onRsb2Press(){
        if(rsb2Toggle.onOnce()){
            launcher.setTargetVelocity(1650);
        }
    }
    int lsb2presses=0;
    public void onLSB2Press(){
        if(lsb2Toggle.onOnce()){
            launcher.setTargetVelocity(1900);
        }
    }
    int x2Presses=0;
    public void onX2Press(){
        if(x2Toggle.onOnce()){
            // x2Presses++;
            // if(rsb2Toggle.get()){
                launcher.setTargetVelocity(launcher.getTargetVelocity()-10);
            // }
            // else{
            //     launcher.setLauncherSpeed(launcher.getLauncherSpeed()-.02);
            // }
        }
    }
    int rb2Presses=0;
    public void onRb2Press(){
        if(rb2Toggle.onOnce()){
            rb2Presses++;
            // if(rsb2Toggle.get()){
                launcher.setTargetVelocity(launcher.getTargetVelocity()+10);
            // }
            // else{
            //     launcher.setLauncherSpeed(launcher.getLauncherSpeed()+.02);
            // }
        }
    }
    public void runGrabberLift(){
        //grabberLift.setInput(gamepad2.left_stick_y);
        Srv.setPower(gamepad2.left_stick_y);
        TopLift.setPower(gamepad2.left_stick_y);
    }
    public void runIntake(){
        intake.setInput(gamepad2.left_trigger-gamepad2.right_trigger);
    }
    public void runConveyor(){
        //if(odom.MOE(launcher.getRPM(),launcher.getTargetVelocity(),150))
        //  if(!gamepad2.left_bumper){//switch between controlling intake + conveyor and just conveyor
        //     conveyor.setInput(gamepad2.left_trigger-gamepad2.right_trigger);
        // }
        // else{
        //     if(launcher.atSpeed){
        //         conveyor.setInput(.6);
        //     }
        //     else{
        //         conveyor.setInput(0);
        //     }
    
        
        //everything below here for the conveyor is basic for changes for Liam.
        if(gamepad2.left_bumper){
            conveyor.setInput(.9);
        }
        else{
            conveyor.setInput(gamepad2.left_trigger-gamepad2.right_trigger);
        }
        
        
    }
    
    public void runGrabber(){
        if(a2Toggle.get()){
            grabber.grabberOpen();
        }
        else{
            grabber.grabberClose();
        }
    }
    public void autoMove(){
        odom.setPrecision(posSavesPrec[saveSlot]);
        odom.setTarget(posSaves[saveSlot]);
    }
    public void autoAim(){
        if(lsb1Toggle.get()){
            odom.setPrecision(posSavesPrec[saveSlot]);
            odom.setTarget(new PositionVar(odom.getPos().getX(),odom.getPos().getY(),0));
        }
        else{
            odom.setPrecision(15);
            odom.setTarget(new PositionVar(odom.getPos().getX(),odom.getPos().getY(),odom.pointedAt(targetSaves[targetSlot])));
        }
    }
    public void runLauncher(){
        if(gamepad2.b){
            if(!launcher.launching){
                launcher.startLaunching();
            }
        }
        else{
            if(launcher.launching){
                launcher.endLaunching();
                launcher.stope();
            }
        }
    }
    
    
    

    public void Update(){
        
        updateToggles();
        
        if(!a1Toggle.get()){
            driveTrain.setSpeedScalar(.9);
        }
        
        driveTrain.setInput(gamepad1.left_stick_x, gamepad1.left_stick_y,gamepad1.right_stick_x);

        runGrabberLift();
        runGrabber();
        runLauncher();
        runIntake();
        runConveyor();
        

        onLSB2Press();
        onRsb2Press();
        onX2Press();
        onRb2Press();
        
        onB1Press();
        onLB1Press();
        onRB1Press();
        onX1Press();
        
        if(launcher.getLaunching()){
            launcher.adjustSpeed();
        }
        else{
            launcher.atSpeed=false;
            launcher.setLauncherSpeed(0);
        }

        if(a1Toggle.get()){
            driveTrain.setSpeedScalar(.35);
        }
        else{
            driveTrain.setSpeedScalar(.7);
        }
        

        su.update();
    }
}
