package com.lab360io.jobio.officeApp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.xwray.fontbinding.FontCache;

import java.util.ArrayList;

import entity.ClientAsset;
import utility.ConstantVal;
import utility.Helper;
import utility.Logger;

public class acSearchAssetByQR extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {
    boolean isPreviewOn = true;
    com.dlazaro66.qrcodereaderview.QRCodeReaderView mydecoderview;
    AppCompatActivity ac;
    Helper objHelper = new Helper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.search_asset_by_qr);
        ac = this;
        setData();
    }

    private void setData() {
        isPreviewOn = true;
        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        mydecoderview.setOnQRCodeReadListener(this);
        try {
            mydecoderview.getCameraManager().startPreview();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mydecoderview.getCameraManager().stopPreview();
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        mydecoderview.getCameraManager().stopPreview();
        isPreviewOn = false;
        if (!isPreviewOn) {
            //Logger.debug("isPreviewOn:" + isPreviewOn);
            final String strQRCode = text;
            new AsyncTask() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    ArrayList<ClientAsset> arr = ClientAsset.getDataFromDatabase(getApplicationContext(), strQRCode);
                    if (arr != null && arr.size() == 1) {
                        Intent i = new Intent(ac.getApplicationContext(), acAssetDetail.class);
                        i.putExtra("AssetId", strQRCode);
                        startActivityForResult(i, ConstantVal.EXIT_REQUEST_CODE);
                        finish();
                    } else {
                        objHelper.displaySnackbar(ac, getString(R.string.msgAssetDetailNotAvail), ConstantVal.ToastBGColor.DANGER);
                        setData();
                    }
                }

                @Override
                protected Object doInBackground(Object[] params) {
                    return null;
                }
            }.execute(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ConstantVal.EXIT_RESPONSE_CODE) {
            ac.setResult(ConstantVal.EXIT_RESPONSE_CODE);
            finish();
        }
    }
}
