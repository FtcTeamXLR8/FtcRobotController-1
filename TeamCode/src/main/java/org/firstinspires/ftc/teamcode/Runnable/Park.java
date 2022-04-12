package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.BlankMovement;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.MecanumDistanceDrive;

@Autonomous(group="#Comp")
public class Park extends BaseAuto{
    public void initializeMovements() {
        moveSequence.add(new BlankMovement().addPostMoveFunction(()->{
            while(opModeIsActive()){
                intakeFlipper.toPosition();
                sleep(2000);
                telemetry.addData("Intake Flipper",intakeFlipper.getPos());
                telemetry.addData("Intake Flipper",intakeFlipper.getPosition());
                telemetry.update();
            }
            intakeFlipper.toPosition(0);
        }));
        interrupt();
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(1000)
            .setSpeed(0.3));
    }
}