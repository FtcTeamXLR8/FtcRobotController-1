package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import java.io.FileInputStream;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;


@Autonomous
@Disabled
public class SaveTest extends LinearOpMode{
    public void runOpMode(){
        try{
            File file = new File("C:\\testFile.txt");
            telemetry.addLine("1");
            if(file.createNewFile())telemetry.addLine("3");
            else telemetry.addLine("2");
            
        }
        catch(IOException e){
            telemetry.addLine("Save Error");
        }
        telemetry.update();
        while(!isStopRequested());
    }
}