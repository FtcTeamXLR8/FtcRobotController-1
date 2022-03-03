package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.MecanumDistanceDrive;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.MoveCycle;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.Movement;

import java.util.concurrent.atomic.AtomicReference;

@Autonomous(group = "#Comp")
public class BlueWarehouse extends BaseAuto{
    @Override
    public void initializeMovements() {
        initBlueStorageCam();

        //move up to shiiping hub
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(480)
                .setRightward(450)
                .setSpeed(0.4)
        );


        //turn to and score in shipping hub
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setRotational(1300)
                .setTolerance(10)
                .setSpeed(0.4)
                .addPostMoveFunction(()->{
                    teLift1.toPosition(0);
                    teLift2.toPosition(0);
                    sleep(400);

                    upExtension.setPower(0.58);
                    switch(cameraResults) {
                        case "CENTER":
                            sleep(880);  break;
                        case "LEFT" :
                            sleep( 680); break;
                        default:
                            sleep(1360); break;
                    }
                    upExtension.setPower(0.03);

                    dumper.toPosition(1);
                    if(cameraResults=="RIGHT")dumper.setPosition(0.37);
                    sleep(700);
                    dumper.toPosition(0);

                    upExtension.setPower(-0.4);
                    switch(cameraResults){
                        case "CENTER":sleep(1100); break;
                        case "LEFT": sleep(880);break;
                        default:sleep(1500);
                    }
                    upExtension.setPower(0);

                    teLift1.toPosition(2);
                    teLift2.toPosition(2);
                })
        );

//        interrupt();

        //move into barrier gap
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(1120)
            .setRightward(215)
            .setRotational(503)
            .setSpeed(0.3)
        );
//        interrupt();

        // move to freight and attempt pickup
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(500)
                .addPreMoveFunction(()->{
                    intakeFlipper.toPosition();
                    intakeFlipper.toPosition();
                    intake.toPower(2);

                    inExtension.setPower(0.7);
                    sleep(700);
                    inExtension.setPower(0);
                })
                .addPostMoveFunction(()-> {
                    sleep(500);

                    inExtension.setPower(-0.7);
                    sleep(700);
                    inExtension.setPower(0);

                    intake.toPower(0);
                    intakeFlipper.toPosition();


                   new MecanumDistanceDrive(driveTrain)
                        .setForward(-200)
                        .setRightward(-70)
                        .execute();

                    MoveCycle grabCycle = new MoveCycle(this);

                    grabCycle.add(new MecanumDistanceDrive(driveTrain)
                            .setForward(200)
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

                                intake.toPower(0);
                            })
                    );
                    grabCycle.add(new MecanumDistanceDrive(driveTrain)
                            .setForward(-200)
                            .setRightward(-30)
                    );

                    while(!hasCube()&&clock.seconds()<3000) {
                        telemetry.addLine("Grabbing");
                        telemetry.update();

                        grabCycle.executeSequence();
                    }
                })
        );

        // if not hasCube park and end auto
        MoveSequence.add( new MecanumDistanceDrive(driveTrain)
            .setForward(200)
            .setRightward(800)
            .setCondition(()->!hasCube())
            .ifEndedByCondition(()->waitForEnd()
        ));

        AtomicReference<Double> starttime = new AtomicReference<>(0d);
        // if hasCube exit warehouse
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(-700)
            .setSpeed(0.4)
            .addPreMoveFunction(()->{
                intakeFlipper.toPosition(0);
                starttime.set(clock.milliseconds());
            })
            .addMoveFunction(()->{
                if(clock.milliseconds()>starttime.get()+600)
                    intake.toPower(1);
            })
        );

//        interrupt();

        // if hasCube attempt to score
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(-870)
            .setRotational(-512)
            .setRightward(60)
            .addPostMoveFunction(()->{
                upExtension.setPower(0.58);
                sleep(1300);
                upExtension.setPower(0.03);

                dumper.toPosition(1);
                sleep(700);
                dumper.toPosition(0);

                upExtension.setPower(-0.4);
                sleep(1360);
                upExtension.setPower(0);

                intake.toPower(0);
            })
        );

        // global telemetry
        MoveSequence.addWhileMoveToEach(()->{
            telemetry.addLine(cameraResults);
            telemetry.addLine(""+intakeScanner.getDistance(DistanceUnit.MM));
            telemetry.update();
        });

        interrupt();

        waitWhileScanning();
    }
}
