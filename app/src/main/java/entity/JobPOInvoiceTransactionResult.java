package entity;

import org.json.JSONObject;

import utility.Logger;

/**
 * Created by SAI on 10/24/2016.
 */
public class JobPOInvoiceTransactionResult {
    boolean success;
    String message, transactionId, linkn;

    public JobPOInvoiceTransactionResult(boolean success, String message, String transactionId, String linkn) {
        this.success = success;
        this.message = message;
        this.transactionId = transactionId;
        this.linkn = linkn;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getLinkn() {
        return linkn;
    }

    public void setLinkn(String linkn) {
        this.linkn = linkn;
    }

    public static JobPOInvoiceTransactionResult parseResult(String strJSON) {
        try {
            JSONObject objJSON = new JSONObject(strJSON);
            boolean success = objJSON.getBoolean("success");
            String message = objJSON.getString("message");
            String transactionId = objJSON.has("transactionId") ? objJSON.getString("transactionId") : "";
            String linkn = objJSON.has("linkn") ? objJSON.getString("linkn") : "";
            return new JobPOInvoiceTransactionResult(success, message, transactionId, linkn);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            return null;
        }
    }
}
