package com.zhiquanyeo.skynet.communications;

public class SkynetMainNode {
	private SkynetMainNode() {}
	
	private static SkynetNode mainNode;
	
	public static synchronized void openSkynetConnection(String host, int port) {
		if (mainNode != null) {
			System.err.println("SkynetMainNode.openSkynetConnection() was already called");
			return;
		}
		mainNode = new SkynetNode("Skynet", host, port);
		mainNode.connect();
	}
	
	public static void publish(String topic, String message) {
		if (mainNode == null) {
			throw new IllegalStateException("SkynetMainNode.openSkynetConnection() should have already been called");
		}
		mainNode.publish(topic, message);
	}
	
	public static void subscribe(String topic, SkynetSubscriberCallback cb) {
		if (mainNode == null) {
			throw new IllegalStateException("SkynetMainNode.openSkynetConnection() should have already been called");
		}
		mainNode.subscribe(topic, cb);
	}
}
