package org.firstinspires.ftc.teamcode.Autos;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Events.Event;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.MecanumDistanceDrive;
import org.firstinspires.ftc.teamcode.Runnable.BaseAuto;

@Autonomous(group = "#CompBlue")
//@Disabled
public class BlueWarehouseNew extends BaseAuto {
    @Override
    public void initializeMovements() {
        // drive and score towards shipping hub
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(-550)
                .setRightward(-300)
                .setRotational(130)
                .addPreMoveFunction(()->upExtension.setPower(-0.7))
                .createEvent(()->{
                            switch (cameraResults){
                                case "LEFT": return upExtension.getCurrentPosition()<-395;
                                case "CENTER": return upExtension.getCurrentPosition()<-740;
                                case "RIGHT": return upExtension.getCurrentPosition()<-1188;
                                default: return true;
                            }
                        },()->upExtension.setPower(-0.03))
                .addPostMoveFunction(()->{
                    teLift.toPosition(1);
                    sleep(99);
                    dumper.toPosition(1);
                    sleep(700);
                    teLift.toPosition(0);
                    dumper.toPosition(0);
                })
        // make sure to retract dumper

        );

        // move to warehouse; prep for cycling
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(900)
                .setRotational(420)
                .setRightward(-140)
                .createTimedEvent(400,()->intakeFlipper.toPosition(0))
                .addPostMoveFunction(()->upExtension.setPower(0.6))
                .addEvent(new Event(()->upExtension.setPower(0),()-> upExtension.getCurrentPosition()>-70).dontForceCompletion())
        );

        // Do grabby thing
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(900)
                .addPreMoveFunction(()->{
                    intake.setPower(-0.7);
                    inExtension.setPower(0.4);
                })
                .addPostMoveFunction(()->intakeFlipper.toPosition(1))
                .addEvent(new Event(()->upExtension.setPower(0),()-> upExtension.getCurrentPosition()>-100).dontForceCompletion())
        );

        // Come back out of warehouse
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(-1200)
//                .setRotational(-420)
                .setRightward(-70)
                .addPreMoveFunction(()->inExtension.setPower(-0.7))
                .addEvent(new Event(()->inExtension.setPower(0),()-> inExtension.getCurrentPosition()<40).dontForceCompletion())
        );
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setRotational(-440)
                .setRightward(30)
                .addPreMoveFunction(()-> intake.setPower(1))
                .addEvent(new Event(()->inExtension.setPower(0),()-> inExtension.getCurrentPosition()<80))
        );

        // Drive to shipping hub and deposit cube
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(-400)
                .addPreMoveFunction(()->upExtension.setPower(0.6))
                        .addPreMoveFunction(()-> {
                            upExtension.setPower(-0.7);
                            intake.setPower(0);
                        })
                        .createEvent(
                                ()-> upExtension.getCurrentPosition()<-1188,()->upExtension.setPower(-0.03))
                        .addPostMoveFunction(()->{
                            teLift.toPosition(1);
                            sleep(99);
                            dumper.toPosition(1);
                            sleep(700);
                            teLift.toPosition(0);
                            dumper.toPosition(0);
                        })
                // make sure to retract dumper
        );

        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(700)
                .setRotational(390)
                .setRightward(-270)
                .addPreMoveFunction(()->upExtension.setPower(0.6))
                .addEvent(new Event(()->upExtension.setPower(0),()-> upExtension.getCurrentPosition()>-70).dontForceCompletion())
        );

        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(700)
            .addEvent(new Event(()->upExtension.setPower(0),()-> upExtension.getCurrentPosition()>-70).dontForceCompletion())
        );

        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setRightward(700)
            .addEvent(new Event(()->upExtension.setPower(0),()-> upExtension.getCurrentPosition()>-100))
            .addPostMoveFunction(()->teLift.toPosition(0))
        );

        // Score in shipping hub


        initBlueStorageCam();
        waitWhileScanning();

    }
}