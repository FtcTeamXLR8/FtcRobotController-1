package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.BlankMovement;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.MecanumDistanceDrive;

@Autonomous(group="#Comp")
public class Park extends BaseAuto{
    public void initializeMovements() {
        MoveSequence.add(new MecanumDistanceDrive(driveTrain)
            .setForward(1000)
            .setSpeed(0.3));
        while(!opModeIsActive()){
        }
    }
}