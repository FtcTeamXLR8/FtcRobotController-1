package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.MovementAlgorithms.BlankMovement;

@Autonomous
//@Disabled
public class HardwareTest extends BaseAuto{
    @Override
    public void initializeMovements() {
        MoveSequence.add(new BlankMovement().addPostMoveFunction(()->{
            upExtension.setPower(-0.4);
        }));
    }
}
