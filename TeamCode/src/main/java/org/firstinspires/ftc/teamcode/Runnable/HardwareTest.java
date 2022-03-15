package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.MovementAlgorithms.BlankMovement;

@Autonomous
//@Disabled
public class HardwareTest extends BaseAuto{
    @Override
    public void initializeMovements() {
        moveSequence.add(new BlankMovement().addPostMoveFunction(()->{
//            cameraResults = "LEFT";
            teLift1.toPosition(0);
            teLift2.toPosition(0);
            sleep(400);

            upExtension.setPower(0.58);
            switch(cameraResults) {
                case "CENTER":
                    sleep(1080); break;
                case "LEFT" :
                    sleep( 680); break;
                default:
                    sleep(1560); break;
            }
            upExtension.setPower(0.03);

            dumper.toPosition(1);
            if(cameraResults=="RIGHT")dumper.setPosition(0.37);
            sleep(700);
            dumper.toPosition(0);

            upExtension.setPower(-0.4);
            switch(cameraResults){
                case "CENTER":sleep(1100); break;
                case "LEFT": sleep(880);break;
                default:sleep(1500);
            }
            upExtension.setPower(0);

            teLift1.toPosition(2);
            teLift2.toPosition(2);
        }));
    }
}
