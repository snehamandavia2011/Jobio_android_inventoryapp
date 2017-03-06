package entity;

/**
 * Created by SAI on 1/1/2016.
 */
public class UserData {
    private BusinessAccountdbDetail objBusinessAccountdbDetail;
    private BusinessAccountMaster objBusinessAccountMaster;
    private ClientAdminUser objClientAdminUser;
    private ClientAdminUserAppsRel objClientAdminUserAppsRel;
    private ClientEmployeeMaster objClientEmployeeMaster;
    private ClientStockSelection objClientStockSelection;
    private ClientRegional objClientRegional;

    public UserData() {
    }

    public UserData(BusinessAccountdbDetail objBusinessAccountdbDetail, BusinessAccountMaster objBusinessAccountMaster, ClientAdminUser objClientAdminUser, ClientAdminUserAppsRel objClientAdminUserAppsRel, ClientEmployeeMaster objClientEmployeeMaster, ClientStockSelection objClientStockSelection, ClientRegional objClientRegional) {
        this.objBusinessAccountdbDetail = objBusinessAccountdbDetail;
        this.objBusinessAccountMaster = objBusinessAccountMaster;
        this.objClientAdminUser = objClientAdminUser;
        this.objClientAdminUserAppsRel = objClientAdminUserAppsRel;
        this.objClientEmployeeMaster = objClientEmployeeMaster;
        this.objClientStockSelection = objClientStockSelection;
        this.objClientRegional = objClientRegional;
    }

    public BusinessAccountdbDetail getObjBusinessAccountdbDetail() {
        return objBusinessAccountdbDetail;
    }

    public void setObjBusinessAccountdbDetail(BusinessAccountdbDetail objBusinessAccountdbDetail) {
        this.objBusinessAccountdbDetail = objBusinessAccountdbDetail;
    }

    public BusinessAccountMaster getObjBusinessAccountMaster() {
        return objBusinessAccountMaster;
    }

    public void setObjBusinessAccountMaster(BusinessAccountMaster objBusinessAccountMaster) {
        this.objBusinessAccountMaster = objBusinessAccountMaster;
    }

    public ClientAdminUser getObjClientAdminUser() {
        return objClientAdminUser;
    }

    public void setObjClientAdminUser(ClientAdminUser objClientAdminUser) {
        this.objClientAdminUser = objClientAdminUser;
    }

    public ClientAdminUserAppsRel getObjClientAdminUserAppsRel() {
        return objClientAdminUserAppsRel;
    }

    public void setObjClientAdminUserAppsRel(ClientAdminUserAppsRel objClientAdminUserAppsRel) {
        this.objClientAdminUserAppsRel = objClientAdminUserAppsRel;
    }

    public ClientEmployeeMaster getObjClientEmployeeMaster() {
        return objClientEmployeeMaster;
    }

    public void setObjClientEmployeeMaster(ClientEmployeeMaster objClientEmployeeMaster) {
        this.objClientEmployeeMaster = objClientEmployeeMaster;
    }

    public ClientStockSelection getObjClientStockSelection() {
        return objClientStockSelection;
    }

    public void setObjClientStockSelection(ClientStockSelection objClientStockSelection) {
        this.objClientStockSelection = objClientStockSelection;
    }

    public ClientRegional getObjClientRegional() {
        return objClientRegional;
    }

    public void setObjClientRegional(ClientRegional objClientRegional) {
        this.objClientRegional = objClientRegional;
    }
}
