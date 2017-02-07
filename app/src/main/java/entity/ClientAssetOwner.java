package entity;

/**
 * Created by SAI on 9/13/2016.
 */
public class ClientAssetOwner {

    public static class CheckInOut {
        public static String CHECK_IN = "CHECK IN";
        public static String CHECK_OUT = "CHECK OUT";
    }

    String aoEmployee_id, aoInOut,aoDate;

    public ClientAssetOwner(String aoEmployee_id, String aoInOut, String aoDate) {
        this.aoEmployee_id = aoEmployee_id;
        this.aoInOut = aoInOut;
        this.aoDate = aoDate;
    }

    public String getAoEmployee_id() {
        return aoEmployee_id;
    }

    public void setAoEmployee_id(String aoEmployee_id) {
        this.aoEmployee_id = aoEmployee_id;
    }

    public String getAoInOut() {
        return aoInOut;
    }

    public void setAoInOut(String aoInOut) {
        this.aoInOut = aoInOut;
    }

    public String getAoDate() {
        return aoDate;
    }

    public void setAoDate(String aoDate) {
        this.aoDate = aoDate;
    }
}

