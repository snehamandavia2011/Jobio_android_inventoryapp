package entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import utility.Logger;

/**
 * Created by SAI on 10/20/2016.
 */
public class ClientPORefDetail implements Serializable {
    String podId, podPo_id, podItem_id, podQty, podCost, podPrice, podOrder_status, podUser_id, imItem_name;
    boolean isPhotoLoaded;

    public String display() {
        return " " + podId + " " + podPo_id + " " + podItem_id + " " + podQty + " " + podCost + " " + podPrice + " " + podOrder_status + " " + podUser_id + " " + imItem_name;
    }

    public ClientPORefDetail(String podId, String podPo_id, String podItem_id, String podQty, String podCost, String podPrice, String podOrder_status, String podUser_id, String imItem_name) {
        this.podId = podId;
        this.podPo_id = podPo_id;
        this.podItem_id = podItem_id;
        this.podQty = podQty;
        this.podCost = podCost;
        this.podPrice = podPrice;
        this.podOrder_status = podOrder_status;
        this.podUser_id = podUser_id;
        this.imItem_name = imItem_name;
    }

    public String getPodId() {
        return podId;
    }

    public void setPodId(String podId) {
        this.podId = podId;
    }

    public String getPodPo_id() {
        return podPo_id;
    }

    public void setPodPo_id(String podPo_id) {
        this.podPo_id = podPo_id;
    }

    public String getPodItem_id() {
        return podItem_id;
    }

    public void setPodItem_id(String podItem_id) {
        this.podItem_id = podItem_id;
    }

    public String getPodQty() {
        return podQty;
    }

    public void setPodQty(String podQty) {
        this.podQty = podQty;
    }

    public String getPodCost() {
        return podCost;
    }

    public void setPodCost(String podCost) {
        this.podCost = podCost;
    }

    public String getPodPrice() {
        return podPrice;
    }

    public void setPodPrice(String podPrice) {
        this.podPrice = podPrice;
    }

    public String getPodOrder_status() {
        return podOrder_status;
    }

    public void setPodOrder_status(String podOrder_status) {
        this.podOrder_status = podOrder_status;
    }

    public String getPodUser_id() {
        return podUser_id;
    }

    public void setPodUser_id(String podUser_id) {
        this.podUser_id = podUser_id;
    }

    public String getImItem_name() {
        return imItem_name;
    }

    public void setImItem_name(String imItem_name) {
        this.imItem_name = imItem_name;
    }

    public boolean isPhotoLoaded() {
        return isPhotoLoaded;
    }

    public void setIsPhotoLoaded(boolean isPhotoLoaded) {
        this.isPhotoLoaded = isPhotoLoaded;
    }

    public static ArrayList<ClientPORefDetail> parseJSON(String strJSON) {
        try {
            ArrayList<ClientPORefDetail> arr = new ArrayList<>();
            JSONArray arrJSON = new JSONArray(strJSON);
            for (int i = 0; i < arrJSON.length(); i++) {
                JSONObject objJSON = arrJSON.getJSONObject(i);
                String podId = objJSON.getString("id");
                String podPo_id = objJSON.getString("po_id").equals("null") ? "" : objJSON.getString("po_id");
                String podItem_id = objJSON.getString("item_id").equals("null") ? "" : objJSON.getString("item_id");
                String podQty = objJSON.getString("qty").equals("null") ? "" : objJSON.getString("qty");
                String podCost = objJSON.getString("cost").equals("null") ? "" : objJSON.getString("cost");
                String podPrice = objJSON.getString("price").equals("null") ? "" : objJSON.getString("price");
                String podOrder_status = objJSON.getString("order_status").equals("null") ? "" : objJSON.getString("order_status");
                String podUser_id = objJSON.getString("user_id").equals("null") ? "" : objJSON.getString("user_id");
                String imItem_name = objJSON.getString("item_name").equals("null") ? "" : objJSON.getString("item_name");
                arr.add(new ClientPORefDetail(podId, podPo_id, podItem_id, podQty, podCost, podPrice, podOrder_status, podUser_id, imItem_name));
            }
            return arr;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            return null;
        }
    }
}
