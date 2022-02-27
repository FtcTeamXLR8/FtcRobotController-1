package org.firstinspires.ftc.teamcode.HardwareSystems;

import org.openftc.easyopencv.OpenCvPipeline;


public abstract class DeterminationClass extends OpenCvPipeline {

    public enum Position {
        LEFT,
        CENTER,
        RIGHT
    }

    public abstract Position getAnalysis();



}