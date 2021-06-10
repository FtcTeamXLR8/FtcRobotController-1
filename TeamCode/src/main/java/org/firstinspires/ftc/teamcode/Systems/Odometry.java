package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.annotation.Target;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Misc.PositionVar;
import org.firstinspires.ftc.teamcode.Toggles.ToggleUpdater;

import java.util.ArrayList;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;


public class Odometry extends System{
    private final double DfCoR = 83,BaseDist = 489, Pi = 2*Math.asin(1), ToMM = 58*Pi/360, corPrec = 130;//ToMM~.182212

    protected double scalar = 1.7;
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
    protected double forw,rightw;
    protected double dists;

    protected boolean opModeIsActive;

    protected PositionVar currentPosition = new PositionVar(0,0,0);
    protected PositionVar targetPosition = new PositionVar(0,0,0);

    protected ArrayList<Double> lD2 = new ArrayList<Double>();
    protected ArrayList<Double> rD2 = new ArrayList<Double>();
    protected ArrayList<Double> sD2 = new ArrayList<Double>();
    
    Drivetrain driveTrain;

    protected double fl,fr,bl,br;
    protected double fls,frs,bls,brs;

    protected int precision, precisionCounter = 0;

    public Odometry(DcMotor leftOdom, DcMotor sideOdom, DcMotor rightOdom, Drivetrain DriveTrain){
        leftOdometer = leftOdom;
        perpindicularOdomoeter = sideOdom;
        rightOdometer = rightOdom;
        driveTrain = DriveTrain;
        driveTrain.disable();
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
            stope();
        }
        else{
            autoSetSpeed();
        }
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

    public double getScalar() {
        return scalar;
    }

    public void setPrecision(int c){
        this.precision = c;
    }
    public int getPrecisionCounter(){
        return precisionCounter;
    }
    public void setScalar(double c){
        this.scalar = c;
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
        lD2.add(-ToMM*leftOdometer.getCurrentPosition()-ldt);//find distance of left side of robot in mm
        sD2.add(-ToMM*perpindicularOdomoeter.getCurrentPosition()-sdt);//find distance of right side of robot in mm
        rD2.add(ToMM*rightOdometer.getCurrentPosition()-rdt);//find distance perpindicular to movement
        ld=lD2.get(lD2.size()-1);//save left distance to variable
        ldt+=ld;// add to sum of left distances
        rd=rD2.get(rD2.size()-1);//save right distance to variable
        rdt+=rd;//add to sum of right distances
        sd=sD2.get(sD2.size()-1);//save perp distance to variable
        sdt+=sd;//add to sum of perp distances
        double dy=0,dx=0;//initialize needed variables
        a=0;
        rc=0;
        rightw=0;//initialize needed variables
        forw=0;
        a=(ld-rd)/BaseDist;//solve for change in rotation
        if(a!=0){//check if change in rotation equals 0 so we don't divide by zero
            sd+=DfCoR*a;//correct perp distance for rotation
            rc=(ld+rd)/(2*a);//find average radius of arclengths
            forw=(rc+sd)*Math.sin(a);//find forward distance robot moves
            rightw=rc*Math.sin(a)*Math.tan(a)+sd;//find rightward distance robot moves
        }
        else{
            forw=ld;//without rotation math gets a lot simpler
            rightw=sd;//without rotation math gets a lot simpler
        }
        
        if(a>0)a%=Pi;
        else a%=-Pi;

        currentPosition.addToR(a);
        dx=forw*Math.sin(currentPosition.getR())+rightw*Math.sin(Pi/2+currentPosition.getR());//solve for change in x relative to field
        dy=forw*Math.cos(currentPosition.getR())+rightw*Math.cos(Pi/2+currentPosition.getR());//solve for change in y relative to field
        currentPosition.addToX(dx);
        currentPosition.addToY(dy);
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
        // else {
            double t1 = Math.tan(-r1);
            double t2 = Math.tan(-r1 - Pi / 2);
            double xr = (-t1 * x1 + y1 - y2 + t2 * x2) / (t2 - t1);//create reference point
            double yr = (t2 * (t1 * (x2 - x1) + y1) - t1 * y2) / (t2 - t1);
            int fp = -1;
            int rp = -1;
            this.forw = distance(x2, xr, y2, yr);//use reference point to find distance forward/rightward
            this.rightw = distance(x1, xr, y1, yr);
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
            this.forw *= fp;
            this.rightw *= rp;

            this.fl = -(this.forw) + this.BaseDist * (r2 - r1) + (this.rightw);
            this.fr = (this.forw) + this.BaseDist * (r2 - r1) + (this.rightw);
            this.bl = (this.forw) - this.BaseDist * (r2 - r1) + (this.rightw);
            this.br = -(this.forw) - this.BaseDist * (r2 - r1) + (this.rightw);

            this.dists = (Math.abs(fl) + Math.abs(fr) + Math.abs(bl) + Math.abs(br));
            this.fls = this.fl / this.dists;
            this.frs = this.fr / this.dists;
            this.bls = this.bl / this.dists;
            this.brs = this.br / this.dists;
        // }
    }//find and save motor speeds to reach next position


    public void autoSetSpeed(){
        toSpeed(FrontLeft,fl,scaled(fls));
        toSpeed(FrontRight,fr,scaled(frs));
        toSpeed(BackLeft,bl,scaled(bls));
        toSpeed(BackRight,br,scaled(brs));
    }//calls all toSpeed functions for current drivetrain
    public void toSpeed(DcMotor M, double dist, double ts){
//        telemetry.addLine("Setting Speed");
        if(!MOE(ts,0,1)){
            M.setPower(1*ts/Math.abs(ts));
        }
        else if(MOE(ts,0,.1)){
            M.setPower(ts/Math.abs(ts)*.1);
        }
        else{
            M.setPower(ts);
        }

    }//clips speeds to range and sets power

    public void stope(){
        FrontLeft.setPower(0);
        FrontRight.setPower(0);
        BackLeft.setPower(0);
        BackRight.setPower(0);
    }

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
    public int loopedInput(int in,int min, int max){
        while(in<min){
            in+=max-min+1;
        }
        while(in>max){
            in-=max-min+1;
        }
        return in;
    }//loops input value ex: 360deg=0deg and  400deg=40deg
    public boolean MOE(double input, double target, double moe){//Margin of Error
        return (input>target-moe && input<target+moe);
    }//return if input with margin of error from target
}
