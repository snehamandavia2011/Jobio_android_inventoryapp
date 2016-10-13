package com.lab360io.jobio.inventoryapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.lab360io.jobio.inventoryapp.R;
import com.xwray.fontbinding.FontCache;

import utility.ConstantVal;
import utility.DotProgressBar;
import utility.Helper;
import utility.HttpEngine;

public class acSearchItemByBarcodeScanner extends AppCompatActivity {
    TextView txtEnterManuallyBarCode;
    AppCompatActivity ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.search_item_by_barcode);
        ac = this;
        txtEnterManuallyBarCode = (TextView) findViewById(R.id.txtEnterManuallyBarCode);
        txtEnterManuallyBarCode.setPaintFlags(txtEnterManuallyBarCode.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        txtEnterManuallyBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), acSearchItemByBarcodeManual.class);
                startActivityForResult(i, ConstantVal.SEARCH_ITEM_BY_BARCODE_REQUEST_CODE);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }
}
