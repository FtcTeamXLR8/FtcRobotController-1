package org.firstinspires.ftc.teamcode.Misc;

public class Pose {
    final double Pi = 2*Math.asin(1);
    protected double x;
    protected double y;
    protected double r;

    public Pose(double x, double y, double r){
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getR() {
        return r;
    }

    public void setX(double X) {
        x = X;
    }
    public void addToX(double dx){
        x+=dx;
    }
    public void setY(double Y) {
        y = Y;
    }
    public void addToY(double dy){
        y+=dy;
    }
    public void setR(double R) {
        r = R;
        r=loopedInput(r,-Pi,Pi);
    }
    public void addToR(double dr){ r+=dr;
        r=loopedInput(r,-Pi,Pi);
    }
    public void setPos(Pose c){
        this.x = c.getX();
        this.y = c.getY();
        this.r = c.getR();
    }
    public void setPos(double X, double Y, double R){
        setPos(new Pose(X,Y,R));
    }

    private double loopedInput(double in,double min, double max){
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
    public String report(){
        return ("X: "+Math.round(getX())+" | Y: "+Math.round(getY())+" | R: "+((double)Math.round(getR()*10000))/10000);}

}
