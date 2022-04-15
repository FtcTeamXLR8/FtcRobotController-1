package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import org.firstinspires.ftc.teamcode.HardwareSystems.ToggleSwitch;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.Runnable.BaseTele;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
//@Disabled
public class Lights extends BaseTele {
    RevBlinkinLedDriver blinkin;
    public boolean lightsOn;
    public boolean a;
    ToggleSwitch lightToggle = new ToggleSwitch();

    public void Init() {
        blinkin = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");
    }

    public void loop() {
        lightToggle.input(gamepad1.a);
        if (lightToggle.getInputResult() && lightToggle.get()) {
//        if (lightToggle.get()){
            blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLACK);
            telemetry.addLine("Black");
            telemetry.update();
        }
//        else{
        if (lightToggle.getInputResult() && !lightToggle.get()){
            blinkin.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);
            telemetry.addLine("Blue");
            telemetry.update();
        }
    }
}
