package org.firstinspires.ftc.teamcode.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.MovementAlgorithms.MecanumDistanceDrive;
import org.firstinspires.ftc.teamcode.Runnable.BaseAuto;

@Autonomous(group = "#")
public class RedStorageNew extends BaseAuto {
    @Override
    public void initializeMovements() {
        // line up with carousel
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(-70)
            .setRightward(600)
            .setRotational(240)
        );

        // drive up to and spin carousel
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(200)
            .setSpeed(0.1)
            .addPostMoveFunction(()->{
                carouselSpinner.setPower(-0.45);
                sleep(6000);
                carouselSpinner.setPower(0);
            })
        );

        // drive around barcode
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setRightward(540)
            .setForward(-594)
        );

        // move up and deposit cube
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(-1100)
            .setRotational(580)
            .setRightward(650)
        );

        // park
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(1100)
            .setRotational(-580)
            .setRightward(-650)
        );

        // park cont
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setRightward(90)
            .setForward(90)
            .setRotational(-180)
        );

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

    }
}
