package com.zhiquanyeo.skynet.communications;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class SkynetNode implements Runnable, MqttCallback {
	private final String name;
	private final String host;
	private final int port;
	
	private MqttClient client;
	
	private MemoryPersistence persist;
	private String clientId;
	
	private final Map<String, SkynetSubscriberCallback> subscriptions = new HashMap<>();
	
	public SkynetNode(String name, String host, int port) {
		this.name = name;
		this.host = host;
		this.port = port;
		
		this.persist = new MemoryPersistence();
		this.clientId = "SkynetControl_" + Double.toString(Math.random()).substring(2, 8);
	}
	
	public synchronized void connect() {
		//new Thread(this).start();
		this.run();
		System.out.println("MQTT Client connected");
		
	}
	
	public synchronized void publish(String topic, String payload) {
		MqttMessage message = new MqttMessage(payload.getBytes());
		try {
			client.publish(topic, message);
		}
		catch (MqttException e) {
			System.err.println("Could not publish message " + e.toString());
			e.printStackTrace();
			
		}
	}
	
	public synchronized void subscribe(String topic, SkynetSubscriberCallback cb) {
		if (subscriptions.containsKey(topic)) {
			System.err.println("Already have a subscription for " + topic);
			return;
		}
		
		subscriptions.put(topic, cb);
	}

	@Override
	public void connectionLost(Throwable exception) {
		System.out.println("Connection to broker was lost! " + exception);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken asyncToken) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		String time = new Timestamp(System.currentTimeMillis()).toString();
		if (topic.equals("skynet/clients/activeClient")) {
			String activeClient = new String(message.getPayload());
			if (activeClient.equals(this.clientId)) {
				System.out.println("I AM THE ACTIVE CLIENT");
			}
		}
		else {
			SkynetSubscriberCallback cb = subscriptions.get(topic);
			if (cb != null) {
				System.out.println("[SkynetNode] Received Topic: " + topic + " and calling back with " + message.toString());
				cb.callback(new String(message.getPayload()));
			}
		}
		
	}

	@Override
	public void run() {
		synchronized(this) {
			String brokerURI = "tcp://" + this.host + ":" + this.port;
			try {
				client = new MqttClient(brokerURI, this.clientId, persist);
				client.setCallback(this);
				client.connect();
				System.out.println("Connected to broker as " + this.clientId);
	
				client.subscribe("skynet/clients/activeClient", 1);
				
				MqttMessage regMessage = new MqttMessage("control".getBytes());
				client.publish("skynet/clients/register", regMessage);
				
				//Subscribe to the sensor messages
				client.subscribe("skynet/robot/sensors/#", 1);
				System.out.println("READY");
			}
			catch (MqttException e) {
				System.err.println("Error while setting up Control Client " + e.toString());
				e.printStackTrace();
			}
		}
	}
	
}
