package coaching;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lab360io.jobio.officeApp.R;

import entity.ClientEmployeeMaster;
import utility.Helper;

/**
 * Created by SAI on 3/16/2017.
 */
public class StockScreen {
    public void showPromptOnClickScanBarcode(AppCompatActivity ac) {
        if (CoachingPreference.needToShowPrompt(ac, CoachingPreference.ADD_ITEM_BY_BARCODE_BUTTON)) {
            new ShowFabPrompt(ac, ac.findViewById(R.id.btnSearchItem), R.drawable.ic_close_white, ac.getString(R.string.strViewItemByScanBarcode), ac.getString(R.string.msgViewItemByScanBarcode));
        }
        CoachingPreference.updatePreference(ac, CoachingPreference.ADD_ITEM_BY_BARCODE_BUTTON);

    }

    public void showPromptOnStockScreen(AppCompatActivity ac) {
        Toolbar tb = (Toolbar) ac.findViewById(R.id.toolbar);
        if (tb != null) {
            if (CoachingPreference.needToShowPrompt(ac, CoachingPreference.STOCK_SCREEN)) {
                new ShowFabPrompt(ac, tb.getChildAt(0), R.drawable.ic_bottommenu, ac.getString(R.string.strStock), ac.getString(R.string.msgStock));
            }
            CoachingPreference.updatePreference(ac, CoachingPreference.STOCK_SCREEN);
        }
    }

    public void showPromptOnTransactionScreen(AppCompatActivity ac) {
        Toolbar tb = (Toolbar) ac.findViewById(R.id.toolbar);
        if (tb != null) {
            if (CoachingPreference.needToShowPrompt(ac, CoachingPreference.TRANSACTION_SCREEN)) {
                new ShowFabPrompt(ac, tb.getChildAt(0), R.drawable.ic_bottommenu, ac.getString(R.string.strTransaction), ac.getString(R.string.msgTransaction));
            }
            CoachingPreference.updatePreference(ac, CoachingPreference.TRANSACTION_SCREEN);
        }
    }
}
