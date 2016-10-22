package fragment;

import android.content.Context;

import com.lab360io.jobio.inventoryapp.R;

/**
 * Created by SAI on 10/20/2016.
 */
public class StockTransactionReferenceType {
    public static String JOB = "J", INVOICE = "I", PURCHASE_ORDER = "P";
    public static String getReferenceType(Context c, String type) {
        if (type.equals(JOB)) {
            return c.getString(R.string.strJob);
        } else if (type.equals(INVOICE)) {
            return c.getString(R.string.strInvoice);
        } else if (type.equals(PURCHASE_ORDER)) {
            return c.getString(R.string.strPurchaseOrder);
        }
        return "";
    }

    public static String getReferenceID(int selStockTransactionStatus) {
        if (selStockTransactionStatus == 2) {//STOCK RETURN
            return JOB;
        } else if (selStockTransactionStatus == 3) {//STOCK RECEIVED
            return PURCHASE_ORDER;
        } else if (selStockTransactionStatus == 4) {//STOCK DAMAGE
            return INVOICE;
        } else if (selStockTransactionStatus == 7) {//STOCK RELEASE
            return JOB;
        }
        return "";
    }
}

