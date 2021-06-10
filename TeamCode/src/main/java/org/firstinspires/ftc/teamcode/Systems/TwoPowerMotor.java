package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.hardware.DcMotor;

public class TwoPowerMotor extends System{

    DcMotor motor;
    public double motorPower;

    public TwoPowerMotor(DcMotor Motor, double Power){
        motor = Motor;
        motorPower = Power;
    }
    public void update();
    public void input();

    public turnOn(){
        motor.setPower(motorPower);
    }
    public turnOff(){
        motor.setPower(0);
    }

}