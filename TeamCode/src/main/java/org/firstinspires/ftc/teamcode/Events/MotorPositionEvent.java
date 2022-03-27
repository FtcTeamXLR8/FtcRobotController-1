package org.firstinspires.ftc.teamcode.Events;

import com.qualcomm.robotcore.hardware.DcMotor;

public class MotorPositionEvent extends Event{
    public MotorPositionEvent(Runnable callback, DcMotor motor, int targetPosition, int MarginOfError,double motorSpeed) {
        super(callback, ()->{
            if(motor.getCurrentPosition()>targetPosition-MarginOfError && motor.getCurrentPosition()<targetPosition+MarginOfError){
                motor.setPower(0);
                return true;
            }
            return false;
        });
        motor.setPower(motorSpeed);
    }
    public MotorPositionEvent(Runnable callback, DcMotor motor, int targetPosition, int MarginOfError) {
        super(callback, ()->{
            if(motor.getCurrentPosition()>targetPosition-MarginOfError && motor.getCurrentPosition()<targetPosition+MarginOfError){
                motor.setPower(0);
                return true;
            }
            return false;
        });
    }
}
