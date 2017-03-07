package com.lab360io.jobio.officeApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.xwray.fontbinding.FontCache;

import java.util.ArrayList;

import adapter.AdminEmployeeListAdapter;
import entity.ClientAdminUser;
import entity.ClientAdminUserEmployee;
import utility.ConstantVal;
import utility.DataBase;
import utility.Helper;
import utility.MessageLoader;
import utility.OptionMenu;

public class acMessageEmployeeList extends AppCompatActivity {
    AppCompatActivity ac;
    Context mContext;
    Helper objHelper = new Helper();
    RelativeLayout rlNoDataFound;
    LinearLayout rlMainContent;
    ListView lvl;
    ArrayList<ClientAdminUserEmployee> arrClientAdminUserEmployee;
    public MessageLoader objMessageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.message_employee_list);
        ac = this;
        mContext = this;
        objMessageLoader = new MessageLoader(mContext);
        objMessageLoader.startTimer();
        objHelper.setActionBar(ac, getString(R.string.strMessage), getString(R.string.strMessage));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {
        new AsyncTask() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                rlMainContent = (LinearLayout) findViewById(R.id.rlMainContent);
                rlNoDataFound = (RelativeLayout) findViewById(R.id.lyNodataFound);
                lvl = (ListView) findViewById(R.id.lvl);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                String loginUserId = Helper.getStringPreference(mContext, ClientAdminUser.Fields.ADMINUSERID, "");
                DataBase db = new DataBase(ac.getApplicationContext());
                db.open();
                Cursor cur = null;
                cur = db.fetch(DataBase.adminuser_employee_table, null);
                if (cur != null && cur.getCount() > 0) {
                    arrClientAdminUserEmployee = new ArrayList<>();
                    cur.moveToFirst();
                    do {
                        if (cur.getString(1).equals(loginUserId))
                            continue;
                        //get count of unread message
                        String where = "from_id='" + cur.getString(1) + "' and to_id='" + loginUserId + "' and is_viewed='N'";
                        //Logger.debug(where);
                        int count = db.getCounts(DataBase.field_message_table, where);
                        arrClientAdminUserEmployee.add(new ClientAdminUserEmployee(cur.getString(1), cur.getString(2), cur.getString(3), cur.getString(4),
                                cur.getString(5), cur.getString(6), cur.getString(7), cur.getString(8), cur.getString(9), count, cur.getString(10)));
                    } while (cur.moveToNext());
                }
                cur.close();
                db.close();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (arrClientAdminUserEmployee != null) {
                    if (arrClientAdminUserEmployee.size() > 0) {
                        rlNoDataFound.setVisibility(View.GONE);
                        rlMainContent.setVisibility(View.VISIBLE);
                        AdminEmployeeListAdapter adp = new AdminEmployeeListAdapter(mContext, arrClientAdminUserEmployee);
                        lvl.setAdapter(adp);
                    } else {
                        rlNoDataFound.setVisibility(View.VISIBLE);
                        rlMainContent.setVisibility(View.GONE);
                    }
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private BroadcastReceiver objMessageList = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

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
        mContext.registerReceiver(objMessageList, new IntentFilter(ConstantVal.BroadcastAction.CHANGED_MESSAGE_LIST));
    }

    @Override
    protected void onStop() {
        super.onStop();
        mContext.unregisterReceiver(objMessageList);
        objHelper.unRegisterSesionTimeOutBroadcast(ac);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        objMessageLoader.stoptimertask();
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
