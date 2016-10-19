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
public class ClientStockTransactionStatusMaster {
    private int id;
    private String action_name;

    public ClientStockTransactionStatusMaster(int id, String action_name) {
        this.id = id;
        this.action_name = action_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAction_name() {
        return action_name;
    }

    public void setAction_name(String action_name) {
        this.action_name = action_name;
    }

    public static ArrayList<ClientStockTransactionStatusMaster> parseList(String JsonString) {
        try {
            ArrayList<ClientStockTransactionStatusMaster> arr = new ArrayList<>();
            JSONArray arrJSON = new JSONArray(JsonString);
            for (int i = 0; i < arrJSON.length(); i++) {
                JSONObject objJSON = arrJSON.getJSONObject(i);
                arr.add(new ClientStockTransactionStatusMaster(objJSON.getInt("id"), objJSON.getString("action_name")));
            }
            return arr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveDataToDatabase(Context ctx, ArrayList<ClientStockTransactionStatusMaster> arr) {
        if (arr != null && arr.size() > 0) {
            DataBase db = new DataBase(ctx);
            db.open();
            for (ClientStockTransactionStatusMaster obj : arr) {
                db.insert(DataBase.stock_transaction_status_table, DataBase.stock_transaction_status_int, new String[]{String.valueOf(obj.getId()), obj.getAction_name()});
            }
            db.close();
        }
    }

    public static ArrayList<ClientStockTransactionStatusMaster> getDataFromDatabase(Context ctx) {
        ArrayList<ClientStockTransactionStatusMaster> arr = null;
        DataBase db = new DataBase(ctx);
        db.open();
        Cursor cur = db.fetchAll(DataBase.stock_transaction_status_table, DataBase.stock_transaction_status_int);
        if (cur != null && cur.getCount() > 0) {
            arr = new ArrayList<>();
            cur.moveToFirst();
            do {
                arr.add(new ClientStockTransactionStatusMaster(cur.getInt(1), cur.getString(2)));
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return arr;
    }

    public static ArrayList<ClientStockTransactionStatusMaster> getDataFromDatabaseIn(Context ctx) {
        ArrayList<ClientStockTransactionStatusMaster> arr = null;
        DataBase db = new DataBase(ctx);
        db.open();
        Cursor cur = db.fetchAll(DataBase.stock_transaction_status_table, DataBase.stock_transaction_status_int);
        if (cur != null && cur.getCount() > 0) {
            arr = new ArrayList<>();
            cur.moveToFirst();
            do {
                if (cur.getInt(1) == 2 || cur.getInt(1) == 3 || cur.getInt(1) == 4 || cur.getInt(1) == 7)
                    arr.add(new ClientStockTransactionStatusMaster(cur.getInt(1), cur.getString(2)));
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();
        return arr;
    }

    @Override
    public String toString() {
        return action_name;
    }
}
