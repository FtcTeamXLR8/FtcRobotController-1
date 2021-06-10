package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
//@Disabled
public class TeleOp1 extends BaseTele {

    @Override
    public void init() {
        initSystems();
        initToggles();
        odom.setPos(-700,2200,0);
    }

    @Override
    public void loop() {}
}
