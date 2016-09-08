package com.lab360io.jobio.inventoryapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.xwray.fontbinding.FontCache;

import utility.ConstantVal;
import utility.Helper;


public class acQRCodeScanner extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {
    private QRCodeReaderView mydecoderview;
    TextView txtEnterManualCode;
    Handler handler = new Handler();
    AppCompatActivity ac;
    Helper objHelper = new Helper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.qrcode_scanner);
        ac = this;
        /*android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle(getString(R.string.strCompany));
        ab.setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.tilt)));*/
        /*new Thread() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        objHelper.setActionBar(ac, getString(R.string.strCompanySetup), null, false);
                    }
                });
            }
        }.start();*/
        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        mydecoderview.setOnQRCodeReadListener(this);

        txtEnterManualCode = (TextView) findViewById(R.id.txtEnterManuallyQRCode);
        txtEnterManualCode.setPaintFlags(txtEnterManualCode.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        txtEnterManualCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), acManualQRCode.class);
                startActivityForResult(i, ConstantVal.EXIT_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantVal.EXIT_REQUEST_CODE && resultCode == ConstantVal.EXIT_RESPONSE_CODE) {
            ac.setResult(ConstantVal.EXIT_RESPONSE_CODE);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mydecoderview.getCameraManager().startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mydecoderview.getCameraManager().stopPreview();
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        String strQRCode = text;
        objHelper.verifyingQRcode(ac, handler, strQRCode, new View[]{});
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (objHelper.dtDialog != null && objHelper.dtDialog.getVisibility() == View.VISIBLE)
            objHelper.dtDialog.setVisibility(View.GONE);
    }

    @Override
    public void cameraNotFound() {

    }

    @Override
    public void QRCodeNotFoundOnCamImage() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }
}
