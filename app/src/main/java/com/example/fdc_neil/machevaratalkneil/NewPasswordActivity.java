package com.example.fdc_neil.machevaratalkneil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewPasswordActivity extends AppCompatActivity {

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;

    @BindView(R.id.ivViewPassword)
    ImageView ivViewPassword;

    @BindView(R.id.ivViewPassword1)
    ImageView ivViewPassword1;

    @BindView(R.id.tvErrorMsg)
    TextView tvErrorMsg;

    @BindView(R.id.btnResetPassword)
    Button btnResetPassword;

    String etPasswordText, etConfirmPasswordText = null;

    private static final String RESET_PASSWORD_URL = "http://pre-macherie.stg.inn.inmgt.com/mobile_api/broadcaster/forgot_password/reset";
    private static RequestQueue requestQueue;
    public static String LOGINTOKEN = "hereismyapitoken";
    public static String IDFA = "hereismyapiidfa";
    public static int TERMINAL_TYPE = 4;
    public static int TIMEOUT_MILLISECONDS = 30 * 1000;
    public static int BROADCASTER_ID = 294;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);


        ButterKnife.bind(this);

        ivViewPassword.setImageResource(R.mipmap.eye_closed);
        ivViewPassword1.setImageResource(R.mipmap.eye_closed);
        btnResetPassword.setEnabled(false);
        tvErrorMsg.setVisibility(View.INVISIBLE);
        etPassword.addTextChangedListener(textWatcher);
        etConfirmPassword.addTextChangedListener(textWatcher);
    }

    public static RequestQueue getRequestQueue() {
        return NewPasswordActivity.requestQueue;
    }

    @OnClick(R.id.ivViewPassword)
    void viewPasswordClicked(){
        int type = etPassword.getInputType();

        if(type == 129) {
            etPassword.setInputType(1);
            etPassword.setSelection(etPassword.length());
            ivViewPassword.setImageResource(R.mipmap.eye_open);
        } else {
            etPassword.setInputType(129);
            etPassword.setSelection(etPassword.length());
            ivViewPassword.setImageResource(R.mipmap.eye_closed);
        }
    }

    @OnClick(R.id.ivViewPassword1)
    void viewPassword1Clicked(){
        int type = etConfirmPassword.getInputType();

        if(type == 129) {
            etConfirmPassword.setInputType(1);
            etConfirmPassword.setSelection(etConfirmPassword.length());
            ivViewPassword1.setImageResource(R.mipmap.eye_open);
        } else {
            etConfirmPassword.setInputType(129);
            etConfirmPassword.setSelection(etConfirmPassword.length());
            ivViewPassword1.setImageResource(R.mipmap.eye_closed);
        }
    }

    @OnClick (R.id.btnResetPassword)
    void clickResetPassword(){
        if (!etPasswordText.equals(etConfirmPasswordText)){
            tvErrorMsg.setText(R.string.error_msg_confirm_password);
            tvErrorMsg.setVisibility(View.VISIBLE);
        }else{

            JSONObject jsonObject = new JSONObject();

            try{
                jsonObject.putOpt("broadcaster_id", BROADCASTER_ID);
                jsonObject.putOpt("password", etPasswordText);
            }catch (Exception e){
                e.printStackTrace();
            }
            resetPasswordRequest(jsonObject);
        }
    }

    private void resetPasswordRequest(JSONObject jsonObject) {

        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
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

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RESET_PASSWORD_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e ("RESPONSE SUCCESS", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e ("RESPONSE ERROR", error.toString());
                    }
                });
        jsonObjectRequest.setShouldCache(false);

        DefaultRetryPolicy policy = new DefaultRetryPolicy(TIMEOUT_MILLISECONDS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        getRequestQueue().add(jsonObjectRequest);
    }

    @OnClick (R.id.ibBack)
    void backPressed(){
        super.onBackPressed();
    }

    private void validateFields() {
        etPasswordText = etPassword.getText().toString();
        etConfirmPasswordText = etConfirmPassword.getText().toString();

        if ((etPasswordText.length() >= 4 && etPasswordText.length() <= 12)
                &&  (etConfirmPasswordText.length() >= 4 && etConfirmPasswordText.length() <= 12)){
            btnResetPassword.setEnabled(true);
            btnResetPassword.setBackgroundResource(R.drawable.bg_button_blue_green_big);
        }
        else {
            btnResetPassword.setEnabled(false);
            btnResetPassword.setBackgroundResource(R.drawable.bg_login_gray_button);
        }
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validateFields();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
