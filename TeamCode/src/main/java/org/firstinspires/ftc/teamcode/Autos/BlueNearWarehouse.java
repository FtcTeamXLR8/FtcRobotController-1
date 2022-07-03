package org.firstinspires.ftc.teamcode.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Events.Event;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.MecanumDistanceDrive;
import org.firstinspires.ftc.teamcode.Runnable.BaseAuto;

@Autonomous(group = "#CompBlue",name = "BlueNearWarehouse")
public class BlueNearWarehouse extends BaseAuto {
    @Override
    public void initializeMovements() {
        // line up with carousel
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(60)
            .setRightward(-770)
            .setRotational(-270)
//          .addPreMoveFunction(()->cameraResults="LEFT")
        );

        // drive up to and spin carousel
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(40)
            .setSpeed(0.1)
            .addPostMoveFunction(()->{
                carouselSpinner.setPower(0.45);
                sleep(6000);
                carouselSpinner.setPower(0);
            })
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

        initBlueStorageCam();
        waitWhileScanning();
    }
}
