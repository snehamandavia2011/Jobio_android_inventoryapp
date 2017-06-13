package utility;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.lab360io.jobio.officeApp.R;
import com.lab360io.jobio.officeApp.acLogin;
import com.lab360io.jobio.officeApp.acSplash;
import com.lab360io.jobio.officeApp.acWelcome;

/**
 * Created by SNEHA MANDAVIA on 3/17/2016.
 */
public class OptionMenu {

    public void hideMenuItem(Menu menu) {
        menu.removeItem(R.id.menuTalkToUs);
        menu.removeItem(R.id.menuHelpArticle);
        menu.removeItem(R.id.menuAboutUs);
    }

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
            case R.id.menuTalkToUs:
                Logger.debug("Help");
                break;
            case R.id.menuHelpArticle: {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://jobio.io/article.php?articleid=Ng=="));
                ac.startActivity(i);
            }
            break;
            case R.id.menuAboutUs: {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://jobio.io/about.php"));
                ac.startActivity(i);
            }
            break;
            case R.id.menuMasterClear:
                Logger.debug("Master clear");
                // Create the Snackbar
                final ConfimationSnackbar snackbar = new ConfimationSnackbar(ac, ConstantVal.ToastBGColor.WARNING);
                snackbar.showSnackBar(ac.getString(R.string.strMastercleanMessage), ac.getString(R.string.strConfirm), ac.getString(R.string.strCancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismissSnackBar();
                        Helper.masterClear(ac.getApplicationContext());
                        Intent i = new Intent(ac.getApplicationContext(), acWelcome.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        ac.startActivity(i);
                        ac.finish();
                    }
                }, null);
                break;
        }
    }
}
