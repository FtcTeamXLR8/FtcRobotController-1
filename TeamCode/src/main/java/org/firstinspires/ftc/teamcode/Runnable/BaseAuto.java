package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.HardwareSystems.*;
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
        executeSequence();
        waitForEnd();
    }

    ArrayList<Movement> MoveSequence = new ArrayList<>();

    public DcMotor FrontLeft, FrontRight, BackLeft, BackRight;

    DcMotor carouselSpinner, upExtension, inExtension;
    MultiPowerMotor intake;

    MultiPositionServo dumper, intakeFlipper;

    MecanumDriveTrain driveTrain;

    String cameraResults = "";

    OpenCvWebcam webcam;
    DeterminationClass pipeline;

    MultiPositionServo teGrabber, teLift1, teLift2;



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

        upExtension =       hardwareMap.dcMotor.get("inExtension");
        inExtension =       hardwareMap.dcMotor.get("upExtension");
        carouselSpinner =   hardwareMap.dcMotor.get("carouselSpinner");

        intakeFlipper = new MultiPositionServo(hardwareMap.get(Servo.class, "intakeFlipper"), 0,.5);
        dumper =        new MultiPositionServo(hardwareMap.get(Servo.class, "dumper"), 1,0.35,0.25,0.22);

        carouselSpinner.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        inExtension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        upExtension.setDirection(DcMotorSimple.Direction.REVERSE);


        teGrabber = new MultiPositionServo(hardwareMap.servo.get("teGrabber"), 0, 1);
        teLift1   = new MultiPositionServo(hardwareMap.servo.get("teLift1"), 0.85,0.6, .36);
        teLift2   = new MultiPositionServo(hardwareMap.servo.get("teLift2"), 0.15,0.4,0.6);


        dumper.toPosition(0);
        intakeFlipper.toPosition(0);
    }

    public void initRedStorageCam(){
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        pipeline = new RedStorageScanner();
        webcam.setPipeline(pipeline);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
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
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {}
        });
    }

    public void executeSequence(){
        for(Movement movement : MoveSequence) {

            // if interrupted wait for robot to stop then end program
            if(movement.getClass().equals(Interrupt.class)){
                driveTrain.stope();

                ElapsedTime waitForEnd = new ElapsedTime();
                while(!isStopRequested() && waitForEnd.seconds() < 1.3);

                return;
            }

            // execute all movements
            while(!isStopRequested() && !movement.execute());
        }
    }

    public void interrupt(){
        MoveSequence.add(new Interrupt());
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
        while(!isStopRequested()&&opModeIsActive());
    }

    public void RunWithOnlyMovements(){
        for(Movement movement : MoveSequence){
            movement.removeAllPostMoveFunctions();
            movement.removeAllPreMoveFunctions();
            movement.removeAllMoveFunctions();
            movement.addPostMoveFunction(()->driveTrain.stope());
        }
    }

    public String scan(){
        return pipeline.getAnalysis().name();
    }
}