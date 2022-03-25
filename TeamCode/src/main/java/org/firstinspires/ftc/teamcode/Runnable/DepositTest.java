package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.MovementAlgorithms.BlankMovement;

@Autonomous(group = "#")
@Disabled
public class DepositTest extends BaseAuto{
    @Override
    public void initializeMovements() {
        moveSequence.add(new BlankMovement()
            .addPostMoveFunction(()->{
//                teLift1.toPosition(0);
//                teLift2.toPosition(0);
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
                    case "LEFT": sleep(400);
                    default:sleep(1200);break;
                }
                upExtension.setPower(0);

//                teLift1.toPosition(2);
//                teLift2.toPosition(2);
            })
        );
    }
}
