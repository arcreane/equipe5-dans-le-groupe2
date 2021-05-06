package com.hogeon.mqtt;

import com.google.gson.Gson;
//import com.google.gson.JsonParser;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
//import org.json.simple.JSONArray;
import com.alibaba.fastjson.JSONArray;
//import org.json.simple.JSONObject;
import com.alibaba.fastjson.JSONObject;
import org.json.simple.parser.JSONParser;
import com.alibaba.fastjson.parser.*;
import com.alibaba.fastjson.*;

import org.json.simple.parser.ParseException;
import org.knowm.xchart.XChartPanel;
//import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * A sample application that demonstrates how to use the Paho MQTT v3.1 Client blocking API.
 */
public class Subscriber implements MqttCallback {

    private final int qos = 1;
    private String topic_reception = "test11";//"application/+/node/+/rx";
    // ************************* ADEUNIS FTD ***********************
    private String adeunisFTDdevEUI = "0018b200000025ed";
    private String adeunisFTD_topic_reception = "application/1/node/" + adeunisFTDdevEUI + "/rx";
    private String adeunisFTD_topic_emission = "sendback";//"application/1/node/" + adeunisFTDdevEUI + "/tx";
    private AdeunisFTD adeunisFTD;
    private JSONObject trameAdeunis;
    private static JPanel chartPanelFTD;
   
    private static ArrayList<Integer> xDataFTD;
	private static ArrayList<Integer> temperatureDataFTD;
	private static Integer compteurFTD = 0; 
    
 // ************************* ONYIELF OY1100 *********************** 
    private String oyodevEUI = "1557344e6f398120";
    private String oyo_topic_reception = "application/3/node/" + oyodevEUI + "/rx";
    private OYO oyo;
    private JSONObject trameOYO;
    private static JPanel chartPanelOYO;
     
    private static ArrayList<Integer> xDataOYO;
	private static ArrayList<Double> temperatureDataOYO;
	private static ArrayList<Double> humiditeDataOYO;
	private static Integer compteurOYO = 0; 
   
    private MqttClient client;
    private static Encoder b64 = Base64.getEncoder();
    private static Subscriber s;
      
    private JSONParser parser;




    public Subscriber(String uri) throws MqttException, URISyntaxException {

        this(new URI(uri));
        //System.out.println("test:"+uri);
        adeunisFTD = new AdeunisFTD(adeunisFTDdevEUI);
        oyo = new OYO(oyodevEUI);
        parser = new JSONParser();
     // Create Chart for FTD


        //this.chartFTD = new XYChartBuilder().width(600).height(400).title("LoRa Adeunis FTD").xAxisTitle("X").yAxisTitle("Y").build();

        // Customize Chart


        //chartFTD.getStyler().setLegendPosition(LegendPosition.InsideNE);


        //chartFTD.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);
     // Series
        //chartFTD.addSeries("temperature", new double[] {0}, new double[] {0});


        xDataFTD = new  ArrayList<Integer>();
        temperatureDataFTD = new  ArrayList<Integer>();
        
//      Create Chart for OYO


//        this.chartOYO = new XYChartBuilder().width(600).height(400).title("OYO 1100").xAxisTitle("X").yAxisTitle("Y").build();

        // Customize Chart
//        chartOYO.getStyler().setLegendPosition(LegendPosition.InsideNE);
//        chartOYO.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);
     // Series
//        chartOYO.addSeries("temperature", new double[] {0}, new double[] {0});
//        chartOYO.addSeries("humidite", new double[] {0}, new double[] {0});
        xDataOYO = new  ArrayList<Integer>();
        temperatureDataOYO = new  ArrayList<Double>();
        humiditeDataOYO = new  ArrayList<Double>();
    }

    public Subscriber(URI uri) throws MqttException {
        String host = String.format("tcp://%s:%d", uri.getHost(), uri.getPort());
        //System.out.println(host);
        String[] auth = this.getAuth(uri);
//        String username = auth[0];
//       String password = auth[1];
        String clientId = "MQTT-Java-Example";
        if (!uri.getPath().isEmpty()) {
            this.adeunisFTD_topic_reception = uri.getPath().substring(1);
        }

        MqttConnectOptions conOpt = new MqttConnectOptions();
        conOpt.setCleanSession(true);
//        conOpt.setUserName(username);
//        conOpt.setPassword(password.toCharArray());

        this.client = new MqttClient(host, clientId, new MemoryPersistence());
        this.client.setCallback(this);
        this.client.connect(conOpt);
        this.client.subscribe(this.topic_reception, qos);
 //       this.client.subscribe(this.adeunisFTD_topic_reception, qos);
        System.out.println("connected to mosquitto broker");
    }

    private String[] getAuth(URI uri) {
        String a = uri.getAuthority();
        String[] first = a.split("@");
        return first[0].split(":");
    }

    public void sendMessage(String payload) throws MqttException {


        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(qos);
        this.client.publish(this.adeunisFTD_topic_emission, message); // Blocking publish
        //System.out.println("message envoy茅");
    }

    /**
     * @see MqttCallback#connectionLost(Throwable)
     */
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost because: " + cause);
        System.exit(1);
    }

    /**
     * @see MqttCallback#deliveryComplete(IMqttDeliveryToken)
     */
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    /**
     * @see MqttCallback#messageArrived(String, MqttMessage)
     */
    public void messageArrived(String topic, MqttMessage message) throws MqttException, SQLException, ClassNotFoundException {

        System.out.println(String.format("[%s] %s", topic, new String(message.getPayload())));
        String chaine = new String(message.getPayload());


        //鎴愬姛鎺ュ彈鏁版嵁锛�

        //娴嬭瘯
        JSONObject json = new JSONObject(true);
        json.put("name","xxgnb");
        json.put("devEUI","0018b200000025ed");
        json.put("data","200");

        String devEUI = "";
        try {
            /* 杞崲鎴恓son鏁版嵁鏍煎紡 */
			devEUI = json.get("devEUI").toString();
			System.out.println(devEUI);

			if (devEUI == null) return;
		} catch (JSONException e) {
			e.printStackTrace();
		}
        devEUI = devEUI.trim();
        System.out.println("devEUI = " + devEUI);
        if (devEUI.compareTo(adeunisFTDdevEUI) == 0)
        {
        	System.out.println("*** ADEUNIS FTD ***");
			trameAdeunis =  adeunisFTD.decodeMessage(chaine);

			String s1 = trameAdeunis.get("temperature").toString();

			jdbctest  test = new jdbctest();
			test.update(s1);
			test.deposit();


			System.out.println(trameAdeunis.toJSONString());

			compteurFTD++;
			xDataFTD.add(compteurFTD);

			//娴嬭瘯娉ㄩ噴鎺変簡
			//Byte temperatureB = (Byte) trameAdeunis.get("temperature");
			//Integer temperatureI = new Integer(temperatureB);



//			temperatureDataFTD.add(temperatureI);
//			chartFTD.updateXYSeries("temperature", xDataFTD, temperatureDataFTD, null);
//			chartPanelFTD.revalidate();
//			chartPanelFTD.repaint();


			String chaineCompteur = "Message " + compteurFTD + " envoye";
			byte[] messageEnvoye = b64.encode(chaineCompteur.getBytes());
			String chaineEnvoyee = new String(messageEnvoye);
			String trameTest = "{\"confirmed\": true, \"data\": \"" + chaineEnvoyee + "\", \"devEUI\": \""
					+ adeunisFTDdevEUI + "\", \"fPort\": 11, \"reference\": \"zorglub\"}";

			//鍦ㄦ澶剆end浜唌essage

			//s.sendMessage(trameTest);
			return;
		}
        if(devEUI.compareTo(oyodevEUI) == 0)
        {
        	System.out.println("*** OYO-1100 ***");
        	trameOYO = oyo.decodeMessage(chaine);
        	System.out.println(trameOYO.toJSONString());
        	compteurOYO++ ;
        	xDataOYO.add(compteurOYO);
        	compteurOYO++ ;
        	xDataOYO.add(compteurOYO);
        	compteurOYO++ ;
        	xDataOYO.add(compteurOYO);
        	JSONArray array = (JSONArray) trameOYO.get("data");
        	JSONObject mesure4 = (JSONObject) array.get(0);
        	double temp4 = (double) mesure4.get("temperature");
        	temperatureDataOYO.add(temp4);
        	double hum4 = (double) mesure4.get("humidite");
        	humiditeDataOYO.add(hum4);
        	JSONObject mesure2 = (JSONObject) array.get(1);
        	double temp2 = (double) mesure2.get("temperature");
        	temperatureDataOYO.add(temp2);
        	double hum2 = (double) mesure2.get("humidite");
        	humiditeDataOYO.add(hum2);
        	JSONObject mesure0 = (JSONObject) array.get(2);
        	double temp0 = (double) mesure0.get("temperature");
        	temperatureDataOYO.add(temp0);
        	double hum0 = (double) mesure0.get("humidite");
        	humiditeDataOYO.add(hum0);

//        	chartOYO.updateXYSeries("temperature", xDataOYO, temperatureDataOYO, null);
//        	chartOYO.updateXYSeries("humidite", xDataOYO, humiditeDataOYO, null);


			chartPanelOYO.revalidate();
			chartPanelOYO.repaint();
			return;
        }
        else
        {
        	System.out.println(devEUI + " != " + oyodevEUI);
        }
        System.out.println("device inconnu");
    }
    
    public static void Run() throws MqttException, URISyntaxException, SQLException, ClassNotFoundException, InterruptedException{
    	s = new Subscriber("tcp://127.0.0.1:1883");

        for(;;) {
            jdbctest check = new jdbctest();
            int p = check.select();

            if(p==1) {
                s.sendMessage("need to close");

            }
            if(p==-1){
                s.sendMessage("need to open");
            }

            Thread.currentThread().sleep(1000);
        }
    }
    
    public static void main(String[] args) throws MqttException, URISyntaxException, SQLException, ClassNotFoundException, InterruptedException {
//        s = new Subscriber("tcp://127.0.0.1:1883");
//
//        for(;;) {
//            jdbctest check = new jdbctest();
//            int p = check.select();
//
//            if(p==1) {
//                s.sendMessage("need to close");
//
//            }
//            if(p==-1){
//                s.sendMessage("need to open");
//            }
//
//            Thread.sleep(10000);
//        }



        //srv-lora.isep.fr
        

        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {

//          @Override
//          public void run() {
//
//            // Create and set up the window.
//            JFrame frame = new JFrame("LoRa");
//            frame.setLayout(new GridLayout(2, 2));
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//            // chart
// //           chartPanelFTD = new XChartPanel<XYChart>(chartFTD);
// //           frame.add(chartPanelFTD, BorderLayout.CENTER);
//            frame.add(chartPanelFTD);
// //           chartPanelOYO = new XChartPanel<XYChart>(chartOYO);
//            frame.add(chartPanelOYO);
//
//            // Display the window.
//            frame.pack();
//            frame.setVisible(true);
//          }
 //       });
    }
}

