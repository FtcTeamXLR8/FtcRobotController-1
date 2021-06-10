package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.annotation.Target;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Misc.PositionVar;
import org.firstinspires.ftc.teamcode.Toggles.ToggleUpdater;

import java.util.ArrayList;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;


public class Odometry1 extends System{
    private final double DfCoR = 83,BaseDist = 489, Pi = 2*Math.asin(1), ToMM = 58*Pi/360, corPrec = 130;

    public double scalar = 1.7;
    public double sm = 1;

    protected DcMotor leftOdometer;
    protected DcMotor rightOdometer;
    protected DcMotor perpindicularOdomoeter;

    protected DcMotor FrontLeft;
    protected DcMotor FrontRight;
    protected DcMotor BackLeft;
    protected DcMotor BackRight;

    public boolean interrupt = false;

    protected double ldt = 0,rdt = 0,sdt = 0;
    protected double ld,rd,sd;
    protected double a,rc;
    protected double forw = 0,rightw = 0, rotat = 0;
    protected double dists;

    protected boolean opModeIsActive;

    protected PositionVar currentPosition = new PositionVar(0,0,0);
    protected PositionVar targetPosition = new PositionVar(0,0,0);

    HolonomicDriveTrain driveTrain;

    protected double fl,fr,bl,br;
    protected double fls,frs,bls,brs;

    protected int precision, precisionCounter = 0;

    public PositionVar initialPos = new PositionVar(0,0,0);

    public Odometry1(DcMotor leftOdom, DcMotor sideOdom, DcMotor rightOdom, HolonomicDriveTrain DriveTrain){
        driveTrain = DriveTrain;
        leftOdometer = leftOdom;
        perpindicularOdomoeter = sideOdom;
        rightOdometer = rightOdom;
    }

    void input(){
        here();
        if(atTarget()){
            this.precisionCounter++;
            setScalar(1.2*sm);
        }
        else if(!atTarget()){
            this.precisionCounter--;
            setScalar(1.7*sm);
        }
        if(this.precisionCounter<0){
            this.precisionCounter=0;
        }
        
    }
    void update(){
        there();
        if(isStopped()){
            driveTrain.stope();
        }
        else{
            driveTrain.setSpeedByDists(forw, rightw, rotat);
        }
    }
    public void setSpeed(double In){
        sm = In;
    }

    public boolean atTarget(){
        double r2 = targetPosition.getR();
        double r1 = currentPosition.getR();
        double x2 = targetPosition.getX();
        double x1 = currentPosition.getX();
        double y2 = targetPosition.getY();
        double y1 = currentPosition.getY();

        if(MOE(x1,x2,corPrec)&&MOE(y1,y2,corPrec)&&
                (MOE(r1,r2,0.06)||MOE(r1,r2+(2*Pi),0.06)||MOE(r1,r2-(2*Pi),0.06))){//check if at target
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isStopped(){
        if(this.precisionCounter>this.precision){
            return true;
        }
        else{
            return false;
        }
    }

    public PositionVar getPositionByDists(double f, double s){
        double r1 = currentPosition.getR();
        double x1 = currentPosition.getX();
        double y1 = currentPosition.getY();
        double x2=f*Math.sin(r1)+s*Math.sin(Pi/2+r1)+x1;//solve for change in x relative to field
        double y2=f*Math.cos(r1)+s*Math.cos(Pi/2+r1)+y1;//solve for change in y relative to field
        return new PositionVar(x2,y2,r1);
    }

    public void setPrecision(int c){
        this.precision = c;
    }
    public int getPrecisionCounter(){
        return precisionCounter;
    }
    
    public void setPos(double X1, double Y1, double R1){
        currentPosition.setX(X1);
        currentPosition.setY(Y1);
        currentPosition.setR(R1);
    }
    public void setPos(PositionVar c){
        currentPosition.setPos(c);
    }
    public PositionVar getTarget(){
        return targetPosition;
    }
    public void setTarget(double X1, double Y1, double R1){
        targetPosition.setX(X1);
        targetPosition.setY(Y1);
        targetPosition.setR(R1);
        this.precisionCounter = 0;
    }
    public void setTarget(PositionVar c){
        targetPosition.setPos(c);
        this.precisionCounter = 0;
    }
    public PositionVar getPos(){
        return currentPosition;
    }
    
    public double pointedAt(PositionVar c){
        return pointedAtFrom(currentPosition,c);
    }
    public double pointedAtFrom(PositionVar current, PositionVar target){
        double m = -(target.getY()-current.getY())/(target.getX()-current.getX());
        double r2=Math.atan(m)+3*Pi/2;
        if(target.getX()>current.getX()){
            r2+=Pi;
        }
        if(r2>Pi){
            r2-=2*Pi;
        }
        return r2;
    }
    public double pointedAt(double x2, double y2){
        return pointedAt(new PositionVar(x2,y2,currentPosition.getR()));
    }

    private void here(){//approcimately .505mm per output unit
        ld = ToMM*leftOdometer.getCurrentPosition();
        rd = ToMM*rightOdometer.getCurrentPosition();
        sd = ToMM*perpindicularOdomoeter.getCurrentPosition();

        double dy=0,dx=0;//initialize needed variables
        a=0;
        rc=0;
        rightw=0;//initialize needed variables
        forw=0;
        a=(ld-rd)/BaseDist;//solve for change in rotation

        double Forw, Rightw; 
        
        if(a!=0){//check if change in rotation equals 0 so we don't divide by zero
            sd+=DfCoR*a;//correct perp distance for rotation
            rc=(ld+rd)/(2*a);//find average radius of arclengths
            Forw=(rc+sd)*Math.sin(a);//find forward distance robot moves
            Rightw=rc*Math.sin(a)*Math.tan(a)+sd;//find rightward distance robot moves
        }
        else{
            Forw=ld;//without rotation math gets a lot simpler
            Rightw=sd;//without rotation math gets a lot simpler
        }

        a=loopedInput(a, -Pi, Pi);

        dx=forw*Math.sin(a+rightw*Math.sin(Pi/2+a));//solve for change in x relative to field
        dy=forw*Math.cos(a+rightw*Math.cos(Pi/2+a));//solve for change in y relative to field

        currentPosition.setR(a+initialPos.getR());
        currentPosition.setX(dx+initialPos.getX());
        currentPosition.setY(dy+initialPos.getY());

    }//find and save current position to x1, y1, and r1
    private void there(){
        double r2 = targetPosition.getR();
        double r1 = currentPosition.getR();
        double x2 = targetPosition.getX();
        double x1 = currentPosition.getX();
        double y2 = targetPosition.getY();
        double y1 = currentPosition.getY();

        boolean check = false;
        if(Math.abs(r2-r1)>Math.abs(r2-2*Pi-r1)){
            r2-=2*Pi;
            check=true;
        }
        if(!check&&Math.abs(r2-r1)>Math.abs(r2+2*Pi-r1)){
            r2+=2*Pi;
        }
        if(r1==0||r1==Pi/2||r1==Pi||r1==-Pi||r1==-Pi/2){
            r1+=.0000001;
        }
        double t1 = Math.tan(-r1);
        double t2 = Math.tan(-r1 - Pi / 2);
        double xr = (-t1 * x1 + y1 - y2 + t2 * x2) / (t2 - t1);//create reference point
        double yr = (t2 * (t1 * (x2 - x1) + y1) - t1 * y2) / (t2 - t1);
        int fp = -1;
        int rp = -1;
        forw = distance(x2, xr, y2, yr);//use reference point to find distance forward/rightward
        rightw = distance(x1, xr, y1, yr);
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

        forw *= fp;
        rightw *= rp;

        rotat = BaseDist*(r2-r1);
    }//find and save motor speeds to reach next position

    public double scaled(double i){
        i*=scalar;
        return (i);
    }//scalar for speed
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
    public double distance(PositionVar p1, PositionVar p2){
        return distance(p1.getX(),p2.getX(),p1.getY(),p2.getY());
    }
    public double loopedInput(double in,double min, double max){
        in=((in-min)%(max-min))+min;
    }//loops input value ex: 360deg=0deg and  400deg=40deg
    public boolean MOE(double input, double target, double moe){//Margin of Error
        return (input>target-moe && input<target+moe);
    }//return if input with margin of error from target
}
