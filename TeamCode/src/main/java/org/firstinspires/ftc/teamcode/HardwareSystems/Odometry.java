package org.firstinspires.ftc.teamcode.HardwareSystems;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Misc.Pose;

import java.util.ArrayList;


public class Odometry extends HardwareSystem {
    final double DfCoR = 1, BaseDist = (13+7/16)*25.4, Pi = Math.PI, ToMM = 58*Pi/1500;
    //DfCoR - Distance from Center of Rotation

    public DcMotor leftOdometer;
    public DcMotor rightOdometer;
    public DcMotor perpindicularOdometer;

    Double RemainingDist;

    Pid pathingPid = new Pid(.001,.1,0,.6);

    double ld,rd,sd;
    double forw = 0,rightw = 0, rotat = 0;

    protected boolean opModeIsActive;

    public Pose initialPos = new Pose(0,0,0);
    Pose currentPosition = new Pose(0,0,0);
    Pose targetPosition = new Pose(0,0,0);

    public Odometry(DcMotor leftOdom, DcMotor sideOdom, DcMotor rightOdom){
        leftOdometer = leftOdom;
        perpindicularOdometer = sideOdom;
        rightOdometer = rightOdom;
    }

    public Pose getNewPosition(){
        ld = -ToMM*leftOdometer.getCurrentPosition();
        rd = ToMM*rightOdometer.getCurrentPosition();
        sd = -ToMM*perpindicularOdometer.getCurrentPosition();

        double a = (ld-rd)/BaseDist; //change in orientation
        double SD = sd+DfCoR*a; //correct sideways movement for rotation
        double rc = (ld+rd)/(2*a); //find radius of movement arc

        double xp = rc * Math.cos(-initialPos.getR()) + initialPos.getX(); //pivot point x
        double yp = rc * Math.sin(-initialPos.getR()) + initialPos.getY(); //pivot point y

        double rf = a+initialPos.getR(); //current position orientation
        double xf = xp - (rc - SD) * Math.cos(rf); //current position x
        double yf = yp + (rc - SD) * Math.sin(rf); //current position y

        currentPosition.setPos(xf,yf,rf);
        return currentPosition;
    }
    public Pose getLastPosition(){
        return currentPosition;
    }

    public boolean atTarget(int MarginOfPositionalError, double MarginOfRotationalError){
        //return if atTarget within margin of error

        double x1 = currentPosition.getX();
        double y1 = currentPosition.getY();
        double r1 = currentPosition.getR();

        double x2 = targetPosition.getX();
        double y2 = targetPosition.getY();
        double r2 = targetPosition.getR();

        if(MOE(x1,x2, MarginOfPositionalError)&&MOE(y1,y2, MarginOfPositionalError)&&
            (MOE(r1,r2, MarginOfRotationalError)||MOE(r1,r2+(2*Pi), MarginOfRotationalError)||MOE(r1,r2-(2*Pi), MarginOfRotationalError))){
            return true;
        }
        else{
            return false;
        }
    }

    public Pose getPositionByDists(double f, double s){
        double r1 = currentPosition.getR();
        double x1 = currentPosition.getX();
        double y1 = currentPosition.getY();
        double x2=f*Math.sin(r1)+s*Math.sin(Pi/2+r1)+x1;//solve for change in x relative to field
        double y2=f*Math.cos(r1)+s*Math.cos(Pi/2+r1)+y1;//solve for change in y relative to field
        return new Pose(x2,y2,r1);
    }

    public Pose getPos(){
        return currentPosition;
    }
    public void setPos(Pose c){
        currentPosition.setPos(c);
    }
    public void setPos(double X1, double Y1, double R1){
        currentPosition.setX(X1);
        currentPosition.setY(Y1);
        currentPosition.setR(R1);
    }

    public Pose getTarget(){
        return targetPosition;
    }
    public void setTarget(Pose c){
        targetPosition.setPos(c);
    }
    public void setTarget(double X1, double Y1, double R1){
        targetPosition.setX(X1);
        targetPosition.setY(Y1);
        targetPosition.setR(R1);
    }

    public double pointedAt(double x2, double y2){
        return pointedAt(new Pose(x2,y2,currentPosition.getR()));
    }
    public double pointedAt(Pose Facing){
        return pointedAtFrom(currentPosition,Facing);
    }
    public double pointedAtFrom(Pose From, Pose Facing){
        //return angle at which you would face a specific point
        double m = -(Facing.getY()-From.getY())/(Facing.getX()-From.getX());
        double r2=Math.atan(m)+3*Pi/2;
        if(Facing.getX()>From.getX()){
            r2+=Pi;
        }
        if(r2>Pi){
            r2-=2*Pi;
        }
        return r2;
    }

    public void there(){
        double x1 = currentPosition.getX();
        double y1 = currentPosition.getY();
        double r1 = currentPosition.getR();

        double x2 = targetPosition.getX();
        double y2 = targetPosition.getY();
        double r2 = targetPosition.getR();

        boolean check = false;
        if(Math.abs(r2-r1)>Math.abs(r2-2*Pi-r1)){
            r2-=2*Pi;
            check=true;
        }
        if(!check && Math.abs(r2-r1) > Math.abs(r2 + 2*Pi - r1)){
            r2+=2*Pi;
        }
        if(r1==0 || r1==Pi || r1==-Pi || r1==Pi/2 || r1==-Pi/2){
            r1+=0.0000001;
        }

        double t1 = Math.tan(-r1);
        double t2 = Math.tan(-r1 - Pi / 2);
        double xr = (-t1 * x1 + y1 - y2 + t2 * x2) / (t2 - t1);//create reference point
        double yr = (t2 * (t1 * (x2 - x1) + y1) - t1 * y2) / (t2 - t1);

        int fp = -1;
        int rp = -1;
        if (t1 * (x2 - x1) + y1 < y2) {
            fp *= -1;
        }
        if (!MOE(r1, 0, this.Pi / 2)) {
            fp *= -1;
        }
        if (t2 * (x1 - x2) + y2 < y1) {
            rp *= -1;
        }
        if (r1 < 0) {
            rp *= -1;
        }

        RemainingDist =  Math.sqrt(forw+rightw);

        forw *= fp;
        rightw *= rp;


        forw = distance(x2, xr, y2, yr);//use reference point to find distance forward/rightward
        rightw = distance(x1, xr, y1, yr);
        rotat = BaseDist*(r2-r1);

    }//find and save motor speeds to reach next position

    public double sum(ArrayList<Double> q){
        double sum = 0;
        int i=0;
        while(i<q.size()){
            sum += q.get(i);
            i++;
        }
        return sum;
    }//finds sum of all values in an arraylist
    public double distance(double X1,double X2,double Y1,double Y2){
        return Math.sqrt((X1-X2)*(X1-X2)+(Y1-Y2)*(Y1-Y2));
    }//find distance between two points
    public double distance(Pose p1, Pose p2){
        return distance(p1.getX(),p2.getX(),p1.getY(),p2.getY());
    }
    public double loopedInput(double in,double min, double max){
        if(max>min){
            double transitioner = min;
            min=max;
            max=transitioner;
        }
        if(in>0){
            in=((in-max)%(min-max))+max;
        }
        else{
            in=((in-min)%(max-min))+min;
        }
        return in;
    }//loops input value ex: 360deg=0deg and  400deg=40deg
    public boolean MOE(double input, double target, double moe){//Margin of Error
        return (input>target-moe && input<target+moe);
    }//return if input with margin of error from target


}
