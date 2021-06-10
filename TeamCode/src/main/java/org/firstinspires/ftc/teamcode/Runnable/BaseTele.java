package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Misc.Position;
import org.firstinspires.ftc.teamcode.Systems.*;
import org.firstinspires.ftc.teamcode.Toggles.ToggleSwitch;
import org.firstinspires.ftc.teamcode.Toggles.ToggleUpdater;

public abstract class BaseTele extends OpMode {
    SystemUpdater su = new SystemUpdater();
    ToggleUpdater tu = new ToggleUpdater();

    HolonomicDriveTrain driveTrain = new HolonomicDriveTrain();
    Odometry odom = new Odometry();


    ToggleSwitch a1Toggle = new ToggleSwitch();//construct controller 1 toggle switches
    ToggleSwitch b1Toggle = new ToggleSwitch();
    ToggleSwitch x1Toggle = new ToggleSwitch();
    ToggleSwitch y1Toggle = new ToggleSwitch();
    ToggleSwitch lb1Toggle = new ToggleSwitch();
    ToggleSwitch rb1Toggle = new ToggleSwitch();
    ToggleSwitch lsb1Toggle = new ToggleSwitch();
    ToggleSwitch rsb1Toggle = new ToggleSwitch();

    ToggleSwitch a2Toggle = new ToggleSwitch();//construct controller 2 toggle switches
    ToggleSwitch b2Toggle = new ToggleSwitch();
    ToggleSwitch x2Toggle = new ToggleSwitch();
    ToggleSwitch y2Toggle = new ToggleSwitch();
    ToggleSwitch lb2Toggle = new ToggleSwitch();
    ToggleSwitch rb2Toggle = new ToggleSwitch();
    ToggleSwitch lsb2Toggle = new ToggleSwitch();
    ToggleSwitch rsb2Toggle = new ToggleSwitch();

    final int NumberOfSaves = 3;
    Position[] posSaves=new Position[NumberOfSaves];
    int saveSlot = 0;

    public void initToggles() {

        {
            tu.addToggle(a1Toggle);
            a1Toggle.setInput("1a");
            tu.addToggle(b1Toggle);
            b1Toggle.setInput("1b");
            tu.addToggle(x1Toggle);
            x1Toggle.setInput("1x");
            tu.addToggle(y1Toggle);
            y1Toggle.setInput("1y");
            tu.addToggle(lb1Toggle);
            lb1Toggle.setInput("1lb");
            tu.addToggle(rb1Toggle);
            rb1Toggle.setInput("1rb");
            tu.addToggle(lsb1Toggle);
            lsb1Toggle.setInput("1lsb");
            tu.addToggle(rsb1Toggle);
            rsb1Toggle.setInput("1rsb");
        }//init controller 1 toggle switches
        {
            tu.addToggle(a2Toggle);
            a2Toggle.setInput("2a");
            tu.addToggle(b2Toggle);
            b2Toggle.setInput("2b");
            tu.addToggle(x2Toggle);
            x2Toggle.setInput("2x");
            tu.addToggle(y2Toggle);
            y2Toggle.setInput("2y");
            tu.addToggle(lb2Toggle);
            lb2Toggle.setInput("2lb");
            tu.addToggle(rb2Toggle);
            rb2Toggle.setInput("2rb");
            tu.addToggle(lsb2Toggle);
            lsb2Toggle.setInput("2lsb");
            tu.addToggle(rsb2Toggle);
            rsb2Toggle.setInput("2rsb");
        }//init controller 2 toggle switches
    }
    public void initSystems(){
        su.addSystem(driveTrain);
        su.addSystem(odom);
        odom.setSystemUpdater(su);
        odom.setLeftOdometer("frontLeft");
        odom.setRightOdometer("backLeft");
        odom.setperpOdometer("frontRight");
    }
    public void overWriteSave(boolean In){
        if(In){
            posSaves[saveSlot] = odom.getPos();
        }
    }
    public void loadSave(boolean In){
        if(In){
            odom.setTarget(posSaves[saveSlot]);
        }
    }
    public void cycleSaveRight(boolean In){
        if(In){
            saveSlot++;
            if(saveSlot==NumberOfSaves){
                saveSlot = 0;
            }
        }
    }
    public void cycleSaveLeft(boolean In){
        if(In){
            saveSlot++;
            if(saveSlot==-1){
                saveSlot = NumberOfSaves-1;
            }
        }
    }
    public void autoMove(){
        if(b1Toggle.get()){
            odom.disable();
            driveTrain.enable();
        }
        else{
            odom.enable();
            driveTrain.disable();
            if(odom.isStopped()){
                b1Toggle.toggle();
            }
        }
    }
}
