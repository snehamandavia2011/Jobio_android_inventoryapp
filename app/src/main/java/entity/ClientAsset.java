package entity;

import java.util.ArrayList;

/**
 * Created by SAI on 9/13/2016.
 */
public class ClientAsset {
    String aoAsset_id, actmCategory_name,
            amAsset_name, amDescription, amModel_name, amManufacturer_name, amSerial_no, amBarcode_no,
            amDate_acquired, amDate_soon, amPurchase_cost, amPurchase_from, amCurrent_value, amDate_expired, amAsset_location, amService_period, amIs_schedule_service_on, amService_aasigned_employee,
            amInspection_period, amIs_schedule_inspection_on, amInspection_aasigned_employee, amNext_service_date,
            amNext_inspection_date, amAsset_status, amCustom_field_1, amCustom_field_2, amCustom_field_3, amCustom_field_4, amCustom_field_5;
    ArrayList<ClientAssetOwner> arrOwner;

    public ClientAsset() {
    }

    public ClientAsset(String aoAsset_id, String actmCategory_name, String amAsset_name, String amDescription, String amModel_name, String amManufacturer_name, String amSerial_no, String amBarcode_no, String amDate_acquired, String amDate_soon, String amPurchase_cost, String amPurchase_from, String amCurrent_value, String amDate_expired, String amAsset_location, String amService_period, String amIs_schedule_service_on, String amService_aasigned_employee, String amInspection_period, String amIs_schedule_inspection_on, String amInspection_aasigned_employee, String amNext_service_date, String amNext_inspection_date, String amAsset_status, String amCustom_field_1, String amCustom_field_2, String amCustom_field_3, String amCustom_field_4, String amCustom_field_5, ArrayList<ClientAssetOwner> arrOwner) {
        this.aoAsset_id = aoAsset_id;
        this.actmCategory_name = actmCategory_name;
        this.amAsset_name = amAsset_name;
        this.amDescription = amDescription;
        this.amModel_name = amModel_name;
        this.amManufacturer_name = amManufacturer_name;
        this.amSerial_no = amSerial_no;
        this.amBarcode_no = amBarcode_no;
        this.amDate_acquired = amDate_acquired;
        this.amDate_soon = amDate_soon;
        this.amPurchase_cost = amPurchase_cost;
        this.amPurchase_from = amPurchase_from;
        this.amCurrent_value = amCurrent_value;
        this.amDate_expired = amDate_expired;
        this.amAsset_location = amAsset_location;
        this.amService_period = amService_period;
        this.amIs_schedule_service_on = amIs_schedule_service_on;
        this.amService_aasigned_employee = amService_aasigned_employee;
        this.amInspection_period = amInspection_period;
        this.amIs_schedule_inspection_on = amIs_schedule_inspection_on;
        this.amInspection_aasigned_employee = amInspection_aasigned_employee;
        this.amNext_service_date = amNext_service_date;
        this.amNext_inspection_date = amNext_inspection_date;
        this.amAsset_status = amAsset_status;
        this.amCustom_field_1 = amCustom_field_1;
        this.amCustom_field_2 = amCustom_field_2;
        this.amCustom_field_3 = amCustom_field_3;
        this.amCustom_field_4 = amCustom_field_4;
        this.amCustom_field_5 = amCustom_field_5;
        this.arrOwner = arrOwner;
    }

    public String getAoAsset_id() {
        return aoAsset_id;
    }

    public void setAoAsset_id(String aoAsset_id) {
        this.aoAsset_id = aoAsset_id;
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

    public String getAmDate_acquired() {
        return amDate_acquired;
    }

    public void setAmDate_acquired(String amDate_acquired) {
        this.amDate_acquired = amDate_acquired;
    }

    public String getAmDate_soon() {
        return amDate_soon;
    }

    public void setAmDate_soon(String amDate_soon) {
        this.amDate_soon = amDate_soon;
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

    public String getAmDate_expired() {
        return amDate_expired;
    }

    public void setAmDate_expired(String amDate_expired) {
        this.amDate_expired = amDate_expired;
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

    public String getAmNext_service_date() {
        return amNext_service_date;
    }

    public void setAmNext_service_date(String amNext_service_date) {
        this.amNext_service_date = amNext_service_date;
    }

    public String getAmNext_inspection_date() {
        return amNext_inspection_date;
    }

    public void setAmNext_inspection_date(String amNext_inspection_date) {
        this.amNext_inspection_date = amNext_inspection_date;
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

    public ArrayList<ClientAssetOwner> getArrOwner() {
        return arrOwner;
    }

    public void setArrOwner(ArrayList<ClientAssetOwner> arrOwner) {
        this.arrOwner = arrOwner;
    }

}
