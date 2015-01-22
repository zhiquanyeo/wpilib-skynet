package com.zhiquanyeo.skynet;

import com.zhiquanyeo.skynet.communications.SkynetMainNode;
import com.zhiquanyeo.skynet.communications.SkynetSubscriberCallback;

public class SkynetDigitalInput {
	private boolean value;
	
	public SkynetDigitalInput(String topic) {
		SkynetMainNode.subscribe(topic, 
			new SkynetSubscriberCallback() {
				@Override
				public void callback(String message) {
					if (message.equals("1") || message.equals("true")) {
						value = true;
					}
					else {
						value = false;
					}
				}
			}
		);
	}
	
	public boolean get() {
		return value;
	}
}
