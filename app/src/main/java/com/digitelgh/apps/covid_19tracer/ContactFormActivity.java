package com.digitelgh.apps.covid_19tracer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.digitelgh.apps.covid_19tracer.service.NetworkSchedulerService;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ContactFormActivity extends AppCompatActivity {
    SharedPreferences appPrefs;
    private String userId, userEmail, userPIN, userName, userToken;

    private View mContentView;
    private static String CHANNEL_ID = "901";
    Button btnStart, btnContinue, btnAssignments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_form);
        mContentView = findViewById(R.id.fullscreen_content);

        Spinner regionSpinner = (Spinner) findViewById(R.id.contact_form_region);
        Spinner districtSpinner = (Spinner) findViewById(R.id.contact_form_district);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> regionAdapter = ArrayAdapter
                .createFromResource(this, R.array.region_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> districtAdapter = ArrayAdapter
                .createFromResource(this, R.array.district_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        regionSpinner.setAdapter(regionAdapter);
        districtSpinner.setAdapter(districtAdapter);
//        hide();

        appPrefs = getSharedPreferences(getString(R.string.pref_key), MODE_PRIVATE);
        userId = appPrefs.getString(getString(R.string.user_id), null);
        userEmail = appPrefs.getString(getString(R.string.user_email), null);
        userPIN = appPrefs.getString(getString(R.string.user_pin), null);
        userName = appPrefs.getString(getString(R.string.user_name), null);
        userToken = appPrefs.getString(getString(R.string.user_token), null);

        scheduleJob();
        createNotificationChannel();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleJob() {
        JobInfo myJob = new JobInfo.Builder(0, new ComponentName(this, NetworkSchedulerService.class))
                .setRequiresCharging(true)
                .setMinimumLatency(1000)
                .setOverrideDeadline(2000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(myJob);
    }

    public boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.hide();
//        }

//        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    }
}
