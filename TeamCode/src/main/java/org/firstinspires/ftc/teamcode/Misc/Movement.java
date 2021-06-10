package org.firstinspires.ftc.teamcode.Misc;

public class Movement{
    public PositionVar coordinate;
    public int precision;
    public double speed;
    public String name;
    public boolean checked = false;

    public Movement(String Name, PositionVar Coordinate, double Speed, int Precision){
        coordinate = Coordinate;
        name = Name;
        speed = Speed;
        precision = Precision;
    }
    public Movement(PositionVar Coordinate, double Speed, int Precision){
        coordinate = Coordinate;
        name = "Unnamed";
        speed = Speed;
        precision = Precision;
    }
    public Movement(String Name, PositionVar Coordinate, int Precision){
        coordinate = Coordinate;
        name = Name;
        speed = 1.7;
        precision = Precision;
    }
    public Movement(String Name, PositionVar Coordinate, double Speed){
        coordinate = Coordinate;
        name = Name;
        speed = Speed;
        precision = 4;
    }
    public Movement(PositionVar Coordinate, int Precision){
        coordinate = Coordinate;
        name = "Unnamed";
        speed = 1.7;
        precision = Precision;
    }
    public Movement(PositionVar Coordinate, double Speed){
        coordinate = Coordinate;
        name = "Unnamed";
        speed = Speed;
        precision = 4;
    }
    public Movement(String Name, PositionVar Coordinate){
        coordinate = Coordinate;
        name = Name;
        speed = 1.7;
        precision = 10;
    }
    public Movement(PositionVar Coordinate){
        coordinate = Coordinate;
        name = "Unnamed";
        speed = 1.7;
        precision = 4;
    }
}
