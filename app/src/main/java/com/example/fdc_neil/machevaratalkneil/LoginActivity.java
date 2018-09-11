package com.example.fdc_neil.machevaratalkneil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.refactor.lib.colordialog.PromptDialog;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.ivViewPassword)
    ImageView ivViewPassword;

    @BindView(R.id.tvErrorMsg)
    TextView tvErrorMsg;

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    /*@BindView(R.id.pbProgressBar)
    ProgressBar pbProgressBar;
*/

    String etEmailText, etPasswordText = null;
    int etEmailLength, etPasswordLength = 0;

    public static final String LOGIN_URL = "http://pre-macherie.stg.inn.inmgt.com/mobile_api/broadcaster/account/login";
    public static String UNAME = "inn";
    public static String PASSWORD = "inns3";
    public static String LOGINTOKEN = "hereismyapitoken";
    private static RequestQueue mQueue;
    public static String IDFA = "hereismyapiidfa";
    public static int TERMINAL_TYPE = 4;
    public static int TIMEOUT_MILLISECONDS = 30 * 1000;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        ivViewPassword.setImageResource(R.mipmap.eye_closed);
        btnLogin.setEnabled(false);
        tvErrorMsg.setVisibility(View.INVISIBLE);
        etEmail.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);

    }

    public static RequestQueue getRequestMQueue() {
        return LoginActivity.mQueue;
    }

    private void views() {
        etEmailText = etEmail.getText().toString();
        etEmailLength = etEmail.getText().toString().length();
        etPasswordText = etPassword.getText().toString();
        etPasswordLength = etPassword.getText().toString().length();
    }

    @OnClick(R.id.tvSignup)
    void clickSignup() {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }

    @OnClick (R.id.tvForgotPassword)
    void clickForgotPassword() {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
    }

    @OnClick (R.id.btnLogin)
    void clickLogin() {

//        pbProgressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);
        btnLogin.setBackgroundResource(R.drawable.bg_login_gray_button);

        JSONObject postObject = new JSONObject();
        try {
            postObject.putOpt("email", etEmail.getText().toString());
            postObject.putOpt("password", etPassword.getText().toString());
            postObject.putOpt("login_token", LOGINTOKEN);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        attemptLogin(postObject);
    }

    private void attemptLogin(JSONObject postObject) {

        if(mQueue == null) {
            mQueue = Volley.newRequestQueue(getApplicationContext());
        }

        try {
            // to prevent login token to be null so the room list would not return param error
            if (LOGINTOKEN == null) {
                LOGINTOKEN = "";
            }

            postObject.putOpt("login_token", LOGINTOKEN);
            postObject.putOpt("idfa", IDFA);
            postObject.putOpt("terminal", TERMINAL_TYPE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, postObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("MACHEVERA-NEIL-LOGIN", response.toString());
//                        pbProgressBar.setVisibility(View.INVISIBLE);
                        btnLogin.setEnabled(true);
                        btnLogin.setBackgroundResource(R.drawable.bg_button_blue_green_big);

                        if (response.optString("result").equals("OK")) {
                            intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else if (response.optString("error_code").equals("405")){
                            new PromptDialog(LoginActivity.this)
                                    .setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                                    .setAnimationEnable(true)
                                    .setTitleText(R.string.error)
                                    .setContentText(response.optString("description"))
                                    .setPositiveListener(R.string.ok, new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog promptDialog) {
                                            promptDialog.dismiss();
                                        }
                                    }).show();
//                            tvErrorMsg.setVisibility(View.VISIBLE);
//                            tvErrorMsg.setText(response.optString("description"));
                        }else if (response.optString("error_code").equals("301")){
                            new PromptDialog(LoginActivity.this)
                                    .setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                                    .setAnimationEnable(true)
                                    .setTitleText(R.string.error)
                                    .setContentText(response.optString("description"))
                                    .setPositiveListener(R.string.ok, new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog promptDialog) {
                                            promptDialog.dismiss();
                                        }
                                    }).show();
//                            tvErrorMsg.setVisibility(View.VISIBLE);
//                            tvErrorMsg.setText(response.optString("description"));
                        }else {
                            new PromptDialog(LoginActivity.this)
                                    .setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                                    .setAnimationEnable(true)
                                    .setTitleText(R.string.error)
                                    .setContentText(response.optString("description"))
                                    .setPositiveListener(R.string.ok, new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog promptDialog) {
                                            promptDialog.dismiss();
                                        }
                                    }).show();
//                            tvErrorMsg.setVisibility(View.VISIBLE);
//                            tvErrorMsg.setText(response.optString("description"));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR RESPONSE", error.toString());
                        tvErrorMsg.setVisibility(View.VISIBLE);
                        tvErrorMsg.setText(error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String credentials = UNAME + ":" + PASSWORD;
                String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + base64EncodedCredentials);
                return headers;
            }
        };
        jsonObjectRequest.setShouldCache(false);

        DefaultRetryPolicy policy = new DefaultRetryPolicy(TIMEOUT_MILLISECONDS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        getRequestMQueue().add(jsonObjectRequest);

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

    private void validateFields() {
        views();

        if ((etEmailLength > 0 && Patterns.EMAIL_ADDRESS.matcher(etEmailText).matches())
                &&  (etPasswordLength >= 4 && etPasswordLength <= 12)){
            btnLogin.setEnabled(true);
            btnLogin.setBackgroundResource(R.drawable.bg_button_blue_green_big);
        }
        else {
            btnLogin.setEnabled(false);
            btnLogin.setBackgroundResource(R.drawable.bg_login_gray_button);
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
