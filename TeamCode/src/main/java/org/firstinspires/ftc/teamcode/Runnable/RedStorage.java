package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.MovementAlgorithms.*;

@Autonomous(group = "#Comp")
public class RedStorage extends BaseAuto{

    public void initializeMovements() {
        initRedStorageCam();

        // line up with carousel
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(720)
            .setRightward(-50)
            .setRotational(-900));

        // drive up to and spin carousel
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(190)
            .setSpeed(0.1)
            .setTolerance(45)
            .addPostMoveFunction(() -> {
                carouselSpinner.setPower(-0.6);
                sleep(6000);
                carouselSpinner.setPower(0);
            })
        );

        // drive around barcode
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(-650)
            .setRightward(850)
        );

        // drive up and deposit cube
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(-800)
            .setRotational(320)
            .setRightward(-65)
            .setTolerance(40)
            .addPostMoveFunction(() -> {
                teLift1.toPosition(0);
                teLift2.toPosition(0);
                sleep(400);

                upExtension.setPower(0.58);
                switch(cameraResults) {
                    case "CENTER":
                        sleep(880);  break;
                    case "LEFT" :
                        sleep( 820); break;
                    default:
                        sleep(1320); break;
                }
                upExtension.setPower(0.03);

                if(cameraResults.equals("LEFT")) dumper.toPosition(2);
                else dumper.toPosition(1);
                sleep(700);
                dumper.toPosition(0);

                upExtension.setPower(-0.45);
                sleep(800);
                if(cameraResults.equals("RIGHT"))sleep(400);
                upExtension.setPower(0);

                teLift1.toPosition(2);
                teLift2.toPosition(2);
            })
        );

        // park
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(1125)
            .setRotational(-610)
            .setRightward(220)
        );

        // add global telemetry to each movement
        for(Movement movement : MoveSequence) {
            movement.addMoveFunction(() -> {
                telemetry.addLine("Movement: " + (MoveSequence.indexOf(movement) + 1) + "/" + MoveSequence.size());
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

        waitWhileScanning();
    }
}
