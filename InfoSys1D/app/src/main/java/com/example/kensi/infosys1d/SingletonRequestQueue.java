package com.example.kensi.infosys1d;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

/*
    Used to ensure that only one RequestQueue is used.
 */
public class SingletonRequestQueue {

    // Define all the variables
    private static SingletonRequestQueue mInstance;
    private Context mContext;
    private RequestQueue mRequestQueue;
    private Response.ErrorListener mErrorListener;


    // Initialise the instance of the class
    private SingletonRequestQueue(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
        mErrorListener = getErrorListener();
    }


    // Return the class instance
    // Create the instance and save it if not found
    public static synchronized SingletonRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SingletonRequestQueue(context);
        }
        return mInstance;
    }


    // Return the RequestQueue instance
    // Create the instance and save it if not found
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }


    // Return the Response.ErrorListener instance
    // Create the instance and save it if not found
    public Response.ErrorListener getErrorListener() {
        if (mErrorListener == null) {
            mErrorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof NetworkError) {
                        Toast.makeText(mContext, "No network available", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            };
        }
        return mErrorListener;
    }
}
