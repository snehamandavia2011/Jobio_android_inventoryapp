package com.stackio.jobio.officeApp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.stephentuso.welcome.FragmentWelcomePage;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeConfiguration;
import com.xwray.fontbinding.FontCache;

import coaching.CoachingPreference;

import onboarding.frOnBoardingScreen;
import onboarding.frOnBoardingScreen1;
import onboarding.frOnBoardingScreen2;
import onboarding.frOnBoardingScreen3;
import onboarding.frOnBoardingScreen4;
import onboarding.frOnBoardingScreen5;
import onboarding.frOnBoardingScreen6;
import utility.Logger;

public class acWelcome extends WelcomeActivity {
    Context ctx;

    @Override
    protected WelcomeConfiguration configuration() {
        ctx = getApplicationContext();
        FontCache.getInstance(ctx).addFont("Ubuntu", "Ubuntu-C.ttf");
        if (CoachingPreference.needToShowPrompt(ctx, CoachingPreference.IS_APP_LOAD_FIRST_TIME)) {
            CoachingPreference.updatePreference(ctx, CoachingPreference.IS_APP_LOAD_FIRST_TIME);
            return new WelcomeConfiguration.Builder(this)
                    .swipeToDismiss(true)
                    .defaultTitleTypefacePath("Ubuntu-C.ttf")
                    .defaultHeaderTypefacePath("Ubuntu-M.ttf")
                    .page(new FragmentWelcomePage() {
                        @Override
                        protected Fragment fragment() {
                            return new frOnBoardingScreen1();
                        }
                    }.background(R.color.welcomeBG))
                    .page(new FragmentWelcomePage() {
                        @Override
                        protected Fragment fragment() {
                            return new frOnBoardingScreen2();
                        }
                    }.background(R.color.promptBgColor))
                    .page(new FragmentWelcomePage() {
                        @Override
                        protected Fragment fragment() {
                            return new frOnBoardingScreen3();
                        }
                    }.background(R.color.welcomeBG))
                    .page(new FragmentWelcomePage() {
                        @Override
                        protected Fragment fragment() {
                            return new frOnBoardingScreen4();
                        }
                    }.background(R.color.promptBgColor))
                    .page(new FragmentWelcomePage() {
                        @Override
                        protected Fragment fragment() {
                            return new frOnBoardingScreen5();
                        }
                    }.background(R.color.welcomeBG))
                    .page(new FragmentWelcomePage() {
                        @Override
                        protected Fragment fragment() {
                            return new frOnBoardingScreen6();
                        }
                    }.background(R.color.promptBgColor))
                    .build();
        } else {
            Intent i = new Intent(ctx, acSplash.class);
            startActivity(i);
            finish();
            return null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.debug("ac welcome onstop");
        Intent i = new Intent(ctx, acSplash.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}