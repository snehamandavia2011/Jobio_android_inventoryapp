package entity;

import android.content.Context;

import utility.ConstantVal;
import utility.Helper;

/**
 * Created by SAI on 12/14/2015.
 */
public class ClientLoginUser {
    String userName, password, token;
    boolean isSessionExpire;

    public ClientLoginUser() {
    }

    public ClientLoginUser(String userName, String password, String token, boolean isSessionExpire) {
        this.userName = userName;
        this.password = password;
        this.token = token;
        this.isSessionExpire = isSessionExpire;
    }

    public ClientLoginUser(String userName, String password, boolean isSessionExpire) {
        this.userName = userName;
        this.password = password;
        this.isSessionExpire = isSessionExpire;
    }

    public boolean isSessionExpire() {
        return isSessionExpire;
    }

    public void setIsSessionExpire(boolean isSessionExpire) {
        this.isSessionExpire = isSessionExpire;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void saveDatatoPreference(Context c) {
        Helper.setStringPreference(c, ClientAdminUser.Fields.USER_NAME, this.getUserName());
        Helper.setStringPreference(c, ClientAdminUser.Fields.PASSWORD, this.getPassword());
        Helper.setStringPreference(c, ConstantVal.TOKEN, this.getToken());
        Helper.setBooleanPreference(c, ConstantVal.IS_SESSION_EXISTS, true);
//        Helper.setStringPreference(c, ConstantVal.QRCODE_VALUE, this.getQRCode());
//        Helper.setBooleanPreference(c, ConstantVal.IS_QRCODE_CONFIGURE, true);
    }

    public static void clearCache(Context c) {
        BusinessAccountdbDetail.clearCache(c);
        BusinessAccountMaster.clearCache(c);
        ClientStockSelection.clearCache(c);
        ClientAdminUser.clearCache(c);
        ClientAdminUserAppsRel.clearCache(c);
        ClientEmployeeMaster.clearCache(c);
        //ClientLocationTrackingInterval.clearCache(c);
        Helper.clearPreference(c, ConstantVal.TOKEN);
        Helper.clearPreference(c, ConstantVal.IS_SESSION_EXISTS);
    }
}
