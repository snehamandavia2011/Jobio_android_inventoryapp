package onboarding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stackio.jobio.officeApp.R;
import com.stephentuso.welcome.WelcomePage;
import com.stephentuso.welcome.WelcomeUtils;
import com.xwray.fontbinding.FontCache;

/**
 * Created by SAI on 3/19/2017.
 */
public class frOnBoardingScreen4 extends Fragment implements WelcomePage.OnChangeListener {
    RelativeLayout container;
    private ViewGroup rootLayout;
    ImageView img;
    TextView txtTitle, txtDesc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FontCache.getInstance(getActivity()).addFont("Ubuntu", "Ubuntu-C.ttf");
        View v = DataBindingUtil.inflate(inflater, R.layout.fr_onboarding_screen_common, null, true).getRoot();
        container = (RelativeLayout) v.findViewById(R.id.container);
        txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        txtDesc = (TextView) v.findViewById(R.id.txtDesc);
        img = (ImageView) v.findViewById(R.id.img);
        container.setBackgroundResource(R.color.promptBgColor);
        txtTitle.setText(R.string.strDoPaperLess);
        txtDesc.setText(R.string.msgDoPaperLess);
        img.setImageResource(R.drawable.ic_on_boarding_do_paper_less4);
        return v;
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

