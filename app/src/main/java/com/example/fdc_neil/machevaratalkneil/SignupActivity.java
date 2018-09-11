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

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.tvErrorMsg)
    TextView tvErrorMsg;

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;

    @BindView(R.id.btnSignup)
    Button btnSignup;

    @BindView(R.id.ivViewPassword)
    ImageView ivViewPassword;

    @BindView(R.id.ivViewPassword1)
    ImageView ivViewPassword1;


    String etEmailText, etPasswordText, etConfirmPasswordText = null;
    int etEmailLength, etPasswordLength, etConfirmPasswordLength = 0;

    private static final String SIGNUP_URL = "http://pre-macherie.stg.inn.inmgt.com/mobile_api/broadcaster/register/email_avail_check";
    public static String LOGINTOKEN = "hereismyapitoken";
    private static RequestQueue requestQueue;
    public static String IDFA = "hereismyapiidfa";
    public static int TERMINAL_TYPE = 4;
    public static int TIMEOUT_MILLISECONDS = 30 * 1000;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        ivViewPassword.setImageResource(R.mipmap.eye_closed);
        ivViewPassword1.setImageResource(R.mipmap.eye_closed);
        tvErrorMsg.setVisibility(View.INVISIBLE);
        etEmail.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);
        etConfirmPassword.addTextChangedListener(textWatcher);
        btnSignup.setEnabled(false);
    }

    public static RequestQueue getRequestMQueue() {
        return SignupActivity.requestQueue;
    }

    private void views(){
        etEmailLength = etEmail.getText().toString().length();
        etPasswordLength = etPassword.getText().toString().length();
        etEmailText = etEmail.getText().toString();
        etPasswordText = etPassword.getText().toString();
        etConfirmPasswordText = etConfirmPassword.getText().toString();
        etConfirmPasswordLength = etConfirmPassword.getText().toString().length();
    }

    @OnClick(R.id.ibBack)
    void backPressed(){
        super.onBackPressed();
    }

    @OnClick(R.id.tvRestoreAccount)
    void clickRestoreAccount(){
        startActivity(new Intent(SignupActivity.this, NewPasswordActivity.class));
    }

    @OnClick (R.id.ivViewPassword)
    void viewPassword() {
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

    @OnClick (R.id.ivViewPassword1)
    void viewPassword1() {
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


    @OnClick(R.id.btnSignup)
    void clickSignup(){
        JSONObject jsonObject = new JSONObject();
        if (!etPasswordText.equals(etConfirmPasswordText)){
            tvErrorMsg.setText(R.string.error_msg_confirm_password);
            tvErrorMsg.setVisibility(View.VISIBLE);
        }else{
            try{
                jsonObject.putOpt("email", etEmail.getText().toString());
            }catch (Exception e){
                e.printStackTrace();
            }

            Log.e("SIGNUPS", jsonObject.toString());

            checkEmailAvailability(jsonObject);
        }
    }

    private void checkEmailAvailability(JSONObject jsonObject) {

        if(requestQueue == null) {
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

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, SIGNUP_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("MACH-NEIL-CHECK", response.toString());

                        String random_profile_img = response.optString("random_profile_image_url");
                        String email = response.optString("email");

                        if (response.optString("result").equals("OK")) {
                            intent = new Intent(SignupActivity.this, ProfileImageDescriptionActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("RANDOM_PROFILE_IMAGE", random_profile_img);
                            intent.putExtra("email", etEmailText);
                            intent.putExtra("password", etConfirmPasswordText);
                            startActivity(intent);

                        }else if (response.optString("error_code").equals("305")){
                            tvErrorMsg.setVisibility(View.VISIBLE);
                            tvErrorMsg.setText(response.optString("description"));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        tvErrorMsg.setVisibility(View.VISIBLE);
                        tvErrorMsg.setText(error.toString());
                    }
                }
        );
        jsonObjectRequest.setShouldCache(false);

        DefaultRetryPolicy policy = new DefaultRetryPolicy(TIMEOUT_MILLISECONDS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        getRequestMQueue().add(jsonObjectRequest);
    }

    private void validateFields() {
        views();

        if ((etEmailLength > 0 && Patterns.EMAIL_ADDRESS.matcher(etEmailText).matches()) &&  (etPasswordLength >= 4 && etPasswordLength <= 12)
                &&  (etConfirmPasswordLength >= 4 && etConfirmPasswordLength <= 12)){
            btnSignup.setEnabled(true);
            btnSignup.setBackgroundResource(R.drawable.bg_button_blue_green_big);
        }
        else {
            btnSignup.setEnabled(false);
            btnSignup.setBackgroundResource(R.drawable.bg_login_gray_button);
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
