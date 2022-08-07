package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import static com.qualcomm.robotcore.hardware.DcMotor.*;


public class MecanumDriveTrain{
    DcMotor FrontLeft, FrontRight, BackLeft, BackRight;
    DcMotor[] Motors;
    public Pid FLPid, FRPid, BLPid, BRPid = null;

    int fld,frd,bld,brd;
    double fls,frs,bls,brs;
    double dists;

    double speedScalar=1;

    public MecanumDriveTrain(DcMotor FL, DcMotor FR, DcMotor BL, DcMotor BR, double Scalar){

        FrontLeft = FL; FrontRight = FR; BackLeft = BL; BackRight = BR;
        Motors = new DcMotor[]{FrontLeft, FrontRight, BackLeft, BackRight};
        FrontRight.setDirection(Direction.REVERSE);
        BackRight.setDirection(Direction.REVERSE);

        for(DcMotor motor : Motors)motor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);

        speedScalar = Scalar;
    }

    public void setSpeed(double Forward, double Rightward, double Rotational){
        //input percentages intended for controllers and PIDs
        //sets speed for all motors in drivetrain

        fls = Forward + Rotational - Rightward;
        frs = Forward - Rotational - Rightward;
        bls = Forward + Rotational + Rightward;
        brs = Forward - Rotational + Rightward;

        FrontLeft.setPower(fls*speedScalar);
        FrontRight.setPower(frs*speedScalar);
        BackLeft.setPower(bls*speedScalar);
        BackRight.setPower(brs*speedScalar);
    }
    public void setSpeedByTargetDists(){
        //set speed relative to remaining distance to drive

        fld = FrontLeft.getTargetPosition()  - FrontLeft.getCurrentPosition();
        frd = FrontRight.getTargetPosition() - FrontRight.getCurrentPosition();
        bld = BackLeft.getTargetPosition()   - BackLeft.getCurrentPosition();
        brd = BackRight.getTargetPosition()  - BackRight.getCurrentPosition();

        dists = (Math.abs(fld) + Math.abs(frd) + Math.abs(bld) + Math.abs(brd));
        fls = fld / dists;
        frs = frd / dists;
        bls = bld / dists;
        brs = brd / dists;

        double scaleToMax = 1/(Math.max(Math.max(Math.abs(fls),Math.abs(frs)),Math.max(Math.abs(bls),Math.abs(brs))));

        fls*=scaleToMax;
        frs*=scaleToMax;
        bls*=scaleToMax;
        brs*=scaleToMax;

        FrontLeft.setPower(fls*speedScalar);
        FrontRight.setPower(frs*speedScalar);
        BackLeft.setPower(bls*speedScalar);
        BackRight.setPower(brs*speedScalar);
    }
    public void setTargetDists(int Forward, int Rightward, int Rotational){
        resetEncoders();

        fld = Forward + Rotational - Rightward;
        frd = Forward - Rotational - Rightward;
        bld = Forward + Rotational + Rightward;
        brd = Forward - Rotational + Rightward;

        FrontLeft.setTargetPosition(fld);
        FrontRight.setTargetPosition(frd);
        BackLeft.setTargetPosition(bld);
        BackRight.setTargetPosition(brd);
    }

    public void stop(){
        //stop all motors
        for(DcMotor motor : Motors)motor.setPower(0);
    }
    public void resetEncoders(){
        //reset all motor encoders
        RunMode prevRunMode = FrontLeft.getMode();
        setAllRunModes(RunMode.STOP_AND_RESET_ENCODER);
        setAllRunModes(prevRunMode);
    }
    public void setAllRunModes(RunMode runMode) {
        //set all run modes to input runmode
        for(DcMotor motor : Motors)motor.setMode(runMode);
    }

    public void setSpeedScalar(double Scalar){ speedScalar = Scalar; }

    public boolean checkIfAtTarget(double tolerance){
        for(DcMotor motor : Motors){
            if(motor.getCurrentPosition() < motor.getTargetPosition()-tolerance
                    || motor.getCurrentPosition() > motor.getTargetPosition()+tolerance)return false;
        }
        return true;
    }

}
