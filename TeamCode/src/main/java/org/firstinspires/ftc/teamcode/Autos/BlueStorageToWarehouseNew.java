package org.firstinspires.ftc.teamcode.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.MovementAlgorithms.MecanumDistanceDrive;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.MoveSequence;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.Movement;
import org.firstinspires.ftc.teamcode.Runnable.BaseAuto;

@Autonomous(group = "#")
public class BlueStorageToWarehouseNew extends BaseAuto {
    @Override
    public void initializeMovements() {

        // line up with carousel
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(-70)
                .setRightward(-600)
                .setRotational(-240)
//                .addPreMoveFunction(()->cameraResults="LEFT")
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
                .setRightward(-540)
                .setForward(-594)
        );

        // move up and deposit cube
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(-1020)
                .setRotational(-485)
                .setRightward(-260)
                .addPreMoveFunction(()->upExtension.setPower(-0.7))
                .createEvent(
                        ()->{
                            switch (cameraResults){
                                case "LEFT": return upExtension.getCurrentPosition()<-395;
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

        // drive around barcode
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(2040)
                .setRotational(1300)
                .setRightward(400)

        );

        // drive into warehouse
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setRightward(-450)
                .setForward(2600)
        );

        initRedCam();
        waitWhileScanning();
    }
}
