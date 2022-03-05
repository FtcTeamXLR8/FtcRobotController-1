package org.firstinspires.ftc.teamcode.MovementAlgorithms;

import org.firstinspires.ftc.teamcode.HardwareSystems.MecanumDriveTrain;

public class MecanumDistanceDrive extends Movement {

    MecanumDriveTrain driveTrain;
    int For = 0, Right = 0, Rotat = 0, Tol = 30;
    double Spd = 0.51;

    public void init(){
        driveTrain.resetEncoders();

        driveTrain.setSpeedScalar(Spd);
        driveTrain.setTargetDists(For,Right,Rotat);
    }

    public MecanumDistanceDrive(MecanumDriveTrain drivetrain){
        driveTrain=drivetrain;
        addPostMoveFunction(() -> driveTrain.stope());
    }

    public MecanumDistanceDrive removeStop(){
        if(postMoveFunctionList.size()==0)return this;

        postMoveFunctionList.remove(0);

        return this;
    }

    public MecanumDistanceDrive setForward(int aFor) {
        For = aFor;
        return this;
    }
    public int getForward(){
        return For;
    }

    public MecanumDistanceDrive setRightward(int aRight) {
        Right = aRight;
        return this;
    }
    public int getRightward(){
        return Right;
    }

    public MecanumDistanceDrive setRotational(int aRotat) {
        Rotat = aRotat;
        return this;
    }
    public int getRotational(){
        return Rotat;
    }

    public MecanumDistanceDrive setSpeed(double aSpd) {
        Spd = aSpd;
        return this;
    }
    public double getSpeed(){
        return Spd;
    }

    public MecanumDistanceDrive setTolerance(int aTol) {
        Tol = aTol;
        return this;
    }
    public int getTolerance(){
        return Tol;
    }

    public boolean moveMethod() {
        driveTrain.setSpeedByTargetDists();
        return driveTrain.checkIfAtTarget(Tol);
    }
}
