package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;

public class MultiPositionServo{

    boolean lastInput=false;
    int cpos = -1;

    Servo servo;

    private ArrayList<Double> positions = new ArrayList<Double>();

    public MultiPositionServo(Servo servo, double... Pos){
        //construct with target servo and list of positions
        this.servo=servo;
        addPosition(Pos);
    }

    public void input(boolean input){
        // default input intended for gamepad buttons
        if(input==lastInput)return;
        if(input) onInput();
        lastInput=input;
    }

    public void onInput(){
        cpos++;
        if(cpos==positions.size()){
            cpos=0;
        }
        toPosition(cpos);
    }

    public void toPosition(int value){
        //move to a position in sequence
        if(value<0||value>=positions.size())return;
        cpos=value;
        servo.setPosition(positions.get(value));
    }
    public ArrayList<Double> getPositions(){
        return positions;
    }
    public void addPosition(double... Pos){
        for (double pos : Pos)positions.add(pos);
    }



    public void setPosition(double value){
        servo.setPosition(value);
    }
    public double getPosition(){
        return servo.getPosition();
    }
    public void scaleRange(double v1, double v2){
        servo.scaleRange(v1, v2);
    }
}
