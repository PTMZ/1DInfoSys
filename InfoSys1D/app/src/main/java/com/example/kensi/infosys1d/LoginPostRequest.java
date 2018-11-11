package com.example.kensi.infosys1d;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginPostRequest {
    public static String login(String password, String email, boolean remember) {
        try {
            String rememberString;
            if (remember) {
                rememberString = "1";
            } else {
                rememberString = "0";
            }
            return login_call_me(password, email, rememberString);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
            return "check log";
        }
    }

    public static String login_call_me(String password, String email, String rememberString) throws Exception {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("password", password);
        parameters.put("email", email);
        parameters.put("remember", rememberString);
        String data = ParameterStringBuilder.getParamsString(parameters);
        String url = "https://chocolatepie.tech/admin/login";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(data);
        out.flush();
        out.close();
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    public static String registration(String password, String email, String username, boolean vendor) {
        try {
            String vendorString;
            if (vendor) {
                vendorString = "1";
            } else {
                vendorString = "0";
            }
            return registration_call_me(password, email, username, vendorString);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
            return "check log";
        }
    }

    public static String registration_call_me(String password, String email, String username, String vendorString) throws Exception {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("password", password);
        parameters.put("email", email);
        parameters.put("username", username);
        parameters.put("is_vendor", vendorString);
        String data = ParameterStringBuilder.getParamsString(parameters);
        String url = "https://chocolatepie.tech/admin/register";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(data);
        out.flush();
        out.close();
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    //not implemented
    public static boolean shortChecker(String email, String password) {
        if (email.length() < 128 && email.length() > 5 && password.length() > 6 && password.length() < 128) {
            return true;
        } else {
            return false;
        }
    }

    //checks email and password
    public static String longChecker(String email, String password) {
        if (!emailCheck(email)) {
            return "E-mail wrong format";
        }
        if (password.length() < 6) {
            return "Password too short";
        }
        if (password.length() > 128) {
            return "Password too long";
        } else {
            return "no_error";
        }
    }

    public static boolean emailCheck(String email) {
        if (email.length() < 7 || email.length() > 128) {
            return false;
        }
        char[] emailChar = email.toCharArray();
        int[] unwanted = {32, 40, 41, 60, 62, 72, 73};
        for (char c : emailChar) {
            int ascii = (int) c;
            if (ascii < 0 || ascii > 176 || contains(unwanted, ascii)) {
                return false;
            }
        }
        if (!email.contains(".")) {
            return false;
        }
        String[] atCheck = email.split("@");
        if (atCheck.length != 2) {
            return false;
        }
        if (atCheck[0].length() < 1 || atCheck[0].length() > 64 || atCheck[1].length() < 1) {
            return false;
        }
        return true;
    }

    public static boolean contains(int[] arr, int check) {
        for (int n : arr) {
            if (n == check) {
                return true;
            }
        }
        return false;
    }

    public static String jsonParse(String serverReply) {
        if (serverReply != null) {
            try {
                JSONObject jsonObj = new JSONObject(serverReply);
                String ans = jsonObj.getString("status");
                return ans;
            } catch (final JSONException e) {
                return "Generic Error";
            }

            } else {
            return "Server Error";
        }
    }
}