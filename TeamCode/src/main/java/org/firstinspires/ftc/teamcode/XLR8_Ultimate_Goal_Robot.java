package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Hardware;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.teamcode.Systems.*;
import org.firstinspires.ftc.teamcode.Misc.*;
import org.firstinspires.ftc.teamcode.Toggles.*;



public class XLR8_Ultimate_Goal_Robot {
    SystemUpdater su = new SystemUpdater();
    ToggleUpdater tu = new ToggleUpdater();
    
    public DcMotor FrontLeft, FrontRight, BackLeft, BackRight, Intake, Conveyor;
    public DcMotorEx LauncherLeft, LauncherRight;
    public CRServo Splitter, GrabberLift;
    public Servo Hook, Stopper, Grabber;
    
    public HolonomicDriveTrain driveTrain;
    public Odometry odom;
    public Launcher launcher;
    public Grabber grabber;
    public Grabber hook;
    public ServoLift grabberLift;
    public Intake intake;
    public Conveyor conveyor;
    
    public Gamepad gamepad1, gamepad2;
    public Telemetry telemetry;
    
    public final int NumberOfSaves = 3;
    public PositionVar[] posSaves = new PositionVar[NumberOfSaves];
    public Integer[] posSavesPrec = new Integer[NumberOfSaves];
    public int saveSlot = 0;
    
    public String OdomMode = "";
    
    ToggleSwitch a1Toggle;
    ToggleSwitch b1Toggle;
    ToggleSwitch x1Toggle;
    ToggleSwitch y1Toggle;
    ToggleSwitch lb1Toggle;
    ToggleSwitch rb1Toggle;
    ToggleSwitch lsb1Toggle;
    ToggleSwitch rsb1Toggle;

    ToggleSwitch a2Toggle;
    ToggleSwitch b2Toggle;
    ToggleSwitch x2Toggle;
    ToggleSwitch y2Toggle;
    ToggleSwitch lb2Toggle;
    ToggleSwitch rb2Toggle;
    ToggleSwitch lsb2Toggle;
    ToggleSwitch rsb2Toggle;
    
    public XLR8_Ultimate_Goal_Robot(HardwareMap Hardware_Map, Gamepad Gamepad1, Gamepad Gamepad2,Telemetry Telemetry){
        telemetry = Telemetry;
        gamepad1 = Gamepad1;
        gamepad2 = Gamepad2;
        FrontLeft =     Hardware_Map.get(DcMotor.class,"frontLeft");
        FrontRight =    Hardware_Map.get(DcMotor.class,"frontRight");
        BackLeft =      Hardware_Map.get(DcMotor.class,"backLeft");
        BackRight =     Hardware_Map.get(DcMotor.class,"backRight");
        Intake =        Hardware_Map.get(DcMotor.class,"intake");
        Conveyor =      Hardware_Map.get(DcMotor.class,"conveyor");
        LauncherRight = Hardware_Map.get(DcMotorEx.class,"launcherRight");
        LauncherLeft =  Hardware_Map.get(DcMotorEx.class,"launcherLeft");
    
        Splitter =      Hardware_Map.crservo.get("splitter");
        GrabberLift =   Hardware_Map.crservo.get("bottomServo");
        
        // S0 = Hardware_Map.servo.get("wobbleUp");
        Hook = Hardware_Map.servo.get("hook");
        Stopper = Hardware_Map.servo.get("stopper");
        Grabber = Hardware_Map.servo.get("grabber");
        
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
        launcher = new Launcher(LauncherLeft, LauncherRight,Stopper,Splitter);
        grabber = new Grabber(Grabber,.5,.8);
        hook = new Grabber(Hook,0,.52);
        grabberLift = new ServoLift(GrabberLift);
        intake = new Intake(Intake);
        conveyor = new Conveyor(Conveyor);
        
        // launcher.runWithVelocity();
        
        su.addSystem(driveTrain);
        su.addSystem(odom);
        su.addSystem(launcher);
        su.addSystem(grabberLift);
        su.addSystem(intake);
        su.addSystem(conveyor);
        
        odom.setPos(-700,2200,0);
        
        a1Toggle = new ToggleSwitch(gamepad1);//construct controller 1 toggle switches
        b1Toggle = new ToggleSwitch(gamepad1);
        x1Toggle = new ToggleSwitch(gamepad1);
        y1Toggle = new ToggleSwitch(gamepad1);
        lb1Toggle = new ToggleSwitch(gamepad1);
        rb1Toggle = new ToggleSwitch(gamepad1);
        lsb1Toggle = new ToggleSwitch(gamepad1);
        rsb1Toggle = new ToggleSwitch(gamepad1);
    
        a2Toggle = new ToggleSwitch(gamepad2);//construct controller 2 toggle switches
        b2Toggle = new ToggleSwitch(gamepad2);
        x2Toggle = new ToggleSwitch(gamepad2);
        y2Toggle = new ToggleSwitch(gamepad2);
        lb2Toggle = new ToggleSwitch(gamepad2);
        rb2Toggle = new ToggleSwitch(gamepad2);
        lsb2Toggle = new ToggleSwitch(gamepad2);
        rsb2Toggle = new ToggleSwitch(gamepad2);
        
        tu.addToggle(a1Toggle);
        tu.addToggle(b1Toggle);
        tu.addToggle(x1Toggle);
        tu.addToggle(y1Toggle);
        tu.addToggle(lb1Toggle);
        tu.addToggle(rb1Toggle);
        tu.addToggle(lsb1Toggle);
        tu.addToggle(rsb1Toggle);
        tu.addToggle(a2Toggle);
        tu.addToggle(b2Toggle);
        tu.addToggle(x2Toggle);
        tu.addToggle(y2Toggle);
        tu.addToggle(lb2Toggle);
        tu.addToggle(rb2Toggle);
        tu.addToggle(lsb2Toggle);
        tu.addToggle(rsb2Toggle);
        
        for(int i=0;i<NumberOfSaves;i++){
            posSaves[i] = odom.getPos();
            posSavesPrec[i] = 15;
        }
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
    public void resetEncoder(DcMotor c){
        c.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        c.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    
    public void runGrabberLift(){
        grabberLift.setInput(gamepad2.left_stick_y);
    }
    public void runIntake(){
        intake.setInput(gamepad2.left_trigger-gamepad2.right_trigger);
    }
    public void runConveyor(){
        if(!gamepad2.left_bumper){//switch between controlling intake + conveyor and just conveyor
            conveyor.setInput(gamepad2.left_trigger-gamepad2.right_trigger);
        }
        else{
            conveyor.setInput(.27);
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
        odom.setPrecision(posSavesPrec[saveSlot]);
        odom.setTarget(new PositionVar(odom.getPos().getX(),odom.getPos().getY(),odom.pointedAt(posSaves[saveSlot])));
    }
    public void runLauncher(){
        if(gamepad2.b){
            launcher.startLaunching();
        }
        else{
            launcher.endLaunching();
            launcher.stope();
        }
    }
    
    public void TelemPosition(){
        telemetry.addLine("Current");
        telemetry.addLine("X: "+odom.getPos().getX());
        telemetry.addLine("Y: "+odom.getPos().getY());
        telemetry.addLine("R: "+odom.getPos().getR());
        telemetry.addLine();
    }
    public void TelemDriveState(){
        if(!driveTrain.getDisabled()){
            telemetry.addLine("Drive On");
        }
        else{
            telemetry.addLine("Drive Off");
        }
        if(!odom.getDisabled()){
            telemetry.addLine("Odom On");
        }
        else{
            telemetry.addLine("Odom Off");
        }
        telemetry.addLine("Is Stopped: "+Boolean.toString(odom.isStopped()));
        telemetry.addLine("At Target: "+Boolean.toString(odom.atTarget()));
        telemetry.addLine("Prec Count: "+odom.getPrecisionCounter());
        telemetry.addLine("Move Mode: "+OdomMode);
        telemetry.addLine();
    }
    public void TelemLauncher(){
        telemetry.addLine("Mode: " + launcher.getMode());
        switch(launcher.getMode()){
            case "Run By Velocity":
                telemetry.addLine("Target Velocity: "+launcher.getLauncherVelocity());
                telemetry.addLine("Current Velocit: y"+launcher.getRPM());
            break;
            default:
                telemetry.addLine("Launcher Speed: "+launcher.getLauncherSpeed());
        }
        telemetry.addLine();
    }
    public void TelemSaves(){
        telemetry.addLine("SaveSlot: "+saveSlot);
        telemetry.addLine("X: "+posSaves[saveSlot].getX());
        telemetry.addLine("Y: "+posSaves[saveSlot].getY());
        telemetry.addLine("R: "+posSaves[saveSlot].getR());
        telemetry.addLine();
    }
    public void TelemInputs(){
        telemetry.addLine("x1: "+x1presses);
        telemetry.addLine("b1: "+b1presses);
        telemetry.addLine("y1: "+y1presses);
        telemetry.addLine("lb1: "+lb1presses);
        telemetry.addLine("rb1: "+rb1presses);
        telemetry.addLine("a2: "+a2presses);
        
        telemetry.addLine("");
        
        if(gamepad1.x){
            telemetry.addLine("x pressed");
        }
        if(gamepad1.b){
            telemetry.addLine("b pressed");
        }
        if(gamepad1.left_bumper){
            telemetry.addLine("lb pressed");
        }
        if(gamepad1.right_bumper){
            telemetry.addLine("rb pressed");
        }
        
        telemetry.addLine();
        
        telemetry.addLine("LSX: "+gamepad1.left_stick_x);
        telemetry.addLine("LSY: "+gamepad1.left_stick_y);
        telemetry.addLine("RSX: "+gamepad1.right_stick_x);
        
        telemetry.addLine();
    }
    public void TelemPowers(){
        telemetry.addLine("FL: "+FrontLeft.getPower());
        telemetry.addLine("FR: "+FrontRight.getPower());
        telemetry.addLine("Bl: "+BackLeft.getPower());
        telemetry.addLine("BR: "+BackRight.getPower());
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
            OdomMode = "Moving";
            autoMove();
            b1presses++;
        }
    }
    int y1presses=0;
    public void onY1Press(){
        if(y1Toggle.onOnce()){
            y1presses++;
            OdomMode = "Aiming";
            autoAim();
        }
    }
    int rb1presses=0;
    public void onRB1Press(){
        if(rb1Toggle.onOnce()){
            saveSlot++;
            if(saveSlot>=NumberOfSaves){
                rb1presses++;
                saveSlot = 0;
            }
        }
    }
    int lb1presses=0;
    public void onLB1Press(){
        if(lb1Toggle.onOnce()){
            lb1presses++;
            saveSlot--;
            if(saveSlot<0){
                saveSlot = NumberOfSaves-1;
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
            switch(launcher.getMode()){
                case "Run By Velocity":
                    launcher.runWithoutVelocity();
                break;
                case "Run By Power":
                    launcher.runWithVelocity();
                break;
                default:
            }
        }
    }
    int x2Presses=0;
    public void onX2Press(){
        if(x2Toggle.onOnce()){
            x2Presses++;
            switch(launcher.getMode()){
                case "Run By Velocity"://max speed 65 min speed 10
                    // if(launcher.getLauncherVelocity()>10){
                        launcher.setLauncherVelocity(launcher.getLauncherVelocity()-1);
                    // }
                break;
                default:
                    launcher.setLauncherSpeed(launcher.getLauncherSpeed()-.02);
            }
        }
    }
    int rb2Presses=0;
    public void onRb2Press(){
        if(rb2Toggle.onOnce()){
            rb2Presses++;
            switch(launcher.getMode()){
                case "Run By Velocity":
                    // if(launcher.getLauncherVelocity()<65){
                        launcher.setLauncherVelocity(launcher.getLauncherVelocity()+1);
                    // }
                break;
                default:
                    launcher.setLauncherSpeed(launcher.getLauncherSpeed()+.02);
            }
        }
    }
}