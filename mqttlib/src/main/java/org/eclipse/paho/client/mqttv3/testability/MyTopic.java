package org.eclipse.paho.client.mqttv3.testability;

public class MyTopic {
	public static String caigouTopic = "topic/caigou/";

	public static String getCaigouTopic(String id) {
		return caigouTopic + id;
	}
}
