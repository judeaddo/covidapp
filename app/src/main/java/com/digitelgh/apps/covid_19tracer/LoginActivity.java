package com.digitelgh.apps.covid_19tracer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rbddevs.splashy.Splashy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences appPrefs;
    private String userId, userEmail, userPIN, userName, userToken;

    private static String CHANNEL_ID = "901";
    String phone, pin;

    private View mContentView;

    TextView tvError;
    EditText etPhone, etPin;
    ProgressBar progress;

    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContentView = findViewById(R.id.fullscreen_content);

        hide();

        appPrefs = getSharedPreferences(getString(R.string.pref_key), MODE_PRIVATE);
        userId = appPrefs.getString(getString(R.string.user_id), null);
        userEmail = appPrefs.getString(getString(R.string.user_email), null);
        userPIN = appPrefs.getString(getString(R.string.user_pin), null);
        userName = appPrefs.getString(getString(R.string.user_name), null);
        userToken = appPrefs.getString(getString(R.string.user_token), null);

        new Splashy(this)
                .setLogo(R.drawable.virus)
                .setAnimation(Splashy.Animation.GLOW_LOGO, 500)
                .setBackgroundResource(R.color.brightGreen)
                .setTitle(R.string.app_name)
                .setTitleColor(R.color.white)
                .setSubTitle(R.string.app_subtitle)
                .setSubTitleColor(R.color.white)
                .setSubTitleItalic(false)
                .setProgressColor(R.color.white)
                .setFullScreen(true)
                //.setInfiniteDuration(true)
                .setTime(3000)
                .show();

        Splashy.Companion.onComplete(new Splashy.OnComplete() {
            @Override
            public void onComplete() {
                if(userId != null && !userId.equals("")) {
                    Intent i = new Intent(LoginActivity.this, FullscreenActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        etPhone = findViewById(R.id.phone);
        etPin = findViewById(R.id.pin);
        tvError = findViewById(R.id.err);
        btnSend = findViewById(R.id.send);
        progress = findViewById(R.id.progress);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                btnSend.setVisibility(View.GONE);
                tvError.setVisibility(View.GONE);
                phone = etPhone.getText().toString();
                pin = etPin.getText().toString();
                signin();
            }
        });

    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    }

    public void signin() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = getResources().getString(R.string.signin_url);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject responseObj = null;
                        try {
                            responseObj = new JSONObject(response);
                            int responseCode = responseObj.getInt("responseCode");
                            if(responseCode == 200){
                                JSONObject userData = responseObj.getJSONObject("data");
                                SharedPreferences appPrefs = getSharedPreferences(getString(R.string.pref_key), MODE_PRIVATE);
                                SharedPreferences.Editor editor = appPrefs.edit();
                                String id = userData.get("id").toString();
                                String name = userData.get("name").toString();
                                String email = userData.get("email").toString();
                                String token = userData.get("api_token").toString();
                                editor.putString(getString(R.string.user_id), id);
                                editor.putString(getString(R.string.user_name), name);
                                editor.putString(getString(R.string.user_email), email);
                                editor.putString(getString(R.string.user_phone), phone);
                                editor.putString(getString(R.string.user_token), token);
                                editor.commit();
                                //pDialog.dismissWithAnimation();
                                Intent i = new Intent(LoginActivity.this, FullscreenActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                String responseMsg = responseObj.getString("msg");
                                tvError.setText(responseMsg);
                                tvError.setVisibility(View.VISIBLE);
                                progress.setVisibility(View.GONE);
                                btnSend.setVisibility(View.VISIBLE);
                                /*pDialog.dismissWithAnimation();
                                new PrettyDialog(StarterActivity.this)
                                        .setIcon(R.drawable.pdlg_icon_close)
                                        .setIconTint(R.color.red)
                                        .setTitle("Error")
                                        .setMessage(responseObj.getString("msg"))
                                        .show();*/
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //mTextView.setText("That didn't work!");
                        //overlay.setVisibility(View.GONE);
                        String message = null;
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ServerError) {
                            message = "Could not connect to the server. Please try again after some time!!";
                        } else if (error instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (error instanceof NoConnectionError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }
                        /*pDialog.dismissWithAnimation();
                        new PrettyDialog(StarterActivity.this)
                                .setIcon(R.drawable.pdlg_icon_close)
                                .setIconTint(R.color.red)
                                .setTitle("Error")
                                .setMessage(message)
                                .show();*/
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put("phoneNumber", phone);
                map.put("password", pin);
                return map;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
