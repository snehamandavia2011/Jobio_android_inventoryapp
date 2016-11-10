package fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.lab360io.jobio.officeApp.R;
import com.lab360io.jobio.officeApp.acJobPOInvoicReferenceList;
import com.thomsonreuters.rippledecoratorview.RippleDecoratorView;
import com.xwray.fontbinding.FontCache;

import java.util.ArrayList;

import entity.BusinessAccountdbDetail;
import entity.ClientCustEmpSupplier;
import entity.ClientJobPOInvoiceReference;
import entity.ClientStockTransactionReason;
import entity.ClientStockTransactionStatusMaster;
import utility.ConstantVal;
import utility.DotProgressBar;
import utility.Helper;
import utility.HttpEngine;
import utility.ServerResponse;
import utility.URLMapping;

/**
 * Created by SAI on 10/5/2016.
 */
public class frStockTransaction extends Fragment {
    Button btnCancel, btnNext;
    RelativeLayout lyNoNetwork, lyMainContent;
    LinearLayout lyFromTo, lyRefList;
    DotProgressBar dot_progress_bar;
    Spinner spnStockTransactionStatus, spnTransactionReason, spnRef, spnFrom, spnTo;
    TextView txtReference, txtNoReferenceFound;
    Context mContext;
    int selStockTransactionStatus, selStockTransactionReason;
    String referenceType, refId, fromId, toId, fromType, toType;
    ArrayAdapter<ClientStockTransactionStatusMaster> adpStockTransactionStatus;
    ArrayAdapter<ClientStockTransactionReason> adpStockTransactionReason;
    ArrayAdapter<ClientCustEmpSupplier> adpClientCustEmpSupplier;
    ArrayAdapter<ClientJobPOInvoiceReference> adpClientJobPOInvoiceReference;
    ArrayList<ClientStockTransactionReason> arrClientStockTransactionReason = null;
    ArrayList<ClientStockTransactionStatusMaster> arrClientStockTransactionStatusMaster = null;
    ArrayList<ClientCustEmpSupplier> arrClientCustEmpSupplier = null;
    ArrayList<ClientJobPOInvoiceReference> arrClientJobPOInvoiceReference = null;
    RippleDecoratorView refView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        FontCache.getInstance(mContext).addFont("Ubuntu", "Ubuntu-C.ttf");
        View view = DataBindingUtil.inflate(inflater, R.layout.frstock_transaction, null, true).getRoot();
        lyMainContent = (RelativeLayout) view.findViewById(R.id.lyMainContent);
        lyNoNetwork = (RelativeLayout) view.findViewById(R.id.lyNoNetwork);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnNext = (Button) view.findViewById(R.id.btnNext);
        dot_progress_bar = (DotProgressBar) view.findViewById(R.id.dot_progress_bar);
        txtReference = (TextView) view.findViewById(R.id.txtReference);
        txtNoReferenceFound = (TextView) view.findViewById(R.id.txtNoReferenceFound);
        spnStockTransactionStatus = (Spinner) view.findViewById(R.id.spnStockType);
        spnTransactionReason = (Spinner) view.findViewById(R.id.spnTransactionReason);
        spnRef = (Spinner) view.findViewById(R.id.spnRef);
        spnFrom = (Spinner) view.findViewById(R.id.spnFrom);
        spnTo = (Spinner) view.findViewById(R.id.spnTo);
        lyFromTo = (LinearLayout) view.findViewById(R.id.lyFromTo);
        lyRefList = (LinearLayout) view.findViewById(R.id.lyRefList);
        refView = (RippleDecoratorView) view.findViewById(R.id.refView);
        btnNext.setEnabled(false);
        setData();
        return view;
    }


    private void setData() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ((ViewGroup) dot_progress_bar.getParent()).removeView(dot_progress_bar);
                } catch (Exception e) {
                }
                Intent i = new Intent(mContext, acJobPOInvoicReferenceList.class);
                i.putExtra("selStockTransactionStatus", selStockTransactionStatus);
                i.putExtra("selStockTransactionReason", selStockTransactionReason);
                i.putExtra("referenceType", referenceType);
                i.putExtra("refId", refId);
                i.putExtra("fromId", fromId);
                i.putExtra("toId", toId);
                i.putExtra("fromType", fromType);
                i.putExtra("toType", toType);
                startActivityForResult(i, ConstantVal.JOB_PO_INVOICE_REF_REQUEST);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        boolean isNetworkAvail = new HttpEngine().isNetworkAvailable(mContext);
        if (isNetworkAvail) {
            lyMainContent.setVisibility(View.VISIBLE);
            setSpnStockType();
            setSpnFromTo();
        } else {
            lyNoNetwork.setVisibility(View.VISIBLE);
        }
    }

    private void setSpnStockType() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                arrClientStockTransactionStatusMaster = ClientStockTransactionStatusMaster.getDataFromDatabaseIn(mContext);
                //arr.add(0, new ClientStockTransactionStatusMaster(0, getString(R.string.strSelectStockType)));
                adpStockTransactionStatus = new ArrayAdapter<ClientStockTransactionStatusMaster>(mContext, R.layout.spinner_item, arrClientStockTransactionStatusMaster); /*{
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0)
                            return false;
                        else
                            return true;
                    }
                }*/
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                spnStockTransactionStatus.setAdapter(adpStockTransactionStatus);
                spnStockTransactionStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selStockTransactionStatus = arrClientStockTransactionStatusMaster.get(position).getId();
                        referenceType = StockTransactionReferenceType.getReferenceID(selStockTransactionStatus);
                        if (selStockTransactionStatus == 2) {//STOCK RETURN
                            txtReference.setText(StockTransactionReferenceType.getReferenceType(mContext, referenceType));
                        } else if (selStockTransactionStatus == 3) {//STOCK RECEIVED
                            txtReference.setText(StockTransactionReferenceType.getReferenceType(mContext, referenceType));
                        } else if (selStockTransactionStatus == 4) {//STOCK DAMAGE
                            txtReference.setText(StockTransactionReferenceType.getReferenceType(mContext, referenceType));
                        } else if (selStockTransactionStatus == 7) {//STOCK RELEASE
                            txtReference.setText(StockTransactionReferenceType.getReferenceType(mContext, referenceType));
                        }
                        setSpnTrasactionReason();
                        setSpnRefList();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setSpnTrasactionReason() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                arrClientStockTransactionReason = ClientStockTransactionReason.getDataFromDatabase(mContext, selStockTransactionStatus);
                adpStockTransactionReason = new ArrayAdapter<ClientStockTransactionReason>(mContext, R.layout.spinner_item, arrClientStockTransactionReason);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                spnTransactionReason.setAdapter(adpStockTransactionReason);
                spnTransactionReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selStockTransactionReason = arrClientStockTransactionReason.get(position).getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setSpnRefList() {
        new AsyncTask() {
            ServerResponse sr;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (btnNext.isEnabled())
                    btnNext.setEnabled(false);
                dot_progress_bar.setVisibility(View.VISIBLE);
                lyRefList.setVisibility(View.GONE);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                final HttpEngine objHttpEngine = new HttpEngine();
                final String tokenId = Helper.getStringPreference(mContext, ConstantVal.TOKEN, "");
                String account_id = Helper.getStringPreference(mContext, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                final URLMapping um = ConstantVal.getTransactionalReferenceList(mContext);
                sr = objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(), new String[]{String.valueOf(selStockTransactionStatus), referenceType, tokenId, account_id}, um.getParamNames(), um.isNeedToSync());
                String result = sr.getResponseString();
                if (result != null && result.length() > 0) {
                    arrClientJobPOInvoiceReference = ClientJobPOInvoiceReference.parseList(result);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                lyRefList.setVisibility(View.VISIBLE);
                if (arrClientJobPOInvoiceReference != null && arrClientJobPOInvoiceReference.size() > 0) {
                    adpClientJobPOInvoiceReference = new ArrayAdapter<ClientJobPOInvoiceReference>(mContext, R.layout.spinner_item, arrClientJobPOInvoiceReference);
                    spnRef.setAdapter(adpClientJobPOInvoiceReference);
                    spnRef.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            refId = arrClientJobPOInvoiceReference.get(position).getUuid();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    if (!btnNext.isEnabled())
                        btnNext.setEnabled(true);
                    txtNoReferenceFound.setVisibility(View.GONE);
                    refView.setVisibility(View.VISIBLE);
                } else {
                    if (btnNext.isEnabled())
                        btnNext.setEnabled(false);
                    txtNoReferenceFound.setVisibility(View.VISIBLE);
                    refView.setVisibility(View.GONE);
                    if (!sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS))
                        Helper.displaySnackbar((AppCompatActivity) getActivity(), sr.getResponseCode());
                }
                dot_progress_bar.setVisibility(View.GONE);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private void setSpnFromTo() {
        new AsyncTask() {
            ServerResponse sr;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dot_progress_bar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                final HttpEngine objHttpEngine = new HttpEngine();
                final String tokenId = Helper.getStringPreference(mContext, ConstantVal.TOKEN, "");
                String account_id = Helper.getStringPreference(mContext, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                final URLMapping um = ConstantVal.getCustomerEmployeeSupplierList(mContext);
                sr = objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(), new String[]{tokenId, account_id}, um.getParamNames(), um.isNeedToSync());
                String result = sr.getResponseString();
                if (result != null && result.length() > 0) {
                    arrClientCustEmpSupplier = ClientCustEmpSupplier.parseData(result);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (arrClientCustEmpSupplier != null) {
                    adpClientCustEmpSupplier = new ArrayAdapter<ClientCustEmpSupplier>(mContext, R.layout.spinner_item, arrClientCustEmpSupplier);
                    spnFrom.setAdapter(adpClientCustEmpSupplier);
                    spnTo.setAdapter(adpClientCustEmpSupplier);
                    spnFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            fromId = arrClientCustEmpSupplier.get(position).getUuid();
                            fromType = arrClientCustEmpSupplier.get(position).getType();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    spnTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            toId = arrClientCustEmpSupplier.get(position).getUuid();
                            toType = arrClientCustEmpSupplier.get(position).getType();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    lyFromTo.setVisibility(View.VISIBLE);
                    btnNext.setEnabled(true);
                } else {
                    Helper.displaySnackbar((AppCompatActivity) getActivity(), ConstantVal.ServerResponseCode.getMessage(mContext, sr.getResponseString()));
                }
                dot_progress_bar.setVisibility(View.GONE);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ConstantVal.EXIT_RESPONSE_CODE) {
            getActivity().setResult(ConstantVal.EXIT_RESPONSE_CODE);
            getActivity().finish();
        }
    }
}
