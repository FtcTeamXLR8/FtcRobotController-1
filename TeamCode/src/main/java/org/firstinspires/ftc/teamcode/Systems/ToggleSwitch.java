package org.firstinspires.ftc.teamcode.Systems;

public class ToggleSwitch{
	boolean lastinput = false;
	boolean out=false;

	public ToggleSwitch toggle(){
		out=!out;
		return this;
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
