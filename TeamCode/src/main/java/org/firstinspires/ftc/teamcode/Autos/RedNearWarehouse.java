package org.firstinspires.ftc.teamcode.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Events.Event;
import org.firstinspires.ftc.teamcode.Events.MotorPositionEvent;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.MecanumDistanceDrive;
import org.firstinspires.ftc.teamcode.Runnable.BaseAuto;

@Autonomous(group = "#CompRed", name = "RedNearWarehouse")
public class RedNearWarehouse extends BaseAuto {
    Integer targetLiftPosition = null;
    @Override
    public void initializeMovements() {

        // add global telemetry to each movement
        moveSequence.addWhileMoveToEach(() -> {
            telemetry.addLine("Scan Results: " + cameraResults);
            telemetry.addLine();

            telemetry.addLine("FL: " + FrontLeft.getCurrentPosition() + " | " + FrontLeft.getTargetPosition());
            telemetry.addLine("FR: " + FrontRight.getCurrentPosition() + " | " + FrontRight.getTargetPosition());
            telemetry.addLine("BL: " + BackLeft.getCurrentPosition() + " | " + BackLeft.getTargetPosition());
            telemetry.addLine("BR: " + BackRight.getCurrentPosition() + " | " + BackRight.getTargetPosition());
            telemetry.addLine();

            telemetry.update();
        });

        initRedCam();
        waitWhileScanning();

        // move to shared
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(-1200)
            .setRotational(-70)
            .addPreMoveFunction(()->upExtension.setPower(-0.7))
            .createEvent(()-> upExtension.getCurrentPosition() < -545,()->upExtension.setPower(-0.03))
            .addPostMoveFunction(()->{
//                teLiftL.toPosition(1);
//                teLiftR.toPosition(1);
                sleep(99);
                dumper.toPosition(1);
                sleep(700);
                teLiftL.toPosition(0);
                teLiftR.toPosition(0);
                dumper.toPosition(0);
            })
        );

        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setSpeed(0.45)
                .setForward(1200)
                .setRotational(70)
                .addPostMoveFunction(()->upExtension.setPower(0.6))
                .addEvent(new Event(()->upExtension.setPower(0),()-> upExtension.getCurrentPosition()>-70).dontForceCompletion())
        );

        // line up with old starting spot for carousel
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setRightward(560)
                .addEvent(new Event(()->upExtension.setPower(0),()-> upExtension.getCurrentPosition()>-55).dontForceCompletion())
        );

        // line up with carousel
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(-130)
            .setRightward(950)
            .setRotational(250)
        );



        // drive up to and spin carousel
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setRightward(-110)
            .setForward(380)
            .setRotational(10)
            .setSpeed(0.1)
            .addPostMoveFunction(()->{
                carouselSpinner.setPower(-0.45);
                sleep(6000);
                carouselSpinner.setPower(0);
            })
        );

        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(-110)
            .setRotational(830)
        );

        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(750)
            .setRightward(-70)
            .setRotational(70)
        );


    }
}
