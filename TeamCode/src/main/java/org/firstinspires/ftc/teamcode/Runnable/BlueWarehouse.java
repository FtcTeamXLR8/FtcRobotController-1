package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.MovementAlgorithms.MecanumDistanceDrive;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.Movement;

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
                            sleep( 580); break;
                        default:
                            sleep(1320); break;
                    }
                    upExtension.setPower(0.03);

                    dumper.toPosition(1);
                    sleep(700);
                    dumper.toPosition(0);

                    upExtension.setPower(-0.45);
                    switch(cameraResults){
                        case "CENTER":sleep(750); break;
                        case "LEFT": sleep(400);break;
                        default:sleep(1200);
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
            .setRightward(230)
            .setRotational(460)
            .setSpeed(0.3)
        );
        // move to freight and attempt pickup
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(400)
                .addPreMoveFunction(()->{
                    intakeFlipper.toPosition();
                    intakeFlipper.toPosition();
                    intake.toPower(1);

                    inExtension.setPower(0.7);
                    sleep(700);
                    inExtension.setPower(0);
                })

                .addPostMoveFunction(()->{
                    inExtension.setPower(-0.7);
                    sleep(700);
                    inExtension.setPower(0);

                    intake.toPower(0);
                    intakeFlipper.toPosition();
                })
        );

        // global telemetry
        for(Movement movement : MoveSequence)movement.addMoveFunction(()->{
            telemetry.addLine(cameraResults);
            telemetry.update();
        });

        waitWhileScanning();
//        RunWithOnlyMovements();
        cameraResults="LEFT";
    }
}
