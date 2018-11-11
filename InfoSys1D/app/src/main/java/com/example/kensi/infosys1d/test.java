package com.example.kensi.infosys1d;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.simple.parser.JSONParser;

public class test {
    public static void main(String[] args) throws Exception {
        String serverReply = LoginPostRequest.login("asdadasd","asdnsnioad@uiasuias.com",true);
        System.out.println(serverReply.toCharArray()[11]);
    }
}
