package com.lab360io.jobio.officeApp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.xwray.fontbinding.FontCache;

import utility.ConstantVal;
import utility.Helper;
import utility.Logger;


public class acQRCodeScanner extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {
    private QRCodeReaderView mydecoderview;
    ImageButton btnBack;
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
        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        mydecoderview.setOnQRCodeReadListener(this);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(getApplicationContext(), acManualQRCode.class);
                //startActivityForResult(i, ConstantVal.EXIT_REQUEST_CODE);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ConstantVal.EXIT_RESPONSE_CODE) {
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
            Logger.writeToCrashlytics(e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mydecoderview.getCameraManager().stopPreview();
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        mydecoderview.getCameraManager().stopPreview();
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
