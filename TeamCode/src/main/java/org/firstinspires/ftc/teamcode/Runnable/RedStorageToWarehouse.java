package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.MovementAlgorithms.MecanumDistanceDrive;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.MoveSequence;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.Movement;

@Autonomous(group = "#CompRed")
public class RedStorageToWarehouse extends BaseAuto{

    public void initializeMovements() {
        initRedCam();

        // line up with carousel
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(720)
            .setRightward(-50)
            .setRotational(-900));

        // drive up to and spin carousel
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(163)
            .setSpeed(0.1)
            .addPostMoveFunction(() -> {
                carouselSpinner.setPower(-0.6);
                sleep(6000);
                carouselSpinner.setPower(0);
            })
        );

        // drive around barcode
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(-750)
            .setRightward(850)
        );

        // drive up and deposit cube
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(-800)
            .setRotational(420)
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

        // line up park
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(1500)
            .setRotational(-1200)
        );

        // park
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(3000)
                .addPreMoveFunction(()->sleep(0))
        );

//        moveSequence.add(new MecanumDistanceDrive(driveTrain).setForward(20).setSpeed(0.2));


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

        waitWhileScanning();
    }
}
