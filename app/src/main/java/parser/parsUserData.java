package parser;

import org.json.JSONException;
import org.json.JSONObject;

import entity.BusinessAccountMaster;
import entity.BusinessAccountdbDetail;
import entity.ClientAdminUser;
import entity.ClientAdminUserAppsRel;
import entity.ClientEmployeeMaster;
import entity.ClientStockSelection;
import entity.UserData;

/**
 * Created by SAI on 1/1/2016.
 */
public class parsUserData {
    public static UserData parseJSON(String JSONString) throws JSONException {
        ClientAdminUser objClientAdminUser = new ClientAdminUser();
        ClientEmployeeMaster objClientEmploeeMaster = new ClientEmployeeMaster();
        ClientAdminUserAppsRel objClientAdminAppRel = new ClientAdminUserAppsRel();
        ClientStockSelection objClientStockSelection = new ClientStockSelection();
        BusinessAccountdbDetail objBusinessAccountDbDetail = new BusinessAccountdbDetail();
        BusinessAccountMaster objBusinessAccountMaster = new BusinessAccountMaster();
        JSONObject objJSON = new JSONObject(JSONString);
        objClientAdminUser.parseJSON(objJSON);
        objClientAdminAppRel.parseJSON(objJSON);
        objClientEmploeeMaster.parseJSON(objJSON);
        objClientStockSelection.parseJSON(objJSON);
        objBusinessAccountDbDetail.parseJSON(objJSON);
        objBusinessAccountMaster.parseJSON(objJSON);
        UserData objUserData = new UserData(objBusinessAccountDbDetail, objBusinessAccountMaster, objClientAdminUser, objClientAdminAppRel, objClientEmploeeMaster, objClientStockSelection);
        return objUserData;
    }
}
