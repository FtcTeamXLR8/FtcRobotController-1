package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;


public class NewCamera extends System{
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";//test comment
    protected static final String LABEL_FIRST_ELEMENT = "Quad";
    protected static final String LABEL_SECOND_ELEMENT = "Single";
    protected static final String VUFORIA_KEY =
            " Aaaew2b/////AAABmS/B75n/+UfNvcAUwIoOyVch3hdMxsyRAYZlEIQfWXatbCF77G3JJDGZxiVRVBa2F56Uk4HqDtZIDW2cWVTNpmnkOw5cj8jcFE6sqxANNw0nmRNt7eOVug48c9V+s1iHwUUFM7+GHq73uRn9pc0ezStKjyolrMG+sInIkEix2wssUWo2tseSkJgSvq+sSUoH5PhLQhmQ6uWAAzviRSh3TXgVV4kIOANG+RUpZBjZzZ77COCzq+BLbqjLbLmzVD4Dic2HdvkC/pkVMHFUwUpXrXJU0ODGj1/w8z+im5+6wtjzEy5tlVeCMlGPs79avj8F59NLQivHHChuiJXkgub7Mpvc5pJEt39NnHLmB9o+YDDI";
    protected CameraName cameraName;
    protected VuforiaLocalizer vuforia;
    protected TFObjectDetector tfod;
    
    public NewCamera(CameraName c){
        this.cameraName = c;
    }

    protected String fieldPos = "default";
    public double width = 0;

    void update() {}

    void input() {}

    public void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = cameraName;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }//init camera

    public void initTfod(int c){
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(c);
        tfodParameters.minResultConfidence = .61f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
        if (tfod != null) {
            tfod.activate();
            //tfod.setZoom(2.5, 1.78);
        }
    }//init tensorflow with camera stream
    public void initTfod(int c, double x, double y){
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(c);
        tfodParameters.minResultConfidence = .61f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(x, y);
        }
    }//init tensorflow with stream and zoom
    public void initTfod(float confidence,int c, double width, double height){
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(c);
        tfodParameters.minResultConfidence = confidence;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(width, height);
        }
    }//init tensorflow with stream and zoom
    public void initTfod(){
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters();
        tfodParameters.minResultConfidence = .61f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
        if (tfod != null) {
            tfod.activate();
            //tfod.setZoom(2.5, 1.78);
        }
    }//init tensorflow
    
    public void endTfod(){
        if (tfod != null) {
            tfod.deactivate();
            // tfod.setZoom(4, 2);
        }
    }




    public void updateCamera(){
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                // step through the list of recognitions and display boundary info.
                int i = 0;
                fieldPos="None";
                for (Recognition recognition : updatedRecognitions) {
                    width=recognition.getRight()-recognition.getLeft();
                    if(width<125){
                        if(recognition.getLabel()!="Quad"){
                            fieldPos="Single";
                        }
                        else{
                            fieldPos="Quad";
                        }
                    }
                    // fieldPos = "hi";
                }
            }
        }
    }

    public String getFieldPos(){
        return fieldPos;
    }

}
