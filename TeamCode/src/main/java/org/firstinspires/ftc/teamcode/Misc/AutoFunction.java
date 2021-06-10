package org.firstinspires.ftc.teamcode.Misc;

// import org.firstinspires.ftc.teamcode.Lift;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import java.lang.reflect.Array;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.*;

public abstract class AutoFunction extends LinearOpMode{
    DcMotor M0,M1,M2,M3,M4,M5,M6,M7;
    Servo S0,S1,S2,S3,S4,S5,S6,S7,S8,S9;
    CRServo Cr0, Cr1, Cr2, Cr3, Cr4, Cr5, Cr6, Cr7, Cr8, Cr9;
    DigitalChannel L1;
    // Lift lift;

    ArrayList<Double> sD2 = new ArrayList<Double>(), lD2 = new ArrayList<Double>(), rD2 = new ArrayList<Double>();
    public boolean interrupt = false;
    int counter = 0, moveCounter, counter2=-1;
    public final double DfCoR = 83,BaseDist = 489, Pi = 2*Math.asin(1), MaxIncr = 1, ToMM = 58*Pi/1000, corPrec = 130;//ToMM=.182212
    double fl=0,fr=0,bl=0,br=0,dR=0,dL=0,fls=0,frs=0,bls=0,brs=0,dRs=0,dLs=0,dists=0;//doubles for there function
    public double x1=0,y1=0,r1=0,x2=0,y2=0,r2=0;//doubles for positions
    double rd=0,sd=0,ld=0,rdt=0,sdt=0,ldt=0;//doubles for here function
    public double a,rc;
    double forw, rightw;
    public boolean a1Once = false,a1Toggle = true,b1Once = false,b1Toggle = false,rb1Once=false,
            rb1Toggle=false,lb1Once=false,lb1Toggle=false,y1Once=false,y1Toggle=false,a2Once=false,a2Toggle=false,
            y2Once=false,y2Toggle=false,ls1Once = false,ls1Toggle = false;//toggle switch variables
    public int saveSlot = 0;
    public double grabberPos;
    public String fieldPos = "default" ;//fieldPos None=targetA Single=targetB Quad=targetC
    final int NumberOfSaves = 3;//this is the total number of saves you can have (max: 2 million)
    final int CameraTimeLimit=3;
    public double[][] saves=new double[NumberOfSaves][3];
    final String driveMode="Holo";
    ElapsedTime timer = new ElapsedTime();
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";
    private static final String VUFORIA_KEY =
            " Aaaew2b/////AAABmS/B75n/+UfNvcAUwIoOyVch3hdMxsyRAYZlEIQfWXatbCF77G3JJDGZxiVRVBa2F56Uk4HqDtZIDW2cWVTNpmnkOw5cj8jcFE6sqxANNw0nmRNt7eOVug48c9V+s1iHwUUFM7+GHq73uRn9pc0ezStKjyolrMG+sInIkEix2wssUWo2tseSkJgSvq+sSUoH5PhLQhmQ6uWAAzviRSh3TXgVV4kIOANG+RUpZBjZzZ77COCzq+BLbqjLbLmzVD4Dic2HdvkC/pkVMHFUwUpXrXJU0ODGj1/w8z+im5+6wtjzEy5tlVeCMlGPs79avj8F59NLQivHHChuiJXkgub7Mpvc5pJEt39NnHLmB9o+YDDI";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    //Start of Functions"""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""""
    public void defaultStartup(){
        x1=0;
        y1=0;
        r1=0;
        timer.reset();
        initVuforia();
        initTfod();
        if (tfod != null) {
            tfod.activate();
            //tfod.setZoom(2.5, 1.78);
        }
    }
    public void configCompDrive(){

        M0 = hardwareMap.dcMotor.get("frontLeft");
        M1 = hardwareMap.dcMotor.get("frontRight");
        M2 = hardwareMap.dcMotor.get("backLeft");
        M3 = hardwareMap.dcMotor.get("backRight");
        M4 = hardwareMap.dcMotor.get("intake");
        M5 = hardwareMap.dcMotor.get("conveyor");
        M6 = hardwareMap.dcMotor.get("launcher1");
        M7 = hardwareMap.dcMotor.get("launcher2");

        L1 = hardwareMap.digitalChannel.get("liftLimit");

        Cr4 = hardwareMap.crservo.get("splitter");
        Cr8 = hardwareMap.crservo.get("bottomServo");

        S7 = hardwareMap.servo.get("hook");
        S8 = hardwareMap.servo.get("stopper");
        S9 = hardwareMap.servo.get("grabber");


        M0.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        M0.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        M1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        M1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        M2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        M2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        M3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        M3.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        M4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        M4.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        M5.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        M5.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        M6.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        M6.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        M7.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        M7.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void TelemPos(){
        telemetry.addLine().addData("Drive Mode:",driveMode);
        telemetry.addLine().addData("Counter",moveCounter);
        telemetry.addLine("Position: ").addData(" X ",Math.round(x1)).addData(" Y ",Math.round(y1)).addData(" R ",r1);
        telemetry.addLine("Target: ").addData(" X ",x2).addData(" Y ",y2).addData(" R ",r2);
        telemetry.addLine().addData("Checker",counter);
        telemetry.addLine().addData("Dists",dists);
        telemetry.addLine();

    }
    public void TelemDirect(){
        telemetry.addLine().addData("Left",M3.getCurrentPosition());
        telemetry.addLine().addData("Side",M7.getCurrentPosition());
        telemetry.addLine().addData("Righ",M2.getCurrentPosition());
        telemetry.addLine();

    }
    public void TelemSaves(){
        telemetry.addLine("Slot: " + (saveSlot+1))
                .addData(" X2",Math.round(saves[saveSlot][0]))
                .addData("Y2",Math.round(saves[saveSlot][1]))
                .addData("R2",saves[saveSlot][2]);
        telemetry.addLine();
    }
    public void TelemDists(){
        telemetry.addLine("fl").addData("",fls).addData("|",fl);
        telemetry.addLine("fr").addData("",frs).addData("|",fr);
        telemetry.addLine("bl").addData("",bls).addData("|",bl);
        telemetry.addLine("br").addData("",brs).addData("|",br);
        telemetry.addLine();
        telemetry.addLine().addData("Forwards",forw);
        telemetry.addLine().addData("Rightwards",rightw);
    }

    public void stope(){
        M0.setPower(0);
        M1.setPower(0);
        M2.setPower(0);
        M3.setPower(0);
    }//stop all drive motors
    public void moveTo(double X2, double Y2, double R2, double precision){
        boolean atTarget = false;
        moveCounter = 0;
        while(opModeIsActive()){
            hereTwo();
            there(X2,Y2,R2);
            if(gamepad1.b&&!b1Once){
                interrupt=true;
            }

            checkToggles();
            intaveyor();
            grabberLift();
            launcher();
            splitter();

            if(!interrupt){
                autoSetSpeed();
            }
            else{
                a1Toggle=false;
                b1Toggle=false;
                return;
            }
            if(MOE(x1,x2,corPrec)&&MOE(y1,y2,corPrec)&&(MOE(r1,r2,0.06)||MOE(r1,r2+(2*Pi),0.06)||MOE(r1,r2-(2*Pi),0.06))){//check if at target
                atTarget = true;
            }
            else{
                atTarget = false;
            }
            if(moveCounter>precision){
                stope();
                telemetry.addLine("Goal");
                TelemPos();
                telemetry.update();
                sleep(500);
                a1Toggle=false;
                b1Toggle=false;
                return;
            }
            if(atTarget){
                moveCounter++;
            }
            else{
                if(moveCounter>0){
                    moveCounter--;
                }
            }
        }
    }//move to input position in a straight line
    public void aimAt(double X2,double Y2){
        double R2=1;
        R2=Math.atan((X2-x1)/(Y2-y1));
        if(Y2<y1){
            R2-=Pi;
            if(R2<-Pi){
                R2+=2*Pi;
            }
        }
        //note: fix problem with forw flipping between Pi and -Pi
        moveTo(x1,y1,R2,20);
    }
    public void autoSetSpeed(){
        if(driveMode=="Holo"){
            toSpeed(M0,fl,scaled(fls));
            toSpeed(M1,fr,scaled(frs));
            toSpeed(M2,bl,scaled(bls));
            toSpeed(M3,br,scaled(brs));
        }
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

    public void hereTwo(){
        counter++;//count movements
        lD2.add(-ToMM*M3.getCurrentPosition()-ldt);//find distance of left side of robot in mm
        sD2.add(-ToMM*M7.getCurrentPosition()-sdt);//find distance of right side of robot in mm
        rD2.add(ToMM*M2.getCurrentPosition()-rdt);//find distance perpindicular to movement
        ld=lD2.get(lD2.size()-1);//save left distance to vatiable
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
            forw=rc*Math.sin(a);//find forward distance robot moves
            rightw=rightw=forw*Math.tan(a)+sd;//find rightward distance robot moves
            /*if(MOE(a,0,.067)){
                rightw=forw*Math.tan(a);
            }
            else{
                rightw=forw*Math.tan(a)+sd;
            }*/
        }
        else{
            forw=ld;//without rotation math gets a lot simpler
            rightw=sd;//without rotation math gets a lot simpler
        }
        r1+=a;//find current orientation
        r1=loopedInput(r1,-Pi,Pi);
        dx=forw*Math.sin(r1)+rightw*Math.sin(Pi/2+r1);//solve for change in x relative to field
        dy=forw*Math.cos(r1)+rightw*Math.cos(Pi/2+r1);//solve for change in y relative to field
        x1+=dx;//add change in x to relative x-coordinate
        y1+=dy;//add change in y to relative y-coordinate
        TelemPos();
        TelemDirect();
        telemetry.update();
    }//find and save current position to x1, y1, and r1
    public void there(double X2, double Y2, double R2){
        boolean check = false;
        x2=X2;
        y2=Y2;
        r2=R2;
        if(Math.abs(r2-r1)>Math.abs(r2-2*Pi-r1)){
            r2-=2*Pi;
            check=true;
        }
        if(!check&&Math.abs(r2-r1)>Math.abs(r2+2*Pi-r1)){
            r2+=2*Pi;
        }
        if(driveMode=="Holo"){

            if(r1==0){
                forw=y2-y1;
                rightw=x2-x1;
            }
            else if(r1==Pi/2){
                forw=x2-x1;
                rightw=y1-y2;
            }
            else if(r1==-Pi/2){
                forw=x1-x2;
                rightw=y2-y1;
            }
            else if(r1==Pi){
                forw=y1-y2;
                rightw=x1-x2;
            }
            else{
                double t1=Math.tan(-r1);
                double t2=Math.tan(-r1-Pi/2);
                double xr = (-t1*x1+y1-y2+t2*x2)/(t2-t1);//create reference point
                double yr = (t2*(t1*(x2-x1)+y1)-t1*y2)/(t2-t1);
                int fp = -1;
                int rp = -1;
                forw = distance(x2,xr,y2,yr);//use reference point to find distance forward/rightward
                rightw = distance(x1,xr,y1,yr);
                if(t1*(x2-x1)+y1<y2){
                    fp*=-1;
                }
                if(!MOE(r1,0,Pi/2)){
                    fp*=-1;
                }
                if(t2*(x1-x2)+y2<y1){
                    rp*=-1;
                }
                if(r1<0){
                    rp*=-1;
                }
                forw*=fp;
                rightw*=rp;
            }

            fl=-(forw)+BaseDist*(r2-r1)+(rightw);
            fr=(forw)+BaseDist*(r2-r1)+(rightw);
            bl=(forw)-BaseDist*(r2-r1)+(rightw);
            br=-(forw)-BaseDist*(r2-r1)+(rightw);

            dists=(Math.abs(fl)+Math.abs(fr)+Math.abs(bl)+Math.abs(br));
            fls=fl/dists;
            frs=fr/dists;
            bls=bl/dists;
            brs=br/dists;
            telemetry.addLine().addData("A",a);
            telemetry.addLine().addData("RC",rc);
            TelemPos();
            TelemDists();
            telemetry.update();
        }
        if(driveMode=="Tank"){
            double rc = (x2-x1)/(2*(Math.cos(r2)-Math.cos(r1)));
            dR = Math.round(BaseDist*(r2-r1)+ld);
            dL = Math.round((x2-x1)*(r2-r1)/(Math.cos(r2)-Math.cos(r1)));
            dists=Math.abs(dR)+Math.abs(dL);
            dRs=Math.abs(dR)/dists;
            dLs=Math.abs(dL)/dists;
        }
    }//find and save motor speeds to reach next position
    public void setPos(int x,int y,double r){
        x1=x;
        y1=y;
        r1=r;
    }

    public void driveTrain(){
        float Frontleft =  gamepad1.left_stick_y + gamepad1.right_stick_x + gamepad1.left_stick_x;
        float Frontright = -gamepad1.left_stick_y + gamepad1.right_stick_x + gamepad1.left_stick_x;
        float Backleft =   -gamepad1.left_stick_y - gamepad1.right_stick_x + gamepad1.left_stick_x;
        float Backright =  gamepad1.left_stick_y - gamepad1.right_stick_x + gamepad1.left_stick_x;

        if(gamepad1.right_bumper){
            M0.setPower(Frontleft*.35);
            M1.setPower(Frontright*.35);
            M2.setPower(Backleft*.35);
            M3.setPower(Backright*.35);
        }
        else if(gamepad1.right_stick_button){//toggle half speed
            M0.setPower(Frontleft/2);
            M1.setPower(Frontright/2);
            M2.setPower(Backright/2);
            M3.setPower(Backleft/2);
        }
        else{
            M0.setPower(Frontleft);
            M1.setPower(Frontright);//full speed
            M2.setPower(Backleft);
            M3.setPower(Backright);
        }

        Frontright = Range.clip(Frontright,-1,1);
        Frontleft = Range.clip(Frontleft,-1,1);
        Backleft = Range.clip(Backleft,-1,1);
        Backright = Range.clip(Backright,-1,1);

    }//run drivetrain for teleOp
    public void driveTrainTwo(){
        fls =  gamepad1.left_stick_y + gamepad1.right_stick_x + gamepad1.left_stick_x;
        frs = -gamepad1.left_stick_y + gamepad1.right_stick_x + gamepad1.left_stick_x;
        bls =   -gamepad1.left_stick_y - gamepad1.right_stick_x + gamepad1.left_stick_x;
        brs =  gamepad1.left_stick_y - gamepad1.right_stick_x + gamepad1.left_stick_x;

        if(a1Toggle){
            fls*=.35;
            frs*=.35;
            bls*=.35;
            brs*=.35;
        }
        autoSetSpeed();

    }//run drivetrain for teleOp
    public void checkToggles(){
        if(gamepad1.a&&!a1Once){
            a1Once=true;
            a1Toggle=!a1Toggle;
        }
        else if(!gamepad1.a){
            a1Once=false;
        }
        if(gamepad2.a&&!a2Once){
            a2Once=true;
            grabber();
            a2Toggle=!a2Toggle;
        }
        else if(!gamepad2.a){
            a2Once=false;
        }
        if(gamepad1.b&&!b1Once){
            b1Once=true;
            b1Toggle=!b1Toggle;
        }
        else if(!gamepad1.b){
            b1Once=false;
        }
        if(gamepad1.left_stick_button&&!ls1Once){
            ls1Once=true;
            setPos(0,0,0);
            ls1Toggle=!ls1Toggle;
        }
        else if(!gamepad1.left_stick_button){
            ls1Once=false;
        }
        if(gamepad1.y&&!y1Once){
            y1Once=true;
            aimAt(0,0);
            y1Toggle=!y1Toggle;
        }
        else if(!gamepad1.y){
            y1Once=false;
        }
        if(gamepad2.y&&!y2Once){
            y2Once=true;
            y2Toggle=!y2Toggle;
        }
        else if(!gamepad2.y){
            y2Once=false;
        }
        if(gamepad1.right_bumper&&!rb1Once){
            rb1Once=true;
            rb1Toggle=!rb1Toggle;
            saveSlot++;
        }
        else if(!gamepad1.right_bumper){
            rb1Once=false;
        }
        if(gamepad1.left_bumper&&!lb1Once){
            lb1Once=true;
            lb1Toggle=!lb1Toggle;
            saveSlot--;
        }
        else if(!gamepad1.left_bumper){
            lb1Once=false;
        }
        saveSlot=loopedInput(saveSlot,0,NumberOfSaves-1);
    }//check toggle switches for teleOp
    public void intaveyor(){
        if(!gamepad2.left_bumper){//switch between controlling intake + conveyor and just conveyor
            M4.setPower(gamepad2.left_trigger-gamepad2.right_trigger);
            M5.setPower(gamepad2.left_trigger-gamepad2.right_trigger);
        }
        else{
            M5.setPower(.27);
        }
    }//run conveyor/intake off controller 2 triggers
    public void grabberLift(){
        Cr8.setPower(gamepad2.left_stick_y);
    }//run servos for lift
    public void grabber(){
        grabberPos = S9.getPosition();
        if(!a2Toggle){
            S9.setPosition(.5);
        }
        else{
            S9.setPosition(.87);
        }
    }
    public void launcher(){
        if(gamepad2.b){
            M6.setPower(-1);
            M7.setPower(1);
            S8.setPosition(.7);
            Cr4.setPower(1);
        }
        else{
            M6.setPower(0);
            M7.setPower(0);
            S8.setPosition(1);
            Cr4.setPower(0);
        }
    }
    public void splitter(){
        /*if(gamepad2.right_trigger-gamepad2.left_trigger!=0){
            Cr4.setPower(1);
        }
        else{
            Cr4.setPower(0);
        }*/
    }

    public void camcam(){
        ElapsedTime time = new ElapsedTime();
        while(time.seconds()<CameraTimeLimit && opModeIsActive()){
            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    // step through the list of recognitions and display boundary info.
                    int i = 0;
                    for (Recognition recognition : updatedRecognitions) {
                        telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                        telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                recognition.getLeft(), recognition.getTop());
                        telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                recognition.getRight(), recognition.getBottom());
                        fieldPos = recognition.getLabel();
                    }
                    telemetry.update();
                }
            }
        }
        if (tfod != null) {
            tfod.shutdown();
        }
    }//run tensorflow for set period of time
    public void waitWithCam(){
        int aisudlh = 0;
        while(!opModeIsActive()&&!isStopRequested()){
            aisudlh++;
            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    // step through the list of recognitions and display boundary info.
                    int i = 0;
                    for (Recognition recognition : updatedRecognitions) {
                        telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                        telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                recognition.getLeft(), recognition.getTop());
                        telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                recognition.getRight(), recognition.getBottom());
                        if(recognition.getBottom()-recognition.getTop()>100 && recognition.getLabel()=="Quad"){
                            fieldPos="None";
                        }
                        else if(recognition.getBottom()-recognition.getTop()>60 && recognition.getLabel()=="Single"){
                            fieldPos="None";
                        }
                        else{
                            fieldPos = recognition.getLabel();
                        }
                    }
                    telemetry.addLine(fieldPos);
                    telemetry.update();
                }
            }
        }
        if (tfod != null) {
            tfod.shutdown();
        }
    }
    private void initTfod() {
//        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
//                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
//        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
//        tfodParameters.minResultConfidence = 0.61f;
//        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
//        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }//init tensorflow
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }//init camera

    public double scaled(double i){
        i*=2;
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
    public double loopedInput(double in,double min, double max){
        while(in<min){
            in+=max-min;
        }
        while(in>max){
            in-=max-min;
        }
        return in;
    }//loops input value ex: 360deg=0deg and  400deg=40deg
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




