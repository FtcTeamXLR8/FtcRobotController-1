package org.firstinspires.ftc.teamcode.HardwareSystems;

public class ToggleSwitch extends HardwareSystems{
	boolean lastinput = false;
	boolean out=false;

	public ToggleSwitch toggle(){
		out=!out;
	}

	public boolean input(boolean input){
		if(input && !lastinput){
			lastinput=input;
			toggle();
			return true;
		}
		lastinput=input;
		return true;
	}

	public boolean get(){
		return out;
	}
}
