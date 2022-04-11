package org.firstinspires.ftc.teamcode.Autos;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.MovementAlgorithms.MecanumDistanceDrive;
import org.firstinspires.ftc.teamcode.Runnable.BaseAuto;

@Autonomous(group = "#")
public class BlueWarehouseNew extends BaseAuto {
    @Override
    public void initializeMovements() {
        // drive and score towards shipping hub
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(-550)
                .setRightward(-300)
                .setRotational(130)
        );

        // move to warehouse; prep for cycling
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(900)
                .setRotational(420)
                .setRightward(-30)
        );

        // Do grabby thing
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(900)

        );

        // Come back out of warehouse
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                        .setForward(-1200)
//                .setRotational(-420)
//                .setRightward(-30)
        );
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setRotational(-440)
                .setRightward(30)

        );

        // Drive to shipping hub and deposit cube
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(400)
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
        // make sure to retract dumpee

        );

        // Move to warehouse; prep for second cycle (your problem evan)
//        moveSequence.add(new MecanumDistanceDrive(driveTrain)
//                .setForward(650)
//                .setRotational(-200)
//                .setRightward(30)
//
//        );
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(400)

        );
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setRotational(220)
                .setRightward(-60)
                .setForward(200)

        );
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setRightward(-200)
        );



        // Score in shipping hub


        initBlueStorageCam();
        waitWhileScanning();

    }
}