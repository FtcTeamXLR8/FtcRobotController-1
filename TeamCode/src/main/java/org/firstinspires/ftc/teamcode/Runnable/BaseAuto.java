package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.HardwareSystems.BlueStorageScanner;
import org.firstinspires.ftc.teamcode.HardwareSystems.DeterminationClass;
import org.firstinspires.ftc.teamcode.HardwareSystems.MecanumDriveTrain;
import org.firstinspires.ftc.teamcode.HardwareSystems.MultiPositionServo;
import org.firstinspires.ftc.teamcode.HardwareSystems.MultiPowerMotor;
import org.firstinspires.ftc.teamcode.HardwareSystems.RedStorageScanner;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.*;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;


import java.util.ArrayList;

public abstract class BaseAuto extends LinearOpMode {
    public abstract void initializeMovements();

    public void runOpMode(){
        initSystems();
        initializeMovements();
        waitForStart();
        moveSequence.executeSequence();
        waitForEnd();
    }

    public DcMotor FrontLeft, FrontRight, BackLeft, BackRight;

    public DcMotor carouselSpinner;
    public DcMotor upExtension;
    public DcMotor inExtension;
    public MultiPowerMotor intake;

    public MultiPositionServo dumper, intakeFlipper;

    public MecanumDriveTrain driveTrain;

    public String cameraResults = "";

    public OpenCvWebcam webcam;
    public DeterminationClass pipeline;

    public MultiPositionServo teGrabber, teLift;

    public DistanceSensor intakeScanner;

    public ElapsedTime caroTimer, clock;

    public MoveSequence moveSequence = new MoveSequence(this);


    public void initSystems(){
        FrontLeft =  hardwareMap.dcMotor.get("frontLeft");
        FrontRight = hardwareMap.dcMotor.get("frontRight");
        BackLeft =   hardwareMap.dcMotor.get("backLeft");
        BackRight =  hardwareMap.dcMotor.get("backRight");

        FrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        BackLeft .setDirection(DcMotorSimple.Direction.REVERSE);

        driveTrain = new MecanumDriveTrain(FrontLeft, FrontRight, BackLeft, BackRight,1);
        driveTrain.setAllRunModes(DcMotor.RunMode.RUN_USING_ENCODER);

        intake =  new MultiPowerMotor(hardwareMap.dcMotor.get("intake"),0,.9,-.7);

        upExtension =       hardwareMap.dcMotor.get("upExtension");
        inExtension =       hardwareMap.dcMotor.get("inExtension");
        carouselSpinner =   hardwareMap.dcMotor.get("carouselSpinner");

        intakeFlipper =   new MultiPositionServo(hardwareMap.servo.get("intakeFlipper"), 0.3,0.6);
        dumper =        new MultiPositionServo(hardwareMap.get(Servo.class, "dumper"), 1,0.35,0.25,0.22);

        carouselSpinner.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        inExtension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

//        upExtension.setDirection(DcMotorSimple.Direction.REVERSE);


        teGrabber = new MultiPositionServo(hardwareMap.servo.get("teGrabber"), 0, 1);
        teLift = new MultiPositionServo(hardwareMap.servo.get("teLift"),0.27,0.58);
//        teLift1   = new MultiPositionServo(hardwareMap.servo.get("teLift1"), 0.85, 0.5, 0.33);
//        teLift2   = new MultiPositionServo(hardwareMap.servo.get("teLift2"), 0.15, 0.5, 0.62);

        intakeScanner = hardwareMap.get(DistanceSensor.class, "intakescanner");

        dumper.toPosition(0);
        intakeFlipper.toPosition(0);
        teLift.toPosition(0);

        clock = new ElapsedTime();
    }

    public void initRedCam(){
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        pipeline = new RedStorageScanner();
        webcam.setPipeline(pipeline);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
              // Original Values: 320,240 for 1080p Logitech Webcan    
              webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {}
        });
    }
    public void initBlueStorageCam(){
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        pipeline = new BlueStorageScanner();
        webcam.setPipeline(pipeline);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
              // Original Values: 320,240 for 1080p Logitech Webcan  
              webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {}
        });
    }

    public void interrupt(){
        moveSequence.interrupt();
    }


    int counter=0;
    public void waitWhileScanning(){
        String[] scanres = new String[10000];
        while(!isStarted()) {
           scanres[counter++ % scanres.length]=scan();
           telemetry.addLine("Count: "+counter);
           telemetry.update();
        }
        int[] counts = {0,0,0};
        for(String scan : scanres){
            switch(scan){
                case "LEFT": counts[0]++; break;
                case "CENTER": counts[1]++; break;
                default: counts[2]++;
            }
        }
        if(counts[0]>Math.max(counts[1],counts[2]))cameraResults="LEFT";
        else if(counts[1]>counts[2])cameraResults="CENTER";
        else cameraResults="RIGHT";
    }
    public void waitForEnd(){
        telemetry.addLine("Waiting for End");
        telemetry.update();
        while(!isStopRequested());
    }


    public String scan(){
        return pipeline.getAnalysis().name();
    }
    public boolean hasCube(){
        if(intakeScanner.getDistance(DistanceUnit.MM)>5000)return true;
        return intakeScanner.getDistance(DistanceUnit.MM)<45;
    }
}
