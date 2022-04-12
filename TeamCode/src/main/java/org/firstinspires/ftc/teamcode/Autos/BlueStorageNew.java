package org.firstinspires.ftc.teamcode.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Events.Event;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.MecanumDistanceDrive;
import org.firstinspires.ftc.teamcode.Runnable.BaseAuto;

@Autonomous(group = "#")
public class BlueStorageNew extends BaseAuto {
    @Override
    public void initializeMovements() {
        // line up with carousel
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(60)
                .setRightward(-770)
                .setRotational(-270)
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

        // drive around barcode
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
//                .setRightward(-10)
                .setRightward(-400)
                .setForward(-594)
        );

        // move up and deposit cube
        // check if this lines up properly
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(-1100)
                .setRotational(-580)
                .setRightward(-650)
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

        // park
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(1100)
                .setRotational(580)
                .setRightward(650)
                .addPreMoveFunction(()->upExtension.setPower(0.6))
                .createEvent(()-> upExtension.getCurrentPosition()>-70,()->upExtension.setPower(0))
        );

        // park cont
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setRightward(-100)
                .setForward(125)
                .setRotational(210)
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
