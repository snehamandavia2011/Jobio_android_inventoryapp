package coaching;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lab360io.jobio.officeApp.R;

import entity.ClientEmployeeMaster;
import utility.Helper;

/**
 * Created by SAI on 3/16/2017.
 */
public class HomeScreen {
    public void showPromptWhileLoadActivity(AppCompatActivity ac) {
        Toolbar tb = (Toolbar) ac.findViewById(R.id.toolbar);
        if (tb != null) {
            if (CoachingPreference.needToShowPrompt(ac, CoachingPreference.HOME_SCREEN)) {
                String name = Helper.getStringPreference(ac, ClientEmployeeMaster.Fields.FIRST_NAME, "");
                new ShowFabPrompt(ac, tb.getChildAt(0), R.drawable.ic_bottommenu, ac.getString(R.string.strWelcome) + " " + name, ac.getString(R.string.msgWelcome));
            }
            CoachingPreference.updatePreference(ac, CoachingPreference.HOME_SCREEN);
        }
    }
}
