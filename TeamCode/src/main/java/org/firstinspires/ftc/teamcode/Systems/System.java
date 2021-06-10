package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.hardware.HardwareMap;

public abstract class System {
    protected boolean disabled = false;
    protected boolean inAuto = false;
    protected HardwareMap hardwareMap;
    

    abstract void update();
    abstract void input();

    public void disable(){
        this.disabled = true;
    }
    public void enable(){
        this.disabled = false;
    }
    public boolean getDisabled(){
        return this.disabled;
    }
    public boolean getEnabled(){
        return !this.disabled;
    }
}
