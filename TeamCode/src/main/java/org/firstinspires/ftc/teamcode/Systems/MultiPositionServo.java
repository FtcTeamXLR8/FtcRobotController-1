package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;

public class MultiPositionServo{

    private int currentPos, cpos = 0;
    boolean lastInput=false;

    Servo servo;

    private ArrayList<Double> positions = new ArrayList<Double>();

    public void input(boolean input){
        // default input intended for gamepad buttons
        if(input==lastInput)return;
        if(input) toPosition();
        lastInput=input;
    }

    public MultiPositionServo(Servo servo, double... Pos){
        //construct with target servo and list of positions
        this.servo=servo;
        addPosition(Pos);
    }
    public double getPos(){
        return positions.get(cpos);
    }
    public ArrayList<Double> getPositions(){
        return positions;
    }
    public void addPosition(double... Pos){
        for (double pos : Pos)positions.add(pos);
    }
    public void toPosition(){
        //move through positions in order of initialization
        cpos=currentPos;
        servo.setPosition(positions.get(currentPos));
        currentPos++;
        if(currentPos==positions.size())currentPos=0;
    }
    public void toPosition(int value){
        //move to a position in sequence
        if(value<0||value>=positions.size())return;
        currentPos=value;
        servo.setPosition(positions.get(currentPos));
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
