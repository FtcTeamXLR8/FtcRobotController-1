package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import java.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.lang.annotation.Target;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.Targets;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

import org.firstinspires.ftc.teamcode.Misc.PositionVar;
import org.firstinspires.ftc.teamcode.Toggles.ToggleSwitch;

@Autonomous(name="Blue_PowerShot",group="Comp")
public class Blue_Power extends BaseAuto {
        
    final PositionVar[] targetSaves = {targets.Tower,targets.PowerShot1,targets.PowerShot2,targets.PowerShot3};
    int targetSlot = 0;
        
    int startSize;
    boolean hasShot = false;
    ToggleSwitch B1Toggle = new ToggleSwitch(gamepad1);
    
    public void runOpMode(){ 
        initSystems();
        PositionVar start = new PositionVar(-2867.2271,30,Pi);
        PositionVar done = odom.getPos();
        PositionVar postShoot = new PositionVar(-720,650,.86);
        lastPos = new PositionVar(start);
        
        
        PositionVar shootingPos = new PositionVar(-2964,1640,0);
        PositionVar shootingPosition = new PositionVar(shootingPos.getX(),shootingPos.getY(),odom.pointedAtFrom(shootingPos,targets.BlueTower)+.01);
        
        PositionVar targetPosition1;
        PositionVar targetPosition2;
        Movement wobble2Grab;
        PositionVar wobble2GrabP;
        PositionVar end = new PositionVar(-2800,2200,0); //2200 old
        odom.setPos(start);
        // odom.disable();

        waitWithCam();
        // change    \/ to the string to test individual detections 
        // fieldPos="Quad";
        //
        ElapsedTime gameTime = new ElapsedTime();
        switch(fieldPos){//set setup specific positions
            case "Single":
                targetPosition1 = new PositionVar(-3100,3350,2.36);
                break;
            case "Quad":
                targetPosition1 = new PositionVar(-4100,4100,2.428);
                break;
            default :
                targetPosition1 = new PositionVar(-4100,2400,2.653);
        }
        
        addCoordinate(-3000,2000,2.9,4);

        addToSequence(targetPosition1,22);

        // if(fieldPos!="None"&&fieldPos!="default"){
        //     addToSequence(new PositionVar(-200,700,0.2),2);//move around ring stack
        // }

        // addToSequence(shootingPosition,45);
        
        addToSequence(shootingPosition,45);
        
        addMovement(new Movement(0,200),1);

        addToSequence(end,10);

        startSize = moveSequence.size();
        
        // odom.setPos(start);
        
        
        // FrontLeft.setPower(1);
        

        launcher.runWithoutVelocity();
        launcher.enable();
        //launcher.disable();
        
        nextMove();

        boolean lastB=false;
        while(opModeIsActive()){
            // su.update();
            // runMoveSequence(!(lastB&&gamepad1.b));
            runMoveSequence();
            lastB=gamepad1.b;
            updateRecommendedRPMBlue();
            
            
            telemetry.update();
            telemetry.addLine("Moves Completed: " + moveCount+"/"+moveSequence.size());
            
            telemetry.addLine();
            
            telemetry.addLine("Is Stopped: "+Boolean.toString(odom.isStopped()));
            telemetry.addLine("At Target: "+Boolean.toString(odom.atTarget()));
            telemetry.addLine("Prec Count: "+odom.getPrecisionCounter());
            telemetry.addLine("AddOnce: "+Boolean.toString(moveCountOnce));
            telemetry.addLine("% Speed: "+odom.getScalar()*50+"%");
            
            telemetry.addLine();
            telemetry.addLine("Target");
            telemetry.addLine("X: "+odom.getTarget().getX());
            telemetry.addLine("Y: "+odom.getTarget().getY());
            telemetry.addLine("R: "+odom.getTarget().getR());
            
            telemetry.addLine();
            telemetry.addLine("Current");
            telemetry.addLine("X: "+odom.getPos().getX());
            telemetry.addLine("Y: "+odom.getPos().getY());
            telemetry.addLine("R: "+odom.getPos().getR());
            
            telemetry.addLine();
            telemetry.addLine("LauncherVelocity"+launcher.getRPM());
            telemetry.addLine("Target Velocity"+recommendedRPM);


            if(lastPos==targetPosition1){
                lastPos=done;
                odom.stope();
                hook.grabberOpen();
                sleep(800);
                grabber.grabberOpen();
                sleep(500);
                grabber.grabberOpen();
                hook.grabberClose();
                sleep(200);
            }
            if(lastPos==shootingPosition){
                if(true){
                    // return;
                }
                lastPos=done;
                odom.stope();
                launcher.startLaunching();
                ElapsedTime shootTimer = new ElapsedTime();
                launcher.setLauncherSpeed(.96);
                // launcher.setLauncherVelocity(recommendedRPM);
                launcher.setTargetVelocity(recommendedRPM-8);
                int shotCount = 0;
                boolean shooting = false;
                    while (shootTimer.milliseconds() < 10000 && shotCount<3 &&!isStopRequested()&&opModeIsActive()){
                        launcher.update();
                        conveyor.update();
                        // launcher.setMode("Run By Power");
                        // conveyor.setInput(.6);
                        launcher.adjustSpeed();
                        
/*                            if(shooting=true&&!launcher.atSpeed){
                            shotCount++;
                            shooting=false;
                        }
*/                          
                        if(launcher.atSpeed||shootTimer.milliseconds()>4500){
                            shooting=true;
                            conveyor.setInput(.5);
                        }
                        else{
                            shooting=false;
                            conveyor.setInput(0);
                        }
                        
                        telemetry.addLine("Stability: "+launcher.stability);
                        telemetry.addLine("Shot Count: "+shotCount);
                        telemetry.addLine("LauncherVelocity"+launcher.getRPM());
                        telemetry.addLine("Target Velocity"+recommendedRPM);
                        telemetry.update();
                    }
                // conveyor.update();
                // ElapsedTime shootTimer = new ElapsedTime();
                //sleep(5000);
                conveyor.setInput(0);
                launcher.endLaunching();
                odom.setScalar(1.4);
            }
            
            if(isStopRequested()){
                odom.stope();
                return;
            }
            
            if(moveCount >= moveSequence.size()){
                requestOpModeStop();
            }
        }
        
        
        
    }
}
