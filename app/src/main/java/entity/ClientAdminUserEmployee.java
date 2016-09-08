package entity;

import java.util.Date;

/**
 * Created by SAI on 6/17/2016.
 */
public class ClientAdminUserEmployee {
    private String auId, empId;
    private String first_name, last_name, employee_status, contact_no, photo, user_type_name, isOnLine;
    private int unreadMessageCount;
    private boolean isImageLoaded = false;

    public ClientAdminUserEmployee(String auId, String empId, String first_name, String last_name, String employee_status, String contact_no, String photo, String user_type_name, String isOnLine) {
        this.auId = auId;
        this.empId = empId;
        this.first_name = first_name;
        this.last_name = last_name;
        this.employee_status = employee_status;
        this.contact_no = contact_no;
        this.photo = photo;
        this.user_type_name = user_type_name;
        this.isOnLine = isOnLine;
    }

    public ClientAdminUserEmployee(String auId, String empId, String first_name, String last_name, String employee_status, String contact_no, String photo, String user_type_name, String isOnLine, int unreadMessageCount) {
        this.auId = auId;
        this.empId = empId;
        this.first_name = first_name;
        this.last_name = last_name;
        this.employee_status = employee_status;
        this.contact_no = contact_no;
        this.photo = photo;
        this.user_type_name = user_type_name;
        this.isOnLine = isOnLine;
        this.unreadMessageCount = unreadMessageCount;
    }

    public String getAuId() {
        return auId;
    }

    public void setAuId(String auId) {
        this.auId = auId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmployee_status() {
        return employee_status;
    }

    public void setEmployee_status(String employee_status) {
        this.employee_status = employee_status;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getUser_type_name() {
        return user_type_name;
    }

    public void setUser_type_name(String user_type_name) {
        this.user_type_name = user_type_name;
    }

    public String getIsOnLine() {
        return isOnLine;
    }

    public void setIsOnLine(String isOnLine) {
        this.isOnLine = isOnLine;
    }

    public boolean isImageLoaded() {
        return isImageLoaded;
    }

    public void setIsImageLoaded(boolean isImageLoaded) {
        this.isImageLoaded = isImageLoaded;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(int unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }
}
