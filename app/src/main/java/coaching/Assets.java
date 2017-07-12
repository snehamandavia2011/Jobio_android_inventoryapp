package coaching;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.stackio.jobio.officeApp.R;

/**
 * Created by SAI on 3/17/2017.
 */
public class Assets {
    public void assetScreen(AppCompatActivity ac) {
        Toolbar toolBar = (Toolbar) ac.findViewById(R.id.toolbar);
        if (CoachingPreference.needToShowPrompt(ac, CoachingPreference.ASSET_SCREEN)) {
            new ShowFabPrompt(ac, toolBar.getChildAt(0), R.drawable.ic_close_white, ac.getString(R.string.strAssetsYouHold), ac.getString(R.string.msgAssetDetail));
        }
        CoachingPreference.updatePreference(ac, CoachingPreference.ASSET_SCREEN);
    }

    public void inspectionScreen(AppCompatActivity ac) {
        Toolbar toolBar = (Toolbar) ac.findViewById(R.id.toolbar);
        if (CoachingPreference.needToShowPrompt(ac, CoachingPreference.INSPECTION_SCREEN)) {
            new ShowFabPrompt(ac, toolBar.getChildAt(0), R.drawable.ic_close_white, ac.getString(R.string.strInspect), ac.getString(R.string.msgInspection));
        }
        CoachingPreference.updatePreference(ac, CoachingPreference.INSPECTION_SCREEN);
    }

    public void serviceScreen(AppCompatActivity ac) {
        Toolbar toolBar = (Toolbar) ac.findViewById(R.id.toolbar);
        if (CoachingPreference.needToShowPrompt(ac, CoachingPreference.SERVICE_SCREEN)) {
            new ShowFabPrompt(ac, toolBar.getChildAt(0), R.drawable.ic_close_white, ac.getString(R.string.strService), ac.getString(R.string.msgService));
        }
        CoachingPreference.updatePreference(ac, CoachingPreference.SERVICE_SCREEN);
    }
}
