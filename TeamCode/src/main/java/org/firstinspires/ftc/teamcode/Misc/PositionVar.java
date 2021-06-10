package org.firstinspires.ftc.teamcode.Misc;

public class Position {
    final double Pi = 2*Math.asin(1);
    protected double x;
    protected double y;
    protected double r;

    public Position(double x, double y, double r){
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }
    public double getR() {
        return this.r;
    }

    public void setX(double x) {
        this.x = x;
    }
    public void addToX(double dx){
        this.x+=dx;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void addToY(double dy){
        this.y+=dy;
    }
    public void setR(double r) {
        this.r = r;
    }
    public void addToR(double dr){
        this.r+=dr;
        this.r=loopedInput(this.r,-Pi,Pi);
    }
    public void setPos(Position c){
        this.x = c.getX();
        this.y = c.getY();
        this.r = c.getR();
    }

    private double loopedInput(double in,double min, double max){
        while(in<min){
            in+=max-min;
        }
        while(in>max){
            in-=max-min;
        }
        return in;
    }//loops input value ex: 360deg=0deg and  400deg=40deg
}
