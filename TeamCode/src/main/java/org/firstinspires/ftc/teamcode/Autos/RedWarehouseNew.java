package org.firstinspires.ftc.teamcode.Autos;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Events.Event;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.MecanumDistanceDrive;
import org.firstinspires.ftc.teamcode.Runnable.BaseAuto;

@Autonomous(group = "#")
public class RedWarehouseNew extends BaseAuto {
    @Override
    public void initializeMovements() {
        // drive and score towards shipping hub
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(-550)
                .setRightward(300)
                .setRotational(-130)
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

        // move to warehouse; prep for cycling
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(900)
                .setRotational(-420)
                .setRightward(30)
                .addPreMoveFunction(()->upExtension.setPower(0.6))
                .addEvent(new Event(()->upExtension.setPower(0),()-> upExtension.getCurrentPosition()>-70).dontForceCompletion())
        );

        // Do grabby thing
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(900)
                .addEvent(new Event(()->upExtension.setPower(0),()-> upExtension.getCurrentPosition()>-70))
        );

        // Come back out of warehouse
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(-1200)
//                .setRotational(-420)
//                .setRightward(-30)
                .addEvent(new Event(()->upExtension.setPower(0),()-> upExtension.getCurrentPosition()>-70))
        );
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setRotational(440)
                .setRightward(-30)

        );

        // Drive to shipping hub
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(-400)
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

        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(690)
                .setRotational(-370)
                .setRightward(280)
                .addPreMoveFunction(()->upExtension.setPower(0.6))
                .addEvent(new Event(()->upExtension.setPower(0),()-> upExtension.getCurrentPosition()>-70).dontForceCompletion())
        );

        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(700)
                .addEvent(new Event(()->upExtension.setPower(0),()-> upExtension.getCurrentPosition()>-70))
        );

        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setRightward(-700)
                .addEvent(new Event(()->upExtension.setPower(0),()-> upExtension.getCurrentPosition()>-70))
        );

    }
}