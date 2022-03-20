package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.HardwareSystems.MecanumDriveTrain;
import org.firstinspires.ftc.teamcode.HardwareSystems.MultiPositionServo;

import org.firstinspires.ftc.teamcode.Events.*;

import java.util.ArrayList;

public abstract class BaseTele extends OpMode {
    ElapsedTime elapsedTime;
    ArrayList<Event> eventList = new ArrayList<>();

    public DcMotor FrontLeft, FrontRight, BackLeft, BackRight;

    DcMotor carouselSpinner, intake, upExtension, inExtension;

    ElapsedTime caroTimer, clock;

    MultiPositionServo teGrabber, teLift1, teLift2;

    MultiPositionServo dumper, intakeFlipper;

    MecanumDriveTrain driveTrain;

    DistanceSensor intakeScanner;

    Event InFullIn,LiftFullIn;

    public void init(){
        FrontLeft =  hardwareMap.dcMotor.get("frontLeft");
        FrontRight = hardwareMap.dcMotor.get("frontRight");
        BackLeft =   hardwareMap.dcMotor.get("backLeft");
        BackRight =  hardwareMap.dcMotor.get("backRight");

        FrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        BackLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        driveTrain = new MecanumDriveTrain(FrontLeft, FrontRight, BackLeft, BackRight,1);

        intake =            hardwareMap.dcMotor.get("intake");
        upExtension =       hardwareMap.dcMotor.get("upExtension");
        inExtension =       hardwareMap.dcMotor.get("inExtension");
        carouselSpinner =   hardwareMap.dcMotor.get("carouselSpinner");

        teGrabber = new MultiPositionServo(hardwareMap.servo.get("teGrabber"), 0, 1);
        teLift1   = new MultiPositionServo(hardwareMap.servo.get("teLift1"), 0.81, 0.5, 0.35);
        teLift2   = new MultiPositionServo(hardwareMap.servo.get("teLift2"), 0.19, 0.5, 0.59);

        intakeFlipper =   new MultiPositionServo(hardwareMap.servo.get("intakeFlipper"), 0.5,0);
        dumper =          new MultiPositionServo(hardwareMap.servo.get("dumper"), 1, 0.35, 0.25);

        carouselSpinner.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        inExtension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        caroTimer = new ElapsedTime();
        clock = new ElapsedTime();

        dumper.toPosition(0);
        intakeFlipper.toPosition(0);

        teGrabber.scaleRange(0,1);

        teGrabber.toPosition(1);
        teLift1.toPosition(2);
        teLift2.toPosition(2);

        intakeScanner = hardwareMap.get(DistanceSensor.class, "intakescanner");
        elapsedTime = new ElapsedTime();


        Init();
    }
    public void loop(){
        for (Event event : eventList) {
            event.test();
        }

        Loop();
    }
    public boolean hasCube(){
        return intakeScanner.getDistance(DistanceUnit.MM)<45;
    }
    public double scaledInput(double input, double multiplier){
	    return input * input * multiplier * Math.signum(input);
    }
    public void Loop(){}
    public void Init(){}
}
