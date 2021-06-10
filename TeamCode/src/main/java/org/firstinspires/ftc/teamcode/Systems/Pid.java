package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.util.ArrayList;


public class Pid{
    private double P=0,I=0,D=0,F=0;
    private double maxIOutput=0,maxError=0,errorSum=0;
    private double maxOutput=0,minOutput=0;
    private double setPoint=0;
    private double lastActual=0;
    private boolean firstRun=true,reversed=false;
    private double outputRampRate=0,lastOutput=0;
    private double outputFilter=0;
    private double setPointRange=0;
    
    public Pid(double p, double i, double d){
        P=p;I=i;D=d;
        checkSigns();
    }
    public Pid(double p, double i, double d,double f){
        P=p;I=i;D=d;F=f;
        checkSigns();
    }
    public void setP(double p){
        P=p;
        checkSigns();
    }
    public void setI(double i){
        if(I!=0){
            errorSum*=I/i;
        }
        if(maxIOutput!=0){
            maxError=maxIOutput/i;
        }
        I=i;
        checkSigns();
    }
    public void setD(double d){
        D=d;
        checkSigns();
    }
    public void setF(double f){
        F=f;
        checkSigns();
    }
    public void setPID(double p,double i,double d){
        P=p;D=d;
        setI(i);
        checkSigns();
    }
    public void setPID(double p,double i,double d,double f){
        P=p;D=d;F=f;
        setI(i);
        checkSigns();
    }
    public void setMaxIOutput(double maximum){
        maxIOutput=maximum;
        if(I!=0){
            maxError=maxIOutput/I;
        }
    }
    public void setOutputLimits(double output){
        if(output<0)output*=-1;
        setOutputLimits(-output,output);
    }
    public void setOutputLimits(double minimum,double maximum){
        if(maximum<minimum){
            double m=maximum;
            maximum=minimum;
            minimum=m;
        }
        maxOutput=maximum;
        minOutput=minimum;
        if(maxIOutput==0||maxIOutput>(maximum-minimum)){
            setMaxIOutput(maximum-minimum);
        }
    }
    public void setDirection(boolean reversed){
        this.reversed = reversed;
    }
    public void setSetPoint(double setPoint){
        this.setPoint=setPoint;
    }
    public double getOutput(double actual, double setPoint){
        double output, Poutput, Ioutput, Doutput, Foutput;
        this.setPoint=setPoint;
        if(setPointRange!=0){
            setPoint=constrain(setPoint,actual-setPointRange,actual+setPointRange);
        }
        double error=setPoint-actual;
        Foutput=F*setPoint;
        Poutput=P*error;
        if(firstRun){
            lastActual=actual;
            lastOutput=Poutput+Foutput;
            firstRun=false;
        }
        Doutput=-D*(actual-lastActual);
        lastActual=actual;
        Ioutput=I*errorSum;
        if(maxIOutput!=0){
            Ioutput=constrain(Ioutput,-maxIOutput,maxIOutput);
        }
        output=Foutput+Poutput+Ioutput+Doutput;
        if(minOutput!=maxOutput&&!bounded(output,minOutput,maxOutput)){
            errorSum=error;
        }
        else if(outputRampRate!=0&&!bounded(output,lastOutput-outputRampRate,lastOutput+outputRampRate)){
            errorSum=error;
        }
        else if(maxIOutput!=0){
            errorSum=constrain(errorSum+error,-maxError,maxError);
        }
        else{
            errorSum+=error;
        }
        if(outputRampRate!=0){
            output=constrain(output,minOutput,maxOutput);
        }
        if(minOutput!=maxOutput){
            output=constrain(output,minOutput,maxOutput);
        }
        if(outputFilter!=0){
            output=lastOutput*outputFilter+output*(1-outputFilter);
        }
        lastOutput=output;
        return output;
    }
    public double getOutput(){
        return getOutput(lastActual,setPoint);
    }
    public double getOutput(double actual){
        return getOutput(actual,setPoint);
    }
    public void reset(){
        firstRun=true;
        errorSum=0;
    }
    public void setOutputRampRate(double rate){
        outputRampRate=rate;
    }
    public void setSetPointRange(double range){
        setPointRange=range;
    }
    public void setOutputFilter(double strength){
        if(strength!=0||bounded(strength,0,1));
    }
    private double constrain(double value, double min,double max){
        if(value>max)return max;
        if(value<min)return min;
        return value;
    }
    private boolean bounded(double value, double min, double max){
        return min<value&&max>value;
    }
    private void checkSigns(){
        if(reversed){
            if(P>0)P*=-1;
            if(I>0)I*=-1;
            if(D>0)D*=-1;
            if(F>0)F*=-1;
        }
        else{
            if(P<0)P*=-1;
            if(I<0)I*=-1;
            if(D<0)D*=-1;
            if(F<0)F*=-1;
        }
    }
}
