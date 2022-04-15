package org.firstinspires.ftc.teamcode.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Events.Event;
import org.firstinspires.ftc.teamcode.Events.MotorPositionEvent;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.MecanumDistanceDrive;
import org.firstinspires.ftc.teamcode.Runnable.BaseAuto;

@Autonomous(group = "#")
public class RedStorageNew extends BaseAuto {
    Integer targetLiftPosition = null;
    @Override
    public void initializeMovements() {
        // line up with carousel
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(-70)
            .setRightward(600)
            .setRotational(250)
//            .addPreMoveFunction(()->cameraResults="CENTER")
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
            .setForward(-1020)
            .setRotational(485)
            .setRightward(260)
            .addPreMoveFunction(()->upExtension.setPower(-0.7))
            .createEvent(
                    ()->{
                        switch (cameraResults){
                            case "LEFT": return upExtension.getCurrentPosition()<-405;
                            case "CENTER": return upExtension.getCurrentPosition()<-740;
                            case "RIGHT": return upExtension.getCurrentPosition()<-1188;
                            default: return true;
                        }
                    },()->upExtension.setPower(-0.03))
            .addPostMoveFunction(()->{
                teLift.toPosition();
                teLift.toPosition();
                sleep(99);
                dumper.toPosition(1);
                sleep(700);
                teLift.toPosition();
                dumper.toPosition(0);
            })
        );

//        interrupt();

        // park
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(1500)
            .setRotational(-980)
            .setRightward(260)
            .addPreMoveFunction(()->upExtension.setPower(0.6))
            .addEvent(new Event(()->upExtension.setPower(0),()-> upExtension.getCurrentPosition()>-70).dontForceCompletion())
        );

//        interrupt();

        // park cont.
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setRightward(-90)
            .setForward(-60)
            .setRotational(-380)
//            .addPreMoveFunction(()->upExtension.setPower(0.6))
            .addEvent(new Event(()->upExtension.setPower(0),()-> upExtension.getCurrentPosition()>-100))
        );

        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(30)
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



        initRedCam();
        waitWhileScanning();
    }
}
