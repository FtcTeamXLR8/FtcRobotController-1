package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

import org.firstinspires.ftc.teamcode.Targets;
import org.firstinspires.ftc.teamcode.Misc.*;
import org.firstinspires.ftc.teamcode.Systems.*;
import org.firstinspires.ftc.teamcode.Toggles.ToggleUpdater;

import java.util.ArrayList;

public abstract class BaseAuto extends LinearOpMode {
    final double Pi = 2*Math.asin(1);
    final double BaseAutoSpeed = 3;
    protected int moveCount=0;
    protected boolean moveCountOnce = false;
    String fieldPos = "";
    SystemUpdater su = new SystemUpdater();
    double recommendedRPM = 0;

    Odometry odom;
    Launcher launcher;
    Grabber grabber,hook;
    ServoLift grabberLift;
    Intake intake;
    Conveyor conveyor;
    NewCamera camera;
    
    
    DcMotor FrontLeft, FrontRight, BackLeft, BackRight;
    DcMotor Intake, Conveyor;
    DcMotorEx LauncherLeft, LauncherRight;
    CRServo Splitter, GrabberLift, TopLift, Srv;//Evan, i made a seccond grabberlift and Srv. servo so we could run the lift but I couldne figure out how to make a new one with your system.
    Servo Hook, Stopper, Grabber;

    ArrayList<PositionVar> moveSequence = new ArrayList<PositionVar>();
    ArrayList<Integer> precisions = new ArrayList<Integer>();
    protected PositionVar lastPos = new PositionVar(0,0,0);

    public void runMoveSequence(){
        if(moveSequence.size()>0&&odom.isStopped()){
            nextMove();
        }
        else if(odom.isStopped()){
            odom.stope();
        }
        else{
            moveCountOnce = true;
        }
        su.update();
    }
    public void runMoveSequence(boolean on){
        if(moveSequence.size()>0&&on){
            nextMove();
        }
        else if(odom.isStopped()){
            odom.stope();
        }
        else{
            moveCountOnce = true;
        }
        su.update();
    }
    public void nextMove(){
        if(moveCountOnce){
            moveCount++;
            moveCountOnce = false;
            lastPos = moveSequence.get(moveCount-1);
        }
        if(moveCount<moveSequence.size()){
            odom.setTarget(moveSequence.get(moveCount));
            odom.setPrecision(precisions.get(moveCount));
        }
        
    }
    public void addCoordinate(double X,double Y,double R,int D){
        addToSequence(new PositionVar(X,Y,R),D);
    }
    public void addCoordinate(PositionVar c, int d){
        addToSequence(c,d);
    }
    public void addToSequence(PositionVar c, int d){
        moveSequence.add(c);
        precisions.add(d);
    }
    public void addMovement(double Forw, double Rightw,int d){
        addMovement(new Movement(Forw,Rightw),d);
    }
    public void addMovement(Movement c, int d){
        PositionVar b = new PositionVar(odom.getPos());
        odom.setPos(moveSequence.get(moveSequence.size()-1));
        addToSequence(new PositionVar(odom.getPositionByDists(c.getF(),c.getS())),d);
        odom.setPos(b);
    }


    public void initSystems(){
        
        
        FrontLeft = hardwareMap.dcMotor.get("frontLeft");
        FrontRight = hardwareMap.dcMotor.get("frontRight");
        BackLeft = hardwareMap.dcMotor.get("backLeft");
        BackRight = hardwareMap.dcMotor.get("backRight");
        Intake = hardwareMap.dcMotor.get("intake");
        Conveyor = hardwareMap.dcMotor.get("conveyor");
        LauncherRight = hardwareMap.get(DcMotorEx.class,"launcherRight");
        LauncherLeft = hardwareMap.get(DcMotorEx.class,"launcherLeft");
        
        Splitter = hardwareMap.crservo.get("splitter");
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
        launcher = new Launcher(LauncherLeft, LauncherRight,Stopper,Splitter, 1700);
        grabber = new Grabber(Grabber,.5,.84);
        hook = new Grabber(Hook,0,.52);
        grabberLift = new ServoLift(GrabberLift);
        intake = new Intake(Intake);
        conveyor = new Conveyor(Conveyor);
        camera = new NewCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        
        camera.initVuforia();
        camera.initTfod(.64f,tfodMonitorViewId,3.,1.8);//(confidence,tfodMonitorViewId,y-zoom,x-zoom)
        // camera.initTfod();
        
        su.addSystem(odom);
        su.addSystem(launcher);
        // su.addSystem(grabber);
        // su.addSystem(grabberLift);
        su.addSystem(intake);
        su.addSystem(conveyor);
        su.addSystem(camera);
        
        odom.enable();
        

        // su.putInAuto();
    }
    public void resetEncoder(DcMotor c){
        c.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        c.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void waitWithCam(){
        while(!opModeIsActive()&&!isStopRequested()) {
            camera.updateCamera();
            fieldPos = "default";
            fieldPos = camera.getFieldPos();
            telemetry.addLine("Detect: "+fieldPos);
            telemetry.addLine("Width: "+camera.width);
            telemetry.update();
        }
        camera.endTfod();

    }
    Targets targets = new Targets();
    public void updateRecommendedRPM(){
        recommendedRPM=1475.16482419*Math.pow(1.0005806474,odom.distance(odom.getPos(),targets.Tower));
    }
    public void updateRecommendedRPMBlue(){
        recommendedRPM=1475.16482419*Math.pow(1.0005806474,odom.distance(odom.getPos(),targets.BlueTower));
    }
    class Movement{
        protected double f,s;
        public Movement(double F,double S){
            this.f = F;
            this.s = S;
        }
        public double getF(){
            return f;
        }
        public double getS(){
            return s;
        }
    }
}
