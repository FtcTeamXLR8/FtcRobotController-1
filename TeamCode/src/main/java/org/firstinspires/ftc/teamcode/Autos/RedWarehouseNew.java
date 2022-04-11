package org.firstinspires.ftc.teamcode.Autos;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.MovementAlgorithms.MecanumDistanceDrive;
import org.firstinspires.ftc.teamcode.Runnable.BaseAuto;

@Autonomous(group = "#")
public class RedWarehouseNew extends BaseAuto {
    @Override
    public void initializeMovements() {
        // drive and score towards shipping hub
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(-550)
                .setRightward(300)
                .setRotational(-130)
        );

        // move to warehouse; prep for cycling
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(900)
                .setRotational(-420)
                .setRightward(30)
        );

        // Do grabby thing
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(900)

        );

        // Come back out of warehouse
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(-1200)
//                .setRotational(-420)
//                .setRightward(-30)
        );
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setRotational(440)
                .setRightward(-30)

        );

        // Drive to shipping hub
        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(-400)

        );

        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(700)
                .setRotational(-390)
                .setRightward(270)
        );

        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setForward(700)
        );

        moveSequence.add(new MecanumDistanceDrive(driveTrain)
                .setRightward(-700)
        );

    }
}