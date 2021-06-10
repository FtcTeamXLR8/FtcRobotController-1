package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.hardware.DcMotor;


public class HolonomicDriveTrain extends System{
    protected DcMotor FrontLeft;
    protected DcMotor FrontRight;
    protected DcMotor BackLeft;
    protected DcMotor BackRight;
    protected double forwardInput;
    protected double rightwardInput;
    protected double rotationalInput;
    public double speedScalar = 1;
    public double rotationalScalar = 1;

    public HolonomicDriveTrain(DcMotor FL, DcMotor FR, DcMotor BL, DcMotor BR){
        FrontLeft = FL;
        FrontRight = FR;
        BackLeft = BL;
        BackRight = BR;
    }
    
    void input(){}
    
    public void setInput(double XIn, double YIn, double RIn){
        forwardInput = YIn;
        rightwardInput = XIn;
        rotationalInput = RIn;
    } 

    public void update(){
        setSpeed(forwardInput*speedScalar,rightwardInput*speedScalar,rotationalInput*rotationalScalar);
    }

    public void setSpeed(double Forward, double Rightward, double Rotational){
        fl = -Forward + Rotational + Rightward;
        fr = Forward + Rotational + Rightward;
        bl = Forward - Rotational + Rightward;
        br = -Forward - Rotational + Rightward;

        FrontLeft.setPower(fls);
        FrontRight.setPower(frs);
        BackLeft.setPower(bls);
        BackRight.setPower(brs);
    }
    public void setSpeedByDists(double Forward, double Rightward, double Rotational){
        fl = -Forward + Rotational + Rightward;
        fr = Forward + Rotational + Rightward;
        bl = Forward - Rotational + Rightward;
        br = -Forward - Rotational + Rightward;

        dists = (Math.abs(fl) + Math.abs(fr) + Math.abs(bl) + Math.abs(br));
        fls = fl / dists;
        frs = fr / dists;
        bls = bl / dists;
        brs = br / dists;

        FrontLeft.setPower(fls*speedScalar);
        FrontRight.setPower(frs*speedScalar);
        BackLeft.setPower(bls*speedScalar);
        BackRight.setPower(brs*speedScalar);
    }
    public void stope(){
        FrontLeft.setPower(0);
        FrontRight.setPower(0);
        BackLeft.setPower(0);
        BackRight.setPower(0);
    }
}
