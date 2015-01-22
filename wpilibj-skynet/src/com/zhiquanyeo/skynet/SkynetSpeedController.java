package com.zhiquanyeo.skynet;

import com.zhiquanyeo.skynet.communications.SkynetMainNode;

public class SkynetSpeedController {
	//TODO: Get a publisher instance 
	
	private double speed;
	private String speedControllerTopic;
	
	public SkynetSpeedController(String topic) {
		this.speedControllerTopic = topic;
	}
	
	public void set(double speed, byte syncGroup) {
		set(speed);
	}
	
	public void set(double speed) {
		speed = speed;
		//TODO Publish the speed
		SkynetMainNode.publish(speedControllerTopic, Double.toString(speed));
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
