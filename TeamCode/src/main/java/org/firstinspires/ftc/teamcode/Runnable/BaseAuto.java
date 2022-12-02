package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Movement.*;
import org.firstinspires.ftc.teamcode.Systems.MecanumDriveTrain;
import org.firstinspires.ftc.teamcode.Systems.SignalScanner;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

public abstract class BaseAuto extends LinearOpMode {
    public abstract void initializeMovements();

    public void runOpMode(){
        initSystems();
        initializeMovements();
        waitForStart();
        gametime.reset();
        moveSequence.executeSequence();
        waitForEnd();
    }

    ElapsedTime gametime = new ElapsedTime();

    public String cameraResults = "";
    MoveSequence moveSequence = new MoveSequence(this);

    //Declare hardware variables {{{

    MecanumDriveTrain driveTrain;

//    SignalScanner pipeline;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;
    OpenCvWebcam webcam;
    
    
    //}}}


    public void initSystems() {
        // Initalize hardware variables {{{
        //    ex: FrontLeft = hardwaremap.dcmotor.get("frontleft");

        driveTrain = new MecanumDriveTrain(
                hardwareMap.dcMotor.get("frontLeft"),
                hardwareMap.dcMotor.get("frontRight"),
                hardwareMap.dcMotor.get("backLeft"),
                hardwareMap.dcMotor.get("backRight"),
                1
        );
    }

    public void initCam(){
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        pipeline = new CameraTestV2();
        webcam.setPipeline(pipeline);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener(){
            @Override
            public void onOpened()
            {
                // Original Values: 320,240 for 1080p Logitech Webcan
                webcam.startStreaming(800, 448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {}
        });
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
                case "THREE": counts[0]++; break;
                case "TWO": counts[1]++; break;
                default: counts[2]++;
            }
        }
        if(counts[0]>Math.max(counts[1],counts[2]))cameraResults="THREE";
        else if(counts[1]>counts[2])cameraResults="TWO";
        else cameraResults="ONE";
    }


    public String scan(){
        return pipeline.getAnalysis().name();
    }

    public void interrupt(){
        moveSequence.interrupt();
    }

    public void waitForEnd(){
        while(!isStopRequested()&&opModeIsActive());
    }
}
