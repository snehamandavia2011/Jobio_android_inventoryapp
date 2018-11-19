package entity;

/**
 * Created by SAI on 9/13/2016.
 */
public class ClientAssetOwner {
    String aoEmployee_id, aoStart_date, aoEnd_date;

    public ClientAssetOwner(String aoEmployee_id, String aoStart_date, String aoEnd_date) {
        this.aoEmployee_id = aoEmployee_id;
        this.aoStart_date = aoStart_date;
        this.aoEnd_date = aoEnd_date;
    }

    public String getAoEmployee_id() {
        return aoEmployee_id;
    }

    public void setAoEmployee_id(String aoEmployee_id) {
        this.aoEmployee_id = aoEmployee_id;
    }

    public String getAoStart_date() {
        return aoStart_date;
    }

    public void setAoStart_date(String aoStart_date) {
        this.aoStart_date = aoStart_date;
    }

    public String getAoEnd_date() {
        return aoEnd_date;
    }

    public void setAoEnd_date(String aoEnd_date) {
        this.aoEnd_date = aoEnd_date;
    }
}

