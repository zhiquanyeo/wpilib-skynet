package com.zhiquanyeo.skynet;

import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.swt.widgets.Display;

//This is the interface between a wpilibj program and the SkynetRobot
public class SkynetNode {
	
	
	
	//Private constructor
	private SkynetNode() {

	}
	
	private static final Logger LOG = Logger.getLogger("Skynet Control Node");
	private static MqttAsyncClient client;
	
	private static final String brokerUri = "tcp://localhost:1883";
	
	public static synchronized void openSkynetConnection() {
		/*
		if (client != null) {
			LOG.warning("SkynetNode.openSkynetConnection(); was already called!");
			return;
		}
		try {
			client = new MqttAsyncClient(brokerUri, "SkynetControlClient");
		}
		catch (MqttException e) {
			
		}
		*/
	}
}
