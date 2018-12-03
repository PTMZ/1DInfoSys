package com.example.kensi.infosys1d.OCBI_API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class APIhandler {
    String API_URL;
    HashMap<String,Object> parameters = new HashMap<>();

    public String getResponseBody(String Token){
        try {
            //Appends url with additional queries where required
            API_URL = getURLwithParams(API_URL);
            URL object = new URL(API_URL);

            //Establish http connection
            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            String readLine;

            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
//            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + Token);

            int HttpResult = con.getResponseCode();
            // Check if Response if OK -- Valid Token
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                // Read JSON
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                StringBuffer response = new StringBuffer();
                while ((readLine = in .readLine()) != null) {
                    response.append(readLine);
                }
                in.close();

                return response.toString();
            } else {
                System.out.println(con.getResponseMessage());

                return null;
            }
        } catch (Exception e) {
            System.out.println("Network Error");
            return null;
        }
    }

    public String getResponseURL(String Token){
        try {
            //Appends url with additional queries where required
            API_URL = getURLwithParams(API_URL);
            URL object = new URL(API_URL);

            HttpURLConnection con = (HttpURLConnection) object.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
//            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + Token);

            // Check if Response if OK -- Valid Token
            int HttpResult = con.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                return con.getURL().toString();
            } else {
                System.out.println(con.getContent());
                System.out.println(con.getResponseMessage());
                return null;
            }
        } catch (Exception e) {
            System.out.println("Network Error");
            return null;
        }
    }

    // Helper Function to append params to url
    public String getURLwithParams(String url){
        String out = "";
        out += url + "?";
        for (String k: parameters.keySet()){
            out += k+"="+parameters.get(k)+"&";
        }
        out += "\b";
        return out;
    }


}
