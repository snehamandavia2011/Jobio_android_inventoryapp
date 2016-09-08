package utility;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.lab360io.jobio.inventoryapp.R;
import com.lab360io.jobio.inventoryapp.acLogin;
import com.lab360io.jobio.inventoryapp.acSplash;

/**
 * Created by SNEHA MANDAVIA on 3/17/2016.
 */
public class OptionMenu {
    public MenuInflater getCommonMenu(AppCompatActivity act, Menu menu) {
        MenuInflater mi = act.getMenuInflater();
        mi.inflate(R.menu.common, menu);
        Logger.debug(act.getClass() + " " + acLogin.class);
        if (act.getClass() != acLogin.class) {
            menu.removeItem(R.id.menuMasterClear);
        }
        return mi;
    }

    public void handleMenuItemClick(final AppCompatActivity ac, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuHelp:
                Logger.debug("Help");
                break;
            case R.id.menuAbout:
                Logger.debug("About");
                break;
            case R.id.menuContact:
                Logger.debug("Contact");
                break;
            case R.id.menuMasterClear:
                Logger.debug("Master clear");
                // Create the Snackbar
                final ConfimationSnackbar snackbar = new ConfimationSnackbar(ac);
                snackbar.showSnackBar(ac.getString(R.string.strMastercleanMessage), ac.getString(R.string.strConfirm), ac.getString(R.string.strCancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismissSnackBar();
                        Helper.masterClear(ac.getApplicationContext());
                        Intent i = new Intent(ac.getApplicationContext(), acSplash.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        ac.startActivity(i);
                        ac.finish();
                    }
                }, null);
                break;
        }
    }
}
