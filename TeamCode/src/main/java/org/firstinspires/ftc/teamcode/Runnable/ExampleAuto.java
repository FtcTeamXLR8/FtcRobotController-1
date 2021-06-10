package org.firstinspires.ftc.teamcode.Runnable;

@Autonomous(name = "Example",group = "Test")
// @Disabled
public class ExampleAuto extends BaseAuto{
    public void runOpMode(){

        int loopCount = 0;

        initSystems();
        enableLoop();
        startingPosition(0,0,0);

        addCoordinate("First", Coordinate(-1000,0,-Pi/2), 1.0, 10);

        addMovement(0, 1000, 1.1,7);

        addCoordinate(Coordinate(0, 1000, 0));

        addMovement("Last", -1000, 0, 1d);

        waitForStart();
        // waitWithCam();


        startMoveSequence();

        while(opModeIsActive()){
            runMoveSequence();
            // runMoveSequence(gamepad1.b);

            if(before("First")){
                loopCount++;
            }

            telemetry.addLine("Move Number: " + moveCount);
            telemetry.addLine("Move Name: " + currentMovement.name);
            telemetry.addLine();
            telemetry.addLine("Cycles: " + loopCount);
            telemetry.update();



            if(isStopRequested()){
                odom.stope();
                return;
            }
        }
        return;
    }
}