package com.digitelgh.apps.covid_19tracer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.digitelgh.apps.covid_19tracer.model.mTrace;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int dbVersion = 1;
    // Database Name
    private static final String dbName = "appDB";

    // Table Names
    private static final String TABLE_TRACES = "tbl_traces";
    // common column names
    private static final String ID = "id";
    private static final String REF = "ref";
    private static final String TRACE_ID = "trace_id";
    private static final String USER_ID = "user_id";
    private static final String F_NAME = "f_name";
    private static final String DOB = "dob";
    private static final String GENDER = "gender";
    private static final String NATIONALITY = "nationality";
    private static final String PHONE = "phone";
    private static final String M_STATUS = "m_status";
    private static final String CHILDREN = "children";
    private static final String TRANS_MODE = "transport_mode";
    private static final String RES_ADDRESS = "res_address";
    private static final String RES_ARRANGEMENT = "res_arrangement";
    private static final String SHARED_FAC = "shared_fac";
    private static final String RES_POP = "res_population";
    private static final String EMP_STATUS = "emp_status";
    private static final String EMPLOYER = "employer";
    private static final String WORK_LOC = "work_loc";
    private static final String MANAGER_CON = "manager_contact";
    private static final String L_W_TIME = "last_work_time";
    private static final String SYMPTOMS = "symptoms";
    private static final String MED_HIS = "med_history";
    private static final String TESTED_COV = "tested_covid";
    private static final String TESTED_DATE = "tested_date";
    private static final String TESTED_HOS = "tested_hospital";
    private static final String TESTED_RES = "tested_result";
    private static final String TRAVELLED = "travelled";
    private static final String TRAV_HIS = "travel_history";
    private static final String REC_CON = "recent_contacts";
    private static final String SAMPLE_TAKEN = "sample_taken";
    private static final String SAMPLE_DATE = "sample_date";
    private static final String SAMPLE_RES = "sample_result";
    private static final String ISO_DEC = "iso_dec";
    private static final String ISO_DATE = "iso_date";
    private static final String REMARKS = "remarks";
    private static final String SENT = "sent";
    private static final String CREATED_AT = "created_at";

    // Table Create Statements
    private static final String CREATE_TABLE_TRACES = "CREATE TABLE "
            + TABLE_TRACES + "(" + ID + " INTEGER PRIMARY KEY,"
            + REF + " VARCHAR (250),"
            + TRACE_ID + " INTEGER,"
            + USER_ID + " INTEGER,"
            + F_NAME + " VARCHAR (250),"
            + DOB + " VARCHAR(20),"
            + GENDER + " VARCHAR (250),"
            + NATIONALITY + " VARCHAR (250),"
            + PHONE + " VARCHAR (20),"
            + M_STATUS + " VARCHAR (250),"
            + CHILDREN + " INTEGER,"
            + TRANS_MODE + " VARCHAR (250),"
            + RES_ADDRESS + " TEXT,"
            + RES_ARRANGEMENT + " VARCHAR (250),"
            + SHARED_FAC + " VARCHAR (250),"
            + RES_POP + " INTEGER,"
            + EMP_STATUS + " VARCHAR (250),"
            + EMPLOYER + " VARCHAR (250),"
            + WORK_LOC + " VARCHAR (250),"
            + MANAGER_CON + " VARCHAR (20),"
            + L_W_TIME + " VARCHAR (20),"
            + SYMPTOMS + " TEXT,"
            + MED_HIS + " TEXT,"
            + TESTED_COV + " VARCHAR (250),"
            + TESTED_DATE + " VARCHAR (20),"
            + TESTED_HOS + " VARCHAR (250),"
            + TESTED_RES + " VARCHAR (250),"
            + TRAVELLED + " VARCHAR (250),"
            + TRAV_HIS + " TEXT,"
            + REC_CON + " TEXT,"
            + SAMPLE_TAKEN + " VARCHAR (250),"
            + SAMPLE_DATE + " VARCHAR (20),"
            + SAMPLE_RES + " VARCHAR (250),"
            + ISO_DEC + " VARCHAR (250),"
            + ISO_DATE + " VARCHAR (20),"
            + REMARKS + " TEXT,"
            + SENT + " INTEGER NOT NULL DEFAULT 0,"
            + CREATED_AT + " DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)";


    public DBHelper(Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TRACES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACES);
        db.execSQL(CREATE_TABLE_TRACES);
    }

    public long addTrace(mTrace trace) {
        long id = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(REF, trace.getRef());
        values.put(TRACE_ID, trace.getTraceId());
        values.put(USER_ID, trace.getUserId());
        values.put(F_NAME, trace.getFullName());
        values.put(DOB, trace.getDob());
        values.put(GENDER, trace.getGender());
        values.put(NATIONALITY, trace.getNationality());
        values.put(PHONE, trace.getPhone());
        values.put(M_STATUS, trace.getMaritalStatus());
        values.put(CHILDREN, trace.getChildren());
        values.put(TRANS_MODE, trace.getTransportMode());
        values.put(RES_ADDRESS, trace.getResAddress());
        values.put(RES_ARRANGEMENT, trace.getResArrangement());
        values.put(SHARED_FAC, trace.getSharedFacilities());
        values.put(RES_POP, trace.getResPopulation());
        values.put(EMP_STATUS, trace.getEmpStatus());
        values.put(EMPLOYER, trace.getEmployer());
        values.put(WORK_LOC, trace.getWorkLoc());
        values.put(MANAGER_CON, trace.getManContact());
        values.put(L_W_TIME, trace.getLastWorkTime());
        values.put(SYMPTOMS, trace.getSymptoms());
        values.put(MED_HIS, trace.getMedHistory());
        values.put(TESTED_COV, trace.getTestedCovid());
        values.put(TESTED_DATE, trace.getTestedDate());
        values.put(TESTED_HOS, trace.getTestedHospital());
        values.put(TESTED_RES, trace.getTestedResult());
        values.put(TRAVELLED, trace.getTravelled());
        values.put(TRAV_HIS, trace.getTravelHistory());
        values.put(REC_CON, trace.getRecentContacts());
        values.put(SAMPLE_TAKEN, trace.getSampleTaken());
        values.put(SAMPLE_DATE, trace.getSampleDate());
        values.put(SAMPLE_RES, trace.getSampleResult());
        values.put(ISO_DEC, trace.getIsoDecision());
        values.put(ISO_DATE, trace.getIsoFinishDate());
        values.put(REMARKS, trace.getRemarks());
        values.put(SENT, trace.getSent());

        // insert row
        try{
            id = db.insert(TABLE_TRACES, null, values);
        }
        catch (SQLException ex){
            //return ex.toString();
        }
        db.close();
        return id;
    }

    public boolean updateTrace(long id, mTrace trace) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(REF, trace.getRef());
        values.put(TRACE_ID, trace.getTraceId());
        values.put(USER_ID, trace.getUserId());
        values.put(F_NAME, trace.getFullName());
        values.put(DOB, trace.getDob());
        values.put(GENDER, trace.getGender());
        values.put(NATIONALITY, trace.getNationality());
        values.put(PHONE, trace.getPhone());
        values.put(M_STATUS, trace.getMaritalStatus());
        values.put(CHILDREN, trace.getChildren());
        values.put(TRANS_MODE, trace.getTransportMode());
        values.put(RES_ADDRESS, trace.getResAddress());
        values.put(RES_ARRANGEMENT, trace.getResArrangement());
        values.put(SHARED_FAC, trace.getSharedFacilities());
        values.put(RES_POP, trace.getResPopulation());
        values.put(EMP_STATUS, trace.getEmpStatus());
        values.put(EMPLOYER, trace.getEmployer());
        values.put(WORK_LOC, trace.getWorkLoc());
        values.put(MANAGER_CON, trace.getManContact());
        values.put(L_W_TIME, trace.getLastWorkTime());
        values.put(SYMPTOMS, trace.getSymptoms());
        values.put(MED_HIS, trace.getMedHistory());
        values.put(TESTED_COV, trace.getTestedCovid());
        values.put(TESTED_DATE, trace.getTestedDate());
        values.put(TESTED_HOS, trace.getTestedHospital());
        values.put(TESTED_RES, trace.getTestedResult());
        values.put(TRAVELLED, trace.getTravelled());
        values.put(TRAV_HIS, trace.getTravelHistory());
        values.put(REC_CON, trace.getRecentContacts());
        values.put(SAMPLE_TAKEN, trace.getSampleTaken());
        values.put(SAMPLE_DATE, trace.getSampleDate());
        values.put(SAMPLE_RES, trace.getSampleResult());
        values.put(ISO_DEC, trace.getIsoDecision());
        values.put(ISO_DATE, trace.getIsoFinishDate());
        values.put(REMARKS, trace.getRemarks());
        values.put(SENT, trace.getSent());
        db.update(TABLE_TRACES, values, ID + " = " + id, null);
        return true;
    }

    public List<mTrace> getUnsentTraces(){
        List<mTrace> traces = new ArrayList<mTrace>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_TRACES + " WHERE " + SENT + " = 0";

        Cursor c = db.rawQuery(query, null);

        try {
            while (c.moveToNext()) {
                mTrace trace = new mTrace();
                trace.setId(c.getInt(c.getColumnIndex(ID)));
                trace.setRef(c.getString(c.getColumnIndex(REF)));
                trace.setTraceId((c.getInt(c.getColumnIndex(TRACE_ID))));
                trace.setUserId((c.getInt(c.getColumnIndex(USER_ID))));
                trace.setFullName((c.getString(c.getColumnIndex(F_NAME))));
                trace.setDob((c.getString(c.getColumnIndex(DOB))));
                trace.setGender((c.getString(c.getColumnIndex(GENDER))));
                trace.setNationality((c.getString(c.getColumnIndex(NATIONALITY))));
                trace.setPhone((c.getString(c.getColumnIndex(PHONE))));
                trace.setMaritalStatus((c.getString(c.getColumnIndex(M_STATUS))));
                trace.setChildren((c.getString(c.getColumnIndex(CHILDREN))));
                trace.setTransportMode((c.getString(c.getColumnIndex(TRANS_MODE))));
                trace.setResAddress(c.getString(c.getColumnIndex(RES_ADDRESS)));
                trace.setResArrangement(c.getString(c.getColumnIndex(RES_ARRANGEMENT)));
                trace.setSharedFacilities(c.getString(c.getColumnIndex(SHARED_FAC)));
                trace.setResPopulation(c.getString(c.getColumnIndex(RES_POP)));
                trace.setEmpStatus(c.getString(c.getColumnIndex(EMP_STATUS)));
                trace.setEmployer(c.getString(c.getColumnIndex(EMPLOYER)));
                trace.setWorkLoc(c.getString(c.getColumnIndex(WORK_LOC)));
                trace.setManContact(c.getString(c.getColumnIndex(MANAGER_CON)));
                trace.setLastWorkTime(c.getString(c.getColumnIndex(L_W_TIME)));
                trace.setSymptoms(c.getString(c.getColumnIndex(SYMPTOMS)));
                trace.setMedHistory(c.getString(c.getColumnIndex(MED_HIS)));
                trace.setTestedCovid(c.getString(c.getColumnIndex(TESTED_COV)));
                trace.setTestedDate(c.getString(c.getColumnIndex(TESTED_DATE)));
                trace.setTestedHospital(c.getString(c.getColumnIndex(TESTED_HOS)));
                trace.setTestedResult(c.getString(c.getColumnIndex(TESTED_RES)));
                trace.setTravelled(c.getString(c.getColumnIndex(TRAVELLED)));
                trace.setTravelHistory(c.getString(c.getColumnIndex(TRAV_HIS)));
                trace.setRecentContacts(c.getString(c.getColumnIndex(REC_CON)));
                trace.setSampleTaken(c.getString(c.getColumnIndex(SAMPLE_TAKEN)));
                trace.setSampleDate(c.getString(c.getColumnIndex(SAMPLE_DATE)));
                trace.setSampleResult(c.getString(c.getColumnIndex(SAMPLE_RES)));
                trace.setIsoDecision(c.getString(c.getColumnIndex(ISO_DEC)));
                trace.setIsoFinishDate(c.getString(c.getColumnIndex(ISO_DATE)));
                trace.setRemarks(c.getString(c.getColumnIndex(REMARKS)));
                trace.setSent(c.getInt(c.getColumnIndex(SENT)));

                traces.add(trace);
            }
        } finally {
            c.close();
        }
        db.close();
        return traces;
    }
}
