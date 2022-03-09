package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.MovementAlgorithms.MecanumDistanceDrive;

@Autonomous(group = "#CompBlue")
public class BlueStorageToWarehouse extends BaseAuto {
    public void initializeMovements() {
        initBlueStorageCam();

       // line up with carousel
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(730)
            .setRightward(50)
            .setRotational(880)
        );

        // drive up to and spin carousel
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(210)
            .setRightward(40)
            .setSpeed(0.1)
            .setTolerance(45)
            .addPostMoveFunction(() -> {
                carouselSpinner.setPower(0.45);
                sleep(6000);
                carouselSpinner.setPower(0);
            })
        );

        // drive around barcode
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(-650)
            .setRightward(-850)
        );

        // line up with tower
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(-700)
            .setRotational(-400)
            .setRightward(-420)
        );

        // drive up to tower and deposit cube
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(-230)
                .setRotational(-50)
                .addPostMoveFunction(() -> {
                    teLift1.toPosition(0);
                    teLift2.toPosition(0);

                    upExtension.setPower(0.6);

                    if(!cameraResults.equals("LEFT") && !cameraResults.equals("CENTER"))sleep(1100);
                    else if(cameraResults.equals("LEFT"))sleep(720);
                    else                      sleep(750);
                    upExtension.setPower(0.03);

                    if(cameraResults.equals("LEFT"))dumper.toPosition(2);
                    else                     dumper.toPosition(1);
                    sleep(700);
                    dumper.toPosition(0);

                    upExtension.setPower(-0.45);
                    sleep(800);
                    if(cameraResults.equals("RIGHT"))sleep(200);
                    upExtension.setPower(0);

                    teLift1.toPosition(2);
                    teLift2.toPosition(2);
                })
        );

        // park
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(1600)
            .setRotational(1200)
        );

        // park
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(3000)
            .addPreMoveFunction(()->sleep(0))
        );

        waitWhileScanning();

        // add drive telemetry to each movement
            MoveSequence.addWhileMoveToEach(() -> {
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
