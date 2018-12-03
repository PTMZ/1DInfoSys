package com.example.kensi.infosys1d.Login;

import android.content.Context;
import android.util.Log;

import com.example.kensi.infosys1d.RequestUtils;
import com.example.kensi.infosys1d.VolleyCallback;

import java.util.HashMap;
import java.util.Map;


public class LoginPostRequest {

    public static void login(final Context context, final String password, final String email, final String deviceID, final VolleyCallback callback) {
        try {
            // Define the url
            String endpoint = "/admin/login";
            String url = RequestUtils.BASE_URL + endpoint;

            // Make params
            Map<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("password", password);
            params.put("deviceID", deviceID);

            // Send form POST request
            RequestUtils.sendPostStringReq(context, url, params, callback);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
//            return "check log";
        }
    }

    public static void logout(final Context context, final VolleyCallback callback) {
        try {

            // Define the url
            String endpoint = "/admin/logout";
            String url = RequestUtils.BASE_URL + endpoint;

            // Send form GET request
            RequestUtils.sendGetStringReq(context, url, callback);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
        }
    }


    public static void registration(final Context context, final String password, final String email, final String username, final boolean vendor, final VolleyCallback callback) {
        try {
            // Convert boolean to 1 or 0
            final String strVendor = vendor ? "1" : "0";

            // Define the url
            String endpoint = "/admin/register";
            String url = RequestUtils.BASE_URL + endpoint;

            Map<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("password", password);
            params.put("username", username);
            params.put("is_vendor", strVendor);

            // Send form POST request
            RequestUtils.sendPostStringReq(context, url, params, callback);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MYAPP", "exception", e);
//            return "check log";
        }
    }

    public static String registrationChecker(String email, String username, String password0, String password1) {
        String errorMsg = longChecker(email, password0);
        if (!errorMsg.equals("no_error")) {
            return errorMsg;
        } else if (username.length() < 6) {
            return "Username too short";
        } else if (username.length() > 64) {
            return "Username too long";
        } else if (!password0.equals(password1)) {
            return "Passwords do not match";
        } else {
            return "no_error";
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

}