package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.MovementAlgorithms.MecanumDistanceDrive;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.MoveSequence;

//@Autonomous(group = "#CompBlue")
public class BlueStorageToWarehouse extends BaseAuto {
    public void initializeMovements() {
        initBlueStorageCam();

       // line up with carousel
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(730)
            .setRightward(50)
            .setRotational(880)
        );

        // drive up to and spin carousel
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
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
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(-650)
            .setRightward(-850)
        );

        // line up with tower
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(-700)
            .setRotational(-400)
            .setRightward(-420)
        );

        // drive up to tower and deposit cube
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(-230)
                .setRotational(-50)
                .addPostMoveFunction(() -> {
//                    teLift1.toPosition(0);
//                    teLift2.toPosition(0);

                    upExtension.setPower(0.6);
                    switch(cameraResults){
                        case "LEFT": sleep(720); break;
                        case "CENTER": sleep(750);break;
                        default: sleep(1100); break;
                    }
                    upExtension.setPower(0.03);

                    switch(cameraResults){
                        case "LEFT": dumper.toPosition(2);break;
                        default: dumper.toPosition(1);
                    }
                    sleep(700);
                    dumper.toPosition(0);

                    upExtension.setPower(-0.45);
                    sleep(800);
                    if(cameraResults.equals("RIGHT"))sleep(200);
                    upExtension.setPower(0);

//                    teLift1.toPosition(2);
//                    teLift2.toPosition(2);
                })
        );

        // park
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(1600)
            .setRotational(1200)
        );

        // park
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(3000)
            .addPreMoveFunction(()->{
                while(clock.seconds()<24 && !isStopRequested());
            })
        );

        waitWhileScanning();

        // add drive telemetry to each movement
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
