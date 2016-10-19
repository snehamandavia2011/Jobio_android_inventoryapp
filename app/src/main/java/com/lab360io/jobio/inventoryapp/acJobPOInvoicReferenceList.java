package com.lab360io.jobio.inventoryapp;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lab360io.jobio.inventoryapp.R;
import com.xwray.fontbinding.FontCache;

import utility.ConstantVal;
import utility.DotProgressBar;
import utility.Helper;

public class acJobPOInvoicReferenceList extends AppCompatActivity {
    ListView lvlJobPOInvoice;
    DotProgressBar dot_progress_bar;
    RelativeLayout lyNoContent,lyMainContent;
    ImageView imgNoData;
    TextView txtNoData;
    Helper objHelper = new Helper();
    AppCompatActivity ac;
    Context mContext;
    int ref_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.po_invoic_reference_list);
        ac = this;
        mContext = this;
        objHelper.setActionBar(ac, mContext.getString(R.string.strAssetDetail));
        setData();
    }

    private void setData() {
        new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (getIntent().getExtras() != null) {
                    ref_type = getIntent().getIntExtra("ref_type", 0);
                }
                lvlJobPOInvoice = (ListView) findViewById(R.id.lvlJobPOInvoice);
                dot_progress_bar = (DotProgressBar) findViewById(R.id.dot_progress_bar);
                lyNoContent = (RelativeLayout) findViewById(R.id.lyNoContent);
                lyMainContent = (RelativeLayout) findViewById(R.id.lyMainContent);
                imgNoData = (ImageView) findViewById(R.id.imgNoData);
                txtNoData = (TextView) findViewById(R.id.txtNoData);
                dot_progress_bar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                dot_progress_bar.setVisibility(View.GONE);
                if (dot_progress_bar != null)
                    ((ViewGroup) dot_progress_bar.getParent()).removeView(dot_progress_bar);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
