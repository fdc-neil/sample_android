package com.example.fdc_neil.machevaratalkneil.Network;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class NetworkHelper {

    public static String loginToken = "";
    private static RequestQueue mQueue;
    public Context mContext;
    public static String UNAME = "inn";
    public static String PASSWORD = "inns3";

    public interface  NetworkCallback {
        void finish(JSONObject object);
        void error(String error_code, String errorMessage);
    }

    public NetworkHelper(Context context){
        mContext = context;
        //queue
        if(mQueue == null) {
            mQueue = Volley.newRequestQueue(context);
        }
    }


    public static boolean isUserLoggedIn(){
        return NetworkHelper.loginToken != null && !NetworkHelper.loginToken.isEmpty();
    }
    public static String getLoginToken() {
        return NetworkHelper.loginToken;
    }

}
