package com.lab360io.jobio.inventoryapp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.lab360io.jobio.inventoryapp.R;
import com.xwray.fontbinding.FontCache;

import me.zhanghai.android.materialedittext.MaterialEditText;
import utility.Helper;
import utility.HttpEngine;
import utility.OptionMenu;

public class acSearchItemByBarcodeManual extends AppCompatActivity {
    Button btnOk, btnCancel;
    MaterialEditText edBarCode;
    AppCompatActivity ac;
    Handler handler = new Handler();
    Helper objHelper = new Helper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.search_item_barcode_manual);
        ac = this;
        objHelper.setActionBar(ac, getString(R.string.strSearchItem));
        edBarCode = (MaterialEditText) findViewById(R.id.edBarCode);
        btnOk = (Button) findViewById(R.id.btnOK);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helper.isFieldBlank(edBarCode.getText().toString())) {
                    edBarCode.setError(getString(R.string.strEnterBarCode));
                    requestFocus(edBarCode);
                } else {
                    objHelper.getItemDetailByBarcode(ac, handler, edBarCode.getText().toString(), new View[]{btnOk, btnCancel}, null);
                }
            }
        });
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        objHelper.unRegisterSesionTimeOutBroadcast(ac);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}