package org.firstinspires.ftc.teamcode.HardwareSystems;

public class ToggleSwitch extends HardwareSystem{
    boolean lastinput = false;
    boolean out = false;
    public boolean input(boolean in){
        if(in && !lastinput){
            toggle();
            lastinput=in;
            return true;
        }
        lastinput=in;
        return false;
    }
    public ToggleSwitch toggle(){
        out=!out;
        return this;
    }
    public boolean get(){
        return out;
    }
}
