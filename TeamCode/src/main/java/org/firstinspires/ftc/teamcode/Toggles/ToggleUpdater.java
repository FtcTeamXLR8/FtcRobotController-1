package org.firstinspires.ftc.teamcode.Toggles;

import java.util.LinkedList;

public class ToggleUpdater {
    LinkedList<ToggleSwitch> switches = new LinkedList<ToggleSwitch>();

    public void addToggle(ToggleSwitch switche){
        this.switches.add(switche);
    }
    public void removeToggle(ToggleSwitch switche){
        this.switches.remove(switche);
    }
    public void update(){
        for(int i=0; i<switches.size(); i++) {
                this.switches.get(i).onPress();
        }
    }
}