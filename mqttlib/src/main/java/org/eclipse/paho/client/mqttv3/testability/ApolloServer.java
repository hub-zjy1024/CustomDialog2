package org.eclipse.paho.client.mqttv3.testability;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * 
 * Title:Server Description: 服务器向多个客户端推送主题，即不同客户端可向服务器订阅相同主题
 * 
 * @author chenrl 2016年1月6日下午3:29:28
 */

public class ApolloServer {

	// public static final String HOST = "tcp://192.168.10.65:61680";
	public static final String HOST = ApolloConfig.HOST;
	public static final String TOPIC = ApolloConfig.TOPIC;
	public static final String TOPIC125 = ApolloConfig.TOPIC125;
	private static final String clientid = ApolloConfig.clientid;
	private static final String ENCODE = ApolloConfig.ENCODE;
	private MqttClient client;
	private MqttTopic topic;
	private MqttTopic topic125;
	private String userName = ApolloConfig.userName;
	private String passWord = ApolloConfig.passWord;

	private MqttMessage message;

	public ApolloServer() throws MqttException {
		// MemoryPersistence设置clientid的保存形式，默认为以内存保存
		client = new MqttClient(HOST, clientid, new MemoryPersistence());
		connect();
	}

	private void connect() {
		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(false);
		options.setUserName(userName);
		options.setPassword(passWord.toCharArray());
		// 设置超时时间
		options.setConnectionTimeout(10);
		// 设置会话心跳时间
		options.setKeepAliveInterval(20);
		try {
			client.setCallback(new MqttCallback() {

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
					cause.printStackTrace(System.err);
					try {
						client.connect();
					} catch (MqttSecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MqttException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("connectionLost----------");
				}
			});
			client.connect(options);
			topic = client.getTopic(TOPIC);
			topic125 = client.getTopic(TOPIC125);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void publish(MqttTopic topic, MqttMessage message) throws MqttPersistenceException, MqttException {
		MqttDeliveryToken token = topic.publish(message);
		token.waitForCompletion();
	}

	public void publish(String topicName, String msg) throws MqttPersistenceException, MqttException {
		MqttTopic t = client.getTopic(topicName);
		MqttMessage mqttMsg = new MqttMessage();
		try {
			mqttMsg.setPayload(msg.getBytes(ENCODE));
			MqttDeliveryToken token = t.publish(mqttMsg);
//			token.waitForCompletion();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws MqttException {
		ApolloServer server = new ApolloServer();
		Scanner in = new Scanner(System.in);
		server.message = new MqttMessage();
		server.message.setQos(2);
		server.message.setRetained(true);
		server.message.setPayload("给客户端124推送的信息".getBytes());
		server.publish(server.topic, server.message);

		server.message = new MqttMessage();
		server.message.setQos(2);
		server.message.setRetained(true);
		server.message.setPayload("给客户端125推送的信息".getBytes());
		server.publish(server.topic125, server.message);
		server.publish(TOPIC, "qwerjioweqjr");
		server.publish("101", "来自102的消息");
		server.publish("102", "来自101的消息");
		System.out.println("push over");
	}
	public void send101(ApolloServer server){
		
	}
}