package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.BlankMovement;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.MecanumDistanceDrive;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.MoveCycle;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.Movement;

@Autonomous(group = "#CompRed")
public class RedWarehouse extends BaseAuto{
    int cubeCount = 0;
    ElapsedTime droptime = new ElapsedTime();
    ElapsedTime raisetime = new ElapsedTime();
    MoveCycle scoreCycle = new MoveCycle(this);
    MoveCycle grabCycle = new MoveCycle(this);

    @Override
    public void initializeMovements() {
        initRedCam();

        //move up to shipping hub
        // 0
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(480)
                .setRightward(-450)
                .setSpeed(0.4)
        );


        //turn to and score in shipping hub
        // 1
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
                        .setRotational(-1300)
                        .setTolerance(12)
                        .setSpeed(0.4)
                        .addPreMoveFunction(()->{
                            upExtension.setPower(0.58);
                            raisetime.reset();
                        })
                        .createEvent(()->{
                            switch(cameraResults) {
                                case "CENTER": return raisetime.milliseconds()>680;
                                case "LEFT" : return raisetime.milliseconds()>420;
                                default: return raisetime.milliseconds()>1560;
                            }
                        },()->upExtension.setPower(0.03))
                        .addPostMoveFunction(()->{
                            teLift1.toPosition(0);
                            teLift2.toPosition(0);
                            sleep(100);

                            dumper.toPosition(1);
                            if(cameraResults=="RIGHT")dumper.setPosition(0.37);
                            sleep(700);
                            dumper.toPosition(0);

                            upExtension.setPower(-0.4);
                            droptime.reset();

                            teLift1.toPosition(2);
                            teLift2.toPosition(2);
                        })
        );

//        interrupt();

        //move into barrier gap
        // 2
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(1120)
                .setRightward(-215)
                .setRotational(-503)
                .setSpeed(0.3)
                .toggleEndWithUntriggeredEvents()
                .addPreMoveFunction(()->droptime.reset())
                .createEvent(()->{
                    switch (cameraResults){
                        case "LEFT": return droptime.milliseconds()>880;
                        case "CENTER": return droptime.milliseconds()>1100;
                        case "RIGHT": return droptime.milliseconds()>1500;
                        default: return true;
                    }
                },()-> upExtension.setPower(0))
        );

//        interrupt();

        grabCycle.add(new MecanumDistanceDrive(driveTrain)
                .setForward(700)
                .createEvent(()->{
                    switch (cameraResults){
                        case "LEFT": return droptime.milliseconds()>880;
                        case "CENTER": return droptime.milliseconds()>1100;
                        case "RIGHT": return droptime.milliseconds()>1500;
                        default: return true;
                    }
                },()-> upExtension.setPower(0))
                .addPreMoveFunction(() -> {
                    intakeFlipper.toPosition(1);
                    intake.toPower(2);

                    inExtension.setPower(0.7);
                    sleep(700);
                    inExtension.setPower(0);
                })
                .addPostMoveFunction(() -> {
                    sleep(500);

                    inExtension.setPower(-0.7);
                    sleep(700);
                    inExtension.setPower(0);
                })
        );
        grabCycle.add(new MecanumDistanceDrive(driveTrain)
                .setForward(-200)
                .setRightward(30)
                .addMoveFunction(()->{
                    if(hasCube())cubeCount++;
                    telemetry.addLine(""+hasCube());
                    telemetry.update();
                })
        );

        // move to freight and attempt pickup
        scoreCycle.add(new BlankMovement()
                .addPostMoveFunction(()-> {
                    int i;
                        for(i=0;i<1;i++)grabCycle.executeSequence();
//                    }
                    new MecanumDistanceDrive(driveTrain).setRightward(i*50).execute(this);
                })
        );

        // exit warehouse
        scoreCycle.add(new MecanumDistanceDrive(driveTrain)
                .setForward(-700)
                .setSpeed(0.4)
                .addPreMoveFunction(()->
                        intakeFlipper.toPosition(0)
                )
                .toggleEndWithUntriggeredEvents()
                .createTimedEvent(800,()->
                        intake.toPower(1)
                )
        );

//        interrupt();

        // if hasCube attempt to score
        scoreCycle.add(new MecanumDistanceDrive(driveTrain)
                .setForward(-870)
                .setRotational(512)
                .setRightward(-60)
                .createTimedEvent(300,()->
                        intake.toPower(1)
                )
                .addPostMoveFunction(()->{
                    upExtension.setPower(0.58);
                    intake.toPower(0);
                    sleep(1580);
                    upExtension.setPower(0.03);

                    dumper.toPosition(1);
                    sleep(200);
                })
        );

        // move back to warehouse entrance
        scoreCycle.add(new MecanumDistanceDrive(driveTrain)
                .setForward(900)
                .setRotational(-500)
                .setRightward(-150)
                .setTolerance(50)
                .createTimedEvent(700,()-> {
                    dumper.toPosition(0);
                    upExtension.setPower(-0.4);
                })
                .createTimedEvent(2200,()->{
                    upExtension.setPower(0);
                    intake.toPower(0);
                })
        );
        scoreCycle.add(new MecanumDistanceDrive(driveTrain)
                .setForward(285)
                .setRightward(60)
        );

//        interrupt();
//        MoveSequence.jumpToHere();
        MoveSequence.add(new BlankMovement().addPostMoveFunction(()->scoreCycle.executeSequence()));
        MoveSequence.add(new BlankMovement().addPostMoveFunction(()->scoreCycle.executeSequence()));
//        interrupt();

        // park
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(700)
                .setRightward(100)
                .setSpeed(0.6)
                .setTolerance(50)
        );
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setRightward(-700)
                .setSpeed(0.6)
                .setTolerance(50)
        );

        // global telemetry
        MoveSequence.addWhileMoveToEach(()->{
            telemetry.addLine("Current Movement: "+moveCount +" / "+(MoveSequence.size()-1));
            telemetry.addLine();
            telemetry.addLine(cameraResults);
            telemetry.addLine("Dist: "+intakeScanner.getDistance(DistanceUnit.MM));
            telemetry.addLine("Has Cube: "+(cubeCount>100));

            telemetry.update();
        });

        // 8
//        interrupt();

        waitWhileScanning();
    }
}