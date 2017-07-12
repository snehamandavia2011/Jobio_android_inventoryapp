package coaching;

import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.stackio.jobio.officeApp.R;
import com.stackio.jobio.officeApp.acAsset;
import com.stackio.jobio.officeApp.acManualQRCode;

import permission.CameraPermission;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

/**
 * Created by SAI on 3/16/2017.
 */
public class ShowFabPrompt {
    MaterialTapTargetPrompt mFabPrompt;

    public ShowFabPrompt(final AppCompatActivity ac, View target, int icon, String title, String text) {
        if (mFabPrompt != null) {
            return;
        }

        mFabPrompt = new MaterialTapTargetPrompt.Builder(ac)
                .setTarget(target)
                .setIcon(icon)
                .setPrimaryText(title)
                .setSecondaryText(text)
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setPrimaryTextColour(ac.getResources().getColor(R.color.white))
                .setSecondaryTextColour(ac.getResources().getColor(R.color.tilt))
                .setBackgroundColour(ac.getResources().getColor(R.color.promptBgColor))
                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                    @Override
                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                        mFabPrompt = null;
                        //Do something such as storing a value so that this prompt is never shown again
                        if (ac.getClass() == acManualQRCode.class || ac.getClass() == acAsset.class) {
                            new CameraPermission(ac).askForPermission();
                        }
                    }

                    @Override
                    public void onHidePromptComplete() {
                    }
                })
                .create();
        mFabPrompt.show();
    }
}
