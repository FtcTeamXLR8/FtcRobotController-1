package org.firstinspires.ftc.teamcode.Competition;

import org.firstinspires.ftc.teamcode.Competition.AutoFunction;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
@Disabled
public class AutoBasedTele extends AutoFunction{
    @Override
    public void runOpMode(){
        defaultStartup();
        configCompDrive();
        scalar = 1.4;
        setPos(-700,2200,0);
        waitForStart();
        while(opModeIsActive()){
            TelemSaves();
            hereTwo();
            checkToggles();
            
            if(gamepad1.x){
                saves[saveSlot][0]=x1;
                saves[saveSlot][1]=y1;
                saves[saveSlot][2]=r1;
            }
            if(b1Toggle){
                moveTo(saves[saveSlot][0],saves[saveSlot][1],saves[saveSlot][2],20);
            }
            else{
                driveTrainTwo();
                interrupt=false;
            }
            intaveyor();
            grabberLift();
            launcher();
            splitter();
            wobbleUp();
        }
    }
}
