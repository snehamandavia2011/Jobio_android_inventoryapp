package com.lab360io.jobio.inventoryapp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lab360io.jobio.inventoryapp.R;
import com.xwray.fontbinding.FontCache;

import io.fabric.sdk.android.services.concurrency.AsyncTask;
import utility.Helper;

public class acInspectTransaction extends AppCompatActivity {
    AppCompatActivity ac;
    Context mContext;
    Helper objHelper = new Helper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.inspect_transaction);
        ac = this;
        mContext = this;
        objHelper.setActionBar(ac, mContext.getString(R.string.strAssetDetail));
        setData();
    }

    private void setData(){
        new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] objects) {
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
