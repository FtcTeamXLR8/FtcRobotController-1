package org.firstinspires.ftc.teamcode.Systems;

import org.openftc.easyopencv.OpenCvPipeline;


public abstract class DeterminationClass extends OpenCvPipeline {

    public enum Position {
        THREE,
        TWO,
        ONE
    }

    public abstract Position getAnalysis();



}