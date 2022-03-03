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
                            sleep(980);  break;
                        case "LEFT" :
                            sleep( 680); break;
                        default:
                            sleep(1360); break;
                    }
                    upExtension.setPower(0.03);

                    dumper.toPosition(1);
                    if(cameraResults=="RIGHT")dumper.setPosition(0.4);
                    sleep(700);
                    dumper.toPosition(0);

                    upExtension.setPower(-0.4);
                    switch(cameraResults){
                        case "CENTER":sleep(960); break;
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


                    Movement move;
                    move = (new MecanumDistanceDrive(driveTrain)
                            .setForward(-200)
                            .setRightward(-70)
                    );
                    move.execute();

                    MoveCycle moves = new MoveCycle(this);

                    moves.add(new MecanumDistanceDrive(driveTrain)
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
                    moves.add(new MecanumDistanceDrive(driveTrain).setForward(-200));

                    while(!hasCube()&&clock.seconds()<20) {
                        telemetry.addLine("Grabbing");
                        telemetry.update();

                        moves.executeSequence();
                    }
                })
        );

        // if not hasCube park and end auto
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setRightward(800)
            .setCondition(()->!hasCube())
            .addPostMoveFunction(()->{if(!hasCube())waitForEnd();})
        );

        AtomicReference<Double> starttime = new AtomicReference<>(0d);
        // if hasCube exit warehouse
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(-700)
            .setSpeed(0.45)
            .addPreMoveFunction(()->{
                intakeFlipper.toPosition(0);
                starttime.set(clock.milliseconds());
            })
            .addMoveFunction(()->{
                if(clock.milliseconds()>starttime.get()+300)intake.toPower(1);
            })
            .addPostMoveFunction(()->{
                intake.toPower(0);
            })
        );

        // if hasCube attempt to score
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(-980)
            .setRotational(-600)
            .setRightward(-100)
        );

        // global telemetry
        MoveSequence.addWhileMoveToEach(()->{
            telemetry.addLine(cameraResults);
            telemetry.addLine(""+intakeScanner.getDistance(DistanceUnit.MM));
            telemetry.update();
        });

        waitWhileScanning();
//        RunWithOnlyMovements();
    }
}
