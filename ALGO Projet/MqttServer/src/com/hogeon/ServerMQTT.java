package com.hogeon;


 
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
//import org.json.simple.JSONObject;
import com.alibaba.fastjson.*;

/**
 *
 * Title:Server
 * Description: 鏈嶅姟鍣ㄥ悜澶氫釜瀹㈡埛绔帹閫佷富棰橈紝鍗充笉鍚屽鎴风鍙悜鏈嶅姟鍣ㄨ闃呯浉鍚屼富棰�
 * @author admin
 * 2017骞�2鏈�10鏃ヤ笅鍗�17:41:10
 */
public class ServerMQTT {
 
    //tcp://MQTT瀹夎鐨勬湇鍔″櫒鍦板潃:MQTT瀹氫箟鐨勭鍙ｅ彿
    public static final String HOST = "tcp://localhost:1883";
    //瀹氫箟涓�涓富棰�
    public static final String TOPIC = "test11";//"application/+/node/+/rx";

    private final int qos = 1;

    //瀹氫箟MQTT鐨処D锛屽彲浠ュ湪MQTT鏈嶅姟閰嶇疆涓寚瀹�
    private static final String clientid = "server11";

    private MqttClient client;
//    private Subscriber client;
    private MqttTopic topic11;
    private String userName = "mosquitto";
    private String passWord = "";

    private MqttMessage message;

    private JSONObject mes=new JSONObject(true);

    /**
     * 鏋勯�犲嚱鏁�
     * @throws MqttException
     */
    public ServerMQTT() throws MqttException {
        // MemoryPersistence璁剧疆clientid鐨勪繚瀛樺舰寮忥紝榛樿涓轰互鍐呭瓨淇濆瓨
        client = new MqttClient(HOST, clientid, new MemoryPersistence());
        //client.subscribe("test11");
        connect();
    }

    /**
     *  鐢ㄦ潵杩炴帴鏈嶅姟鍣�
     */
    private void connect() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        // 璁剧疆瓒呮椂鏃堕棿
        options.setConnectionTimeout(10);
        // 璁剧疆浼氳瘽蹇冭烦鏃堕棿
        options.setKeepAliveInterval(20);
        try {


            client.setCallback(new PushCallback());//Subscriber("tcp://127.0.0.1:1883"));
            client.connect(options);

            topic11 = client.getTopic(TOPIC);
            client.subscribe("sendback",qos);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param topic
     * @param message
     * @throws MqttPersistenceException
     * @throws MqttException
     */
    public void publish(MqttTopic topic , MqttMessage message) throws MqttPersistenceException,
            MqttException {
        MqttDeliveryToken token = topic.publish(message);
        token.waitForCompletion();
        System.out.println("message is published completely! "
                + token.isComplete());
    }

    /**
     *  鍚姩鍏ュ彛
     * @param args
     * @throws MqttException
     */
    public static void main(String[] args) throws MqttException, InterruptedException {
        ServerMQTT server = new ServerMQTT();

        server.message = new MqttMessage();
        server.message.setQos(1);
        server.message.setRetained(true);

        server.mes.put("name","hogeon");
        server.mes.put("devEUI","0018b200000025ed");
        server.mes.put("data","200");


        for(;;) {
            server.message.setPayload(server.mes.toJSONString().getBytes());
            server.publish(server.topic11, server.message);
            //System.out.println(server.message.isRetained() + "------ratained鐘舵��");
            Thread.sleep(1000);
        }

    }
}