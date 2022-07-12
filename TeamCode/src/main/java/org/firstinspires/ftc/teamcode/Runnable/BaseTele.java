package org.firstinspires.ftc.teamcode.Runnable;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Events.*;

import java.util.ArrayList;

public abstract class BaseTele extends OpMode {
    ElapsedTime elapsedTime;
    ArrayList<Event> eventList = new ArrayList<>();

    // Declare hardware variables{{{
    
    
    
    
    
    
    // }}}


    public void init(){
        elapsedTime = new ElapsedTime();
	// initalize hardware variables{{{
	
	
	
	
	
	
	
	// }}}
    }
    public void loop(){
        Loop();

        for(Event event : eventList)event.test();
    }
    public abstract void Loop();
}
