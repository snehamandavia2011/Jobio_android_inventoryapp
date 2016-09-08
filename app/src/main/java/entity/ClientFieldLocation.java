package entity;

import android.util.Log;

import utility.Logger;

/**
 * Created by SAI on 1/5/2016.
 */
public class ClientFieldLocation {
    int id, jobId;
    String reverseGeoCodeName, appType, userId;
    MyLocation objMyLocation;

    public ClientFieldLocation(int id, String userId, int jobId, String reverseGeoCodeName, String appType, MyLocation objMyLocation) {
        this.id = id;
        this.userId = userId;
        this.jobId = jobId;
        this.reverseGeoCodeName = reverseGeoCodeName;
        this.appType = appType;
        this.objMyLocation = objMyLocation;
    }

    public void display() {
        Logger.debug("....................In Field Location................");
        Logger.debug("userId:" + userId);
        Logger.debug("jobId:" + jobId);
        Logger.debug("reverseGeoCodeName:" + reverseGeoCodeName);
        Logger.debug("appType:" + appType);
        if (objMyLocation != null) {
            //objMyLocation.display();
        }
        Logger.debug("......................................................");
    }

    public ClientFieldLocation(MyLocation objMyLocation) {
        this.objMyLocation = objMyLocation;
    }

    public ClientFieldLocation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getReverseGeoCodeName() {
        return reverseGeoCodeName;
    }

    public void setReverseGeoCodeName(String reverseGeoCodeName) {
        this.reverseGeoCodeName = reverseGeoCodeName;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public MyLocation getObjMyLocation() {
        return objMyLocation;
    }

    public void setObjMyLocation(MyLocation objMyLocation) {
        this.objMyLocation = objMyLocation;
    }
}
