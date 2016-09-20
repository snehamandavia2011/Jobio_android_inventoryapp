package entity;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;

import utility.DataBase;
import utility.Logger;

/**
 * Created by SAI on 9/13/2016.
 */
public class ClientAssetInspect {
    String aitId, aitName, aitAssetId, aitAsset_name, aitAssetBarcode, aitAssignedTo, aitIsPresent,
            aitNote, aitStatusId, photo, actmCategory_name,
            amAsset_name, amDescription, amModel_name, amManufacturer_name, amSerial_no, amBarcode_no,
            amPurchase_cost, amPurchase_from, amCurrent_value, amAsset_location, amService_period, amIs_schedule_service_on, amService_aasigned_employee,
            amInspection_period, amIs_schedule_inspection_on, amInspection_aasigned_employee,
            amAsset_status, amCustom_field_1, amCustom_field_2, amCustom_field_3, amCustom_field_4, amCustom_field_5;
    Date aitAssignedDate, aitDateTime, amDate_acquired, amDate_soon, amDate_expired, amNext_service_date, amNext_inspection_date;
    int viewStatus;

    public void display() {
        Logger.debug(aitName);
        Logger.debug(aitAssetId);
        Logger.debug(aitAsset_name);
        Logger.debug(aitAssetBarcode);
        Logger.debug(aitAssignedTo);
        Logger.debug(aitIsPresent);
        Logger.debug(aitNote);
        Logger.debug(aitStatusId);
        Logger.debug(actmCategory_name);
        Logger.debug(amAsset_name);
        Logger.debug(amDescription);
        Logger.debug(amModel_name);
        Logger.debug(amManufacturer_name);
        Logger.debug(amSerial_no);
        Logger.debug(amBarcode_no);
        Logger.debug(amPurchase_cost);
        Logger.debug(amPurchase_from);
        Logger.debug(amCurrent_value);
        Logger.debug(amAsset_location);
        Logger.debug(amService_period);
        Logger.debug(amIs_schedule_service_on);
        Logger.debug(amService_aasigned_employee);
        Logger.debug(amInspection_period);
        Logger.debug(amIs_schedule_inspection_on);
        Logger.debug(amInspection_aasigned_employee);
        Logger.debug(amAsset_status);
        Logger.debug(amCustom_field_1);
        Logger.debug(amCustom_field_2);
        Logger.debug(amCustom_field_3);
        Logger.debug(amCustom_field_4);
        Logger.debug(amCustom_field_5);
        Logger.debug(aitAssignedDate.toString());
        Logger.debug(aitDateTime.toString());
        Logger.debug(amDate_acquired.toString());
        Logger.debug(amDate_soon.toString());
        Logger.debug(amDate_expired.toString());
        Logger.debug(amNext_service_date.toString());
        Logger.debug(amNext_inspection_date.toString());
        Logger.debug(String.valueOf(viewStatus));
    }

    public ClientAssetInspect() {
    }

    public ClientAssetInspect(String aitId, String aitName, String aitAssetId, String aitAsset_name, String aitAssetBarcode, String aitAssignedTo,
                              Date aitAssignedDate, String aitIsPresent, Date aitDateTime, String aitNote, String photo, String aitStatusId,
                              String actmCategory_name, String amAsset_name, String amDescription, String amModel_name,
                              String amManufacturer_name, String amSerial_no, String amBarcode_no, Date amDate_acquired,
                              Date amDate_soon, String amPurchase_cost, String amPurchase_from, String amCurrent_value,
                              Date amDate_expired, String amAsset_location, String amService_period, String amIs_schedule_service_on,
                              String amService_aasigned_employee, String amInspection_period, String amIs_schedule_inspection_on,
                              String amInspection_aasigned_employee, Date amNext_service_date, Date amNext_inspection_date, String amAsset_status, String amCustom_field_1,
                              String amCustom_field_2, String amCustom_field_3, String amCustom_field_4, String amCustom_field_5, int viewStatus) {
        this.aitId = aitId;
        this.aitName = aitName;
        this.aitAssetId = aitAssetId;
        this.aitAsset_name = aitAsset_name;
        this.aitAssetBarcode = aitAssetBarcode;
        this.aitAssignedTo = aitAssignedTo;
        this.aitAssignedDate = aitAssignedDate;
        this.aitIsPresent = aitIsPresent;
        this.aitDateTime = aitDateTime;
        this.aitNote = aitNote;
        this.aitStatusId = aitStatusId;
        this.photo = photo;
        this.actmCategory_name = actmCategory_name;
        this.amAsset_name = amAsset_name;
        this.amDescription = amDescription;
        this.amModel_name = amModel_name;
        this.amManufacturer_name = amManufacturer_name;
        this.amSerial_no = amSerial_no;
        this.amBarcode_no = amBarcode_no;
        this.amPurchase_cost = amPurchase_cost;
        this.amPurchase_from = amPurchase_from;
        this.amCurrent_value = amCurrent_value;
        this.amAsset_location = amAsset_location;
        this.amService_period = amService_period;
        this.amIs_schedule_service_on = amIs_schedule_service_on;
        this.amService_aasigned_employee = amService_aasigned_employee;
        this.amInspection_period = amInspection_period;
        this.amIs_schedule_inspection_on = amIs_schedule_inspection_on;
        this.amInspection_aasigned_employee = amInspection_aasigned_employee;
        this.amAsset_status = amAsset_status;
        this.amCustom_field_1 = amCustom_field_1;
        this.amCustom_field_2 = amCustom_field_2;
        this.amCustom_field_3 = amCustom_field_3;
        this.amCustom_field_4 = amCustom_field_4;
        this.amCustom_field_5 = amCustom_field_5;
        this.amDate_acquired = amDate_acquired;
        this.amDate_soon = amDate_soon;
        this.amDate_expired = amDate_expired;
        this.amNext_service_date = amNext_service_date;
        this.amNext_inspection_date = amNext_inspection_date;
        this.viewStatus = viewStatus;
    }

    public String getAitName() {
        return aitName;
    }

    public void setAitName(String aitName) {
        this.aitName = aitName;
    }

    public String getAitAssetId() {
        return aitAssetId;
    }

    public void setAitAssetId(String aitAssetId) {
        this.aitAssetId = aitAssetId;
    }

    public String getAitAsset_name() {
        return aitAsset_name;
    }

    public void setAitAsset_name(String aitAsset_name) {
        this.aitAsset_name = aitAsset_name;
    }

    public String getAitAssetBarcode() {
        return aitAssetBarcode;
    }

    public void setAitAssetBarcode(String aitAssetBarcode) {
        this.aitAssetBarcode = aitAssetBarcode;
    }

    public String getAitAssignedTo() {
        return aitAssignedTo;
    }

    public void setAitAssignedTo(String aitAssignedTo) {
        this.aitAssignedTo = aitAssignedTo;
    }


    public String getAitIsPresent() {
        return aitIsPresent;
    }

    public void setAitIsPresent(String aitIsPresent) {
        this.aitIsPresent = aitIsPresent;
    }

    public String getAitNote() {
        return aitNote;
    }

    public void setAitNote(String aitNote) {
        this.aitNote = aitNote;
    }

    public String getAitStatusId() {
        return aitStatusId;
    }

    public void setAitStatusId(String aitStatusId) {
        this.aitStatusId = aitStatusId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getActmCategory_name() {
        return actmCategory_name;
    }

    public void setActmCategory_name(String actmCategory_name) {
        this.actmCategory_name = actmCategory_name;
    }

    public String getAmAsset_name() {
        return amAsset_name;
    }

    public void setAmAsset_name(String amAsset_name) {
        this.amAsset_name = amAsset_name;
    }

    public String getAmDescription() {
        return amDescription;
    }

    public void setAmDescription(String amDescription) {
        this.amDescription = amDescription;
    }

    public String getAmModel_name() {
        return amModel_name;
    }

    public void setAmModel_name(String amModel_name) {
        this.amModel_name = amModel_name;
    }

    public String getAmManufacturer_name() {
        return amManufacturer_name;
    }

    public void setAmManufacturer_name(String amManufacturer_name) {
        this.amManufacturer_name = amManufacturer_name;
    }

    public String getAmSerial_no() {
        return amSerial_no;
    }

    public void setAmSerial_no(String amSerial_no) {
        this.amSerial_no = amSerial_no;
    }

    public String getAmBarcode_no() {
        return amBarcode_no;
    }

    public void setAmBarcode_no(String amBarcode_no) {
        this.amBarcode_no = amBarcode_no;
    }

    public String getAmPurchase_cost() {
        return amPurchase_cost;
    }

    public void setAmPurchase_cost(String amPurchase_cost) {
        this.amPurchase_cost = amPurchase_cost;
    }

    public String getAmPurchase_from() {
        return amPurchase_from;
    }

    public void setAmPurchase_from(String amPurchase_from) {
        this.amPurchase_from = amPurchase_from;
    }

    public String getAmCurrent_value() {
        return amCurrent_value;
    }

    public void setAmCurrent_value(String amCurrent_value) {
        this.amCurrent_value = amCurrent_value;
    }

    public String getAmAsset_location() {
        return amAsset_location;
    }

    public void setAmAsset_location(String amAsset_location) {
        this.amAsset_location = amAsset_location;
    }

    public String getAmService_period() {
        return amService_period;
    }

    public void setAmService_period(String amService_period) {
        this.amService_period = amService_period;
    }

    public String getAmIs_schedule_service_on() {
        return amIs_schedule_service_on;
    }

    public void setAmIs_schedule_service_on(String amIs_schedule_service_on) {
        this.amIs_schedule_service_on = amIs_schedule_service_on;
    }

    public String getAmService_aasigned_employee() {
        return amService_aasigned_employee;
    }

    public void setAmService_aasigned_employee(String amService_aasigned_employee) {
        this.amService_aasigned_employee = amService_aasigned_employee;
    }

    public String getAmInspection_period() {
        return amInspection_period;
    }

    public void setAmInspection_period(String amInspection_period) {
        this.amInspection_period = amInspection_period;
    }

    public String getAmIs_schedule_inspection_on() {
        return amIs_schedule_inspection_on;
    }

    public void setAmIs_schedule_inspection_on(String amIs_schedule_inspection_on) {
        this.amIs_schedule_inspection_on = amIs_schedule_inspection_on;
    }

    public String getAmInspection_aasigned_employee() {
        return amInspection_aasigned_employee;
    }

    public void setAmInspection_aasigned_employee(String amInspection_aasigned_employee) {
        this.amInspection_aasigned_employee = amInspection_aasigned_employee;
    }

    public String getAmAsset_status() {
        return amAsset_status;
    }

    public void setAmAsset_status(String amAsset_status) {
        this.amAsset_status = amAsset_status;
    }

    public String getAmCustom_field_1() {
        return amCustom_field_1;
    }

    public void setAmCustom_field_1(String amCustom_field_1) {
        this.amCustom_field_1 = amCustom_field_1;
    }

    public String getAmCustom_field_2() {
        return amCustom_field_2;
    }

    public void setAmCustom_field_2(String amCustom_field_2) {
        this.amCustom_field_2 = amCustom_field_2;
    }

    public String getAmCustom_field_3() {
        return amCustom_field_3;
    }

    public void setAmCustom_field_3(String amCustom_field_3) {
        this.amCustom_field_3 = amCustom_field_3;
    }

    public String getAmCustom_field_4() {
        return amCustom_field_4;
    }

    public void setAmCustom_field_4(String amCustom_field_4) {
        this.amCustom_field_4 = amCustom_field_4;
    }

    public String getAmCustom_field_5() {
        return amCustom_field_5;
    }

    public void setAmCustom_field_5(String amCustom_field_5) {
        this.amCustom_field_5 = amCustom_field_5;
    }

    public Date getAmDate_acquired() {
        return amDate_acquired;
    }

    public void setAmDate_acquired(Date amDate_acquired) {
        this.amDate_acquired = amDate_acquired;
    }

    public Date getAmDate_soon() {
        return amDate_soon;
    }

    public void setAmDate_soon(Date amDate_soon) {
        this.amDate_soon = amDate_soon;
    }

    public Date getAmDate_expired() {
        return amDate_expired;
    }

    public void setAmDate_expired(Date amDate_expired) {
        this.amDate_expired = amDate_expired;
    }

    public Date getAmNext_service_date() {
        return amNext_service_date;
    }

    public void setAmNext_service_date(Date amNext_service_date) {
        this.amNext_service_date = amNext_service_date;
    }

    public Date getAmNext_inspection_date() {
        return amNext_inspection_date;
    }

    public void setAmNext_inspection_date(Date amNext_inspection_date) {
        this.amNext_inspection_date = amNext_inspection_date;
    }

    public int getViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(int viewStatus) {
        this.viewStatus = viewStatus;
    }

    public Date getAitAssignedDate() {
        return aitAssignedDate;
    }

    public void setAitAssignedDate(Date aitAssignedDate) {
        this.aitAssignedDate = aitAssignedDate;
    }

    public Date getAitDateTime() {
        return aitDateTime;
    }

    public void setAitDateTime(Date aitDateTime) {
        this.aitDateTime = aitDateTime;
    }

    public String getAitId() {
        return aitId;
    }

    public void setAitId(String aitId) {
        this.aitId = aitId;
    }

    public static ArrayList<ClientAssetInspect> getDataFromDatabase(Context mContext, String aitID) {
        ArrayList<ClientAssetInspect> arrClientAsset = null;
        DataBase db = new DataBase(mContext);
        db.open();
        Cursor cur;
        if (aitID == null)
            cur = db.fetch(DataBase.inspect_table, null);
        else
            cur = db.fetch(DataBase.inspect_table, "aitId=" + aitID);
        if (cur != null && cur.getCount() > 0) {
            arrClientAsset = new ArrayList<ClientAssetInspect>();
            cur.moveToFirst();
            do {
                int viewStatus = -1;
                Cursor inspectView = db.fetch(DataBase.inspect_view_table, DataBase.inspect_view_int, "aitId='" + cur.getString(1) + "'");
                if (inspectView != null && inspectView.getCount() > 0) {
                    inspectView.moveToFirst();
                    viewStatus = inspectView.getInt(2);
                }
                inspectView.close();
                arrClientAsset.add(new ClientAssetInspect(cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4), cur.getString(5), cur.getString(6),
                        new Date(cur.getLong(7)), cur.getString(8), new Date(cur.getLong(9)), cur.getString(10), cur.getString(11),
                        cur.getString(12), cur.getString(13), cur.getString(14), cur.getString(15), cur.getString(16),
                        cur.getString(17), cur.getString(18), cur.getString(19), new Date(cur.getLong(20)),
                        new Date(cur.getLong(21)), cur.getString(22), cur.getString(23), cur.getString(24),
                        new Date(cur.getLong(25)), cur.getString(26), cur.getString(27), cur.getString(28),
                        cur.getString(29), cur.getString(30), cur.getString(31),
                        cur.getString(32), new Date(cur.getLong(33)), new Date(cur.getLong(34)), cur.getString(35), cur.getString(36),
                        cur.getString(37), cur.getString(38), cur.getString(39), cur.getString(40), viewStatus));
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return arrClientAsset;
    }
}
