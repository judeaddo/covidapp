package com.digitelgh.apps.covid_19tracer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.Iterator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/**
 * An activity representing a single Trace detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link TraceListActivity}.
 */
public class ContinueTraceActivity extends AppCompatActivity {
    EditText etFullName, etPhone, etChildren, etResAddress, etResPop, etEmpName, etWorkLoc, etManContact, etTestHospital;
    TextView etDob, etSampleDate, etTestDate, etLastWorkTime, etIsoFinishDate, returnDate;
    RadioGroup rgTransportMode, rgResArrangement, rgEmpStatus, rgTestedCovid, rgTravelled, rgSampleTaken;
    CheckBox checkToilet, checkBath, checkFever, checkCough, checkRunnyNose, checkBreath, checkHypertension,
            checkAsthma, checkDiabetes, checkCancer, checkHeart, checkHepatitis, checkSmoking, checkAllergies;
    Spinner spGender, spNationality, spMaritalStatus, spTestResult, spSampleResult, spIsoDecision;

    String tempStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_trace);


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
        returnDate = findViewById(R.id.travel_return_date);
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

        checkFever = findViewById(R.id.trace_fever);
        checkCough = findViewById(R.id.trace_cough);
        checkRunnyNose = findViewById(R.id.trace_runnynose);
        checkBreath = findViewById(R.id.trace_breath);

        checkHypertension = findViewById(R.id.trace_hypertension);
        checkAsthma = findViewById(R.id.trace_asthma);
        checkDiabetes = findViewById(R.id.trace_diabetes);
        checkCancer = findViewById(R.id.trace_cancer);
        checkHeart = findViewById(R.id.trace_heart);
        checkHepatitis = findViewById(R.id.trace_hepatitis);
        checkSmoking = findViewById(R.id.trace_smoking);
        checkAllergies = findViewById(R.id.trace_allergies);

        checkToilet = findViewById(R.id.trace_toilet);
        checkBath = findViewById(R.id.trace_bathroom);

//        checkFever = findViewById(R.id.fever);
//        checkFever = findViewById(R.id.fever);
//        checkFever = findViewById(R.id.fever);
//        checkFever = findViewById(R.id.fever);
//        checkFever = findViewById(R.id.fever);
//        checkFever = findViewById(R.id.fever);
//        checkFever = findViewById(R.id.fever);
//        checkFever = findViewById(R.id.fever);
//        rootMedHistory = findViewById(R.id.med_history);
//        rootDestinations = findViewById(R.id.destinations);
////        rootContacts = findViewById(R.id.contacts);
//        rootFacilities = findViewById(R.id.shared_facilities);
//
//        btnAddDestination = findViewById(R.id.add_destination);
////        btnAddContact = findViewById(R.id.add_contact);
//        btnSend = findViewById(R.id.send_btn);
//        progress = findViewById(R.id.progress);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        String record = getIntent().getStringExtra(ContinueTraceFragment.ARG_ITEM_ID);
        Log.i("EBO: ",record);

        try {
            JSONObject rec = new JSONObject(record);
            etFullName.setText(rec.getString("f_name"));
            etPhone.setText(rec.getString("phone"));
            etChildren.setText(rec.getString("children"));
            etResAddress.setText(rec.getString("res_address"));
            etResPop.setText(rec.getString("res_population"));
            etEmpName.setText(rec.getString("employer"));
            etWorkLoc.setText(rec.getString("work_loc"));
            etManContact.setText(rec.getString("manager_contact"));
            etTestHospital.setText(rec.getString("tested_hospital"));

            etDob.setText(rec.getString("dob"));
            etSampleDate.setText(rec.getString("sample_date"));
            etTestDate.setText(rec.getString("tested_date"));
            etLastWorkTime.setText(rec.getString("last_work_time"));
            etIsoFinishDate.setText(rec.getString("iso_date"));

            spGender.setSelection(((ArrayAdapter)spGender.getAdapter()).getPosition(rec.getString("gender")));
            spNationality.setSelection(((ArrayAdapter)spNationality.getAdapter()).getPosition(rec.getString("nationality")));
            spMaritalStatus.setSelection(((ArrayAdapter)spMaritalStatus.getAdapter()).getPosition(rec.getString("m_status")));
            spTestResult.setSelection(((ArrayAdapter)spTestResult.getAdapter()).getPosition(rec.getString("tested_result")));
            spSampleResult.setSelection(((ArrayAdapter)spSampleResult.getAdapter()).getPosition(rec.getString("testing_result")));
            spIsoDecision.setSelection(((ArrayAdapter)spIsoDecision.getAdapter()).getPosition(rec.getString("iso_dec")));

            if (rec.getString("tested_covid").equals("0")) {((RadioButton)rgTestedCovid.findViewById(R.id.testedNo)).setChecked(true);}
            if (rec.getString("tested_covid").equals("1")) {((RadioButton)rgTestedCovid.findViewById(R.id.testedYes)).setChecked(true);}
            //  residential arrangements
            if (rec.getString("emp_status").equals("Unemployed")) {((RadioButton)rgEmpStatus.findViewById(R.id.es1)).setChecked(true);}
            if (rec.getString("emp_status").equals("Self employed")) {((RadioButton)rgEmpStatus.findViewById(R.id.es2)).setChecked(true);}
            if (rec.getString("emp_status").equals("Employed")) {((RadioButton)rgEmpStatus.findViewById(R.id.es3)).setChecked(true);}

            if (rec.getString("res_arrangement").equals("Self contained")) {((RadioButton)rgResArrangement.findViewById(R.id.ra1)).setChecked(true);}
            if (rec.getString("res_arrangement").equals("Compound")) {((RadioButton)rgResArrangement.findViewById(R.id.ra2)).setChecked(true);}
            if (rec.getString("res_arrangement").equals("Apartments/Flats")) {((RadioButton)rgResArrangement.findViewById(R.id.ra3)).setChecked(true);}

            if (rec.getString("transport_mode").equals("Self drive")) {((RadioButton)rgTransportMode.findViewById(R.id.transportSelf)).setChecked(true);}
            if (rec.getString("transport_mode").equals("Chauffeured")) {((RadioButton)rgTransportMode.findViewById(R.id.transportChauf)).setChecked(true);}
            if (rec.getString("transport_mode").equals("Public transport")) {((RadioButton)rgTransportMode.findViewById(R.id.transportPublic)).setChecked(true);}

            if (rec.getString("sample_taken").equals("0")) {((RadioButton)rgSampleTaken.findViewById(R.id.testingYes)).setChecked(true);}
            if (rec.getString("sample_taken").equals("1")) {((RadioButton)rgSampleTaken.findViewById(R.id.testingNo)).setChecked(true);}

            if (rec.getString("travelled").equals("0")) {((RadioButton)rgTravelled.findViewById(R.id.traveledYes)).setChecked(true);}
            if (rec.getString("travelled").equals("1")) {((RadioButton)rgTravelled.findViewById(R.id.traveledNo)).setChecked(true);}

            // TODO: change the references here to string resources
            JSONObject cbSymptoms = new JSONObject(rec.getString("symptoms"));
            Iterator<String> symKeys = cbSymptoms.keys();

            while(symKeys.hasNext()) {
                String key = symKeys.next();
                Log.d("key",key);
                String sym = cbSymptoms.getString(key);
                Log.d("SYM",sym);
                if (sym.equals("Fever")) {((CheckBox)checkFever.findViewById(R.id.trace_fever)).setChecked(true);}
                if (sym.equals("Cough")) {((CheckBox)checkCough.findViewById(R.id.trace_cough)).setChecked(true);}
                if (sym.equals("Runny nose")) {((CheckBox)checkRunnyNose.findViewById(R.id.trace_runnynose)).setChecked(true);}
                if (sym.equals("Shortness of breath")) {((CheckBox)checkBreath.findViewById(R.id.trace_breath)).setChecked(true);}
            }

            JSONObject cbMedHistory = new JSONObject(rec.getString("med_history"));
            Iterator<String> medKeys = cbMedHistory.keys();

            while(medKeys.hasNext()) {
                String key = medKeys.next();
                Log.d("key",key);
                String med = cbMedHistory.getString(key);
                Log.d("MED",med);
                if (med.equals("Hypertension")) {((CheckBox)checkHypertension.findViewById(R.id.trace_hypertension)).setChecked(true);}
                if (med.equals("Asthma")) {((CheckBox)checkAsthma.findViewById(R.id.trace_asthma)).setChecked(true);}
                if (med.equals("Diabetes")) {((CheckBox)checkDiabetes.findViewById(R.id.trace_diabetes)).setChecked(true);}
                if (med.equals("Cancer")) {((CheckBox)checkCancer.findViewById(R.id.trace_cancer)).setChecked(true);}
                if (med.equals("Heart disease")) {((CheckBox)checkHeart.findViewById(R.id.trace_heart)).setChecked(true);}
                if (med.equals("Hepatitis")) {((CheckBox)checkHepatitis.findViewById(R.id.trace_hepatitis)).setChecked(true);}
                if (med.equals("Smoking")) {((CheckBox)checkSmoking.findViewById(R.id.trace_smoking)).setChecked(true);}
                if (med.equals("Allergies")) {((CheckBox)checkAllergies.findViewById(R.id.trace_allergies)).setChecked(true);}
            }

            JSONObject cbSharedFac = new JSONObject(rec.getString("shared_fac"));
            Iterator<String> facKeys = cbSharedFac.keys();

            while(facKeys.hasNext()) {
                String key = facKeys.next();
                String fac = cbSharedFac.getString(key);
                Log.d("FAC",fac);
                if (fac.equals("Toilet")) {((CheckBox)checkToilet.findViewById(R.id.trace_toilet)).setChecked(true);}
                if (fac.equals("Bathroom")) {((CheckBox)checkBath.findViewById(R.id.trace_bathroom)).setChecked(true);}
            }

//            rgTransportMode.setText(rec.getString("transport_mode"));
//            rgResArrangement.setText(rec.getString("res_arrangement"));
//            sharedfa.setText(rec.getString("shared_fac"));
//            .setText(rec.getString("shared_num"));
//            rgEmpStatus.setText(rec.getString("emp_status"));
//            etChildren.setText(rec.getString("remarks"));
        } catch (JSONException e) {
            e.printStackTrace();
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
        // TODO: DUNNO IF IT WILL BE NEEDED BUT CHECK THIS LATER ON
//        if (savedInstanceState == null) {
//            // Create the detail fragment and add it to the activity
//            // using a fragment transaction.
//            Bundle arguments = new Bundle();
//            arguments.putString(ContinueTraceFragment.ARG_ITEM_ID,
//                    getIntent().getStringExtra(ContinueTraceFragment.ARG_ITEM_ID));
//            ContinueTraceFragment fragment = new ContinueTraceFragment();
//            fragment.setArguments(arguments);
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.trace_contacts_container, fragment)
//                    .commit();
//        }
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
            navigateUpTo(new Intent(this, ContactsListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
