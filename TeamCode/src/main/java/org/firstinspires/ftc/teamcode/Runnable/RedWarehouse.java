package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.MovementAlgorithms.MecanumDistanceDrive;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.Movement;

@Autonomous(group = "#Comp")
public class RedWarehouse extends BaseAuto{
    public void initializeMovements() {
        initBlueStorageCam();
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(300)
                .setRightward(-550)
        );
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setRotational(-1300)
                .addPostMoveFunction(()->{
                    teLift1.toPosition(0);
                    teLift2.toPosition(0);
                    sleep(400);

                    upExtension.setPower(0.58);
                    switch(cameraResults) {
                        case "CENTER":
                            sleep(880);  break;
                        case "LEFT" :
                            sleep( 650); break;
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
        for(Movement movement : MoveSequence)movement.addMoveFunction(()->{
            telemetry.addLine(cameraResults);
            telemetry.update();
        });
        waitWhileScanning();
    }
}
