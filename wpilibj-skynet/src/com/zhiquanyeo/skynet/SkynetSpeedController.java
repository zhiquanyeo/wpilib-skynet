package com.zhiquanyeo.skynet;

public class SkynetSpeedController {
	//TODO: Get a publisher instance 
	
	private double speed;
	
	public SkynetSpeedController(String topic) {
		//TODO hook up to the publisher
	}
	
	public void set(double speed, byte syncGroup) {
		set(speed);
	}
	
	public void set(double speed) {
		speed = speed;
		//TODO Publish the speed
		
	}
	
	public double get() {
		return speed;
	}
	
	public void disable() {
		set(0);
	}
	
	public void pidWrite(double output) {
		set(output);
	}
}
