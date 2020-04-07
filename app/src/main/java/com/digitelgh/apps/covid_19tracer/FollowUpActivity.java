package com.digitelgh.apps.covid_19tracer;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/**
 * An activity representing a single Trace detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link TraceListActivity}.
 */
public class FollowUpActivity extends AppCompatActivity {

    TextView dayCount;

    CheckBox fever;
    CheckBox sore_throat;
    CheckBox cough;
    CheckBox runny_nose;
    CheckBox shortness_of_breath;
    CheckBox abdominal_pain;
    CheckBox nausea;
    CheckBox vomiting;
    CheckBox diarrhoea;
    TextView remarks;

    // Get user defined values
    Integer feverData;
    Integer soreThroatData;
    Integer coughData;
    Integer runnyNoseData;
    Integer shortnessOfBreathData;
    Integer abdominalPainData;
    Integer nauseaData;
    Integer vomitingData;
    Integer diarrhoeaData;

    Button submitData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup_form);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
//        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(FollowUpFormFragment.ARG_ITEM_ID,getIntent()
                    .getStringExtra(FollowUpFormFragment.ARG_ITEM_ID));
            FollowUpFormFragment fragment = new FollowUpFormFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.follow_up_form_container, fragment)
                    .commit();
        }
        // TODO: change this to call from server
        dayCount = findViewById(R.id.follow_up_day);
        dayCount.setText("Day 1");
        //
        TextView dayDate = (TextView) findViewById(R.id.follow_up_date);
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        String strDate = dateFormat.format(date);
        dayDate.setText("Date: " +strDate);
        // get form fields
        fever = (CheckBox) findViewById(R.id.fever);
        sore_throat = (CheckBox) findViewById(R.id.sore_throat);
        cough = (CheckBox) findViewById(R.id.cough);
        runny_nose = (CheckBox) findViewById(R.id.runny_nose);
        shortness_of_breath = (CheckBox) findViewById(R.id.shortness_of_breath);
        abdominal_pain = (CheckBox) findViewById(R.id.abdominal_pain);
        nausea = (CheckBox) findViewById(R.id.nausea);
        vomiting = (CheckBox) findViewById(R.id.vomiting);
        diarrhoea = (CheckBox) findViewById(R.id.diarrhoea);
        remarks = (TextView) findViewById(R.id.remarks);

        submitData = (Button) findViewById(R.id.submit_follow_up);
        submitData.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v)
            {
                try{
                    if (fever.isChecked()) { feverData=1; }else { feverData=0; }
                    if (sore_throat.isChecked()) { soreThroatData=1; }else { soreThroatData=0; }
                    if (cough.isChecked()) { coughData=1; }else { coughData=0; }
                    if (runny_nose.isChecked()) { runnyNoseData=1; }else { runnyNoseData=0; }
                    if (shortness_of_breath.isChecked()) { shortnessOfBreathData=1; }else { shortnessOfBreathData=0; }
                    if (abdominal_pain.isChecked()) { abdominalPainData=1; }else { abdominalPainData=0; }
                    if (nausea.isChecked()) { nauseaData=1; }else { nauseaData=0; }
                    if (vomiting.isChecked()) { vomitingData=1; }else { vomitingData=0; }
                    if (diarrhoea.isChecked()) { diarrhoeaData=1; }else { diarrhoeaData=0; }
                    // CALL GetText method to make post method call
                    postReport(v);
                }
                catch(Exception ex)
                {
                    Log.i("COVID", "url exception!");
                }
            }
        });
    }

    public void postReport(final View view) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = getResources().getString(R.string.followup_save_url);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject responseObj = null;
                        try {
                            responseObj = new JSONObject(response);
                            Log.i("COVID", response);
                            int responseCode = responseObj.getInt("responseCode");
                            if(responseCode == 200){
                                Snackbar.make(view, "Report submitted successfully!", Snackbar.LENGTH_LONG)
                                        .setCallback(new Snackbar.Callback() {
                                            @Override
                                            public void onDismissed(Snackbar snackbar, int event) {
                                                super.onDismissed(snackbar, event);
                                                Intent i = new Intent(FollowUpActivity.this, TraceListActivity.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        }).show();
                            } else {
                                Snackbar.make(view, "Your report can not be submitted", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }){
//            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put("fever", feverData.toString());
                map.put("sore_throat", soreThroatData.toString());
                map.put("cough", coughData.toString());
                map.put("runny_nose", runnyNoseData.toString());
                map.put("shortness_of_breath", shortnessOfBreathData.toString());
                map.put("abdominal_pain", abdominalPainData.toString());
                map.put("nausea", nauseaData.toString());
                map.put("vomiting", vomitingData.toString());
                map.put("diarrhoea", diarrhoeaData.toString());
                map.put("remarks", remarks.getText().toString());
                map.put("trace_id", getIntent().getStringExtra(TraceDetailFragment.ARG_ITEM_ID));
                // save this in appPref and fetch after for use
                map.put("api_token", "GIXzIAdOiAo1AojHBRUasmttNrWqCWGO7hV12n7v3W2xfkUy1wI3Bpl2jXex");
                return map;
            }

            /** Passing some request headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, TraceListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
