package com.hogeon.mqtt;

import java.util.Base64;
import java.util.Date;
import java.util.Base64.Decoder;

//import org.json.simple.JSONObject;
import com.alibaba.fastjson.JSONObject;

/** decodes the LoRa data message of the Adeunis FTD
 * and returns a JSON object
 * 
 * @author Gilles Carpentier
 *
 */
public class AdeunisFTD
{
	private Decoder b64d;
	private String devEUI;

	public AdeunisFTD(String devEUI)
	{
		this.devEUI = devEUI;
		b64d = Base64.getDecoder();		
	}
	public JSONObject decodeMessage(String chaine)
	{
		JSONObject json = new JSONObject();
		json.put("time", (new Date()).toString());
		json.put("devEUI", devEUI);
		json.put("description", "Adeunis FTD");
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

			byte statut = octets[0];
			String indicateurs = Integer.toBinaryString((statut & 0xFF) + 0x100).substring(1);

			boolean presenceTemperature = false;
			boolean declenchementAccelerometre = false;
			boolean declenchementBoutonPoussoir = false;
			boolean presenceGPS = false;
			boolean upLinkCounter = false;
			boolean downLinkCounter = false;
			boolean presenceBatterie = false;
			boolean presenceSNR = false;



			presenceTemperature = true;//indicateurs.charAt(0) == '1';


			declenchementAccelerometre = true;//indicateurs.charAt(1) == '1';
			declenchementBoutonPoussoir = true;//indicateurs.charAt(2) == '1';
			presenceGPS = true;//indicateurs.charAt(3) == '1';
			upLinkCounter = true;//indicateurs.charAt(4) == '1';
			downLinkCounter = true;//indicateurs.charAt(5) == '1';
			presenceBatterie = true;//indicateurs.charAt(6) == '1';
			presenceSNR = true;//indicateurs.charAt(7) == '1';
			/*System.out.println("Présence température : " + presenceTemperature);
			System.out.println("Declenchement Accelerometre : " + declenchementAccelerometre);
			System.out.println("Declenchement Bouton Poussoir : " + declenchementBoutonPoussoir);
			System.out.println("Présence GPS : " + presenceGPS);
			System.out.println("Uplink counter : " + upLinkCounter);
			System.out.println("Downlink counter : " + downLinkCounter);
			System.out.println("Présence batterie : " + presenceBatterie);
			System.out.println("Présence SNR : " + presenceSNR);*/
			int numeroOctet = 1;
			int unsigned = 0;
			if (presenceTemperature)
			{
				//System.out.println("1");

				test t = new test();
				if(test.lastvalue>=0.8){

					t.randowdown();
				}else if(test.lastvalue<=0.3){

					t.randowup();
				}else{
					t.randowtest();
				}



				json.put("temperature", test.lastvalue);//octets[numeroOctet]);
				numeroOctet++;
			}
//			if (presenceGPS)
//			{
//
//				System.out.println("2");
//
//				int degres = ((octets[numeroOctet] & 240) >> 4) * 10 + (octets[numeroOctet] & 15);
//				int minutes = ((octets[numeroOctet + 1] & 240) >> 4)  * 10 + (octets[numeroOctet + 1] & 15);
//				int secondes = (((octets[numeroOctet + 2] & 240) >> 4) * 100) + (octets[numeroOctet + 2] & 15) * 10 + ((octets[numeroOctet + 3] & 240) >> 4);
//				char hemisphere ='N';
//				if ((octets[numeroOctet + 3] & 1) == 1)  hemisphere ='S';
//				String latitude = "" +  degres + "°" + minutes + "," + secondes + hemisphere;
//				json.put("latitude", latitude);
//				numeroOctet = numeroOctet + 4;
//				degres = (octets[numeroOctet] & 240) * 10 + (octets[numeroOctet] & 15);
//				minutes = (octets[numeroOctet + 1] & 240) * 10 + (octets[numeroOctet + 1] & 15);
//				degres = ((octets[numeroOctet] & 240) >> 4) * 100 + (octets[numeroOctet] & 15) * 10 + ((octets[numeroOctet + 1] & 240) >> 4);
//				minutes = (octets[numeroOctet + 1] & 15) * 10 + ((octets[numeroOctet + 2] & 240) >> 4);
//				secondes = (octets[numeroOctet + 2] & 15) * 10 + ((octets[numeroOctet + 3] & 240) >> 4);
//				hemisphere ='E';
//				if ((octets[numeroOctet + 3] & 1) == 1)  hemisphere ='W';
//				String longitude = "" + degres + "°" + minutes + "," + secondes + hemisphere;
//				json.put("longitude", longitude);
//				numeroOctet = numeroOctet + 4;
//				numeroOctet = numeroOctet + 1;
//			}

//			if (upLinkCounter)
//			{
//				System.out.println("3");
//
//				unsigned = (octets[numeroOctet] & 0xff);
//				json.put("upLinkCounter", unsigned);
//				numeroOctet++;
//			}
//			if (downLinkCounter)
//			{
//
//				System.out.println("4");
//
//				unsigned = (octets[numeroOctet] & 0xff);
//				json.put("downLinkCounter", unsigned);
//				numeroOctet++;
//			}
//			if (presenceBatterie)
//			{
//
//				System.out.println("4");
//
//				unsigned = (octets[numeroOctet] & 0xff);
//				int niveau =  (unsigned * 256);
//				unsigned = (octets[numeroOctet + 1] & 0xff);
//				niveau = niveau	+ unsigned;
//				json.put("batteryLevel", niveau);
//				numeroOctet = numeroOctet + 2;
//			}

//			if (presenceSNR)
//			{
//
//				System.out.println("5");
//
////				unsigned = (octets[octets.length - 1] & 0xff);
//				json.put("snr", octets[octets.length - 1]);
//			}
		}

		//System.out.println(json.toJSONString());
		return json;
	}
}
