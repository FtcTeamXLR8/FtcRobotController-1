package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Events.Event;
import org.firstinspires.ftc.teamcode.Events.TimedEvent;
import org.firstinspires.ftc.teamcode.Movement.MecanumDriveByDistance;
import org.firstinspires.ftc.teamcode.Movement.Movement;

import java.util.ArrayList;

@Autonomous(group = "#")
//@Disabled
public class AutoExample extends BaseAuto{
    @Override
    public void initializeMovements() {


        /*   Example Movement
            moveSequence.add(new HolonomicDistanceDrive(driveTrain)
                .setForward(100)
                .setRightward(-100)
                .setRotational(30)
                .setSpeed(0.2)
                .setTolerance(30)
            );
        */

        // all of the values in the example movement may be removed to apply default value

        // default values:
        //   Forward = 0
        //   Rightward = 0
        //   Rotational = 0
        //   Speed = 0.5
        //   Tolerance = 25 (Default Tolerance may be too low, edit in MecanumDistanceDrive.java)

        // call interrupt(); to add an end to the auto into the MoveSequence

        /* call Movement.addPreMove, Movement.addMove, or Movement.addPostMove to add events that
         *    occur before, during, or after the movement respectively
         *
         *  note: Movement.addMove will be called on a loop while the robot drives while the other
         *    two get called only once
         */

        // call Movement.addEvent to add an event that will be called upon its condtion being fulfilled




        // create movements here //

        moveSequence.add(new MecanumDriveByDistance(driveTrain)
                .setForward(500)
                .addForcedEvent(new TimedEvent(5000,()->{}))

        );

        moveSequence.add(new MecanumDriveByDistance(driveTrain)
                .setRotational(900)
                .addEvent(new TimedEvent(10000,()->{telemetry.addLine("too long");telemetry.update();}))
        );






        // add telemetry listing currentmovement / totalmovements to each movement
        moveSequence.whileExecuting(()->{
            telemetry.addLine("Current Movement: "+ (moveSequence.getCurrentMovementIndex() +"/"+moveSequence.size()));
//            telemetry.update();
        });
    }
}
