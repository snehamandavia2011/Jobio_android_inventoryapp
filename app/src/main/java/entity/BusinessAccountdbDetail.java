package entity;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import utility.Logger;
import utility.Helper;

/**
 * Created by SAI on 12/14/2015.
 */
public class BusinessAccountdbDetail {
    private String subDomain;
    private String accountId;

    public BusinessAccountdbDetail() {
    }

    public final class Fields {
        public static final String SUB_DOMAIM = "subDomain";
        public static final String ACCOUNT_ID = "account_id";
    }

    public void display() {
        Logger.debug(".....................BusinessAccountdbDetail...........................");
        Logger.debug("subDomain:" + subDomain);
    }

    public void parseJSON(JSONObject objJSON) throws JSONException {
        this.setSubDomain(objJSON.getString("sub_domain"));
        this.setAccountId(objJSON.getString("account_id"));
    }

    public void saveFiledsToPreferences(Context c) {
        Helper.setStringPreference(c, Fields.SUB_DOMAIM, getSubDomain());
        Helper.setStringPreference(c, Fields.ACCOUNT_ID, getAccountId());
    }

    public static void clearCache(Context c) {
        Helper.clearPreference(c, Fields.SUB_DOMAIM);
        Helper.clearPreference(c, Fields.ACCOUNT_ID);
    }

    public String getSubDomain() {
        return subDomain;
    }

    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
