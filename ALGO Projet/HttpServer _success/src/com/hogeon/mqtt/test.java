package com.hogeon.mqtt;

import com.alibaba.fastjson.JSON;
import org.json.simple.parser.JSONParser;
//import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import com.alibaba.fastjson.*;
import com.alibaba.fastjson.parser.*;

import java.security.PrivateKey;
import java.util.Random;

import java.util.LinkedHashMap;

public class test {

    public static double lastvalue=0.5;
    public Random r = new Random();

    public double randowtest(){



        lastvalue = (r.nextDouble()*0.1+0.95)*lastvalue;

        return lastvalue;

    }

    public double randowdown(){

        lastvalue = (r.nextDouble()*0.2+0.75)*lastvalue;

        return lastvalue;

    }

    public double randowup(){

        lastvalue = (r.nextDouble()*0.2+1.05)*lastvalue;

        return lastvalue;

    }





    public static void main(String[] args) throws ParseException {
        JSONObject test=new JSONObject(true);

        test.put("name","yhy");
        test.put("data","18");


        String str = test.toJSONString();
        System.out.println(str);
        String [] s = str.split("\"data\":\"");

        System.out.println(s[0]);
        System.out.println(s[1]);

        test.clear();
    }

}
