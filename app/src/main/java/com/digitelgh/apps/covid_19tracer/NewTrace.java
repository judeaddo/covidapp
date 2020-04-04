package com.digitelgh.apps.covid_19tracer;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.digitelgh.apps.covid_19tracer.db.DBHelper;
import com.digitelgh.apps.covid_19tracer.model.mTrace;
import com.digitelgh.apps.covid_19tracer.utils.tools;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import libs.mjn.prettydialog.PrettyDialog;

public class NewTrace extends AppCompatActivity {
    private View mContentView;
    private static String CHANNEL_ID = "901";
    SharedPreferences appPrefs;
    private String userId, userEmail, userPIN, userName, userToken;
    DBHelper db;
    Gson json;

    private static final int MY_SOCKET_TIMEOUT_MS = 60000; // 1 min

    Button btnSend;
    ImageButton btnAddDestination, btnAddContact;
    LinearLayout /*rootDestinationView, rootContactView,*/ rootSymptoms, rootMedHistory, rootDestinations, rootContacts, rootFacilities;

    EditText etFullName, etPhone, etChildren, etResAddress, etResPop, etEmpName, etWorkLoc, etManContact, etTestHospital;
    TextView etDob, etSampleDate, etTestDate, etLastWorkTime, etIsoFinishDate, returnDate;
    RadioGroup rgTransportMode, rgResArrangement, rgEmpStatus, rgTestedCovid, rgTravelled, rgSampleTaken;
    //CheckBox checkToilet, checkBath, checkFever, checkCough, checkRunnyNose, checkBreath;
    Spinner spGender, spNationality, spMaritalStatus, spTestResult, spSampleResult, spIsoDecision;

    DatePickerDialog datePickerDialog;
    int year, month, dayOfMonth;
    Calendar calendar;

    ProgressBar progress;

    String fullName="", phone="", dob="", children="", resAddress="", resPop="0", empName="", workLoc="", manContact="", lastWorkTime="", testedCovid="", testDate="", testHospital="", testResult="", sampleTaken="", sampleDate="",
            isoFinishDate="", transportMode="", resArrangement="", empStatus="", travelled="", sharedFacilities="", symptoms="", gender="", nationality="", mStatus="", sampleResult="", isoDecision="",
            destinations="", contacts="", medHistory="", REF;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trace);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContentView = findViewById(R.id.fullscreen_content);
        hideUI();

        db = new DBHelper(getApplicationContext());
        appPrefs = getSharedPreferences(getString(R.string.pref_key), MODE_PRIVATE);
        userId = appPrefs.getString(getString(R.string.user_id), null);
        userEmail = appPrefs.getString(getString(R.string.user_email), null);
        userPIN = appPrefs.getString(getString(R.string.user_pin), null);
        userName = appPrefs.getString(getString(R.string.user_name), null);
        userToken = appPrefs.getString(getString(R.string.user_token), null);

        REF = tools.md5(userId + Calendar.getInstance().getTimeInMillis());
        /*CustomViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setPagingEnabled(false);
        viewPager.setCurrentItem(0);*/
        etFullName = findViewById(R.id.name);
        etPhone = findViewById(R.id.phone);
        etChildren = findViewById(R.id.children);
        etResAddress = findViewById(R.id.res_address);
        etResPop = findViewById(R.id.res_pop);
        etEmpName = findViewById(R.id.emp_name);
        etWorkLoc = findViewById(R.id.work_loc);
        etManContact = findViewById(R.id.manager_number);
        etTestHospital = findViewById(R.id.test_hospital);

        etDob = findViewById(R.id.dob);
        returnDate = findViewById(R.id.return_date);
        etSampleDate = findViewById(R.id.sample_date);
        etTestDate = findViewById(R.id.test_date);
        etLastWorkTime = findViewById(R.id.last_work_time);
        etIsoFinishDate = findViewById(R.id.qua_date);

        rgTransportMode = findViewById(R.id.transport_mode);
        rgResArrangement = findViewById(R.id.res_arrangement);
        rgEmpStatus = findViewById(R.id.emp_status);
        rgTestedCovid = findViewById(R.id.tested_covid);
        rgTravelled = findViewById(R.id.travelled);
        rgSampleTaken = findViewById(R.id.sample_taken);

        spGender = findViewById(R.id.gender);
        spNationality = findViewById(R.id.nationality);
        spMaritalStatus = findViewById(R.id.marital);
        spTestResult = findViewById(R.id.test_result);
        spSampleResult = findViewById(R.id.sample_result);
        spIsoDecision = findViewById(R.id.iso_decision);

        /*rootDestinationView = findViewById(R.id.destinations_view);
        rootContactView = findViewById(R.id.contacts_view);*/

        rootSymptoms = findViewById(R.id.symptoms);
        rootMedHistory = findViewById(R.id.med_history);
        rootDestinations = findViewById(R.id.destinations);
        rootContacts = findViewById(R.id.contacts);
        rootFacilities = findViewById(R.id.shared_facilities);

        btnAddDestination = findViewById(R.id.add_destination);
        btnAddContact = findViewById(R.id.add_contact);
        btnSend = findViewById(R.id.send_btn);
        progress = findViewById(R.id.progress);

        json = new Gson();

        btnAddDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View destinationItem = getLayoutInflater().inflate(R.layout.item_destination, null);
                rootDestinations.addView(destinationItem);
            }
        });

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View contactItem = getLayoutInflater().inflate(R.layout.item_contact, null);
                rootContacts.addView(contactItem);
            }
        });

        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(etDob);
            }
        });

        returnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(returnDate);
            }
        });

        etSampleDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(etSampleDate);
            }
        });

        etTestDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(etTestDate);
            }
        });

        etLastWorkTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(etLastWorkTime);
            }
        });

        etIsoFinishDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(etIsoFinishDate);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSend.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                Map<Integer,String> symptomsArray = new HashMap<Integer,String>();
                Map<Integer,String> medHistoryArray = new HashMap<Integer,String>();
                Map<Integer,String> destinationsArray = new HashMap<Integer,String>();
                Map<Integer,String> contactsArray = new HashMap<Integer,String>();
                Map<Integer,String> facilitiesArray = new HashMap<Integer,String>();
                for (int i = 0; i < rootSymptoms.getChildCount(); i++) {
                    final View child = rootSymptoms.getChildAt(i);
                    if (child instanceof CheckBox) {
                        if(((CheckBox) child).isChecked()) {
                            symptomsArray.put(i, ((CheckBox) child).getText().toString());
                        }
                    }
                }
                for (int i = 0; i < rootMedHistory.getChildCount(); i++) {
                    final View child = rootMedHistory.getChildAt(i);
                    if (child instanceof CheckBox) {
                        if(((CheckBox) child).isChecked()) {
                            medHistoryArray.put(i, ((CheckBox) child).getText().toString());
                        }
                    }
                }
                for (int i = 0; i < rootFacilities.getChildCount(); i++) {
                    final View child = rootFacilities.getChildAt(i);
                    if (child instanceof CheckBox) {
                        if(((CheckBox) child).isChecked()) {
                            facilitiesArray.put(i, ((CheckBox) child).getText().toString());
                        }
                    }
                }
                for (int i = 0; i < rootDestinations.getChildCount(); i++) {
                    final View child = rootDestinations.getChildAt(i);
                    if (child instanceof CheckBox) {
                        if(((CheckBox) child).isChecked()) {
                            destinationsArray.put(i, ((CheckBox) child).getText().toString());
                        }
                    }
                }
                for (int i = 0; i < rootContacts.getChildCount(); i++) {
                    final View child = rootContacts.getChildAt(i);
                    if (child instanceof CheckBox) {
                        if(((CheckBox) child).isChecked()) {
                            contactsArray.put(i, ((CheckBox) child).getText().toString());
                        }
                    }
                }
                RadioButton rdTransportMode = rgTransportMode.findViewById(rgTransportMode.getCheckedRadioButtonId());
                RadioButton rdResArrangement = rgResArrangement.findViewById(rgResArrangement.getCheckedRadioButtonId());
                RadioButton rdEmpStatus = rgEmpStatus.findViewById(rgEmpStatus.getCheckedRadioButtonId());
                RadioButton rdTestedCovid = rgTestedCovid.findViewById(rgTestedCovid.getCheckedRadioButtonId());
                RadioButton rdTravelled = rgTravelled.findViewById(rgTravelled.getCheckedRadioButtonId());
                RadioButton rdSampleTaken = rgSampleTaken.findViewById(rgSampleTaken.getCheckedRadioButtonId());
                if(rdTransportMode != null)
                    transportMode = ((RadioButton) rgTransportMode.getChildAt(rgTransportMode.indexOfChild(rdTransportMode))).getText().toString();
                if(rdResArrangement != null)
                    resArrangement = ((RadioButton) rgResArrangement.getChildAt(rgResArrangement.indexOfChild(rdResArrangement))).getText().toString();
                if(rdEmpStatus != null)
                    empStatus = ((RadioButton) rgEmpStatus.getChildAt(rgEmpStatus.indexOfChild(rdTransportMode))).getText().toString();
                if(rdTestedCovid != null)
                    testedCovid = ((RadioButton) rgTestedCovid.getChildAt(rgTestedCovid.indexOfChild(rdTransportMode))).getText().toString();
                if(rdTravelled != null)
                    travelled = ((RadioButton) rgTravelled.getChildAt(rgTravelled.indexOfChild(rdTransportMode))).getText().toString();
                if(rdSampleTaken != null)
                    sampleTaken = ((RadioButton) rgSampleTaken.getChildAt(rgSampleTaken.indexOfChild(rdTransportMode))).getText().toString();

                symptoms = json.toJson(symptomsArray);
                medHistory = json.toJson(medHistoryArray);
                destinations = json.toJson(destinationsArray);
                contacts = json.toJson(contactsArray);
                sharedFacilities = json.toJson(facilitiesArray);

                gender = spGender.getSelectedItem().toString();
                nationality = spNationality.getSelectedItem().toString();
                mStatus = spMaritalStatus.getSelectedItem().toString();
                testResult = spTestResult.getSelectedItem().toString();
                sampleResult = spSampleResult.getSelectedItem().toString();
                isoDecision = spIsoDecision.getSelectedItem().toString();

                fullName = etFullName.getText().toString();
                phone = etPhone.getText().toString();
                children = etChildren.getText().toString();
                resAddress = etResAddress.getText().toString();
                resPop = etResPop.getText().toString();
                empName = etEmpName.getText().toString();
                workLoc = etWorkLoc.getText().toString();
                manContact = etManContact.getText().toString();
                testHospital = etTestHospital.getText().toString();
                dob  = etDob.getText().toString();
                lastWorkTime  = etLastWorkTime.getText().toString();
                testDate  = etTestDate.getText().toString();
                sampleDate  = etSampleDate.getText().toString();
                isoFinishDate  = etIsoFinishDate.getText().toString();

                //mTrace trace = new mTrace(REF, 0, Integer.parseInt(userId), fullName, );
                mTrace trace = new mTrace(REF, 0, Integer.parseInt(userId), fullName, dob, gender, nationality, phone, mStatus, children, transportMode, resAddress, resArrangement,
                        sharedFacilities, resPop, empStatus, empName, workLoc, manContact, lastWorkTime, symptoms, medHistory, testedCovid, testDate, testHospital, testResult, travelled,
                        destinations, contacts, sampleTaken, sampleDate, sampleResult, isoDecision, isoFinishDate, "", 0);

                //long id = db.addTrace(trace);
                //sendTrace(trace, id);
                //Toast.makeText(getApplicationContext(), json.toJson(symptomsArray) + ":" + isoDecision, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void hideUI() {
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

    public void showDatePicker(final TextView et) {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(NewTrace.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        et.setText(year + "-" + String.format("%02d", (month + 1)) + "-" + String.format("%02d", day));
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void sendTrace(final mTrace trace, final long recordID) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = getResources().getString(R.string.save_trace_url);
        final DBHelper db = new DBHelper(getApplicationContext());

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.startsWith("OK:")) {
                            trace.setSent(1);
                            db.updateTrace(recordID, trace);
                            progress.setVisibility(View.GONE);
                            for (int i = 0; i < rootSymptoms.getChildCount(); i++) {
                                final View child = rootSymptoms.getChildAt(i);
                                if (child instanceof CheckBox) {
                                    if(((CheckBox) child).isChecked()) {
                                        ((CheckBox) child).setChecked(false);
                                    }
                                }
                            }
                            for (int i = 0; i < rootMedHistory.getChildCount(); i++) {
                                final View child = rootMedHistory.getChildAt(i);
                                if (child instanceof CheckBox) {
                                    if(((CheckBox) child).isChecked()) {
                                        ((CheckBox) child).setChecked(false);
                                    }
                                }
                            }
                            for (int i = 0; i < rootFacilities.getChildCount(); i++) {
                                final View child = rootFacilities.getChildAt(i);
                                if (child instanceof CheckBox) {
                                    if(((CheckBox) child).isChecked()) {
                                        ((CheckBox) child).setChecked(false);
                                    }
                                }
                            }
                            for (int i = 0; i < rootDestinations.getChildCount(); i++) {
                                final View child = rootDestinations.getChildAt(i);
                                if (child instanceof CheckBox) {
                                    if(((CheckBox) child).isChecked()) {
                                        ((CheckBox) child).setChecked(false);
                                    }
                                }
                            }
                            for (int i = 0; i < rootContacts.getChildCount(); i++) {
                                final View child = rootContacts.getChildAt(i);
                                if (child instanceof CheckBox) {
                                    if(((CheckBox) child).isChecked()) {
                                        ((CheckBox) child).setChecked(false);
                                    }
                                }
                            }
                            for (int i = 0; i < rgTransportMode.getChildCount(); i++) {
                                final View child = rgTransportMode.getChildAt(i);
                                if (child instanceof RadioButton) {
                                    if(((RadioButton) child).isChecked()) {
                                        ((RadioButton) child).setChecked(false);
                                    }
                                }
                            }
                            for (int i = 0; i < rgResArrangement.getChildCount(); i++) {
                                final View child = rgResArrangement.getChildAt(i);
                                if (child instanceof RadioButton) {
                                    if(((RadioButton) child).isChecked()) {
                                        ((RadioButton) child).setChecked(false);
                                    }
                                }
                            }
                            for (int i = 0; i < rgEmpStatus.getChildCount(); i++) {
                                final View child = rgEmpStatus.getChildAt(i);
                                if (child instanceof RadioButton) {
                                    if(((RadioButton) child).isChecked()) {
                                        ((RadioButton) child).setChecked(false);
                                    }
                                }
                            }
                            for (int i = 0; i < rgTestedCovid.getChildCount(); i++) {
                                final View child = rgTestedCovid.getChildAt(i);
                                if (child instanceof RadioButton) {
                                    if(((RadioButton) child).isChecked()) {
                                        ((RadioButton) child).setChecked(false);
                                    }
                                }
                            }
                            for (int i = 0; i < rgTravelled.getChildCount(); i++) {
                                final View child = rgTravelled.getChildAt(i);
                                if (child instanceof RadioButton) {
                                    if(((RadioButton) child).isChecked()) {
                                        ((RadioButton) child).setChecked(false);
                                    }
                                }
                            }
                            for (int i = 0; i < rgSampleTaken.getChildCount(); i++) {
                                final View child = rgSampleTaken.getChildAt(i);
                                if (child instanceof RadioButton) {
                                    if(((RadioButton) child).isChecked()) {
                                        ((RadioButton) child).setChecked(false);
                                    }
                                }
                            }
                            spGender.setSelection(0);
                            spNationality.setSelection(0);
                            spMaritalStatus.setSelection(0);
                            spTestResult.setSelection(0);
                            spSampleResult.setSelection(0);
                            spIsoDecision.setSelection(0);
                            etFullName.setText("");
                            etPhone.setText("");
                            etChildren.setText("");
                            etResAddress.setText("");
                            etResPop.setText("");
                            etEmpName.setText("");
                            etWorkLoc.setText("");
                            etManContact.setText("");
                            etTestHospital.setText("");
                            etDob.setText("");
                            etLastWorkTime.setText("");
                            etTestDate.setText("");
                            etSampleDate.setText("");
                            etIsoFinishDate.setText("");
                        } else if(response.startsWith("suspend")) {
                            //db.deleteTmpFarmSent(recordID);
                        } else {
                            //db.deleteTmpFarmSent(recordID);
                        }
                        JSONObject responseObj = null;
                        try {
                            responseObj = new JSONObject(response);
                            int responseCode = responseObj.getInt("responseCode");
                            //String responseMsg = responseObj.getString("msg");
                            if(responseCode == 200){
                                trace.setSent(1);
                                db.updateTrace(recordID, trace);
                                progress.setVisibility(View.GONE);
                                btnSend.setVisibility(View.VISIBLE);
                                new PrettyDialog(NewTrace.this)
                                        .setIcon(R.drawable.pdlg_icon_success)
                                        .setIconTint(R.color.shadeGreen)
                                        .setTitle("Success")
                                        .setMessage("Data sent")
                                        .show();
                            } else {
                                //String responseMsg = responseObj.getString("msg");
                                progress.setVisibility(View.GONE);
                                btnSend.setVisibility(View.VISIBLE);
                                new PrettyDialog(NewTrace.this)
                                        .setIcon(R.drawable.pdlg_icon_close)
                                        .setIconTint(R.color.red)
                                        .setTitle("Error")
                                        .setMessage("Could not connect. Please again later.")
                                        .show();
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
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
