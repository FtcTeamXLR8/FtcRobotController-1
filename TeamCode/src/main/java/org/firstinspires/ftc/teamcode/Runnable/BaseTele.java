package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.MovementAlgorithms.Movement;
import org.firstinspires.ftc.teamcode.HardwareSystems.MecanumDriveTrain;
import org.firstinspires.ftc.teamcode.HardwareSystems.MultiPositionServo;

import java.util.ArrayList;

public abstract class BaseTele extends OpMode {
    ElapsedTime clock;

    // Declare hardware variables{{{
    
    
    
    
    
    
    // }}}


    public void init(){
        clock = new ElapsedTime();
	// initalize hardware variables{{{
	
	
	
	
	
	
	
	// }}}
    }

    public double scaledInput(double input,double multiplier){
      	// map analog input quadratically
        double positivity = 1;
        if(input<0)positivity*=-1;
        return Math.pow(input,2)*positivity*multiplier;
    }
}
