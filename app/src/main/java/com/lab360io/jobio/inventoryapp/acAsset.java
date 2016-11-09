package com.lab360io.jobio.inventoryapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.lab360io.jobio.inventoryapp.R;
import com.xwray.fontbinding.FontCache;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import adapter.TabsPagerAdapter;
import fragment.frAssetsAsset;
import fragment.frAssetsInspect;
import fragment.frAssetsService;
import utility.ConstantVal;
import utility.Helper;
import utility.Logger;
import utility.MessageLoader;

public class acAsset extends ActionBarActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
    public static final String ASSET = "Tab1";
    public static final String SERVICE = "Tab2";
    public static final String INSPECT = "Tab3";
    String current_tab = ASSET;
    ActionBarActivity ac;
    Context mContext;
    Helper objHelper = new Helper();
    private TabHost mTabHost;
    private ViewPager mViewPager;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
    private PagerAdapter mPagerAdapter;
    RelativeLayout lyNoContent;
    FrameLayout lyMainContent;

    private class TabInfo {
        private String tag;
        private Class<?> clss;
        private Bundle args;
        private Fragment fragment;

        TabInfo(String tag, Class<?> clazz, Bundle args) {
            this.tag = tag;
            this.clss = clazz;
            this.args = args;
        }

    }

    class TabFactory implements TabHost.TabContentFactory {
        private final Context mContext;

        public TabFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //v.setMinimumWidth(0);
            //v.setMinimumHeight(0);
            return v;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.asset);
        ac = this;
        mContext = this;
        mViewPager = (ViewPager) super.findViewById(R.id.viewpager);
        lyNoContent = (RelativeLayout) findViewById(R.id.lyNoContent);
        lyMainContent = (FrameLayout) findViewById(R.id.lyMainContent);
        objHelper.setActionBar(ac, getString(R.string.strAsset), getString(R.string.strAsset));
        if (this.getIntent().getExtras() != null) {
            try {
                current_tab = this.getIntent().getStringExtra("tab");
            } catch (Exception e) {
                e.printStackTrace();
                current_tab = ASSET;
            }
        }
        if (Helper.isModuleAccessAllow(mContext, ConstantVal.ModuleAccess.ASSET) == false) {
            lyNoContent.setVisibility(View.VISIBLE);
            lyMainContent.setVisibility(View.GONE);
            ((TextView) findViewById(R.id.txtMessage)).setText(getString(R.string.msgFeatureNotAvailContactAdmin));
            ((ImageView) findViewById(R.id.imgIcon)).setBackgroundResource(R.drawable.ic_asset_white_big);
        } else {
            lyNoContent.setVisibility(View.GONE);
            lyMainContent.setVisibility(View.VISIBLE);
            this.initialiseTabHost(savedInstanceState);
            if (savedInstanceState != null) {
                mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
            }
            this.intialiseViewPager();
        }
    }

    private void intialiseViewPager() {

        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(new frAssetsAsset());
        fragments.add(new frAssetsInspect());
        fragments.add(new frAssetsService());
        this.mPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(), fragments);
        //
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.setOnPageChangeListener(this);
    }

    /**
     * Initialise the Tab Host
     */
    private static void AddTab(acAsset activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    private void initialiseTabHost(Bundle args) {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        TabInfo tabInfo = null;
        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View vAsset = DataBindingUtil.inflate(mInflater, R.layout.custom_tab, null, true).getRoot();
        ((TextView) (vAsset.findViewById(R.id.text))).setText(getString(R.string.strAsset).toUpperCase());
        ((ImageView) (vAsset.findViewById(R.id.img))).setImageResource(R.drawable.ic_asset);

        View iAsset = DataBindingUtil.inflate(mInflater, R.layout.custom_tab, null, true).getRoot();
        ((TextView) (iAsset.findViewById(R.id.text))).setText(getString(R.string.strInspect).toUpperCase());
        ((ImageView) (iAsset.findViewById(R.id.img))).setImageResource(R.drawable.ic_inspect);

        View sAsset = DataBindingUtil.inflate(mInflater, R.layout.custom_tab, null, true).getRoot();
        ((TextView) (sAsset.findViewById(R.id.text))).setText(getString(R.string.strService).toUpperCase());
        ((ImageView) (sAsset.findViewById(R.id.img))).setImageResource(R.drawable.ic_service);

        acAsset.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec(ASSET).setIndicator(vAsset), (tabInfo = new TabInfo(ASSET, frAssetsAsset.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        acAsset.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec(INSPECT).setIndicator(iAsset), (tabInfo = new TabInfo(INSPECT, frAssetsInspect.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        acAsset.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec(SERVICE).setIndicator(sAsset), (tabInfo = new TabInfo(SERVICE, frAssetsService.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        mTabHost.setCurrentTabByTag(current_tab);
        this.onTabChanged(current_tab);
        mTabHost.setOnTabChangedListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString("tab", mTabHost.getCurrentTabTag());
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.mTabHost.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabChanged(String tabId) {
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            View viewTab = mTabHost.getTabWidget().getChildAt(i);
            viewTab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.tilt)));
        }
        View viewCurrent = mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab());
        viewCurrent.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.lightGrey)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        objHelper.registerSessionTimeoutBroadcast(ac);
    }

    @Override
    protected void onStop() {
        super.onStop();
        objHelper.unRegisterSesionTimeOutBroadcast(ac);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ConstantVal.EXIT_RESPONSE_CODE) {
            ac.setResult(ConstantVal.EXIT_RESPONSE_CODE);
            finish();
        }
    }
}
