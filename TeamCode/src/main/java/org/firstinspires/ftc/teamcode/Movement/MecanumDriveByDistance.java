package org.firstinspires.ftc.teamcode.Movement;

import org.firstinspires.ftc.teamcode.HardwareSystems.MecanumDriveTrain;

public class MecanumDriveByDistance extends Movement {

    MecanumDriveTrain driveTrain;
    int For = 0, Right = 0, Rotat = 0, Tol = 25;
    double Spd = 0.5;

    public void init(){
        driveTrain.resetEncoders();

        driveTrain.setSpeedScalar(Spd);
        driveTrain.setTargetDists(For,Right,Rotat);
    }

    public MecanumDriveByDistance(MecanumDriveTrain drivetrain){
        driveTrain=drivetrain;
        addPostMoveFunction(() -> driveTrain.stop());
    }

    public MecanumDriveByDistance removeStop(){
        if(postMoveFunctionList.size()==0)return this;

        postMoveFunctionList.remove(0);

        return this;
    }

    public MecanumDriveByDistance setForward(int aFor) {
        For = aFor;
        return this;
    }
    public int getForward(){
        return For;
    }

    public MecanumDriveByDistance setRightward(int aRight) {
        Right = aRight;
        return this;
    }
    public int getRightward(){
        return Right;
    }

    public MecanumDriveByDistance setRotational(int aRotat) {
        Rotat = aRotat;
        return this;
    }
    public int getRotational(){
        return Rotat;
    }

    public MecanumDriveByDistance setSpeed(double aSpd) {
        Spd = aSpd;
        return this;
    }
    public double getSpeed(){
        return Spd;
    }

    public MecanumDriveByDistance setTolerance(int aTol) {
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
