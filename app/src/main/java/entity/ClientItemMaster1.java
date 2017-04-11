package entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import utility.Logger;

/**
 * Created by SAI on 10/17/2016.
 */
public class ClientItemMaster1 {
    String uuid, item_name, specification, category_name, monthly_demand, min_qty_for_restock, available_qty, photo;
    boolean isImageLoaded = false;

    public ClientItemMaster1() {

    }

    public ClientItemMaster1(String uuid, String item_name, String specification, String category_name, String monthly_demand, String min_qty_for_restock, String available_qty, String photo) {
        this.uuid = uuid;
        this.item_name = item_name;
        this.specification = specification;
        this.category_name = category_name;
        this.monthly_demand = monthly_demand;
        this.min_qty_for_restock = min_qty_for_restock;
        this.available_qty = available_qty;
        this.photo = photo;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getMonthly_demand() {
        return monthly_demand;
    }

    public void setMonthly_demand(String monthly_demand) {
        this.monthly_demand = monthly_demand;
    }

    public String getMin_qty_for_restock() {
        return min_qty_for_restock;
    }

    public void setMin_qty_for_restock(String min_qty_for_restock) {
        this.min_qty_for_restock = min_qty_for_restock;
    }

    public String getAvailable_qty() {
        return available_qty;
    }

    public void setAvailable_qty(String available_qty) {
        this.available_qty = available_qty;
    }

    public boolean isImageLoaded() {
        return isImageLoaded;
    }

    public void setIsImageLoaded(boolean isImageLoaded) {
        this.isImageLoaded = isImageLoaded;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public static ArrayList<ClientItemMaster1> parseList(String JSONString) {
        ArrayList<ClientItemMaster1> arrClientItemMaster1 = new ArrayList<>();
        try {
            JSONArray arrJSON = new JSONArray(JSONString);
            for (int i = 0; i < arrJSON.length(); i++) {
                JSONObject obj = arrJSON.getJSONObject(i);
                String uuid = (obj.getString("uuid").equals("null") ? "" : obj.getString("uuid"));
                String item_name = (obj.getString("item_name").equals("null") ? "" : obj.getString("item_name"));
                String specification = (obj.getString("specification").equals("null") ? "" : obj.getString("specification"));
                String category_name = (obj.getString("category_name").equals("null") ? "" : obj.getString("category_name"));
                String monthly_demand = (obj.getString("monthly_demand").equals("null") ? "" : obj.getString("monthly_demand"));
                String min_qty_for_restock = (obj.getString("min_qty_for_restock").equals("null") ? "" : obj.getString("min_qty_for_restock"));
                String available_qty = (obj.getString("available_qty").equals("null") ? "" : obj.getString("available_qty"));
                arrClientItemMaster1.add(new ClientItemMaster1(uuid, item_name, specification, category_name, monthly_demand, min_qty_for_restock, available_qty, ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
        }
        return arrClientItemMaster1;
    }

}
