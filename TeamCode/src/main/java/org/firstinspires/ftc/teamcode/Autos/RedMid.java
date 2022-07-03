package org.firstinspires.ftc.teamcode.Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Events.Event;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.MecanumDistanceDrive;
import org.firstinspires.ftc.teamcode.Runnable.BaseAuto;

@Autonomous(group = "#CompRed", name = "RedMid")
//@Disabled
public class RedMid extends BaseAuto {
    @Override
    public void initializeMovements() {
        // drive to and score in shipping hub
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(-550)
                .setRightward(-300)
                .setRotational(130)
                .addPreMoveFunction(()->upExtension.setPower(-0.7))
                .createEvent(()->{
                    switch (cameraResults){
                        case "LEFT": return upExtension.getCurrentPosition()<-884;
                        case "CENTER": return upExtension.getCurrentPosition()<-770;
                        case "RIGHT": return upExtension.getCurrentPosition()<-1188;
                        default: return true;
                    }
                },()->upExtension.setPower(-0.03))
                .addPostMoveFunction(()->{
                    teLiftL.toPosition(1);
                    teLiftR.toPosition(1);
                    sleep(99);
                    switch (cameraResults){
                        case "LEFT": dumper.toPosition(2); break;
                        default:  dumper.toPosition(1);
                    }
                    sleep(700);
                    teLiftL.toPosition(0);
                    teLiftR.toPosition(0);
                    dumper.toPosition(0);
                })
        );

//        interrupt();

        // drive to and score in shipping hub
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(460)
                .setRightward(400)
                .setRotational(-670)
                .addPreMoveFunction(()->upExtension.setPower(0.6))
                .addEvent(new Event(()->upExtension.setPower(0),()-> upExtension.getCurrentPosition()>-70).dontForceCompletion())
        );

        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(2000)
            .addPreMoveFunction(()->sleep(0))
            .addEvent(new Event(()->upExtension.setPower(0),()-> upExtension.getCurrentPosition()>-70).dontForceCompletion())
        );



        initBlueStorageCam();
        waitWhileScanning();
    }
}
