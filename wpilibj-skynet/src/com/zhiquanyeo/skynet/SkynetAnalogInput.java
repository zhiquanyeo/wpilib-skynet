package com.zhiquanyeo.skynet;

import com.zhiquanyeo.skynet.communications.SkynetMainNode;
import com.zhiquanyeo.skynet.communications.SkynetSubscriberCallback;

public class SkynetAnalogInput {
	private double value;
	
	public SkynetAnalogInput(String topic) {
		SkynetMainNode.subscribe(topic, 
			new SkynetSubscriberCallback() {
				@Override
				public void callback(String message) {
					value = Double.parseDouble(message);
				}
			}
		);
	}
	
	public double get() {
		return value;
	}
}
