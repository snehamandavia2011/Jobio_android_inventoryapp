package coaching;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lab360io.jobio.officeApp.R;

/**
 * Created by SAI on 3/17/2017.
 */
public class ManageMessage {
    public void employeeListScreen(AppCompatActivity ac) {
        Toolbar toolBar = (Toolbar) ac.findViewById(R.id.toolbar);
        if (CoachingPreference.needToShowPrompt(ac, CoachingPreference.MESSAGE_EMPLOYEE_LIST_SCREEN)) {
            new ShowFabPrompt(ac, toolBar.getChildAt(0), R.drawable.ic_close_white, ac.getString(R.string.strMessageEmployeeList), ac.getString(R.string.msgMessageEmployeeList));
        }
        CoachingPreference.updatePreference(ac, CoachingPreference.MESSAGE_EMPLOYEE_LIST_SCREEN);
    }

    public void sendMessageScreen(AppCompatActivity ac) {
        Toolbar toolBar = (Toolbar) ac.findViewById(R.id.toolbar);
        if (CoachingPreference.needToShowPrompt(ac, CoachingPreference.SEND_MESSAGE_SCREEN)) {
            new ShowFabPrompt(ac, toolBar.getChildAt(0), R.drawable.ic_close_white, ac.getString(R.string.strSendMessage), ac.getString(R.string.msgSendMessage));
        }
        CoachingPreference.updatePreference(ac, CoachingPreference.SEND_MESSAGE_SCREEN);
    }
}
