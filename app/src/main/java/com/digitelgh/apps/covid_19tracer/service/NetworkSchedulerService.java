package com.digitelgh.apps.covid_19tracer.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.digitelgh.apps.covid_19tracer.R;
import com.digitelgh.apps.covid_19tracer.db.DBHelper;
import com.digitelgh.apps.covid_19tracer.model.mTrace;
import com.digitelgh.apps.covid_19tracer.receiver.ConnectivityReceiver;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class NetworkSchedulerService extends JobService implements ConnectivityReceiver.ConnectivityReceiverListener {
    List<String> genderArray;
    SharedPreferences appPrefs;
    private String userId, userEmail, userPIN, userName, userToken;
    DBHelper db;
    Context c;

    private static final String TAG = NetworkSchedulerService.class.getSimpleName();

    private ConnectivityReceiver mConnectivityReceiver;

    private static final int MY_SOCKET_TIMEOUT_MS = 60000; // 1 min

    @Override
    public void onCreate() {
        super.onCreate();
        this.c = getApplicationContext();
        Log.i(TAG, "Service created");
        mConnectivityReceiver = new ConnectivityReceiver(this);
    }



    /**
     * When the app's NetworkConnectionActivity is created, it starts this service. This is so that the
     * activity and this service can communicate back and forth. See "setUiCallback()"
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        return START_NOT_STICKY;
    }


    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "onStartJob" + mConnectivityReceiver);
        registerReceiver(mConnectivityReceiver, new IntentFilter(CONNECTIVITY_ACTION));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "onStopJob");
        unregisterReceiver(mConnectivityReceiver);
        return true;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        //String message = isConnected ? "Good! Connected to Internet" : "Sorry! Not connected to internet";
        //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        if(isConnected) {
            appPrefs = c.getSharedPreferences(c.getString(R.string.pref_key), MODE_PRIVATE);
            userId = appPrefs.getString(c.getString(R.string.user_id), null);
            userEmail = appPrefs.getString(c.getString(R.string.user_email), null);
            userPIN = appPrefs.getString(c.getString(R.string.user_pin), null);
            userName = appPrefs.getString(c.getString(R.string.user_name), null);
            userToken = appPrefs.getString(c.getString(R.string.user_token), null);

            db = new DBHelper(c);
            List<mTrace> traces = db.getUnsentTraces();

            if(traces.size() > 0) {
                Toast.makeText(c, "Synchronizing App", Toast.LENGTH_LONG).show();
                for (int i = 0; i < traces.size(); i++) {
                    mTrace trace = traces.get(i);
                    long id = trace.getId();
                    sendTrace(trace, id);
                }
            }
        }
    }

    public String getBase64Image(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void sendTrace(final mTrace trace, final long recordID) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(c.getApplicationContext());
        String url = c.getResources().getString(R.string.save_trace_url);
        final DBHelper db = new DBHelper(c.getApplicationContext());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.startsWith("OK:")) {
                            trace.setSent(1);
                            db.updateTrace(recordID, trace);
                        } else if(response.startsWith("suspend")) {
                            //db.deleteTmpFarmSent(recordID);
                        } else {
                            //db.deleteTmpFarmSent(recordID);
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
                            message = "Requested resource not found...Please check resource url!";
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
                        //db.deleteTmpFarmSent(recordID);
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put("ref", String.valueOf(trace.getRef()));
                map.put("trace_id", String.valueOf(trace.getTraceId()));
                map.put("user_id", String.valueOf(userId));
                map.put("f_name", String.valueOf(trace.getFullName()));
                map.put("dob", String.valueOf(trace.getDob()));
                map.put("gender", trace.getGender());
                map.put("nationality", trace.getNationality());
                map.put("phone", trace.getPhone());
                map.put("m_status", String.valueOf(trace.getMaritalStatus()));
                map.put("children", String.valueOf(trace.getChildren()));
                map.put("transport_mode", String.valueOf(trace.getTransportMode()));
                map.put("res_address", String.valueOf(trace.getResAddress()));
                map.put("res_arrangement", String.valueOf(trace.getResArrangement()));
                map.put("shared_fac", String.valueOf(trace.getSharedFacilities()));
                map.put("shared_num", String.valueOf(trace.getSharedNum()));
                map.put("res_population", String.valueOf(trace.getResPopulation()));
                map.put("emp_status", String.valueOf(trace.getEmpStatus()));
                map.put("employer", String.valueOf(trace.getEmployer()));
                map.put("work_loc", String.valueOf(trace.getWorkLoc()));
                map.put("manager_contact", String.valueOf(trace.getManContact()));
                map.put("last_work_time", String.valueOf(trace.getLastWorkTime()));
                map.put("symptoms", String.valueOf(trace.getSymptoms()));
                map.put("med_history", String.valueOf(trace.getMedHistory()));
                map.put("tested_covid", String.valueOf(trace.getTestedCovid()));
                map.put("tested_date", String.valueOf(trace.getTestedDate()));
                map.put("tested_hospital", String.valueOf(trace.getTestedHospital()));
                map.put("tested_result", String.valueOf(trace.getTestedResult()));
                map.put("travelled", String.valueOf(trace.getTravelled()));
                map.put("travel_history", String.valueOf(trace.getTravelHistory()));
                map.put("recent_contacts", String.valueOf(trace.getRecentContacts()));
                map.put("sample_taken", String.valueOf(trace.getSampleTaken()));
                map.put("sample_date", String.valueOf(trace.getSampleDate()));
                map.put("testing_result", String.valueOf(trace.getSampleResult()));
                map.put("iso_dec", String.valueOf(trace.getIsoDecision()));
                map.put("iso_date", String.valueOf(trace.getIsoFinishDate()));
                map.put("remarks", String.valueOf(trace.getRemarks()));
                return map;
            }
        };

        // Add the request to the RequestQueue.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }
}
