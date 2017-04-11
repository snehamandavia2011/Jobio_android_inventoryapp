package entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import utility.Logger;

/**
 * Created by SAI on 10/20/2016.
 */
public class ClientJobInvoiceRefDetail implements Serializable {
    String imItem_name, imModel, imPhoto, itId, itUUID, itParent_uuid, itPo_transaction_uuid, itPlan_id, itItem_id, itStatus_id, itQty,
            itAvailable_qty, itCost, itPrice, itReason_id, itNote, itBarcode, itExpiry, itFrom_id, itTo_id, itFrom_type, itTo_type, itRef_id,
            itRef_type, itUser_id, stsmAction_name;
    boolean isPhotoLoaded = false;

    public String display() {
        return " imItem_name:" + imItem_name + " imModel:" + imModel + " imPhoto:" + imPhoto + " itId:" + itId + " itUUID:" + itUUID + " itParent_uuid:" + itParent_uuid + " itPo_transaction_uuid:" + itPo_transaction_uuid + " itPlan_id:" + itPlan_id + " itItem_id:" + itItem_id + " itStatus_id:" + itStatus_id + " itQty:" + itQty + " itAvailable_qty:" +
                itAvailable_qty + " itCost:" + itCost + " itPrice:" + itPrice + " itReason_id:" + itReason_id + " itNote:" + itNote + " itBarcode:" + itBarcode + " itExpiry:" + itExpiry + " itFrom_id:" + itFrom_id + " itTo_id:" + itTo_id + " itFrom_type:" + itFrom_type + " itTo_type:" + itTo_type + " itRef_id:" + itRef_id + " itRef_type:" +
                itRef_type + " itUser_id:" + itUser_id + " stsmAction_name:" + stsmAction_name;
    }

    public ClientJobInvoiceRefDetail(String imItem_name, String imModel, String imPhoto, String itId, String itUUID, String itParent_uuid, String itPo_transaction_uuid, String itPlan_id, String itItem_id, String itStatus_id, String itQty, String itAvailable_qty, String itCost, String itPrice, String itReason_id, String itNote, String itBarcode, String itExpiry, String itFrom_id, String itTo_id, String itFrom_type, String itTo_type, String itRef_id, String itRef_type, String itUser_id, String stsmAction_name) {
        this.imItem_name = imItem_name;
        this.imModel = imModel;
        this.imPhoto = imPhoto;
        this.itId = itId;
        this.itUUID = itUUID;
        this.itParent_uuid = itParent_uuid;
        this.itPo_transaction_uuid = itPo_transaction_uuid;
        this.itPlan_id = itPlan_id;
        this.itItem_id = itItem_id;
        this.itStatus_id = itStatus_id;
        this.itQty = itQty;
        this.itAvailable_qty = itAvailable_qty;
        this.itCost = itCost;
        this.itPrice = itPrice;
        this.itReason_id = itReason_id;
        this.itNote = itNote;
        this.itBarcode = itBarcode;
        this.itExpiry = itExpiry;
        this.itFrom_id = itFrom_id;
        this.itTo_id = itTo_id;
        this.itFrom_type = itFrom_type;
        this.itTo_type = itTo_type;
        this.itRef_id = itRef_id;
        this.itRef_type = itRef_type;
        this.itUser_id = itUser_id;
        this.stsmAction_name = stsmAction_name;
    }

    public String getImItem_name() {
        return imItem_name;
    }

    public void setImItem_name(String imItem_name) {
        this.imItem_name = imItem_name;
    }

    public String getImModel() {
        return imModel;
    }

    public void setImModel(String imModel) {
        this.imModel = imModel;
    }

    public String getImPhoto() {
        return imPhoto;
    }

    public void setImPhoto(String imPhoto) {
        this.imPhoto = imPhoto;
    }

    public String getItId() {
        return itId;
    }

    public void setItId(String itId) {
        this.itId = itId;
    }

    public String getItUUID() {
        return itUUID;
    }

    public void setItUUID(String itUUID) {
        this.itUUID = itUUID;
    }

    public String getItParent_uuid() {
        return itParent_uuid;
    }

    public void setItParent_uuid(String itParent_uuid) {
        this.itParent_uuid = itParent_uuid;
    }

    public String getItPo_transaction_uuid() {
        return itPo_transaction_uuid;
    }

    public void setItPo_transaction_uuid(String itPo_transaction_uuid) {
        this.itPo_transaction_uuid = itPo_transaction_uuid;
    }

    public String getItPlan_id() {
        return itPlan_id;
    }

    public void setItPlan_id(String itPlan_id) {
        this.itPlan_id = itPlan_id;
    }

    public String getItItem_id() {
        return itItem_id;
    }

    public void setItItem_id(String itItem_id) {
        this.itItem_id = itItem_id;
    }

    public String getItStatus_id() {
        return itStatus_id;
    }

    public void setItStatus_id(String itStatus_id) {
        this.itStatus_id = itStatus_id;
    }

    public String getItQty() {
        return itQty;
    }

    public void setItQty(String itQty) {
        this.itQty = itQty;
    }

    public String getItAvailable_qty() {
        return itAvailable_qty;
    }

    public void setItAvailable_qty(String itAvailable_qty) {
        this.itAvailable_qty = itAvailable_qty;
    }

    public String getItCost() {
        return itCost;
    }

    public void setItCost(String itCost) {
        this.itCost = itCost;
    }

    public String getItPrice() {
        return itPrice;
    }

    public void setItPrice(String itPrice) {
        this.itPrice = itPrice;
    }

    public String getItReason_id() {
        return itReason_id;
    }

    public void setItReason_id(String itReason_id) {
        this.itReason_id = itReason_id;
    }

    public String getItNote() {
        return itNote;
    }

    public void setItNote(String itNote) {
        this.itNote = itNote;
    }

    public String getItBarcode() {
        return itBarcode;
    }

    public void setItBarcode(String itBarcode) {
        this.itBarcode = itBarcode;
    }

    public String getItExpiry() {
        return itExpiry;
    }

    public void setItExpiry(String itExpiry) {
        this.itExpiry = itExpiry;
    }

    public String getItFrom_id() {
        return itFrom_id;
    }

    public void setItFrom_id(String itFrom_id) {
        this.itFrom_id = itFrom_id;
    }

    public String getItTo_id() {
        return itTo_id;
    }

    public void setItTo_id(String itTo_id) {
        this.itTo_id = itTo_id;
    }

    public String getItFrom_type() {
        return itFrom_type;
    }

    public void setItFrom_type(String itFrom_type) {
        this.itFrom_type = itFrom_type;
    }

    public String getItTo_type() {
        return itTo_type;
    }

    public void setItTo_type(String itTo_type) {
        this.itTo_type = itTo_type;
    }

    public String getItRef_id() {
        return itRef_id;
    }

    public void setItRef_id(String itRef_id) {
        this.itRef_id = itRef_id;
    }

    public String getItRef_type() {
        return itRef_type;
    }

    public void setItRef_type(String itRef_type) {
        this.itRef_type = itRef_type;
    }

    public String getItUser_id() {
        return itUser_id;
    }

    public void setItUser_id(String itUser_id) {
        this.itUser_id = itUser_id;
    }

    public String getStsmAction_name() {
        return stsmAction_name;
    }

    public void setStsmAction_name(String stsmAction_name) {
        this.stsmAction_name = stsmAction_name;
    }

    public boolean isPhotoLoaded() {
        return isPhotoLoaded;
    }

    public void setIsPhotoLoaded(boolean isPhotoLoaded) {
        this.isPhotoLoaded = isPhotoLoaded;
    }

    public static ArrayList<ClientJobInvoiceRefDetail> parseJSON(String strJSON) {
        try {
            ArrayList<ClientJobInvoiceRefDetail> arr = new ArrayList<>();
            JSONArray arrJSON = new JSONArray(strJSON);
            for (int i = 0; i < arrJSON.length(); i++) {
                JSONObject objJSON = arrJSON.getJSONObject(i);
                String imItem_name = objJSON.getString("item_name").equals("null") ? "" : objJSON.getString("item_name");
                String imModel = objJSON.getString("model").equals("null") ? "" : objJSON.getString("model");
                String imPhoto = "";
                String itId = objJSON.getString("id").equals("null") ? "" : objJSON.getString("id");
                String itUUID = objJSON.getString("uuid").equals("null") ? "" : objJSON.getString("uuid");
                String itParent_uuid = objJSON.getString("parent_uuid").equals("null") ? "" : objJSON.getString("parent_uuid");
                String itPo_transaction_uuid = objJSON.getString("po_transaction_uuid").equals("null") ? "" : objJSON.getString("po_transaction_uuid");
                String itPlan_id = objJSON.getString("plan_id").equals("null") ? "" : objJSON.getString("plan_id");
                String itItem_id = objJSON.getString("item_id").equals("null") ? "" : objJSON.getString("item_id");
                String itStatus_id = objJSON.getString("status_id").equals("null") ? "" : objJSON.getString("status_id");
                String itQty = objJSON.getString("qty").equals("null") ? "" : objJSON.getString("qty");
                String itAvailable_qty = objJSON.getString("available_qty").equals("null") ? "" : objJSON.getString("available_qty");
                String itCost = objJSON.getString("cost").equals("null") ? "" : objJSON.getString("cost");
                String itPrice = objJSON.getString("price").equals("null") ? "" : objJSON.getString("price");
                String itReason_id = objJSON.getString("reason_id").equals("null") ? "" : objJSON.getString("reason_id");
                String itNote = objJSON.getString("note").equals("null") ? "" : objJSON.getString("note");
                String itBarcode = objJSON.getString("barcode").equals("null") ? "" : objJSON.getString("barcode");
                String itExpiry = objJSON.getString("expiry").equals("null") ? "0000-00-00" : objJSON.getString("expiry");
                String itFrom_id = objJSON.getString("from_id").equals("null") ? "" : objJSON.getString("from_id");
                String itTo_id = objJSON.getString("to_id").equals("null") ? "" : objJSON.getString("to_id");
                String itFrom_type = objJSON.getString("from_type").equals("null") ? "" : objJSON.getString("from_type");
                String itTo_type = objJSON.getString("to_type").equals("null") ? "" : objJSON.getString("to_type");
                String itRef_id = objJSON.getString("ref_id").equals("null") ? "" : objJSON.getString("ref_id");
                String itRef_type = objJSON.getString("ref_type").equals("null") ? "" : objJSON.getString("ref_type");
                String itUser_id = objJSON.getString("user_id").equals("null") ? "" : objJSON.getString("user_id");
                String stsmAction_name = objJSON.getString("action_name").equals("null") ? "" : objJSON.getString("action_name");
                arr.add(new ClientJobInvoiceRefDetail(imItem_name, imModel, imPhoto, itId, itUUID, itParent_uuid, itPo_transaction_uuid, itPlan_id, itItem_id, itStatus_id, itQty, itAvailable_qty, itCost, itPrice, itReason_id, itNote, itBarcode, itExpiry, itFrom_id, itTo_id, itFrom_type, itTo_type, itRef_id, itRef_type, itUser_id, stsmAction_name));
            }
            return arr;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            return null;
        }
    }
}
