package org.firstinspires.ftc.teamcode.Runnable;


import org.firstinspires.ftc.teamcode.MovementAlgorithms.MecanumDistanceDrive;

@Autonomous(group = "#CompRed")
public class RedWarehouse extends BaseAuto{
    @Override
    public void initializeMovements() {
        // move up to shipping hub
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(480)
                // rightward needs tuning –- delete when done
                .setRightward(150)
                .setSpeed(0.4)
        );

        // score in shipping hub




    }
}