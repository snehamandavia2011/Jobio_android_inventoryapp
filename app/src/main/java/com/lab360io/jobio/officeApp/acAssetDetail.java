package com.lab360io.jobio.officeApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xwray.fontbinding.FontCache;

import asyncmanager.asyncLoadCommonData;
import entity.ClientAsset;
import entity.ClientAssetOwner;
import entity.ClientRegional;
import utility.CircleImageView;
import utility.ConstantVal;
import utility.DataBase;
import utility.DotProgressBar;
import utility.Helper;
import utility.Logger;

public class acAssetDetail extends AppCompatActivity {
    DotProgressBar dot_progress_bar;
    CircleImageView asset_image;
    ScrollView scrlMailContent;
    AppCompatActivity ac;
    Context mContext;
    Helper objHelper = new Helper();
    TextView txtAssetNameCategory, txtDesc, txtModel, txtManufacturer, txtSerialNumber, txtAssetLocation, txtCost, txtCurrentValue, txtPurchaseFrom, txtExpireDate, txtStatus, txtBarcode;
    LinearLayout lyassetOwner;
    public static LinearLayout lyAssetDetail;
    String assetId;
    ClientAsset objClientAsset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.asset_detail);
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
                objHelper.setActionBar(ac, mContext.getString(R.string.strAssetDetail));
                dot_progress_bar = (DotProgressBar) findViewById(R.id.dot_progress_bar);
                dot_progress_bar.setVisibility(View.VISIBLE);
                scrlMailContent = (ScrollView) findViewById(R.id.scrlMainContent);
                txtAssetNameCategory = (TextView) findViewById(R.id.txtAssetNameCategory);
                txtDesc = (TextView) findViewById(R.id.txtDesc);
                txtModel = (TextView) findViewById(R.id.txtModel);
                txtManufacturer = (TextView) findViewById(R.id.txtManufacturer);
                txtSerialNumber = (TextView) findViewById(R.id.txtSerialNumber);
                txtAssetLocation = (TextView) findViewById(R.id.txtAssetLocation);
                txtCost = (TextView) findViewById(R.id.txtCost);
                txtCurrentValue = (TextView) findViewById(R.id.txtCurrentValue);
                txtPurchaseFrom = (TextView) findViewById(R.id.txtPurchaseFrom);
                txtExpireDate = (TextView) findViewById(R.id.txtExpireDate);
                txtStatus = (TextView) findViewById(R.id.txtStatus);
                txtBarcode = (TextView) findViewById(R.id.txtBarcode);
                txtBarcode.setPaintFlags(txtBarcode.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                lyassetOwner = (LinearLayout) findViewById(R.id.lyassetOwner);
                lyAssetDetail = (LinearLayout) findViewById(R.id.lyAssetDetail);
                asset_image = (CircleImageView) findViewById(R.id.asset_image);
                Helper.setViewLayoutParmas(asset_image, 30, mContext);

                if (ac.getIntent().getExtras() != null) {
                    assetId = ac.getIntent().getStringExtra("AssetId");
                }
                txtBarcode.setOnClickListener(openBarcode);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                objClientAsset = ClientAsset.getDataFromDatabase(mContext, assetId).get(0);
                if (objClientAsset != null) {
                    new asyncLoadCommonData(mContext).loadAssetPhotoById(asset_image, objClientAsset, null, R.drawable.ic_nopic);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (objClientAsset != null) {
                    txtAssetNameCategory.setText(objClientAsset.getAmAsset_name() + "[" + objClientAsset.getActmCategory_name() + "]");
                    txtDesc.setText(objClientAsset.getAmDescription().equals("") ? getString(R.string.msgDescNotAvail) : objClientAsset.getAmDescription());
                    txtModel.setText(objClientAsset.getAmModel_name());
                    txtManufacturer.setText(objClientAsset.getAmManufacturer_name());
                    txtSerialNumber.setText(objClientAsset.getAmSerial_no());
                    txtAssetLocation.setText(objClientAsset.getAmAsset_location());
                    txtCost.setText(objClientAsset.getAmPurchase_cost());
                    txtCurrentValue.setText(objClientAsset.getAmCurrent_value());
                    txtPurchaseFrom.setText(objClientAsset.getAmPurchase_from());
                    txtExpireDate.setText(Helper.convertDateToString(objClientAsset.getAmDate_expired(),
                            Helper.getStringPreference(mContext, ClientRegional.Fields.DATE_FORMAT, ConstantVal.DATE_FORMAT)));
                    txtStatus.setText(objClientAsset.getAmAsset_status());
                    for (ClientAssetOwner obj : objClientAsset.getArrOwner()) {
                        View v = addItemToLayout(obj);
                        lyassetOwner.addView(v);
                    }
                    //txtBarcode.setText(objClientAsset.getAmBarcode_no());
                    scrlMailContent.setVisibility(View.VISIBLE);
                }
                dot_progress_bar.clearAnimation();
                dot_progress_bar.setVisibility(View.GONE);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    View.OnClickListener openBarcode = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Helper.openBarcodeDialog(mContext, objClientAsset.getAmBarcode_no());

        }
    };

    private View addItemToLayout(ClientAssetOwner obj) {
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View v = DataBindingUtil.inflate(mInflater, R.layout.asset_owner_item, null, true).getRoot();
        TextView txtEmployeeName = (TextView) v.findViewById(R.id.txtEmployeeName);
        TextView txtInOut = (TextView) v.findViewById(R.id.txtInOut);
        TextView txtDate = (TextView) v.findViewById(R.id.txtDate);
        ImageView imgInOut = (ImageView) v.findViewById(R.id.imgInOut);
        DataBase db = new DataBase(mContext);
        db.open();
        Cursor curEmp = db.fetch(DataBase.adminuser_employee_table, DataBase.adminuser_employee_int, "empId='" + obj.getAoEmployee_id() + "'");
        //Logger.debug(obj.getAoEmployee_id() + " " + curEmp.getCount());
        if (curEmp != null && curEmp.getCount() > 0) {
            curEmp.moveToFirst();
            txtEmployeeName.setText(curEmp.getString(3) + " " + curEmp.getString(4) + " [" + curEmp.getString(7) + "]");
            txtInOut.setText(obj.getAoInOut());
            if (obj.getAoInOut().equals(ClientAssetOwner.CheckInOut.CHECK_IN)) {
                txtInOut.setTextAppearance(mContext, R.style.styDescDarkGreyX);
                imgInOut.setImageResource(R.drawable.ic_checkin_small);
            } else if (obj.getAoInOut().equals(ClientAssetOwner.CheckInOut.CHECK_OUT)) {
                txtInOut.setTextAppearance(mContext, R.style.styDescWhite);
                imgInOut.setImageResource(R.drawable.ic_checkout_small);
            }
            String strDate = Helper.convertDateToString(Helper.convertStringToDate(obj.getAoDate(), ConstantVal.DATE_FORMAT), Helper.getStringPreference(mContext, ClientRegional.Fields.DATE_FORMAT, ConstantVal.DATE_FORMAT));
            txtDate.setText(": " + (strDate.equals("") ? getString(R.string.strNA) : strDate));
        }
        curEmp.close();
        db.close();
        return v;
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
