package entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import utility.DataBase;

/**
 * Created by SAI on 10/11/2016.
 */
public class ClientItemMaster {
    String uuid, item_name, short_code, specification, category_name, uom_name,
            package_type_name, location_name, item_status, manufacturer, model, photo, monthly_demand,
            min_qty_for_restock, display_status, custom_field_1, custom_field_2, custom_field_3,
            custom_field_4, custom_field_5, last_update_date_time;
    ArrayList<ClientItemTransaction> arrItemTransaction;

    public ClientItemMaster(String uuid, String item_name, String short_code, String specification, String category_name, String uom_name, String package_type_name, String location_name, String item_status, String manufacturer, String model, String photo, String monthly_demand, String min_qty_for_restock, String display_status, String custom_field_1, String custom_field_2, String custom_field_3, String custom_field_4, String custom_field_5, String last_update_date_time, ArrayList<ClientItemTransaction> arrItemTransaction) {
        this.uuid = uuid;
        this.item_name = item_name;
        this.short_code = short_code;
        this.specification = specification;
        this.category_name = category_name;
        this.uom_name = uom_name;
        this.package_type_name = package_type_name;
        this.location_name = location_name;
        this.item_status = item_status;
        this.manufacturer = manufacturer;
        this.model = model;
        this.photo = photo;
        this.monthly_demand = monthly_demand;
        this.min_qty_for_restock = min_qty_for_restock;
        this.display_status = display_status;
        this.custom_field_1 = custom_field_1;
        this.custom_field_2 = custom_field_2;
        this.custom_field_3 = custom_field_3;
        this.custom_field_4 = custom_field_4;
        this.custom_field_5 = custom_field_5;
        this.last_update_date_time = last_update_date_time;
        this.arrItemTransaction = arrItemTransaction;
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

    public String getShort_code() {
        return short_code;
    }

    public void setShort_code(String short_code) {
        this.short_code = short_code;
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

    public String getUom_name() {
        return uom_name;
    }

    public void setUom_name(String uom_name) {
        this.uom_name = uom_name;
    }

    public String getPackage_type_name() {
        return package_type_name;
    }

    public void setPackage_type_name(String package_type_name) {
        this.package_type_name = package_type_name;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getItem_status() {
        return item_status;
    }

    public void setItem_status(String item_status) {
        this.item_status = item_status;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

    public String getDisplay_status() {
        return display_status;
    }

    public void setDisplay_status(String display_status) {
        this.display_status = display_status;
    }

    public String getCustom_field_1() {
        return custom_field_1;
    }

    public void setCustom_field_1(String custom_field_1) {
        this.custom_field_1 = custom_field_1;
    }

    public String getCustom_field_2() {
        return custom_field_2;
    }

    public void setCustom_field_2(String custom_field_2) {
        this.custom_field_2 = custom_field_2;
    }

    public String getCustom_field_3() {
        return custom_field_3;
    }

    public void setCustom_field_3(String custom_field_3) {
        this.custom_field_3 = custom_field_3;
    }

    public String getCustom_field_4() {
        return custom_field_4;
    }

    public void setCustom_field_4(String custom_field_4) {
        this.custom_field_4 = custom_field_4;
    }

    public String getCustom_field_5() {
        return custom_field_5;
    }

    public void setCustom_field_5(String custom_field_5) {
        this.custom_field_5 = custom_field_5;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLast_update_date_time() {
        return last_update_date_time;
    }

    public void setLast_update_date_time(String last_update_date_time) {
        this.last_update_date_time = last_update_date_time;
    }

    public static ArrayList<ClientItemMaster> getDataFromDatabase(Context ctx, String itemUUId, String barcode) {
        ArrayList<ClientItemMaster> arrClientItemMaster = null;
        ClientItemMaster objClientItemMaster = null;
        DataBase db = new DataBase(ctx);
        db.open();

        Cursor cur = null;
        if (itemUUId != null) {
            cur = db.fetch(DataBase.item_master_table, DataBase.item_master_int, "uuid='" + itemUUId + "'");
        } else {
            cur = db.fetchAll(DataBase.item_master_table, DataBase.item_master_int);
        }
        if (cur != null && cur.getCount() > 0) {
            arrClientItemMaster = new ArrayList<>();
            cur.moveToFirst();
            do {
                Cursor cutT = null;
                if (barcode.equals(""))
                    cutT = db.fetch(DataBase.item_transaction_table, DataBase.item_transaction_int, "imUUID='" + cur.getString(1) + "'");
                else
                    cutT = db.fetch(DataBase.item_transaction_table, DataBase.item_transaction_int, "imUUID='" + cur.getString(1) + "' and barcode='" + barcode + "'");
                ArrayList<ClientItemTransaction> arrT = null;
                if (cutT != null && cutT.getCount() > 0) {
                    arrT = new ArrayList<>();
                    cutT.moveToFirst();
                    do {
                        arrT.add(new ClientItemTransaction(cutT.getString(2), cutT.getString(3), cutT.getString(4), cutT.getString(5), cutT.getString(6), cutT.getString(7)));
                    } while (cutT.moveToNext());
                }
                cutT.close();
                objClientItemMaster = new ClientItemMaster(cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4),
                        cur.getString(5), cur.getString(6), cur.getString(7), cur.getString(8), cur.getString(9), cur.getString(10),
                        cur.getString(11), cur.getString(12), cur.getString(13), cur.getString(14), cur.getString(15), cur.getString(16),
                        cur.getString(17), cur.getString(18), cur.getString(19), cur.getString(20), cur.getString(21), arrT);
                arrClientItemMaster.add(objClientItemMaster);
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return arrClientItemMaster;
    }

    public void saveUpdateItemData(Context ctx) {
        DataBase db = new DataBase(ctx);
        db.open();
        Cursor cur = db.fetch(DataBase.item_master_table, DataBase.item_master_int, "uuid='" + this.getUuid() + "'");
        if (cur != null && cur.getCount() == 1) {//update
            ContentValues cv = new ContentValues();
            cv.put("uuid", this.getUuid());
            cv.put("item_name", this.getItem_name());
            cv.put("short_code", this.getShort_code());
            cv.put("specification", this.getSpecification());
            cv.put("category_name", this.getCategory_name());
            cv.put("uom_name", this.getUom_name());
            cv.put("package_type_name", this.getPackage_type_name());
            cv.put("location_name", this.getLocation_name());
            cv.put("item_status", this.getItem_status());
            cv.put("manufacturer", this.getManufacturer());
            cv.put("model", this.getModel());
            cv.put("photo", this.getPhoto());
            cv.put("monthly_demand", this.getMonthly_demand());
            cv.put("min_qty_for_restock", this.getMin_qty_for_restock());
            cv.put("display_status", this.getDisplay_status());
            cv.put("custom_field_1", this.getCustom_field_1());
            cv.put("custom_field_2", this.getCustom_field_2());
            cv.put("custom_field_3", this.getCustom_field_3());
            cv.put("custom_field_4", this.getCustom_field_4());
            cv.put("custom_field_5", this.getCustom_field_5());
            cv.put("last_update_date_time", new Date().getTime());
            db.update(DataBase.item_master_table, DataBase.item_master_int, "uuid='" + this.getUuid() + "'", cv);
        } else {//insert
            String data[] = {this.getUuid(), this.getItem_name(), this.getShort_code(), this.getSpecification(), this.getCategory_name(), this.getUom_name(), this.getPackage_type_name(),
                    this.getLocation_name(), this.getItem_status(), this.getManufacturer(), this.getModel(), this.getPhoto(), this.getMonthly_demand(), this.getMin_qty_for_restock(),
                    this.getDisplay_status(), this.getCustom_field_1(), this.getCustom_field_2(), this.getCustom_field_3(), this.getCustom_field_4(), this.getCustom_field_5(), String.valueOf(new Date().getTime())};
            db.insert(DataBase.item_master_table, DataBase.item_master_int, data);
        }
        cur.close();
        for (ClientItemTransaction objT : arrItemTransaction) {
            Cursor curT = db.fetch(DataBase.item_transaction_table, DataBase.item_transaction_int, "itUUID='" + objT.getUuid() + "'");
            if (curT != null && curT.getCount() == 1) {
                ContentValues cvT = new ContentValues();
                cvT.put("imUUID", this.getUuid());
                cvT.put("itUUID", objT.getUuid());
                cvT.put("available_qty", objT.getAvailable_qty());
                cvT.put("cost", objT.getCost());
                cvT.put("price", objT.getPrice());
                cvT.put("barcode", objT.getBarcode());
                cvT.put("spplierName", objT.getSpplierName());
                db.update(DataBase.item_transaction_table, DataBase.item_transaction_int, "itUUID='" + objT.getUuid() + "'", cvT);
            } else {
                db.insert(DataBase.item_transaction_table, DataBase.item_transaction_int, new String[]{this.getUuid(), objT.getUuid(),
                        objT.getAvailable_qty(), objT.getCost(), objT.getPrice(), objT.getBarcode(), objT.getSpplierName()});
            }
            curT.close();
        }
        db.close();
    }


    public ArrayList<ClientItemTransaction> getArrItemTransaction() {
        return arrItemTransaction;
    }

    public void setArrItemTransaction(ArrayList<ClientItemTransaction> arrItemTransaction) {
        this.arrItemTransaction = arrItemTransaction;
    }
}
