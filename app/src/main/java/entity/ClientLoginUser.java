package entity;

import android.content.Context;

import utility.ConstantVal;
import utility.Helper;

/**
 * Created by SAI on 12/14/2015.
 */
public class ClientLoginUser {
    String userName, password, token;
    int tokenId;
    boolean isSessionExpire;

    String deviceName, osName;

    public ClientLoginUser() {
    }

    public ClientLoginUser(String userName, String password, String token, int tokenId, boolean isSessionExpire) {
        this.userName = userName;
        this.password = password;
        this.token = token;
        this.tokenId = tokenId;
        this.isSessionExpire = isSessionExpire;
    }

    public ClientLoginUser(String userName, String password, boolean isSessionExpire) {
        this.userName = userName;
        this.password = password;
        this.isSessionExpire = isSessionExpire;
    }

    public ClientLoginUser(String deviceName, String osName, String token) {
        this.deviceName = deviceName;
        this.osName = osName;
        this.token = token;
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

    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public void saveDatatoPreference(Context c) {
        Helper.setStringPreference(c, ClientAdminUser.Fields.USER_NAME, this.getUserName());
        Helper.setStringPreference(c, ClientAdminUser.Fields.PASSWORD, this.getPassword());
        Helper.setStringPreference(c, ConstantVal.TOKEN, this.getToken());
        Helper.setIntPreference(c, ConstantVal.TOKEN_ID, this.getTokenId());
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
        Helper.clearPreference(c, ConstantVal.TOKEN_ID);
        Helper.clearPreference(c, ConstantVal.IS_SESSION_EXISTS);
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }
}
