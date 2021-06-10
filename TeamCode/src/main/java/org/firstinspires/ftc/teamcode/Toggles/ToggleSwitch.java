package org.firstinspires.ftc.teamcode.Toggles;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad2;

public class ToggleSwitch {
    protected boolean toggle=false,toggle2 = false;
    protected boolean once=false;
    protected String input;

    void onPress(){
        if(this.input()&&!this.once){
            this.once=true;
            this.toggle=!this.toggle;
        }
        else if(!this.input()){
            this.once=false;
        }
    }
    public boolean onOnce(){
        if(toggle==toggle2){
            return false;
        }
        else{
            toggle2=toggle;
            return true;
        }
    }

    public boolean get(){
        return this.toggle;
    }

    public boolean input(){
        switch(input){
            case "1a":
                return gamepad1.a;
            case "2a":
                return gamepad2.a;
            case "1b":
                return gamepad1.b;
            case "2b":
                return gamepad2.b;
            case "1x":
                return gamepad1.x;
            case "2x":
                return gamepad2.x;
            case "1y":
                return gamepad1.y;
            case "2y":
                return gamepad2.y;
            case "1lb":
                return gamepad1.left_bumper;
            case "2lb":
                return gamepad2.left_bumper;
            case "1rb":
                return gamepad1.right_bumper;
            case "2rb":
                return gamepad2.right_bumper;
            case "1lsb":
                return gamepad1.left_stick_button;
            case "2lsb":
                return gamepad2.left_stick_button;
            case "1rsb":
                return gamepad1.right_stick_button;
            case "2rsb":
                return gamepad2.right_stick_button;
            default:
                return false;
        }
    }
    public void setInput(String c){
        this.input = c;
    }
    public void toggle() {
        this.toggle = !this.toggle;
    }
}
