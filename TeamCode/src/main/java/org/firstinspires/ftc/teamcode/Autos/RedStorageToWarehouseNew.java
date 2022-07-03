package org.firstinspires.ftc.teamcode.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Events.Event;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.MecanumDistanceDrive;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.MoveSequence;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.Movement;
import org.firstinspires.ftc.teamcode.Runnable.BaseAuto;

@Autonomous(group = "#CompRed", name = "RedStorageToWarehouse")
@Disabled
public class RedStorageToWarehouseNew extends BaseAuto {
    @Override
    public void initializeMovements() {

        // line up with carousel
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(-70)
                .setRightward(680)
                .setRotational(250)
//                .addPreMoveFunction(()->cameraResults="LEFT")
        );

        // drive up to and spin carousel
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(175)
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
                                case "CENTER": return upExtension.getCurrentPosition()<-840;
                                case "RIGHT": return upExtension.getCurrentPosition()<-1288;
                                default: return true;
                            }
                        },()->upExtension.setPower(-0.03))
                .addPostMoveFunction(()->{
                    teLiftL.toPosition(1);
                    teLiftR.toPosition(1);
                    sleep(99);
                    dumper.toPosition(1);
                    sleep(700);
                    teLiftL.toPosition(0);
                    teLiftR.toPosition(0);
                    dumper.toPosition(0);
                })
        );

        // drive around barcode
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(2040)
                .setRotational(-1300)
                .setRightward(-400)
                .addMoveFunction(()->upExtension.setPower(0.6))
                .addEvent(new Event(()->upExtension.setPower(0),()-> upExtension.getCurrentPosition()>-100).dontForceCompletion())

        );

        // drive into warehouse
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setRightward(450)
                .setForward(2600)
                .addEvent(new Event(()->upExtension.setPower(0),()-> upExtension.getCurrentPosition()>-140))
        );

        initRedCam();
        waitWhileScanning();
    }
}
