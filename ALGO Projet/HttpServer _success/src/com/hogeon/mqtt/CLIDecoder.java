package com.hogeon.mqtt;

import java.util.Scanner;

//import org.json.simple.JSONObject;
import com.alibaba.fastjson.JSONObject;
public class CLIDecoder {

	public static void main(String[] args)
	{
		String adeunisFTDdevEUI = "0018b200000025ed";
		String oyodevEUI = "1557344E6F398120";
		AdeunisFTD adeunisFTD = new AdeunisFTD(adeunisFTDdevEUI);
        OYO oyo = new OYO(oyodevEUI);
		Scanner clavier = new Scanner(System.in);
		System.out.print("message contenu les données en Base 64 : ");
		String chaine = clavier.nextLine();
		System.out.println("FTD (F) ou OY1100 (O) ? : ");
		String device = clavier.nextLine();
		JSONObject json = null;
		switch (device)
		{
		case "F":
			json = adeunisFTD.decodeMessage(chaine);
			break;
		case "O":
			json = oyo.decodeMessage(chaine);
			break;
		default:
			System.err.println("décodeur inconnu");
		}
		System.out.println(json.toString());
	}

}
