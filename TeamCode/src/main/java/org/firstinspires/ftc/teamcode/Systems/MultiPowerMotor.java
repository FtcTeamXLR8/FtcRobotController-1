package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;

public class MultiPowerMotor {
    private int currentPow, cpow = 0;
    boolean lastInput=false;

    DcMotor motor;

    private ArrayList<Double> powers = new ArrayList<>();

    public void input(boolean input){
        if(input==lastInput)return;
        if(input) toPower();
        lastInput=input;
    }

    public MultiPowerMotor(DcMotor motor, double... Powers){
        this.motor=motor;
        addPower(Powers);
    }
    public double getPow(){
        return powers.get(cpow);
    }
    public String getPowers(){
        String out = "";
        for(double pow : powers)out+=pow + " | ";
        return out;
    }
    public void addPower(double... Powers){
        for (double pow : Powers)powers.add(pow);
    }
    public void toPower(){
        cpow=currentPow;
        motor.setPower(powers.get(currentPow));
        currentPow++;
        if(currentPow==powers.size())currentPow=0;
    }
    public void toPower(int value){
        if(value<0||value>powers.size())return;
        currentPow=value;
        motor.setPower(powers.get(currentPow));
    }
    public void setPower(double value){
        motor.setPower(value);
    }
    public double getPower(){
        return motor.getPower();
    }
}
