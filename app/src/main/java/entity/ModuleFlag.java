package entity;

import utility.Logger;

/**
 * Created by SAI on 12/19/2015.
 */
public class ModuleFlag {
    private int localPKModuleId;
    private String moduleName, serverPKModuleId, access;

    public ModuleFlag(int localPKModuleId, String serverPKModuleId, String moduleName, String access) {
        this.localPKModuleId = localPKModuleId;
        this.moduleName = moduleName;
        this.serverPKModuleId = serverPKModuleId;
        this.access = access;
    }

    public ModuleFlag(String serverPKModuleId, String moduleName, String access) {
        this.moduleName = moduleName;
        this.serverPKModuleId = serverPKModuleId;
        this.access = access;
    }

    public void display() {
        Logger.debug("....................................");
        Logger.debug("ModuleName:" + moduleName);
        Logger.debug("serverPKModuleId:" + serverPKModuleId);
    }

    public int getLocalPKModuleId() {
        return localPKModuleId;
    }

    public void setLocalPKModuleId(int localPKModuleId) {
        this.localPKModuleId = localPKModuleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getServerPKModuleId() {
        return serverPKModuleId;
    }

    public void setServerPKModuleId(String serverPKModuleId) {
        this.serverPKModuleId = serverPKModuleId;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }
}
