package com.zhiquanyeo.skynet;

import com.zhiquanyeo.skynet.communications.SkynetMainNode;
import com.zhiquanyeo.skynet.communications.SkynetSubscriberCallback;

public class SkynetDigitalInput {
	private boolean value;
	
	private long lastInputTime = 0;
	
	public SkynetDigitalInput(String topic) {
		SkynetMainNode.subscribe(topic, 
			new SkynetSubscriberCallback() {
				@Override
				public void callback(String message) {
					long currTime = System.currentTimeMillis();
					if (currTime - lastInputTime > 7) {
						lastInputTime = currTime;
						if (message.equals("1") || message.equals("true") && value != true) {
							value = true;
						}
						else if (value != false) {
							value = false;
						}
					}
				}
			}
		);
	}
	
	public boolean get() {
		return value;
	}
}
