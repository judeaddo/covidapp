package com.digitelgh.apps.covid_19tracer.model;

public class mTrace {
    int id, traceId, userId, sharedNum, sent;
    String ref, fullName, dob, gender, nationality, phone, maritalStatus, children, transportMode, resAddress, resArrangement, resPopulation, sharedFacilities, empStatus, employer, workLoc, manContact, lastWorkTime,
            symptoms, medHistory, testedCovid, testedDate, testedHospital, testedResult, travelled, travelHistory, recentContacts, sampleTaken, sampleDate, sampleResult, isoDecision, isoFinishDate, remarks;

    public mTrace(){}

    public mTrace(String ref, int traceId, int userId, String fullName, String dob, String gender, String nationality, String phone, String maritalStatus, String children,
                  String transportMode, String resAddress, String resArrangement, String sharedFacilities, String resPopulation, String empStatus, String employer,
                  String workLoc, String manContact, String lastWorkTime, String symptoms, String medHistory, String testedCovid, String testedDate, String testedHospital, String testedResult,
                  String travelled, String travelHistory, String recentContacts, String sampleTaken, String sampleDate, String sampleResult, String isoDecision, String isoFinishDate, String remarks, int sent) {
        this.ref = ref;
        this.traceId = traceId;
        this.userId = userId;
        this.fullName = fullName;
        this.dob = dob;
        this.gender = gender;
        this.nationality = nationality;
        this.phone = phone;
        this.maritalStatus = maritalStatus;
        this.children = children;
        this.transportMode = transportMode;
        this.resAddress = resAddress;
        this.resArrangement = resArrangement;
        this.sharedFacilities = sharedFacilities;
        this.resPopulation = resPopulation;
        this.empStatus = empStatus;
        this.employer = employer;
        this.workLoc = workLoc;
        this.manContact = manContact;
        this.lastWorkTime = lastWorkTime;
        this.symptoms = symptoms;
        this.medHistory = medHistory;
        this.testedCovid = testedCovid;
        this.testedDate = testedDate;
        this.testedHospital = testedHospital;
        this.testedResult = testedResult;
        this.travelled = travelled;
        this.travelHistory = travelHistory;
        this.recentContacts = recentContacts;
        this.sampleTaken = sampleTaken;
        this.sampleDate = sampleDate;
        this.sampleResult = sampleResult;
        this.isoDecision = isoDecision;
        this.isoFinishDate = isoFinishDate;
        this.remarks = remarks;
        this.sent = sent;
    }

    public String getChildren() {
        return children;
    }

    public int getId() {
        return id;
    }

    public String getResPopulation() {
        return resPopulation;
    }

    public int getSent() {
        return sent;
    }

    public int getSharedNum() {
        return sharedNum;
    }

    public int getTraceId() {
        return traceId;
    }

    public int getUserId() {
        return userId;
    }

    public String getDob() {
        return dob;
    }

    public String getEmployer() {
        return employer;
    }

    public String getEmpStatus() {
        return empStatus;
    }

    public String getFullName() {
        return fullName;
    }

    public String getGender() {
        return gender;
    }

    public String getManContact() {
        return manContact;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public String getNationality() {
        return nationality;
    }

    public String getPhone() {
        return phone;
    }

    public String getLastWorkTime() {
        return lastWorkTime;
    }

    public String getRef() {
        return ref;
    }

    public String getResAddress() {
        return resAddress;
    }

    public String getResArrangement() {
        return resArrangement;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public String getSharedFacilities() {
        return sharedFacilities;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWorkLoc() {
        return workLoc;
    }

    public void setEmpStatus(String empStatus) {
        this.empStatus = empStatus;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getMedHistory() {
        return medHistory;
    }

    public void setLastWorkTime(String lastWorkTime) {
        this.lastWorkTime = lastWorkTime;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setManContact(String manContact) {
        this.manContact = manContact;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public void setMedHistory(String medHistory) {
        this.medHistory = medHistory;
    }

    public void setResAddress(String resAddress) {
        this.resAddress = resAddress;
    }

    public String getTestedCovid() {
        return testedCovid;
    }

    public void setResArrangement(String resArrangement) {
        this.resArrangement = resArrangement;
    }

    public void setResPopulation(String resPopulation) {
        this.resPopulation = resPopulation;
    }

    public void setSent(int sent) {
        this.sent = sent;
    }

    public void setSharedFacilities(String sharedFacilities) {
        this.sharedFacilities = sharedFacilities;
    }

    public void setSharedNum(int sharedNum) {
        this.sharedNum = sharedNum;
    }

    public void setTraceId(int traceId) {
        this.traceId = traceId;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public void setTestedCovid(String testedCovid) {
        this.testedCovid = testedCovid;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTestedDate() {
        return testedDate;
    }

    public void setWorkLoc(String workLoc) {
        this.workLoc = workLoc;
    }

    public String getIsoDecision() {
        return isoDecision;
    }

    public void setTestedDate(String testedDate) {
        this.testedDate = testedDate;
    }

    public String getIsoFinishDate() {
        return isoFinishDate;
    }

    public String getRecentContacts() {
        return recentContacts;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getTestedHospital() {
        return testedHospital;
    }

    public String getSampleDate() {
        return sampleDate;
    }

    public String getSampleTaken() {
        return sampleTaken;
    }

    public String getTestedResult() {
        return testedResult;
    }

    public void setTestedHospital(String testedHospital) {
        this.testedHospital = testedHospital;
    }

    public String getSampleResult() {
        return sampleResult;
    }

    public String getTravelHistory() {
        return travelHistory;
    }

    public void setTestedResult(String testedResult) {
        this.testedResult = testedResult;
    }

    public String getTravelled() {
        return travelled;
    }

    public void setIsoDecision(String isoDecision) {
        this.isoDecision = isoDecision;
    }

    public void setIsoFinishDate(String isoFinishDate) {
        this.isoFinishDate = isoFinishDate;
    }

    public void setRecentContacts(String recentContacts) {
        this.recentContacts = recentContacts;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setSampleDate(String sampleDate) {
        this.sampleDate = sampleDate;
    }

    public void setSampleTaken(String sampleTaken) {
        this.sampleTaken = sampleTaken;
    }

    public void setSampleResult(String sampleResult) {
        this.sampleResult = sampleResult;
    }

    public void setTravelHistory(String travelHistory) {
        this.travelHistory = travelHistory;
    }

    public void setTravelled(String travelled) {
        this.travelled = travelled;
    }
}
