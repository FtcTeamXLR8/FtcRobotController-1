package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class CameraTest extends BaseAuto{
    @Override
    public void initializeMovements() {
        initCam();

        waitWhileScanning();
    }
}
