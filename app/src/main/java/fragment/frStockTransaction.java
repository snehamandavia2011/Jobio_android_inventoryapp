package fragment;

import android.app.Activity;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.lab360io.jobio.officeApp.R;
import com.lab360io.jobio.officeApp.acEmpCustomerSupplierSelection;
import com.lab360io.jobio.officeApp.acJobPOInvoicReferenceList;
import com.thomsonreuters.rippledecoratorview.RippleDecoratorView;
import com.xwray.fontbinding.FontCache;

import java.util.ArrayList;

import adapter.ClientCustomerEmployeeSupplierAdapter;
import entity.BusinessAccountdbDetail;
import entity.ClientCustEmpSupplier;
import entity.ClientJobPOInvoiceReference;
import entity.ClientStockTransactionReason;
import entity.ClientStockTransactionStatusMaster;
import utility.ConstantVal;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;
import utility.ServerResponse;
import utility.URLMapping;

/**
 * Created by SAI on 10/5/2016.
 */
public class frStockTransaction extends Fragment {
    public static final String PARTY_TYPE = "party_type";
    public static final String FROM = "from";
    public static final String TO = "to";
    Button btnCancel, btnNext;
    RelativeLayout lyNoNetwork, lyMainContent;
    LinearLayout lyFromTo, lyRefList, lyTransactionReason, lyReferenceType;
    Spinner spnTransactionReason, spnRef;
    RadioGroup rgStockType;
    RadioButton rd1, rd2, rd3, rd4;
    Button btnFrom, btnTo;
    TextView txtReference, txtNoReferenceFound;
    Context mContext;
    int selStockTransactionStatus, selStockTransactionReason;
    String referenceType, refId, fromId, toId, fromType, toType;
    ArrayAdapter<ClientStockTransactionStatusMaster> adpStockTransactionStatus;
    ArrayAdapter<ClientStockTransactionReason> adpStockTransactionReason;
    ArrayAdapter<ClientJobPOInvoiceReference> adpClientJobPOInvoiceReference;
    ArrayList<ClientStockTransactionReason> arrClientStockTransactionReason = null;
    ArrayList<ClientStockTransactionStatusMaster> arrClientStockTransactionStatusMaster = null;
    ArrayList<ClientJobPOInvoiceReference> arrClientJobPOInvoiceReference = null;
    RippleDecoratorView refView;
    ClientCustEmpSupplier objClientCustEmpSupplierFrom, objClientCustEmpSupplierTo;
    LinearLayout dot_progress_bar_container;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        FontCache.getInstance(mContext).addFont("Ubuntu", "Ubuntu-C.ttf");
        View view = DataBindingUtil.inflate(inflater, R.layout.frstock_transaction, null, true).getRoot();
        rgStockType = (RadioGroup) view.findViewById(R.id.rgStockType);
        rd1 = (RadioButton) view.findViewById(R.id.rd1);
        rd2 = (RadioButton) view.findViewById(R.id.rd2);
        rd3 = (RadioButton) view.findViewById(R.id.rd3);
        rd4 = (RadioButton) view.findViewById(R.id.rd4);
        rd1.setSelected(true);
        rd2.setSelected(true);
        rd3.setSelected(true);
        rd4.setSelected(true);
        lyMainContent = (RelativeLayout) view.findViewById(R.id.lyMainContent);
        lyNoNetwork = (RelativeLayout) view.findViewById(R.id.lyNoNetwork);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnNext = (Button) view.findViewById(R.id.btnNext);
        dot_progress_bar_container = (LinearLayout) view.findViewById(R.id.dot_progress_bar_container);
        txtReference = (TextView) view.findViewById(R.id.txtReference);
        txtNoReferenceFound = (TextView) view.findViewById(R.id.txtNoReferenceFound);
        //spnStockTransactionStatus = (Spinner) view.findViewById(R.id.spnStockType);
        spnTransactionReason = (Spinner) view.findViewById(R.id.spnTransactionReason);
        spnRef = (Spinner) view.findViewById(R.id.spnRef);
        btnFrom = (Button) view.findViewById(R.id.btnFrom);
        btnTo = (Button) view.findViewById(R.id.btnTo);
        lyFromTo = (LinearLayout) view.findViewById(R.id.lyFromTo);
        lyRefList = (LinearLayout) view.findViewById(R.id.lyRefList);
        lyTransactionReason = (LinearLayout) view.findViewById(R.id.lyTransactionReason);
        lyReferenceType = (LinearLayout) view.findViewById(R.id.lyReferenceType);
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
                    hideDotProgressBar();
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
            lyNoNetwork.setVisibility(View.GONE);
            setSpnStockType();
        } else {
            lyMainContent.setVisibility(View.GONE);
            lyNoNetwork.setVisibility(View.VISIBLE);
        }
    }

    private void setSpnStockType() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                arrClientStockTransactionStatusMaster = ClientStockTransactionStatusMaster.getDataFromDatabaseIn(mContext);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (arrClientStockTransactionStatusMaster != null && arrClientStockTransactionStatusMaster.size() > 0) {
                    try {
                        for (int i = 0; i < arrClientStockTransactionStatusMaster.size(); i++) {
                            if (i == 0) {
                                rd1.setText(arrClientStockTransactionStatusMaster.get(i).getAction_name().replace("STOCK", ""));
                                rd1.setTag(arrClientStockTransactionStatusMaster.get(i).getId());
                            } else if (i == 1) {
                                rd2.setText(arrClientStockTransactionStatusMaster.get(i).getAction_name().replace("STOCK", ""));
                                rd2.setTag(arrClientStockTransactionStatusMaster.get(i).getId());
                            } else if (i == 2) {
                                rd3.setText(arrClientStockTransactionStatusMaster.get(i).getAction_name().replace("STOCK", ""));
                                rd3.setTag(arrClientStockTransactionStatusMaster.get(i).getId());
                            } else if (i == 3) {
                                rd4.setText(arrClientStockTransactionStatusMaster.get(i).getAction_name().replace("STOCK", ""));
                                rd4.setTag(arrClientStockTransactionStatusMaster.get(i).getId());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    rgStockType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            invisibleAllView();
                            selStockTransactionStatus = Integer.parseInt(group.findViewById(checkedId).getTag().toString());
                            Logger.debug("selStockTransactionStatus:" + selStockTransactionStatus);
                            setSpnTrasactionReason();
                        }
                    });
                    rgStockType.check(R.id.rd1);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setSpnTrasactionReason() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                arrClientStockTransactionReason = ClientStockTransactionReason.getDataFromDatabase(mContext, selStockTransactionStatus);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (arrClientStockTransactionReason != null && arrClientStockTransactionReason.size() > 0) {
                    lyTransactionReason.setVisibility(View.VISIBLE);
                    adpStockTransactionReason = new ArrayAdapter<ClientStockTransactionReason>(mContext, R.layout.spinner_item_no_padding, arrClientStockTransactionReason);
                    adpStockTransactionReason.setDropDownViewResource(R.layout.spinner_item);
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
                    lyReferenceType.setVisibility(View.VISIBLE);
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
                    setSpnRefList();
                }
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
                showDotProgressBar();
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
                    adpClientJobPOInvoiceReference = new ArrayAdapter<ClientJobPOInvoiceReference>(mContext, R.layout.spinner_item_no_padding, arrClientJobPOInvoiceReference);
                    adpClientJobPOInvoiceReference.setDropDownViewResource(R.layout.spinner_item);
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
                    getFromTo();
                } else {
                    if (btnNext.isEnabled())
                        btnNext.setEnabled(false);
                    txtNoReferenceFound.setVisibility(View.VISIBLE);
                    refView.setVisibility(View.GONE);
                    if (!sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS) && !sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SESSION_EXPIRED))
                        Helper.displaySnackbar((AppCompatActivity) getActivity(), sr.getResponseCode(), ConstantVal.ToastBGColor.DANGER);
                }
                hideDotProgressBar();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private void getFromTo() {
        btnFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientCustEmpSupplier objClientCustEmpSupplier = (ClientCustEmpSupplier) btnFrom.getTag();
                Intent i = new Intent(mContext, acEmpCustomerSupplierSelection.class);
                if (objClientCustEmpSupplier != null) {
                    if (objClientCustEmpSupplier.getType().equals(getString(R.string.strSupplier)))
                        i.putExtra(acEmpCustomerSupplierSelection.CURRENT_TAB, acEmpCustomerSupplierSelection.SUPPLIER);
                    else if (objClientCustEmpSupplier.getType().equals(getString(R.string.strEmployee)))
                        i.putExtra(acEmpCustomerSupplierSelection.CURRENT_TAB, acEmpCustomerSupplierSelection.EMPLOYEE);
                    else if (objClientCustEmpSupplier.getType().equals(getString(R.string.strCustomer)))
                        i.putExtra(acEmpCustomerSupplierSelection.CURRENT_TAB, acEmpCustomerSupplierSelection.CUSTOMER);
                    else
                        i.putExtra(acEmpCustomerSupplierSelection.CURRENT_TAB, acEmpCustomerSupplierSelection.ALL);
                } else {
                    i.putExtra(acEmpCustomerSupplierSelection.CURRENT_TAB, acEmpCustomerSupplierSelection.ALL);
                }
                i.putExtra(PARTY_TYPE, FROM);
                startActivityForResult(i, ConstantVal.FROM_USER_SELECTION_REQUEST);
            }
        });

        btnTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientCustEmpSupplier objClientCustEmpSupplier = (ClientCustEmpSupplier) btnTo.getTag();
                Intent i = new Intent(mContext, acEmpCustomerSupplierSelection.class);
                i.putExtra(PARTY_TYPE, TO);
                if (objClientCustEmpSupplier != null) {
                    if (objClientCustEmpSupplier.getType().equals(getString(R.string.strSupplier)))
                        i.putExtra(acEmpCustomerSupplierSelection.CURRENT_TAB, acEmpCustomerSupplierSelection.SUPPLIER);
                    else if (objClientCustEmpSupplier.getType().equals(getString(R.string.strEmployee)))
                        i.putExtra(acEmpCustomerSupplierSelection.CURRENT_TAB, acEmpCustomerSupplierSelection.EMPLOYEE);
                    else if (objClientCustEmpSupplier.getType().equals(getString(R.string.strCustomer)))
                        i.putExtra(acEmpCustomerSupplierSelection.CURRENT_TAB, acEmpCustomerSupplierSelection.CUSTOMER);
                    else
                        i.putExtra(acEmpCustomerSupplierSelection.CURRENT_TAB, acEmpCustomerSupplierSelection.ALL);
                } else {
                    i.putExtra(acEmpCustomerSupplierSelection.CURRENT_TAB, acEmpCustomerSupplierSelection.ALL);
                }
                startActivityForResult(i, ConstantVal.TO_USER_SELECTION_REQUEST);
            }
        });
        lyFromTo.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.debug("in frtockTransaction on activity result:" + requestCode + " " + resultCode);
        if (resultCode == ConstantVal.EXIT_RESPONSE_CODE) {
            getActivity().setResult(ConstantVal.EXIT_RESPONSE_CODE);
            getActivity().finish();
        } else if (requestCode == ConstantVal.FROM_USER_SELECTION_REQUEST && resultCode == ConstantVal.FROM_TO_USER_SELECTION_RESPONSE) {
            if (data.getExtras() != null) {
                objClientCustEmpSupplierFrom = (ClientCustEmpSupplier) data.getSerializableExtra("selectedUser");
                fromId = objClientCustEmpSupplierFrom.getUuid();
                fromType=objClientCustEmpSupplierFrom.getType();
                btnFrom.setText(objClientCustEmpSupplierFrom.toString());
                btnFrom.setTag(objClientCustEmpSupplierFrom);
            }
        } else if (requestCode == ConstantVal.TO_USER_SELECTION_REQUEST && resultCode == ConstantVal.FROM_TO_USER_SELECTION_RESPONSE) {
            if (data.getExtras() != null) {
                objClientCustEmpSupplierTo = (ClientCustEmpSupplier) data.getSerializableExtra("selectedUser");
                toId = objClientCustEmpSupplierTo.getUuid();
                toType=objClientCustEmpSupplierTo.getType();
                btnTo.setText(objClientCustEmpSupplierTo.toString());
                btnTo.setTag(objClientCustEmpSupplierTo);
            }
        }
    }

    private void invisibleAllView() {
        lyTransactionReason.setVisibility(View.GONE);
        lyReferenceType.setVisibility(View.GONE);
        lyRefList.setVisibility(View.GONE);
        lyFromTo.setVisibility(View.GONE);
    }

    private void showDotProgressBar() {
        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final View view = mInflater.inflate(R.layout.ly_dot_progress_bar, null);
        view.setVisibility(View.VISIBLE);
        dot_progress_bar_container.addView(view);
        dot_progress_bar_container.setVisibility(View.VISIBLE);
    }

    private void hideDotProgressBar() {
        dot_progress_bar_container.removeViewAt(0);
        dot_progress_bar_container.setVisibility(View.GONE);
    }
}
