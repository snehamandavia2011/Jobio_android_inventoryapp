package coaching;

import android.support.v7.app.AppCompatActivity;

import com.stackio.jobio.officeApp.R;

/**
 * Created by SAI on 3/16/2017.
 */
public class QRCodeConfigurationScreen {

    public void showPromptWhileLoadActivity(AppCompatActivity ac) {
        if (CoachingPreference.needToShowPrompt(ac, CoachingPreference.MANUAL_QR_CODE_SCREEN))
            new ShowFabPrompt(ac, ac.findViewById(R.id.openPrompt), R.drawable.ic_back, ac.getString(R.string.strSetUpYourBusiness), ac.getString(R.string.msgSetUpYourBusiness));
        CoachingPreference.updatePreference(ac, CoachingPreference.MANUAL_QR_CODE_SCREEN);
    }

    public void showPromptOnClickScanQRCodeIcon(AppCompatActivity ac) {
        if (CoachingPreference.needToShowPrompt(ac, CoachingPreference.MANUAL_QR_CODE_SCAN_QR_CODE_BUTTON))
            new ShowFabPrompt(ac, ac.findViewById(R.id.btnScanQR), R.drawable.ic_close_white, ac.getString(R.string.strScanDomainName), ac.getString(R.string.msgOpenCamera));
        CoachingPreference.updatePreference(ac, CoachingPreference.MANUAL_QR_CODE_SCAN_QR_CODE_BUTTON);
    }
}
