package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.hardware.DcMotor;

public class TwoPowerServo extends System{

    CRServo servo;
    public double servoPower;

    public TwoPowerMotor(CRServo Servo, double Power){
        servo = Servo;
        servoPower = Power;
    }
    public void update();
    public void input();

    public turnOn(){
        servo.setPower(servoPower);
    }
    public turnOff(){
        servo.setPower(0);
    }

}