package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.MovementAlgorithms.MoveCycle;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.MoveSequence;
import org.firstinspires.ftc.teamcode.MovementAlgorithms.Movement;

import java.util.ArrayList;

@Autonomous(group = "#")
@Disabled
public class AutoExplanation extends BaseAuto{
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
        //   Tolerance = 80

        // call interrupt(); to add an end to the auto into moveSequence

        /* call Movement.addPreMove, Movement.addMove, or Movement.addPostMove to add events that
         *    occur before, during, or after the movement respectively
         *
         *  note: Movement.addMove will be called on a loop while the robot drives while the other
         *    two get called only once
         */




        // create movements below //




        // end movements //




        // add an interrupt at end of moveSequence
        interrupt();
        waitWhileScanning();
//        waitForStart();



        // add telemetry listing currentmovement / totalmovements
        MoveCycle withoutInterrupts = new MoveCycle(this);
        for(Movement move : moveSequence){
            withoutInterrupts.add(move);
        }

        moveSequence.addWhileMoveToEach(()->{
            telemetry.addLine("Camera: "+cameraResults);
            telemetry.update();
        });
    }
}
