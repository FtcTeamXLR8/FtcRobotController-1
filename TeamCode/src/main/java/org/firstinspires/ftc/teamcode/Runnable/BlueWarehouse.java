package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.BlankMovement;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.MecanumDistanceDrive;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.MoveCycle;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.Movement;

import java.util.concurrent.atomic.AtomicReference;

@Autonomous(group = "#Comp")
public class BlueWarehouse extends BaseAuto{
    double starttime = 0;

    @Override
    public void initializeMovements() {
        initBlueStorageCam();

        //move up to shiiping hub
        // 0
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(480)
                .setRightward(450)
                .setSpeed(0.4)
        );


        //turn to and score in shipping hub
        // 1
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setRotational(1300)
                .setTolerance(10)
                .setSpeed(0.4)
                .addPostMoveFunction(()->{
//                    cameraResults = "LEFT";
                    teLift1.toPosition(0);
                    teLift2.toPosition(0);
                    sleep(400);

                    upExtension.setPower(0.58);
                    switch(cameraResults) {
                        case "CENTER":
                            sleep(1080); break;
                        case "LEFT" :
                            sleep( 680); break;
                        default:
                            sleep(1560); break;
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
        // 2
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(1120)
            .setRightward(215)
            .setRotational(503)
            .setSpeed(0.3)
        );

//        interrupt();
        MoveCycle cycle = new MoveCycle(this);

        // move to freight and attempt pickup
        // 3
        cycle.add(new MecanumDistanceDrive(driveTrain)
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
                        .execute(this);

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

                    while(opModeIsActive()) {
                        if(hasCube())break;
                        if(clock.seconds()>23)break;

                        telemetry.addLine("Grabbing");
                        telemetry.update();

                        grabCycle.executeSequence();
                    }
                })
        );

        // start park if not hasCube
        // 4
        cycle.add(new MecanumDistanceDrive(driveTrain)
            .setForward(200)
            .setCondition(()->!hasCube())
        );
        // if not hasCube park and end auto
        // 5
        cycle.add( new MecanumDistanceDrive(driveTrain)
            .setRightward(800)
            .setCondition(()->!hasCube())
            .ifNotEndedByCondition(this::waitForEnd)
        );

        // if hasCube exit warehouse
        // 6
        cycle.add(new MecanumDistanceDrive(driveTrain)
            .setForward(-700)
            .setSpeed(0.4)
            .addPreMoveFunction(()->{
                intakeFlipper.toPosition(0);
                starttime = clock.milliseconds();
            })
            .addMoveFunction(()->{
                if(clock.milliseconds()>starttime + 600)
                    intake.toPower(1);
            })
        );

//        interrupt();

        // if hasCube attempt to score
        // 7
        cycle.add(new MecanumDistanceDrive(driveTrain)
            .setForward(-870)
            .setRotational(-512)
            .setRightward(60)
            .addPostMoveFunction(()->{
                upExtension.setPower(0.58);
                sleep(1580);
                upExtension.setPower(0.03);

                dumper.toPosition(1);
                sleep(700);
                dumper.toPosition(0);

                upExtension.setPower(-0.4);
                sleep(1500);
                upExtension.setPower(0);

                intake.toPower(0);
            })
        );

        // loop cycling MoveCycle
        MoveSequence.add(new BlankMovement().addPostMoveFunction(()->{
            for(int i=0;i<1;i++) cycle.executeSequence();
        }));

        // global telemetry
        MoveSequence.addWhileMoveToEach(()->{
            telemetry.addLine("Current Movement: "+moveCount +" / "+(MoveSequence.size()-1));
            telemetry.addLine();
            telemetry.addLine(cameraResults);
            telemetry.addLine("Dist: "+intakeScanner.getDistance(DistanceUnit.MM));
            telemetry.addLine("Has Cube: "+hasCube());

            telemetry.update();
        });

        // 8
//        interrupt();

        waitWhileScanning();
    }
}
