package org.eclipse.paho.client.mqttv3.testability;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MyMqCallback implements MqttCallback{

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("messageArrived----------");
		System.out.println(topic + "---" + message.toString());
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		System.out.println("deliveryComplete---------" + token.isComplete());
	}

	@Override
	public void connectionLost(Throwable cause) {
		// //连接丢失后，一般在这里面进行重连
		System.out.println("connectionLost----------");
	}

}
