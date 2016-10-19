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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.lab360io.jobio.inventoryapp.R;
import com.lab360io.jobio.inventoryapp.acJobPOInvoicReferenceList;
import com.xwray.fontbinding.FontCache;

import java.util.ArrayList;

import entity.BusinessAccountdbDetail;
import entity.ClientCustEmpSupplier;
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
    LinearLayout lyFromTo;
    DotProgressBar dot_progress_bar;
    Spinner spnStockTransactionStatus, spnTransactionReason, spnFrom, spnTo;
    TextView txtReference;
    Context mContext;
    int selStockTransactionStatus, selStockTransactionReason, referenceType;
    ArrayAdapter<ClientStockTransactionStatusMaster> adpStockTransactionStatus;
    ArrayAdapter<ClientStockTransactionReason> adpStockTransactionReason;
    ArrayAdapter<ClientCustEmpSupplier> adpClientCustEmpSupplier;
    ArrayList<ClientStockTransactionReason> arrClientStockTransactionReason = null;
    ArrayList<ClientStockTransactionStatusMaster> arrClientStockTransactionStatusMaster = null;
    ArrayList<ClientCustEmpSupplier> arrClientCustEmpSupplier = null;

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
        spnStockTransactionStatus = (Spinner) view.findViewById(R.id.spnStockType);
        spnTransactionReason = (Spinner) view.findViewById(R.id.spnTransactionReason);
        spnFrom = (Spinner) view.findViewById(R.id.spnFrom);
        spnTo = (Spinner) view.findViewById(R.id.spnTo);
        lyFromTo = (LinearLayout) view.findViewById(R.id.lyFromTo);
        btnNext.setEnabled(false);
        setData();
        return view;
    }

    private void setData() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, acJobPOInvoicReferenceList.class);
                i.putExtra("ref_type", referenceType);
                startActivityForResult(i, ConstantVal.EXIT_REQUEST_CODE);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        setSpnStockType();
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
                        referenceType = ReferenceType.getReferenceID(selStockTransactionStatus);
                        if (selStockTransactionStatus == 2) {//STOCK RETURN
                            txtReference.setText(ReferenceType.getReferenceType(mContext, referenceType));
                        } else if (selStockTransactionStatus == 3) {//STOCK RECEIVED
                            txtReference.setText(ReferenceType.getReferenceType(mContext, referenceType));
                        } else if (selStockTransactionStatus == 4) {//STOCK DAMAGE
                            txtReference.setText(ReferenceType.getReferenceType(mContext, referenceType));
                        } else if (selStockTransactionStatus == 7) {//STOCK RELEASE
                            txtReference.setText(ReferenceType.getReferenceType(mContext, referenceType));
                        }
                        setSpnTrasactionReason();
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
                setSpnFromTo();
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
                    lyFromTo.setVisibility(View.VISIBLE);
                    btnNext.setEnabled(true);
                } else {
                    Helper.displaySnackbar((AppCompatActivity) getActivity(), ConstantVal.ServerResponseCode.getMessage(mContext, sr.getResponseString()));
                }
                dot_progress_bar.setVisibility(View.GONE);
                if (dot_progress_bar != null)
                    ((ViewGroup) dot_progress_bar.getParent()).removeView(dot_progress_bar);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static class ReferenceType {
        static int JOB = 1, INVOICE = 2, PURCHASE_ORDER = 3;

        public static String getReferenceType(Context c, int type) {
            if (type == JOB) {
                return c.getString(R.string.strJob);
            } else if (type == INVOICE) {
                return c.getString(R.string.strInvoice);
            } else if (type == PURCHASE_ORDER) {
                return c.getString(R.string.strPurchaseOrder);
            }
            return "";
        }

        public static int getReferenceID(int selStockTransactionStatus) {
            if (selStockTransactionStatus == 2) {//STOCK RETURN
                return JOB;
            } else if (selStockTransactionStatus == 3) {//STOCK RECEIVED
                return PURCHASE_ORDER;
            } else if (selStockTransactionStatus == 4) {//STOCK DAMAGE
                return INVOICE;
            } else if (selStockTransactionStatus == 7) {//STOCK RELEASE
                return JOB;
            }
            return 0;
        }
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
