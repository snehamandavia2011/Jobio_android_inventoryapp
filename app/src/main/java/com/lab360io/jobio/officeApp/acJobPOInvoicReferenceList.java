package com.lab360io.jobio.officeApp;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xwray.fontbinding.FontCache;

import java.util.ArrayList;

import adapter.JobInvoiceRefDetailAdapter;
import adapter.PORefDetailAdapter;
import entity.BusinessAccountdbDetail;
import entity.ClientJobInvoiceRefDetail;
import entity.ClientPORefDetail;
import fragment.StockTransactionReferenceType;
import utility.ConstantVal;
import utility.DotProgressBar;
import utility.Helper;
import utility.HttpEngine;
import utility.ServerResponse;
import utility.URLMapping;

public class acJobPOInvoicReferenceList extends AppCompatActivity {
    ListView lvlJobPOInvoice;
    DotProgressBar dot_progress_bar;
    RelativeLayout lyNoContent, lyMainContent;
    Helper objHelper = new Helper();
    AppCompatActivity ac;
    Context mContext;
    ImageView imgNoData;
    TextView txtNoData;
    int selStockTransactionStatus, selStockTransactionReason;
    String referenceType, refId, fromId, toId, fromType, toType;
    ArrayList<ClientPORefDetail> arrClientPORefDetail;
    ArrayList<ClientJobInvoiceRefDetail> arrClientJobInvoiceRefDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.po_invoic_reference_list);
        ac = this;
        mContext = this;
        setData();
    }

    private void setData() {
        new AsyncTask() {
            ServerResponse sr;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (getIntent().getExtras() != null) {
                    selStockTransactionStatus = getIntent().getIntExtra("selStockTransactionStatus", 0);
                    selStockTransactionReason = getIntent().getIntExtra("selStockTransactionReason", 0);
                    referenceType = getIntent().getStringExtra("referenceType");
                    refId = getIntent().getStringExtra("refId");
                    fromId = getIntent().getStringExtra("fromId");
                    toId = getIntent().getStringExtra("toId");
                    fromType = getIntent().getStringExtra("fromType");
                    toType = getIntent().getStringExtra("toType");
                }
                if (referenceType.equals(StockTransactionReferenceType.JOB)) {
                    objHelper.setActionBar(ac, mContext.getString(R.string.strJob));
                } else if (referenceType.equals(StockTransactionReferenceType.INVOICE)) {
                    objHelper.setActionBar(ac, mContext.getString(R.string.strInvoice));
                } else if (referenceType.equals(StockTransactionReferenceType.PURCHASE_ORDER)) {
                    objHelper.setActionBar(ac, mContext.getString(R.string.strPurchaseOrder));
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
                final HttpEngine objHttpEngine = new HttpEngine();
                final String tokenId = Helper.getStringPreference(mContext, ConstantVal.TOKEN, "");
                String account_id = Helper.getStringPreference(mContext, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                final URLMapping um = ConstantVal.getReferenceDetail(mContext);
                sr = objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(), new String[]{refId, referenceType, String.valueOf(selStockTransactionStatus), account_id, tokenId}, um.getParamNames(), um.isNeedToSync());
                String result = sr.getResponseString();
                if (result != null && result.length() > 0) {
                    if (referenceType.equals(StockTransactionReferenceType.JOB) || referenceType.equals(StockTransactionReferenceType.INVOICE)) {
                        arrClientJobInvoiceRefDetail = ClientJobInvoiceRefDetail.parseJSON(result);
                    } else if (referenceType.equals(StockTransactionReferenceType.PURCHASE_ORDER)) {
                        arrClientPORefDetail = ClientPORefDetail.parseJSON(result);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (!sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS))
                    Helper.displaySnackbar(ac, ConstantVal.ServerResponseCode.getMessage(mContext, sr.getResponseCode()));
                else {
                    if (referenceType.equals(StockTransactionReferenceType.JOB) || referenceType.equals(StockTransactionReferenceType.INVOICE)) {
                        if (arrClientJobInvoiceRefDetail != null && arrClientJobInvoiceRefDetail.size() > 0) {
                            lyMainContent.setVisibility(View.VISIBLE);
                            lyNoContent.setVisibility(View.GONE);
                            lvlJobPOInvoice.setAdapter(new JobInvoiceRefDetailAdapter(mContext, arrClientJobInvoiceRefDetail, selStockTransactionStatus,
                                    selStockTransactionReason, referenceType, refId, fromId, toId, fromType, toType));
                        } else {
                            lyMainContent.setVisibility(View.GONE);
                            lyNoContent.setVisibility(View.VISIBLE);
                            if (referenceType.equals(StockTransactionReferenceType.JOB)) {
                                txtNoData.setText(getString(R.string.strNoJob));
                            } else if (referenceType.equals(StockTransactionReferenceType.INVOICE)) {
                                txtNoData.setText(getString(R.string.strNoInvoice));
                            }
                        }
                    } else if (referenceType.equals(StockTransactionReferenceType.PURCHASE_ORDER)) {
                        if (arrClientPORefDetail != null && arrClientPORefDetail.size() > 0) {
                            lyMainContent.setVisibility(View.VISIBLE);
                            lyNoContent.setVisibility(View.GONE);
                            lvlJobPOInvoice.setAdapter(new PORefDetailAdapter(mContext, arrClientPORefDetail, selStockTransactionStatus,
                                    selStockTransactionReason, referenceType, refId, fromId, toId, fromType, toType));
                        } else {
                            lyMainContent.setVisibility(View.GONE);
                            lyNoContent.setVisibility(View.VISIBLE);
                            if (referenceType.equals(StockTransactionReferenceType.PURCHASE_ORDER)) {
                                txtNoData.setText(getString(R.string.strNoPO));
                            }
                        }
                    }
                }
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
        } else if (requestCode == ConstantVal.EDIT_JOB_INVOICE_REFERENCE_REQUEST && resultCode == ConstantVal.EDIT_JOB_INVOICE_REFERENCE_RESPONSE) {
            ac.setResult(ConstantVal.JOB_PO_INVOICE_REF_RESPONSE);
            finish();
        } else if (requestCode == ConstantVal.EDIT_PO_REFERENCE_REQUEST && resultCode == ConstantVal.EDIT_PO_REFERENCE_RESPONSE) {
            ac.setResult(ConstantVal.JOB_PO_INVOICE_REF_RESPONSE);
            finish();
        }
    }

}
