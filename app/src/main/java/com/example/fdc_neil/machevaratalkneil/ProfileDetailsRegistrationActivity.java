package com.example.fdc_neil.machevaratalkneil;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ContentFrameLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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
import cn.refactor.lib.colordialog.PromptDialog;

public class ProfileDetailsRegistrationActivity extends AppCompatActivity {

    @BindView(R.id.etPDRNickname)
    EditText etPDRNickname;

    @BindView(R.id.etPDRAge)
    EditText etPDRAge;

    @BindView(R.id.etPDROccupation)
    EditText etPDROccupation;

    @BindView(R.id.etPDRPlace)
    EditText etPDRPlace;

    @BindView(R.id.etIntroduction)
    EditText etIntroduction;

    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    @BindView(R.id.tvCountCharacters)
    TextView tvCountCharacters;

    String etPDRNicknameText, etPDRAgeText, etPDROccupationText, etPDRPlaceText, etIntroductionText, email, password;
    int etPDRAgeInt, etPDRAreaInt;

    private static final String PROFILE_DETAILS_REGISTRATION = "http://pre-macherie.stg.inn.inmgt.com/mobile_api/broadcaster/register/account";
    public static String IDFA = "hereismyapiidfa";
    public static int RANDOM_PROFILE_IMAGE_ID = 1;
    public static String UNAME = "inn";
    public static String PASSWORD = "inns3";
    public static String LOGINTOKEN = "hereismyapitoken";
    private static RequestQueue requestQueue;
    public static int TERMINAL_TYPE = 3;
    public static int TIMEOUT_MILLISECONDS = 30 * 1000;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details_registration);
        ButterKnife.bind(this);

        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        etPDRNickname.addTextChangedListener(textWatcher);
        etPDRAge.addTextChangedListener(textWatcher);
        etPDROccupation.addTextChangedListener(textWatcher);
        etPDRPlace.addTextChangedListener(textWatcher);
        etIntroduction.addTextChangedListener(textWatcher);
    }

    public static RequestQueue getRequestMQueue() {
        return ProfileDetailsRegistrationActivity.requestQueue;
    }

    @OnClick(R.id.dropdown1)
    void clickDropdownAge(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View viewDialog = layoutInflater.inflate(R.layout.age_dialog_dropdown, null);
        dialog.setView(viewDialog);
        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();

        TextView tvNineteen = viewDialog.findViewById(R.id.tvNineteen);
        TextView tvTwenty = viewDialog.findViewById(R.id.tvTwenty);
        TextView tvTwentyone = viewDialog.findViewById(R.id.tvTwenty1);
        TextView tvTwentytwo = viewDialog.findViewById(R.id.tvTwenty2);
        TextView tvTwentythree = viewDialog.findViewById(R.id.tvTwenty3);
        Button btnSubmit = viewDialog.findViewById(R.id.btnSubmit);

        tvNineteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPDRAge.setText(R.string.nineteen);
            }
        });
        tvTwenty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPDRAge.setText(R.string.twenty);
            }
        });

        tvTwentyone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPDRAge.setText(R.string.twentyone);
            }
        });
        tvTwentytwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPDRAge.setText(R.string.twentytwo);
            }
        });
        tvTwentythree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPDRAge.setText(R.string.twentythree);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }


    @OnClick (R.id.dropdown2)
    void clickDropdownPlace(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View viewDialog = layoutInflater.inflate(R.layout.dialog_place_dropdown, null);
        dialog.setView(viewDialog);
        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();

        TextView tvOne = viewDialog.findViewById(R.id.tvOne);
        TextView tvZero = viewDialog.findViewById(R.id.tvZero);
        Button btnSubmit = viewDialog.findViewById(R.id.btnSubmit);

        tvZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPDRPlace.setText(R.string.zero);
            }
        });
        tvOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPDRPlace.setText(R.string.one);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @OnClick (R.id.dropdown3)
    void clickDropdownOccupation(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View viewDialog = layoutInflater.inflate(R.layout.dialog_occupation_dropdown, null);
        dialog.setView(viewDialog);
        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();

        TextView tvEntertainment = viewDialog.findViewById(R.id.tvEntertainment);
        Button btnSubmit = viewDialog.findViewById(R.id.btnSubmit);

        tvEntertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPDROccupation.setText(R.string.entertainment);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void validateFields() {
        etPDRNicknameText = etPDRNickname.getText().toString();
        etPDRAgeText = etPDRAge.getText().toString();
        etPDROccupationText = etPDROccupation.getText().toString();
        etPDRPlaceText = etPDRPlace.getText().toString();
        etIntroductionText = etIntroduction.getText().toString();

        if (etPDRNicknameText.length() > 0 && etPDRAgeText.length() > 0 && etPDROccupationText.length() > 0
                && etPDRPlaceText.length() > 0 && etIntroductionText.length() > 0){
            btnSubmit.setEnabled(true);
            btnSubmit.setBackgroundResource(R.drawable.bg_button_blue_green_big);
        }else {
            btnSubmit.setEnabled(false);
            btnSubmit.setBackgroundResource(R.drawable.bg_login_gray_button);
        }
    }

    @OnClick (R.id.btnSubmit)
    void submitClick(){
        etPDRAgeInt = (Integer.parseInt(etPDRAge.getText().toString()));
        etPDRAreaInt = (Integer.parseInt(etPDRPlace.getText().toString()));

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt("profile_image_id", RANDOM_PROFILE_IMAGE_ID);
            jsonObject.putOpt("email", email);
            jsonObject.putOpt("password", password);
            jsonObject.putOpt("nickname", etPDRNicknameText);
            jsonObject.putOpt("age", etPDRAgeInt);
            jsonObject.putOpt("area", etPDRPlaceText);
            jsonObject.putOpt("performer_job", etPDROccupationText);
            jsonObject.putOpt("self_introduction", etIntroductionText);

        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e("JSON OBJECTS", jsonObject.toString());
        attemptCreateAccount(jsonObject);
    }

    private void attemptCreateAccount(JSONObject jsonObject) {

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
/*
        final ProgressBarUtil pb = new ProgressBarUtil(getApplicationContext());
        final ContentFrameLayout rootlayout = this.findViewById(android.R.id.content);
        pb.addProgressSpinner(btnSubmit, R.color.whiteTransparent, Color.TRANSPARENT, rootlayout,0);
        Log.e("JSON", jsonObject.toString());*/

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, PROFILE_DETAILS_REGISTRATION, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e ("SUCCESS", response.toString());

                        if (response.optString("result").equals("OK")) {
                            intent = new Intent(ProfileDetailsRegistrationActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }else if (response.optString("error_code").equals("101")){
                            new PromptDialog(ProfileDetailsRegistrationActivity.this)
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
                        } else if (response.optString("error_code").equals("325")){
                            new PromptDialog(ProfileDetailsRegistrationActivity.this)
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
//                        pb.removeProgressSpinner();
                        Log.e ("ERROR", error.toString());
                        Toast.makeText(ProfileDetailsRegistrationActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
        jsonObjectRequest.setShouldCache(false);

        DefaultRetryPolicy policy = new DefaultRetryPolicy(TIMEOUT_MILLISECONDS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        getRequestMQueue().add(jsonObjectRequest);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validateFields();
            tvCountCharacters.setText("{" + String.valueOf(etIntroduction.getText().toString().length()) + "/255}");
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

}
