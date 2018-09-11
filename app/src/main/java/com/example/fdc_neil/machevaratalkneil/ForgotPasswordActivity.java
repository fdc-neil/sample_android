package com.example.fdc_neil.machevaratalkneil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    @BindView(R.id.tvErrorMsg)
    TextView tvErrorMsg;

    String etEmailText;

    public static final String FORGOT_PASSWORD_URL = "http://pre-macherie.stg.inn.inmgt.com/mobile_api/broadcaster/forgot_password/request";
    private static RequestQueue mQueue;
    public static String LOGINTOKEN = "hereismyapitoken";
    public static String IDFA = "hereismyapiidfa";
    public static int TERMINAL_TYPE = 4;
    public static int TIMEOUT_MILLISECONDS = 30 * 1000;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        tvErrorMsg.setVisibility(View.INVISIBLE);
        etEmail.addTextChangedListener(textWatcher);
    }

    public static RequestQueue getQueue() {
        return ForgotPasswordActivity.mQueue;
    }

    private void validateField(){
        etEmailText = etEmail.getText().toString();

        if (etEmailText.length() > 0 && Patterns.EMAIL_ADDRESS.matcher(etEmailText).matches()){
            btnSubmit.setEnabled(true);
            btnSubmit.setBackgroundResource(R.drawable.bg_button_blue_green_big);
        }else {
            btnSubmit.setEnabled(false);
            btnSubmit.setBackgroundResource(R.drawable.bg_login_gray_button);
        }
    }
    @OnClick(R.id.ibBack)
    void clickBack() {
        super.onBackPressed();
    }

    @OnClick (R.id.btnSubmit)
    void clickSubmitForgotPassword(){

        if (mQueue == null){
            mQueue = Volley.newRequestQueue(getApplicationContext());
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt("email", etEmailText);
        }catch (Exception e){
            e.printStackTrace();
        }

        forgotPasswordRequest(jsonObject);
    }

    private void forgotPasswordRequest(JSONObject jsonObject) {


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

        Log.e("JSONObject", jsonObject.toString());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, FORGOT_PASSWORD_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("SUCCESS RESPONSE", response.toString());

                        if (response.optString("error_code").equals("201")){
                            tvErrorMsg.setText(response.optString("description"));
                            tvErrorMsg.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(ForgotPasswordActivity.this, "CHECKED", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError:", error.toString());
                        Toast.makeText(ForgotPasswordActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        jsonObjectRequest.setShouldCache(false);

        DefaultRetryPolicy policy = new DefaultRetryPolicy(TIMEOUT_MILLISECONDS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        getQueue().add(jsonObjectRequest);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validateField();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
