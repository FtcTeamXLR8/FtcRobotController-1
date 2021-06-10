package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.LinkedList;

public class SystemUpdater {
    LinkedList<System> systems = new LinkedList<System>();
    protected boolean inAuto = false;
    protected HardwareMap hardwareMap;

    public void addSystem(System system){
        this.systems.add(system);
    }
    public void removeSystem(System system){
        this.systems.remove(system);
    }
    public void putInAuto(){
        for(int i=0; i<systems.size(); i++) {
            this.inAuto = true;
        }
    }
    public void update(){
        for(int i=0; i<systems.size(); i++) {
            System tempSystem = this.systems.get(i);
            if(!inAuto||tempSystem.getClass()==Odometry.class) {
                tempSystem.input();
            }

            if(!tempSystem.getDisabled()) {
                tempSystem.update();
            }
        }
    }
    
}
