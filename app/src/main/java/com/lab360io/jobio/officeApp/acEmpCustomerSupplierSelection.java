package com.lab360io.jobio.officeApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.xwray.fontbinding.FontCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import adapter.EmpCustomerSupplierAdapter;
import adapter.TabsPagerAdapter;
import entity.BusinessAccountdbDetail;
import entity.ClientCustEmpSupplier;
import fragment.frAllUsers;
import fragment.frStockTransaction;
import utility.ConstantVal;
import utility.DotProgressBar;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;
import utility.ServerResponse;
import utility.URLMapping;

public class acEmpCustomerSupplierSelection extends AppCompatActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
    public static final String ALL = "0";
    public static final String CUSTOMER = "1";
    public static final String EMPLOYEE = "2";
    public static final String SUPPLIER = "3";
    public static final String USER_TYPE = "user_type";
    public static final String CURRENT_TAB = "current_tab";
    AppCompatActivity ac;
    Context mContext;
    Helper objHelper = new Helper();
    private TabHost mTabHost;
    private ViewPager mViewPager;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
    private PagerAdapter mPagerAdapter;
    String strPartyType, current_tab;
    int actionBarHeight = 0;
    EditText edSearchBox;
    RelativeLayout rlSearchBox;
    ImageButton btnSearchClear;
    ArrayList<ClientCustEmpSupplier> arrAllUser = null;
    ArrayList<ClientCustEmpSupplier> arrEmployee = null;
    ArrayList<ClientCustEmpSupplier> arrCustomer = null;
    ArrayList<ClientCustEmpSupplier> arrSupplier = null;
    LinearLayout lyMainContent;
    DotProgressBar dot_progress_bar;
    RelativeLayout lyNoContent;
    ImageView imgIcon;
    TextView txtMessage;
    String actionbarText;

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
        DataBindingUtil.setContentView(this, R.layout.emp_customer_supplier_selection);
        ac = this;
        mContext = this;
        TypedValue tv = new TypedValue();
        if (((AppCompatActivity) mContext).getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, mContext.getResources().getDisplayMetrics());
        }
        rlSearchBox = (RelativeLayout) findViewById(R.id.rlSearchBox);
        btnSearchClear = (ImageButton) findViewById(R.id.btnSearchClear);
        edSearchBox = (EditText) findViewById(R.id.edSearchBox);
        mViewPager = (ViewPager) super.findViewById(R.id.viewpager);
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        lyMainContent = (LinearLayout) findViewById(R.id.lyMainContent);
        lyNoContent = (RelativeLayout) findViewById(R.id.lyNoContent);
        dot_progress_bar = (DotProgressBar) findViewById(R.id.dot_progress_bar);
        imgIcon = (ImageView) findViewById(R.id.imgIcon);
        txtMessage = (TextView) findViewById(R.id.txtMessage);

        if (this.getIntent().getExtras() != null) {
            strPartyType = this.getIntent().getStringExtra(frStockTransaction.PARTY_TYPE);//either from or to
            current_tab = this.getIntent().getStringExtra(CURRENT_TAB);
        }
        if (strPartyType.equals(frStockTransaction.FROM)) {
            actionbarText = getString(R.string.strSelectFromType);
        } else if (strPartyType.equals(frStockTransaction.TO)) {
            actionbarText = getString(R.string.strSelectToType);
        }
        setActionBar(ac, actionbarText, false);

        if (strPartyType.equals(ALL))
            edSearchBox.setHint(getString(R.string.strSearchParty) + " " + getString(R.string.strAll));
        else if (strPartyType.equals(EMPLOYEE))
            edSearchBox.setHint(getString(R.string.strSearchParty) + " " + getString(R.string.strEmployee));
        else if (strPartyType.equals(CUSTOMER))
            edSearchBox.setHint(getString(R.string.strSearchParty) + " " + getString(R.string.strCustomer));
        else if (strPartyType.equals(SUPPLIER))
            edSearchBox.setHint(getString(R.string.strSearchParty) + " " + getString(R.string.strSupplier));

        this.initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
        this.intialiseViewPager();


        edSearchBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edSearchBox.requestFocusFromTouch();
                return true;
            }
        });

        edSearchBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Logger.debug("edSearchBox onFocusChange:" + hasFocus);
                if (hasFocus) {
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                            .showSoftInput(edSearchBox, InputMethodManager.SHOW_FORCED);
                } else {
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(edSearchBox.getWindowToken(), 0);
                }
            }
        });

        btnSearchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edSearchBox.setText("");
            }
        });

        edSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String str = s.toString();
                fillListView(str);
            }
        });
        getFromToList();
    }

    private void intialiseViewPager() {

        List<Fragment> fragments = new Vector<Fragment>();
        Fragment frAllUser = new frAllUsers();
        Bundle bdlAllUser = new Bundle();
        bdlAllUser.putString(USER_TYPE, ALL);
        frAllUser.setArguments(bdlAllUser);

        Fragment frCustomer = new frAllUsers();
        Bundle bdlCustomer = new Bundle();
        bdlCustomer.putString(USER_TYPE, CUSTOMER);
        frCustomer.setArguments(bdlCustomer);

        Fragment frEmp = new frAllUsers();
        Bundle bdlEmp = new Bundle();
        bdlEmp.putString(USER_TYPE, EMPLOYEE);
        frEmp.setArguments(bdlEmp);

        Fragment frSupplier = new frAllUsers();
        Bundle bdlSupplier = new Bundle();
        bdlSupplier.putString(USER_TYPE, SUPPLIER);
        frSupplier.setArguments(bdlSupplier);

        fragments.add(frAllUser);
        fragments.add(frEmp);
        fragments.add(frCustomer);
        fragments.add(frSupplier);

        this.mPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(), fragments);
        //
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.setOnPageChangeListener(this);
    }

    /**
     * Initialise the Tab Host
     */
    private static void AddTab(acEmpCustomerSupplierSelection activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    private void initialiseTabHost(Bundle args) {
        mTabHost.setup();
        TabInfo tabInfo = null;
        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View all = DataBindingUtil.inflate(mInflater, R.layout.custom_tab, null, true).getRoot();
        ((TextView) (all.findViewById(R.id.text))).setText(getString(R.string.strAll).toUpperCase());
        ((ImageView) (all.findViewById(R.id.img))).setVisibility(View.GONE);

        View customer = DataBindingUtil.inflate(mInflater, R.layout.custom_tab, null, true).getRoot();
        ((TextView) (customer.findViewById(R.id.text))).setText(getString(R.string.strCustomer).toUpperCase());
        ((ImageView) (customer.findViewById(R.id.img))).setVisibility(View.GONE);

        View employee = DataBindingUtil.inflate(mInflater, R.layout.custom_tab, null, true).getRoot();
        ((TextView) (employee.findViewById(R.id.text))).setText(getString(R.string.strEmployee).toUpperCase());
        ((ImageView) (employee.findViewById(R.id.img))).setVisibility(View.GONE);

        View supplier = DataBindingUtil.inflate(mInflater, R.layout.custom_tab, null, true).getRoot();
        ((TextView) (supplier.findViewById(R.id.text))).setText(getString(R.string.strSupplier).toUpperCase());
        ((ImageView) (supplier.findViewById(R.id.img))).setVisibility(View.GONE);

        acEmpCustomerSupplierSelection.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec(ALL).setIndicator(all), (tabInfo = new TabInfo(ALL, frAllUsers.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        acEmpCustomerSupplierSelection.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec(CUSTOMER).setIndicator(customer), (tabInfo = new TabInfo(CUSTOMER, frAllUsers.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        acEmpCustomerSupplierSelection.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec(EMPLOYEE).setIndicator(employee), (tabInfo = new TabInfo(EMPLOYEE, frAllUsers.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        acEmpCustomerSupplierSelection.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec(SUPPLIER).setIndicator(supplier), (tabInfo = new TabInfo(SUPPLIER, frAllUsers.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        mTabHost.setCurrentTabByTag(current_tab);
        this.onTabChanged(current_tab);
        this.mViewPager.post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(Integer.parseInt(current_tab));
            }
        });
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
        //Logger.debug("............................." + this.mViewPager.getCurrentItem() + " " + pos);
        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            View viewTab = mTabHost.getTabWidget().getChildAt(i);
            viewTab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.tilt)));
        }
        View viewCurrent = mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab());
        viewCurrent.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.lightGrey)));

        fillListView(edSearchBox.getText().toString());
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

    Toolbar toolbar;

    public void setActionBar(final AppCompatActivity ac, final String strText, final boolean needToShowSwitch) {
        new AsyncTask() {
            Toolbar toolbar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (toolbar == null) {
                    toolbar = (Toolbar) ac.findViewById(R.id.toolbar);
                    ac.setSupportActionBar(toolbar);
                }
                ac.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ac.getResources()
                        .getColor(R.color.tilt)));
            }

            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                ac.invalidateOptionsMenu();
                ActionBar actionBar;
                actionBar = ac.getSupportActionBar();
                /*actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);*/
                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setBackgroundDrawable(new ColorDrawable(ac.getResources()
                        .getColor(R.color.tilt)));
                final View v = DataBindingUtil.inflate(ac.getLayoutInflater(), R.layout.action_search_user, null, true).getRoot();
                v.setVisibility(View.GONE);
                actionBar.setCustomView(v);
                actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

                Toolbar parent = (Toolbar) v.getParent();
                parent.setPadding(0, 0, 0, 0);//for tab otherwise give space in tab
                parent.setContentInsetsAbsolute(0, 0);
                TextView txtName = (TextView) v.findViewById(R.id.txtName);
                txtName.setText(strText);

                ImageButton btnBack = (ImageButton) v.findViewById(R.id.btnBack);
                RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(actionBarHeight, actionBarHeight);
                rlParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                btnBack.setLayoutParams(rlParams);
                v.setVisibility(View.VISIBLE);

                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                SwitchCompat switchCompat = (SwitchCompat) v.findViewById(R.id.switchSearch);
                if (needToShowSwitch)
                    switchCompat.setVisibility(View.VISIBLE);
                else
                    switchCompat.setVisibility(View.GONE);
                switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            rlSearchBox.setVisibility(View.VISIBLE);
                        } else {
                            edSearchBox.setText("");
                            rlSearchBox.setVisibility(View.GONE);
                        }
                    }
                });

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void getFromToList() {
        new AsyncTask() {
            ServerResponse sr;
            boolean isNetworkAvail;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dot_progress_bar.setVisibility(View.VISIBLE);
                isNetworkAvail = new HttpEngine().isNetworkAvailable(mContext);
                if (!isNetworkAvail) {
                    dot_progress_bar.setVisibility(View.GONE);
                    lyNoContent.setVisibility(View.VISIBLE);
                    lyMainContent.setVisibility(View.GONE);
                    txtMessage.setText(getString(R.string.strInternetNotAvaiable));
                    imgIcon.setImageResource(R.drawable.ic_field_engineer_grey);
                    setActionBar(ac, actionbarText, false);
                } else {
                    lyNoContent.setVisibility(View.GONE);
                    lyMainContent.setVisibility(View.VISIBLE);
                    setActionBar(ac, actionbarText, true);
                }
            }

            @Override
            protected Object doInBackground(Object[] params) {
                if (isNetworkAvail) {
                    final HttpEngine objHttpEngine = new HttpEngine();
                    final String tokenId = Helper.getStringPreference(mContext, ConstantVal.TOKEN, "");
                    String account_id = Helper.getStringPreference(mContext, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                    final URLMapping um = ConstantVal.getCustomerEmployeeSupplierList(mContext);
                    sr = objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(), new String[]{tokenId, account_id}, um.getParamNames(), um.isNeedToSync());
                    String result = sr.getResponseString();
                    if (result != null && result.length() > 0) {
                        arrAllUser = ClientCustEmpSupplier.parseData(result);
                        if (arrAllUser != null) {
                            arrCustomer = new ArrayList<ClientCustEmpSupplier>();
                            arrEmployee = new ArrayList<ClientCustEmpSupplier>();
                            arrSupplier = new ArrayList<ClientCustEmpSupplier>();
                            for (ClientCustEmpSupplier objClientCustEmpSupplier : arrAllUser) {
                                if (objClientCustEmpSupplier.getType().equals(getString(R.string.strCustomer)))
                                    arrCustomer.add(objClientCustEmpSupplier);
                                else if (objClientCustEmpSupplier.getType().equals(getString(R.string.strEmployee)))
                                    arrEmployee.add(objClientCustEmpSupplier);
                                else if (objClientCustEmpSupplier.getType().equals(getString(R.string.strSupplier)))
                                    arrSupplier.add(objClientCustEmpSupplier);
                            }
                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                dot_progress_bar.clearAnimation();
                dot_progress_bar.setVisibility(View.GONE);
                if (arrAllUser == null && isNetworkAvail) {
                    setActionBar(ac, actionbarText, false);
                    Helper.displaySnackbar(ac, ConstantVal.ServerResponseCode.getMessage(mContext, sr.getResponseString()), ConstantVal.ToastBGColor.INFO);
                    lyNoContent.setVisibility(View.VISIBLE);
                    lyMainContent.setVisibility(View.GONE);
                    txtMessage.setText(ConstantVal.ServerResponseCode.getMessage(mContext, sr.getResponseCode()));
                    imgIcon.setImageResource(R.drawable.ic_field_engineer_grey);
                } else if (arrAllUser != null) {
                    setActionBar(ac, actionbarText, true);
                    lyNoContent.setVisibility(View.GONE);
                    lyMainContent.setVisibility(View.VISIBLE);
                    fillListView("");
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void fillListView(final String strFilteredText) {
        if (arrAllUser == null)
            return;
        new AsyncTask() {
            ImageView imgNoUser;
            TextView txtMessage;
            LinearLayout lyMainContent;
            RelativeLayout lyNoContent;
            ListView lvlUser;
            String strCurrentTag;
            ArrayList<ClientCustEmpSupplier> filteredUsers = new ArrayList<ClientCustEmpSupplier>();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Logger.debug(mTabHost.getCurrentTabTag() + " " + mTabHost.getCurrentTab());
                strCurrentTag = mTabHost.getCurrentTabTag();
                Fragment frCurrent = (Fragment) mViewPager.getAdapter().instantiateItem(mViewPager, (mTabHost.getCurrentTab()));
                View view = frCurrent.getView();
                lyMainContent = (LinearLayout) view.findViewById(R.id.lyMainContent);
                lyNoContent = (RelativeLayout) view.findViewById(R.id.lyNoContent);
                lvlUser = (ListView) view.findViewById(R.id.lvlUser);
                imgNoUser = (ImageView) view.findViewById(R.id.imgNoUser);
                txtMessage = (TextView) view.findViewById(R.id.txtMessage);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                if (strCurrentTag.equals(ALL)) {
                    if (strFilteredText.trim().equals("")) {
                        filteredUsers = arrAllUser;
                    } else {
                        for (ClientCustEmpSupplier objClientCustEmpSupplier : arrAllUser) {
                            if (objClientCustEmpSupplier.getName().toLowerCase().contains(strFilteredText.toLowerCase()))
                                filteredUsers.add(objClientCustEmpSupplier);
                        }
                    }
                } else if (strCurrentTag.equals(CUSTOMER)) {
                    if (strFilteredText.trim().equals("")) {
                        filteredUsers = arrCustomer;
                    } else {
                        for (ClientCustEmpSupplier objClientCustEmpSupplier : arrCustomer) {
                            if (objClientCustEmpSupplier.getName().toLowerCase().contains(strFilteredText.toLowerCase()))
                                filteredUsers.add(objClientCustEmpSupplier);
                        }
                    }
                } else if (strCurrentTag.equals(EMPLOYEE)) {
                    if (strFilteredText.trim().equals("")) {
                        filteredUsers = arrEmployee;
                    } else {
                        for (ClientCustEmpSupplier objClientCustEmpSupplier : arrEmployee) {
                            if (objClientCustEmpSupplier.getName().toLowerCase().contains(strFilteredText.toLowerCase()))
                                filteredUsers.add(objClientCustEmpSupplier);
                        }
                    }
                } else if (strCurrentTag.equals(SUPPLIER)) {
                    if (strFilteredText.trim().equals("")) {
                        filteredUsers = arrSupplier;
                    } else {
                        for (ClientCustEmpSupplier objClientCustEmpSupplier : arrSupplier) {
                            if (objClientCustEmpSupplier.getName().toLowerCase().contains(strFilteredText.toLowerCase()))
                                filteredUsers.add(objClientCustEmpSupplier);
                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (filteredUsers.size() >= 1) {
                    lyMainContent.setVisibility(View.VISIBLE);
                    lyNoContent.setVisibility(View.GONE);
                    lvlUser.setAdapter(new EmpCustomerSupplierAdapter(mContext, filteredUsers));
                } else {
                    lyMainContent.setVisibility(View.GONE);
                    lyNoContent.setVisibility(View.VISIBLE);
                    imgNoUser.setImageResource(R.drawable.ic_field_engineer_grey);
                    txtMessage.setText(getString(R.string.strNo) + " " + getUserName(current_tab).toString() + " " + getString(R.string.strFoundWith) + " '" + strFilteredText + "'");
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public String getUserName(String type) {
        if (type.equals(ALL)) {
            return getString(R.string.strUSer);
        } else if (type.equals(EMPLOYEE)) {
            return getString(R.string.strEmployee);
        } else if (type.equals(CUSTOMER)) {
            return getString(R.string.strCustomer);
        } else if (type.equals(SUPPLIER)) {
            return getString(R.string.strSupplier);
        }
        return "";
    }

}
