package entity;

import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import utility.DataBase;

/**
 * Created by SAI on 10/19/2016.
 */
public class ClientStockTransactionReason {
    private int id, stockTransactionStatusId;
    private String reason;

    public ClientStockTransactionReason(int id, int stockTransactionStatusId, String reason) {
        this.id = id;
        this.stockTransactionStatusId = stockTransactionStatusId;
        this.reason = reason;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStockTransactionStatusId() {
        return stockTransactionStatusId;
    }

    public void setStockTransactionStatusId(int stockTransactionStatusId) {
        this.stockTransactionStatusId = stockTransactionStatusId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static ArrayList<ClientStockTransactionReason> parseList(String JsonString) {
        try {
            ArrayList<ClientStockTransactionReason> arr = new ArrayList<>();
            JSONArray arrJSON = new JSONArray(JsonString);
            for (int i = 0; i < arrJSON.length(); i++) {
                JSONObject objJSON = arrJSON.getJSONObject(i);
                arr.add(new ClientStockTransactionReason(objJSON.getInt("id"), objJSON.getInt("stock_transaction_status_id"), objJSON.getString("reason")));
            }
            return arr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveDataToDatabase(Context ctx, ArrayList<ClientStockTransactionReason> arr) {
        if (arr != null && arr.size() > 0) {
            DataBase db = new DataBase(ctx);
            db.open();
            db.cleanTable(DataBase.stock_transaction_reason_int);
            for (ClientStockTransactionReason obj : arr) {
                db.insert(DataBase.stock_transaction_reason_table, DataBase.stock_transaction_reason_int, new String[]{String.valueOf(obj.getId()), String.valueOf(obj.getStockTransactionStatusId()), obj.getReason()});
            }
            db.close();
        }
    }

    public static ArrayList<ClientStockTransactionReason> getDataFromDatabase(Context ctx, int stock_transaction_status_id) {
        ArrayList<ClientStockTransactionReason> arr = null;
        DataBase db = new DataBase(ctx);
        db.open();
        Cursor cur = null;
        if (stock_transaction_status_id == 0)
            cur = db.fetchAll(DataBase.stock_transaction_reason_table, DataBase.stock_transaction_reason_int);
        else
            cur = db.fetch(DataBase.stock_transaction_reason_table, DataBase.stock_transaction_reason_int, "stock_transaction_status_id=" + stock_transaction_status_id);
        if (cur != null && cur.getCount() > 0) {
            arr = new ArrayList<>();
            cur.moveToFirst();
            do {
                arr.add(new ClientStockTransactionReason(cur.getInt(1), cur.getInt(2), cur.getString(3)));
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return arr;
    }

    @Override
    public String toString() {
        return reason;
    }
}
