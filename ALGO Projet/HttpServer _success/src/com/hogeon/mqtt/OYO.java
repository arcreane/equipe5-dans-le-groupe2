package com.hogeon.mqtt;

import java.util.Base64;
import java.util.Date;
import java.util.Base64.Decoder;
import java.util.BitSet;

//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class OYO
{
	private Decoder b64d;
	private JSONParser parser;
	private String devEUI;
	
	public OYO(String devEUI)
	{
		this.devEUI = devEUI;
		b64d = Base64.getDecoder();
		parser = new JSONParser();
	}
	public JSONObject decodeMessage(String chaine)
	{
		String time = (new Date()).toString();
		JSONObject json = new JSONObject();
		json.put("time", (new Date()).toString());
		json.put("devEUI", devEUI);
		json.put("description", "OYO");
		
		JSONArray array = new JSONArray();
		String[] trame = chaine.split("\"data\":\"");
		if (trame.length != 2)
		{
			System.err.println("erreur format de trame");
		}
		else
		{
			String lora = trame[1].substring(0,  trame[1].length() - 2);		
			byte[] octets = b64d.decode(lora);
			int nbOctets = octets.length;
			System.out.println("nombre d'octets = " + nbOctets);
			if (nbOctets == 9)
			{
				JSONObject mesure4 = new JSONObject();
//				 BitSet bitset = BitSet.valueOf(octets);
//				 System.out.println("nombre de bits = " + bitset.length());
				 /*BitSet tm4ABC = new BitSet(15);
				 tm4ABC.set(0, bitset.get(0));
				 tm4ABC.set(1, bitset.get(1));
				 tm4ABC.set(2, bitset.get(2));
				 tm4ABC.set(3, bitset.get(17));*/
				 int tm4AB = octets[0];
				 int tm4C = octets[2] >> 4;
				 int tm4 = tm4AB * 16 + tm4C;
				 double temp4 = tm4 * 0.1;
				 mesure4.put("temperature", temp4);
				 int hm4DE = octets[1];
				 int hm4F = octets[2] & 0x015;
				 int hm4 = hm4DE  * 16 + hm4F;
				 double humidite4 = hm4 * 0.1;
				 mesure4.put("humidite", humidite4);
				 array.add(mesure4);
				 
				 JSONObject mesure2 = new JSONObject();
				 int tm2AB = octets[3];
				 int tm2C = octets[5] >> 4;
				 int tm2 = tm2AB * 16 + tm2C;
				 double temp2 = tm2 * 0.1;
				 mesure2.put("temperature", temp2);
				 int hm2DE = octets[4];
				 int hm2F = octets[5] & 0x015;
				 int hm2 = hm2DE  * 16 + hm2F;
				 double humidite2 = hm2 * 0.1;
				 mesure2.put("humidite", humidite2);
				 array.add(mesure2);
				 
				 JSONObject mesure0 = new JSONObject();
				 int tm0AB = octets[6];
				 int tm0C = octets[8] >> 4;
				 int tm0 = tm0AB * 16 + tm0C;
				 double temp0 = tm0 * 0.1;
				 mesure0.put("temperature", temp0);
				 int hm0DE = octets[7];
				 int hm0F = octets[8] & 0x015;
				 int hm0 = hm0DE  * 16 + hm0F;
				 double humidite0 = hm0 * 0.1;
				 mesure0.put("humidite", humidite0);
				 array.add(mesure0);
			}
			else
			{
				for (int i = 0; i < octets.length; i++)
				{
					array.add(octets[i]);
				}
			}
		}
		json.put("data", array);
		return json;
	}
}
