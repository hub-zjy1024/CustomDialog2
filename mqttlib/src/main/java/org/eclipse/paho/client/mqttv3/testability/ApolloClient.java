package org.eclipse.paho.client.mqttv3.testability;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ScheduledExecutorService;

public class ApolloClient {
    // public static final String HOST = "tcp://192.168.10.65:61680";
    public static final String HOST = ApolloConfig.HOST;
    public static final String TOPIC = ApolloConfig.TOPIC;
    public static final String TOPIC125 = ApolloConfig.TOPIC125;
    private static String clientid = ApolloConfig.clientid;
    private static final String ENCODE = ApolloConfig.ENCODE;
    private MqttClient client;
    private MqttConnectOptions options;
    private String userName = ApolloConfig.userName;
    private String passWord = ApolloConfig.passWord;

    private ScheduledExecutorService scheduler;

    public ApolloClient() {

    }

    private void start() throws MqttException {
        try {
            client = new MqttClient(HOST, clientid, new MemoryPersistence());
        } catch (MqttException e) {
            e.printStackTrace();
        }
        // host为主机名，clientid即连接MQTT的客户端ID，一般以唯一标识符表示，MemoryPersistence设置clientid
        // 的保存形式，默认为以内存保存
        // MQTT的连接设置
        options = new MqttConnectOptions();
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
        options.setCleanSession(true);
        // 设置连接的用户名
        options.setUserName(userName);
        // 设置连接的密码
        options.setPassword(passWord.toCharArray());
        // 设置超时时间 单位为秒
        options.setConnectionTimeout(10);
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        options.setKeepAliveInterval(20);
        // 设置回调
        final MqttClient tempClient = client;

        MqttTopic topic = client.getTopic("clientError");
        // setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
        options.setWill(topic, "connect close".getBytes(), 2, true);

        client.connect(options);

    }

    public void setCallBack(MqttCallback callBack) {
        client.setCallback(callBack);
    }

    public void start(String uid) throws MqttException {
        clientid = String.valueOf(System.currentTimeMillis());
        start();
    }

    public void subscribe(String topic, int qos) throws MqttException {
        client.subscribe(topic, qos);
    }

    public void publish(String topicName, String msg) throws MqttException {
        MqttTopic t = client.getTopic(topicName);
        MqttMessage mqttMsg = new MqttMessage();
        try {
            mqttMsg.setPayload(msg.getBytes(ENCODE));
            MqttDeliveryToken token = t.publish(mqttMsg);
            token.waitForCompletion();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws MqttException {
        ApolloClient client = new ApolloClient();
        //		client.start();
        client.start("");
        // 订阅消息
        int[] Qos = {1};
        // client.subscribe(TOPIC, 1);

        //		 System.out.println("101Client");
        //		 client.subscribe("101", 1);
        //		 System.out.println("102Client");
        //		 client.subscribe("102", 1);
        client.publish("102", "from==给102的消息" + System.currentTimeMillis());
        //		client.publish("101", "给101的消息" + System.currentTimeMillis());
        //		client.client.disconnect();
        //
    }
}
