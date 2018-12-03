package com.example.kensi.infosys1d.OCBI_API;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class Utils {

    public static final String UTILS_TAG = "UtilsTag";

    public static InputStream getInputStream(URL url){

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
        }catch(IOException e) {
            e.printStackTrace();
            inputStream = null;
        }

        return inputStream;

    }

    public static String getJson(URL url){

        return convertStreamToString(getInputStream(url));
    }

    public static String convertStreamToString(InputStream inputStream){

        BufferedReader reader = null;
        String outString;

        StringBuffer buffer = new StringBuffer();
        if (inputStream == null) {
            // Nothing to do.
            return null;
        }
        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        try{
            while ((line = reader.readLine()) != null) {
   /* Since it's JSON, adding a newline isn't necessary (it won't affect
      parsing) but it does make debugging a *lot* easier if you print out the
      completed buffer for debugging. */
            buffer.append(line + "\n");
            }

        } catch( IOException e){
            e.printStackTrace();

        }
        if (buffer.length() == 0) {
            // Stream was empty.  No point in parsing.
            return null;
        }
        outString = buffer.toString();
        Log.i(UTILS_TAG,outString);
        return outString;

    }



    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean haveNetwork = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        Log.i(UTILS_TAG, "Active Network: " + haveNetwork);
        return haveNetwork;
    }

    public static Bitmap convertStreamToBitmap (InputStream inputStream){

        return BitmapFactory.decodeStream(inputStream);

    }

    public static Bitmap getBitmap(URL url){

        return convertStreamToBitmap(getInputStream(url));
    }

    public static HashMap<String, String> getParams(String url){
        HashMap<String, String> out = new HashMap<>();
        String[] split1 = new String[0];
        if(url.contains("#")){
            split1 = url.split("#");
        } else if (url.contains("?")){
            split1 = url.split("\\?"); // ? is a meta character and we would have to use \\?
        }

        if (split1.length != 0) {
            String params = split1[1];
            if(params.contains("&")) {
                String[] split2 = params.split("&");
                for (String a : split2){
                    if (a.contains("=")){
                        String[] split3 = a.split("=");
                        try {
                            out.put(split3[0], split3[1]);
                        } catch(ArrayIndexOutOfBoundsException e){
                            Log.e("error",a);
                        }
                    }
                }
            } else {
                if (params.contains("=")){
                    String[] split3 = params.split("=");
                    out.put(split3[0],split3[1]);
                }
            }

        }
        return out;
    }

    public static String printParams(HashMap<String, String> params){
        String out = "";
        for (String key : params.keySet()){
            out += key + ": " +  params.get(key) + "\n";
        }
        if (out != "") {
            out += "\b";
        }
        return out;
    }

}
