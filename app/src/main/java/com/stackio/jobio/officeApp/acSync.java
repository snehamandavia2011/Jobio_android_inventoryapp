package com.stackio.jobio.officeApp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xwray.fontbinding.FontCache;

import entity.ClientLocationTrackingInterval;
import service.serDeviceToServerSync;
import service.serLocationTracker;
import service.serServerToDeviceSync;
import utility.CircleImageView;
import utility.ConstantVal;
import utility.DataBase;
import utility.Helper;
import utility.Logger;
import utility.OptionMenu;


public class acSync extends AppCompatActivity {
    AppCompatActivity ac;
    Helper objHelper = new Helper();
    TextView txtMessageCount, txtInspectCount, txtServiceCount, txtTransactionCount, txtSyncServiceState, txtNetworkState;
    ImageView imgNetworkState;
    CircleImageView imgServiceState;
    LinearLayout lyUnSyncData, lyRestartService;
    ImageButton btnStartService;
    TelephonyManager telephonyManager;
    myPhoneStateListener psListener;
    boolean DtoS = false, StoD = false, Loc = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.sync);
        ac = this;
        psListener = new myPhoneStateListener();
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        setData();
        isMyServiceRunning();
    }

    private void setData() {
        new AsyncTask() {
            int unsyncMessage = 0, unsyncInspect = 0, unsyncService = 0, unsyncTransaction = 0;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                objHelper.setActionBar(ac, getString(R.string.strSync));
                txtMessageCount = (TextView) findViewById(R.id.txtMessageCount);
                txtInspectCount = (TextView) findViewById(R.id.txtInspectCount);
                txtServiceCount = (TextView) findViewById(R.id.txtServiceCount);
                txtTransactionCount = (TextView) findViewById(R.id.txtTransactionCount);
                txtSyncServiceState = (TextView) findViewById(R.id.txtSyncServiceState);
                txtNetworkState = (TextView) findViewById(R.id.txtNetworkState);
                imgServiceState = (CircleImageView) findViewById(R.id.imgServiceState);
                imgNetworkState = (ImageView) findViewById(R.id.imgNetworkState);
                lyUnSyncData = (LinearLayout) findViewById(R.id.lyUnSyncData);
                lyRestartService = (LinearLayout) findViewById(R.id.lyRestartService);
                btnStartService = (ImageButton) findViewById(R.id.btnStartService);
                btnStartService.setOnClickListener(startService);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                DataBase db = new DataBase(ac.getApplicationContext());
                db.open();
                unsyncInspect = db.getCounts(DataBase.device_to_db_sync_table, "URL like '%updateInspectTransaction%' and isSync='0'");
                unsyncService = db.getCounts(DataBase.device_to_db_sync_table, "URL like '%updateServiceTransaction%' and isSync='0'");
                unsyncTransaction = db.getCounts(DataBase.device_to_db_sync_table, "URL like '%saveJobInvoiceTransaction%' or URL like '%savePOTransaction%' and isSync='0'");
                unsyncMessage = db.getCounts(DataBase.device_to_db_sync_table, "URL like '%managemessage/saveMessage%' and isSync='0'");
                Logger.debug(unsyncInspect + " " + unsyncService + " " + unsyncTransaction + " " + unsyncMessage);
                db.close();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (unsyncMessage == 0 && unsyncInspect == 0 && unsyncService == 0 && unsyncTransaction == 0) {
                    lyUnSyncData.setVisibility(View.GONE);
                } else {
                    lyUnSyncData.setVisibility(View.VISIBLE);
                    txtInspectCount.setText(String.valueOf(unsyncInspect));
                    txtServiceCount.setText(String.valueOf(unsyncService));
                    txtTransactionCount.setText(String.valueOf(unsyncTransaction));
                    txtMessageCount.setText(String.valueOf(unsyncMessage));
                }
            }
        }.execute();
    }

    OptionMenu objOptionMenu = new OptionMenu();

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
    protected void onStart() {
        super.onStart();
        objHelper.registerSessionTimeoutBroadcast(ac);
        telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    @Override
    protected void onStop() {
        super.onStop();
        objHelper.unRegisterSesionTimeOutBroadcast(ac);
        telephonyManager.listen(null, PhoneStateListener.LISTEN_NONE);
    }

    private class myPhoneStateListener extends PhoneStateListener {
        public int signalStrengthValue;

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            int strength = signalStrength.getGsmSignalStrength();//number of bars not ASU
            try {//Actual signal strength is hidden
                Class classFromName = Class.forName(SignalStrength.class.getName());
                java.lang.reflect.Method method = classFromName.getDeclaredMethod("getAsuLevel");//getDbm
                strength = (int) method.invoke(signalStrength);
                txtNetworkState.setText(getString(R.string.strConnected));
                imgNetworkState.setImageResource(R.drawable.ic_network_high_connection);
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.writeToCrashlytics(ex);
            }
            Logger.debug("Network strength:" + strength);
            if (strength == 99) {
                Logger.debug("ERROR!  GSM signal strength not available!");
                txtNetworkState.setText(getString(R.string.strDisconnected));
                imgNetworkState.setImageResource(R.drawable.ic_network_no_connection);
                return;
            }//99 = Unknown
            if (strength == 255) {
                txtNetworkState.setText(getString(R.string.strDisconnected));
                imgNetworkState.setImageResource(R.drawable.ic_network_no_connection);
                Logger.debug("ERROR!  UMTS signal strength not available!");
                return;
            }//255 = Unknown
        }
    }

    private void isMyServiceRunning() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                    if (serDeviceToServerSync.class.getName().equals(service.service.getClassName())) {
                        DtoS = true;
                    }
                    if (serServerToDeviceSync.class.getName().equals(service.service.getClassName())) {
                        StoD = true;
                    }
                    if (serLocationTracker.class.getName().equals(service.service.getClassName())) {
                        Loc = true;
                    }
                }
                Logger.debug(DtoS + " " + StoD + " " + Loc);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (StoD && DtoS && Loc) {
                    lyRestartService.setVisibility(View.GONE);
                    imgServiceState.setImageResource(R.color.green);
                    txtSyncServiceState.setText(getString(R.string.strConnected));
                } else {
                    lyRestartService.setVisibility(View.VISIBLE);
                    imgServiceState.setImageResource(R.color.lightGrey);
                    txtSyncServiceState.setText(getString(R.string.strDisconnected));
                }
            }
        }.execute();
    }

    View.OnClickListener startService = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    if (!DtoS || !StoD) {
                        Helper.startBackgroundService(ac.getApplicationContext());
                    }
                    if (!Loc) {
                        int intIntervalTime = Helper.getIntPreference(ac.getApplicationContext(), ClientLocationTrackingInterval.Fields.LOCATION_TRACKING_INTERVAL, 0);
                        Helper.scheduleLocationService(ac.getApplicationContext(), intIntervalTime);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    isMyServiceRunning();
                }
            }.execute();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ConstantVal.EXIT_RESPONSE_CODE) {
            setResult(ConstantVal.EXIT_RESPONSE_CODE);
            finish();
        }
    }
}
