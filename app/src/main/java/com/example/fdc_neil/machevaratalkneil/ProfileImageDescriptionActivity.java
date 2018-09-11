package com.example.fdc_neil.machevaratalkneil;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.refactor.lib.colordialog.PromptDialog;

public class ProfileImageDescriptionActivity extends AppCompatActivity {

    @BindView(R.id.imgTopPic)
    ImageView imgTopPic;

    @BindView(R.id.imgAdd)
    ImageView imgAdd;

    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    Intent intent;
    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 2;
    String random_profile_image_url, email, password;
    File finalFile = null;
    Uri selectedImageUri = null;

    public static final String PROFILE_IMAGE_URL = "http://pre-macherie.stg.inn.inmgt.com/mobile_api/broadcaster/register/profile_image";
    private static RequestQueue mQueue;
    public static String IDFA = "hereismyapiidfa";
    public static int TERMINAL_TYPE = 4;
    public static int TIMEOUT_MILLISECONDS = 30 * 1000;
    public static String UNAME = "inn";
    public static String PASSWORD = "inns3";
    public static String LOGINTOKEN = "hereismyapitoken";
    public static int profile_image_id= 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_profile_image_description);
        ButterKnife.bind(this);

        random_profile_image_url = getIntent().getStringExtra("RANDOM_PROFILE_IMAGE");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        imgTopPic.setImageURI(Uri.parse(random_profile_image_url));

        if (imgTopPic.getDrawable() == null){
            btnSubmit.setEnabled(false);
            btnSubmit.setBackgroundResource(R.drawable.bg_login_gray_button);
        } else{
            btnSubmit.setEnabled(true);
            btnSubmit.setBackgroundResource(R.drawable.bg_button_blue_green_big);
        }
    }

    public static RequestQueue getRequestMQueue() {
        return ProfileImageDescriptionActivity.mQueue;
    }

    @OnClick(R.id.btnSubmit)
    void clickSubmit(){

        intent = new Intent(ProfileImageDescriptionActivity.this, ProfileDetailsRegistrationActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        startActivity(intent);

        /*JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.putOpt("profile_image", random_profile_image_url);
        }catch (Exception e){
            e.printStackTrace();
        }

        Log.d("PROFILE_IMAGE", jsonObject.toString());
        attempSendImage(jsonObject);*/
    }

    private void attempSendImage(JSONObject jsonObject) {

        if (mQueue == null){
            mQueue = Volley.newRequestQueue(getApplicationContext());
        }

        try {
            // to prevent login token to be null so the room list would not return param error
            if (LOGINTOKEN == null) {
                LOGINTOKEN = "";
            }
            jsonObject.putOpt("login_token", LOGINTOKEN);
            jsonObject.putOpt("idfa", IDFA);
            jsonObject.putOpt("terminal", TERMINAL_TYPE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("JSON OBJECTS", jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, PROFILE_IMAGE_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("SUCCESS RESPONSE", response.toString());

                        if (response.optString("result_code").equals("OK")){
                            intent = new Intent(ProfileImageDescriptionActivity.this, ProfileDetailsRegistrationActivity.class);
                            intent.putExtra("email", email);
                            intent.putExtra("password", password);
                            startActivity(intent);
                        }else{
                            new PromptDialog(ProfileImageDescriptionActivity.this)
                                    .setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                                    .setAnimationEnable(true)
                                    .setTitleText(R.string.error)
                                    .setContentText(response.optString("description"))
                                    .setPositiveListener(R.string.ok, new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog promptDialog) {
                                            intent = new Intent(ProfileImageDescriptionActivity.this, ProfileDetailsRegistrationActivity.class);
                                            intent.putExtra("email", email);
                                            intent.putExtra("password", password);
                                            startActivity(intent);
                                        }
                                    }).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR RESPONSE", error.toString());
                    }
                });
        jsonObjectRequest.setShouldCache(false);

        DefaultRetryPolicy policy = new DefaultRetryPolicy(TIMEOUT_MILLISECONDS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        getRequestMQueue().add(jsonObjectRequest);
    }

    @OnClick(R.id.imgTopPic)
    void topPicClick(){

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfileImageDescriptionActivity.this);

        imgTopPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_upload_selection, null);
                dialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                TextView tvCamera =  dialogView.findViewById(R.id.tv_camera);
                TextView tvGallery = dialogView.findViewById(R.id.tv_gallery);
                TextView tvCancel =  dialogView.findViewById(R.id.tv_cancel);

                tvCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(ProfileImageDescriptionActivity.this
                                , Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ProfileImageDescriptionActivity.this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
                        }else{
                            cameraIntent();
                            alertDialog.dismiss();
                        }
                    }
                });

                tvGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        galleryIntent();
                    }
                });

                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_upload_selection, null);
                dialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                TextView tvCamera =  dialogView.findViewById(R.id.tv_camera);
                TextView tvGallery = dialogView.findViewById(R.id.tv_gallery);
                TextView tvCancel =  dialogView.findViewById(R.id.tv_cancel);


                tvCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(ProfileImageDescriptionActivity.this
                                , Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ProfileImageDescriptionActivity.this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
                        }else{
                            cameraIntent();
                            alertDialog.dismiss();
                        }
                    }
                });
                tvGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        galleryIntent();
                    }
                });

                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }
    private void galleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent1, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

            BitmapShader shader = new BitmapShader(bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Paint paint = new Paint();
            paint.setShader(shader);
            paint.setAntiAlias(true);
            Canvas c = new Canvas(circleBitmap);
            c.drawCircle(bitmap.getWidth()/(float)2, bitmap.getHeight()/(float)2, bitmap.getWidth()/(float)2, paint);

            imgTopPic.setImageBitmap(circleBitmap); // W O R K I N G

           /* Bitmap photo = (Bitmap) data.getExtras().get("data");
            imgTopPic.setImageBitmap(photo);*/ // W O R K I N G

        }
        else if (requestCode == SELECT_FILE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            imgTopPic.setImageURI(selectedImage);
        }

        if (imgTopPic.getDrawable() != null){
            btnSubmit.setEnabled(true);
            btnSubmit.setBackgroundResource(R.drawable.bg_button_blue_green_big);
        }
    }

}
