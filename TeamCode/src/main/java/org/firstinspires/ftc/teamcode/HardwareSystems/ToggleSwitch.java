package org.firstinspires.ftc.teamcode.HardwareSystems;

public class ToggleSwitch extends HardwareSystem{
	boolean lastinput = false;
	boolean inputResult = false;
	boolean out=false;

	public ToggleSwitch toggle(){
		out=!out;
		return this;
	}

	public boolean input(boolean input){
		if(input && !lastinput){
			lastinput=input;
			toggle();
			inputResult = true;
			return true;
		}
		lastinput=input;
		inputResult = false;
		return true;
	}

	public boolean get(){
		return out;
	}
	public boolean getInputResult(){
		return inputResult;
	}
}
