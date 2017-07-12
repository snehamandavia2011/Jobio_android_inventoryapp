package onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stackio.jobio.officeApp.R;
import com.stephentuso.welcome.WelcomePage;
import com.stephentuso.welcome.WelcomeUtils;

/**
 * Created by SAI on 3/19/2017.
 */
public class frOnBoardingScreen1 extends Fragment implements WelcomePage.OnChangeListener {

    private ViewGroup rootLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fr_onboarding_screen1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onWelcomeScreenPageScrolled(int pageIndex, float offset, int offsetPixels) {
        if (rootLayout != null)
            WelcomeUtils.applyParallaxEffect(rootLayout, true, offsetPixels, 0.3f, 0.2f);
    }

    @Override
    public void onWelcomeScreenPageSelected(int pageIndex, int selectedPageIndex) {
        //Not used
    }

    @Override
    public void onWelcomeScreenPageScrollStateChanged(int pageIndex, int state) {
        //Not used
    }
}

