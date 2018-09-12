package com.example.fdc_neil.machevaratalkneil.Network;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

/**
 * Created by ASUS on 4/23/2018.
 */

public class ImageUploadTask extends AsyncTask<String, Integer, JSONObject> {

    private NetworkHelper.NetworkCallback mAsyncCallback = null;
    private boolean authRequired = false;
    private Bitmap mBitmap;
    private String error;

    public void setAuthRequired(boolean authRequired) {
        this.authRequired = authRequired;
    }

    public ImageUploadTask(Bitmap bitmap, NetworkHelper.NetworkCallback callBack) {
        mBitmap = bitmap;
        mAsyncCallback = callBack;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);

        if(result == null){
            mAsyncCallback.error("", error);
            return;
        }

        if(result.optString("result").equals("OK")){
            mAsyncCallback.finish(result);
        } else {
            mAsyncCallback.error(result.optString("error_code"), result.optString("description"));
        }
    }

    @Override
    protected JSONObject doInBackground(String... str) {
        String url = str[0];
        try {
            HttpPost httpPost = new HttpPost(url);
            DefaultHttpClient client = new DefaultHttpClient();

            String credentials = NetworkHelper.UNAME+":"+ NetworkHelper.PASSWORD;
            String basicAuth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            httpPost.setHeader("Authorization", basicAuth);

            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.setCharset(Charset.forName("UTF-8"));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 95, baos);
            byte[] imageBytes = baos.toByteArray();
            entity.addBinaryBody("profile_image", imageBytes, ContentType.create("image/jpeg"), "upload.jpg");

            ContentType textContentType = ContentType.APPLICATION_JSON;

            if (authRequired) {
                if(NetworkHelper.isUserLoggedIn()){
                    entity.addTextBody("login_token", NetworkHelper.getLoginToken(), textContentType);
                }
            }

            httpPost.setEntity(entity.build());

            HttpResponse response = client.execute(httpPost);

            HttpEntity responseEntity = response.getEntity();
            String jsonString = EntityUtils.toString(responseEntity, "UTF-8");

            return new JSONObject(jsonString);
        } catch (Exception e) {
            error = e.getMessage();
            e.printStackTrace();
        }
        return null;
    }

}
