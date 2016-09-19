package entity;

import java.util.Date;

import utility.Logger;

/**
 * Created by SAI on 9/19/2016.
 */
public class InspectTransaction {
    private String inspectionName, assetId, assetName, assetBarcode, assignedTo, isPresent, note, photo, statusId;
    private Date date_time, assignedDate;

    public void display() {
        Logger.debug("inspectionName:" + inspectionName);
        Logger.debug("assetId:" + assetId);
        Logger.debug("assetName:" + assetName);
        Logger.debug("assetName:" + assetBarcode);
        Logger.debug("assignedTo:" + assignedTo);
        Logger.debug("isPresent:" + isPresent);
        Logger.debug("note:" + note);
        Logger.debug("photo:" + photo);
        Logger.debug("statusId:" + statusId);
        Logger.debug("date_time:" + date_time.toString());
        Logger.debug("assignedDate:" + assignedDate);
    }

    public InspectTransaction() {
    }

    public String getInspectionName() {
        return inspectionName;
    }

    public void setInspectionName(String inspectionName) {
        this.inspectionName = inspectionName;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetBarcode() {
        return assetBarcode;
    }

    public void setAssetBarcode(String assetBarcode) {
        this.assetBarcode = assetBarcode;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getIsPresent() {
        return isPresent;
    }

    public void setIsPresent(String isPresent) {
        this.isPresent = isPresent;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public Date getDate_time() {
        return date_time;
    }

    public void setDate_time(Date date_time) {
        this.date_time = date_time;
    }

    public Date getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }
}
