package com.lab360io.jobio.officeApp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.xwray.fontbinding.FontCache;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import entity.BusinessAccountdbDetail;
import entity.ClientAdminUserEmployee;
import entity.ClientAssetInspectServiceStatus;
import entity.ClientAssetService;
import io.fabric.sdk.android.services.concurrency.AsyncTask;
import me.zhanghai.android.materialedittext.MaterialEditText;
import utility.ConstantVal;
import utility.DataBase;
import utility.Helper;
import utility.HttpEngine;
import utility.ServerResponse;
import utility.URLMapping;

public class acServiceTransaction extends AppCompatActivity {
    ArrayList<ClientAdminUserEmployee> arrClientAdminUserEmployee = new ArrayList<>();
    ArrayList<ClientAssetInspectServiceStatus> arrClientAssetInspectServiceStatus = new ArrayList<>();
    ClientAssetService objClientAssetService = new ClientAssetService();
    boolean isDataEntedProperly = true;
    DateFormat dateFormat = new SimpleDateFormat("dd MMM yyy");
    DateFormat timeFormate = new SimpleDateFormat("hh:mm");
    Date dtCurrentDate = new Date();
    TextView txtAssetName, txtAssignedTo;
    MaterialEditText edServiceName, edServiceFirmName, edCost, edServiceDate, edServiceTime, edServiceNote;
    ImageButton btnUploadPic, btnUploadInvoice;
    ImageView imgPicture, imgInvoic;
    Spinner spnStatus, spnPerformBy;
    Button btnCancel, btnSave;
    AppCompatActivity ac;
    Context mContext;
    Helper objHelper = new Helper();
    Calendar calServiceDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.service_transaction);
        ac = this;
        mContext = this;
        objHelper.setActionBar(ac, mContext.getString(R.string.strService));
        setData();
    }


    private void setData() {
        new AsyncTask() {
            String astId = "", assignedT0EmpName = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                imgPicture = (ImageView) findViewById(R.id.imgPicture);
                imgInvoic = (ImageView) findViewById(R.id.imgInvoice);
                txtAssetName = (TextView) findViewById(R.id.txtAssetName);
                txtAssignedTo = (TextView) findViewById(R.id.txtAssignedTo);
                edServiceName = (MaterialEditText) findViewById(R.id.edServiceName);
                edServiceFirmName = (MaterialEditText) findViewById(R.id.edServiceFirmName);
                edCost = (MaterialEditText) findViewById(R.id.edCost);
                edServiceDate = (MaterialEditText) findViewById(R.id.edServiceDate);
                edServiceTime = (MaterialEditText) findViewById(R.id.edServiceTime);
                edServiceNote = (MaterialEditText) findViewById(R.id.edServiceNote);
                btnUploadPic = (ImageButton) findViewById(R.id.btnUploadPic);
                btnUploadInvoice = (ImageButton) findViewById(R.id.btnUploadInvoice);
                spnStatus = (Spinner) findViewById(R.id.spnStatus);
                spnPerformBy = (Spinner) findViewById(R.id.spnPerformBy);
                btnSave = (Button) findViewById(R.id.btnSave);
                btnCancel = (Button) findViewById(R.id.btnCancel);
                btnCancel.setOnClickListener(handleClick);
                btnSave.setOnClickListener(handleClick);
                edServiceDate.setOnClickListener(handleClick);
                edServiceTime.setOnClickListener(handleClick);
                btnUploadInvoice.setOnClickListener(handleClick);
                btnUploadPic.setOnClickListener(handleClick);
                if (ac.getIntent().getExtras() != null) {
                    astId = ac.getIntent().getStringExtra("astId");
                }
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                arrClientAssetInspectServiceStatus = ConstantVal.assetServiceInspectionStatus.getStatusArr(mContext);
                objClientAssetService = ClientAssetService.getDataFromDatabase(mContext, astId).get(0);
                DataBase db = new DataBase(mContext);
                db.open();
                Cursor curEmp = db.fetch(DataBase.adminuser_employee_table, null);
                if (curEmp != null && curEmp.getCount() > 0) {
                    curEmp.moveToFirst();
                    do {
                        ClientAdminUserEmployee objClientAdminUserEmployee = new ClientAdminUserEmployee(curEmp.getString(1), curEmp.getString(2),
                                curEmp.getString(3), curEmp.getString(4), curEmp.getString(5),
                                curEmp.getString(6), curEmp.getString(7), curEmp.getString(8), curEmp.getString(9));
                        arrClientAdminUserEmployee.add(objClientAdminUserEmployee);
                        if (objClientAdminUserEmployee.getEmpId().equals(objClientAssetService.getAmService_aasigned_employee())) {
                            assignedT0EmpName = objClientAdminUserEmployee.getFirst_name() + " " + objClientAdminUserEmployee.getLast_name();
                        }
                    } while (curEmp.moveToNext());
                }
                curEmp.close();
                db.close();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                ArrayAdapter<ClientAssetInspectServiceStatus> adpSpinner = new ArrayAdapter<ClientAssetInspectServiceStatus>(mContext, R.layout.spinner_item, arrClientAssetInspectServiceStatus);
                spnStatus.setAdapter(adpSpinner);

                ArrayAdapter<ClientAdminUserEmployee> adpPerformBy = new ArrayAdapter<ClientAdminUserEmployee>(mContext, R.layout.spinner_item, arrClientAdminUserEmployee);
                spnPerformBy.setAdapter(adpPerformBy);

                txtAssetName.setText(objClientAssetService.getAstAsset_name());
                txtAssignedTo.setText(assignedT0EmpName);
                edServiceDate.setText(dateFormat.format(calServiceDate.getTime()));
                edServiceTime.setText(timeFormate.format(calServiceDate.getTime()));
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    View.OnClickListener handleClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.edServiceDate: {
                    final Dialog dp = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            view.setMinDate(dtCurrentDate.getTime());
                            calServiceDate.set(Calendar.YEAR, year);
                            calServiceDate.set(Calendar.MONTH, monthOfYear);
                            calServiceDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            edServiceDate.setText(dateFormat.format(calServiceDate.getTime()));
                            objClientAssetService.setAstDateTime(calServiceDate.getTime());
                        }
                    }, calServiceDate.get(Calendar.YEAR), calServiceDate.get(Calendar.MONTH), calServiceDate.get(Calendar.DAY_OF_MONTH));
                    dp.show();
                    break;
                }

                case R.id.edServiceTime: {
                    final Dialog tp = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            calServiceDate.set(Calendar.HOUR, hourOfDay);
                            calServiceDate.set(Calendar.MINUTE, minute);
                            edServiceTime.setText(timeFormate.format(calServiceDate.getTime()));
                            objClientAssetService.setAstDateTime(calServiceDate.getTime());
                        }
                    }, calServiceDate.get(Calendar.HOUR), calServiceDate.get(Calendar.MINUTE), true);
                    tp.show();
                    break;
                }
                case R.id.btnUploadPic: {
                    Intent cameraIntent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, ConstantVal.REQUEST_TO_START_CAMERA_ACTIVITY);
                    break;
                }
                case R.id.btnUploadInvoice: {
                    Intent cameraIntent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, ConstantVal.REQUEST_TO_START_CAMERA_ACTIVITY1);
                    break;
                }
                case R.id.btnSave: {
                    new AsyncTask() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            isDataEntedProperly = true;
                            if (Helper.isFieldBlank(edServiceName.getText().toString())) {
                                edServiceName.setError(getString(R.string.msgEnterInspectionName));
                                requestFocus(edServiceName);
                                isDataEntedProperly = false;
                            } else if (Helper.isFieldBlank(edCost.getText().toString())) {
                                edCost.setError(getString(R.string.msgEnterCost));
                                requestFocus(edCost);
                                isDataEntedProperly = false;
                            }
                        }

                        @Override
                        protected Object doInBackground(Object[] params) {
                            if (isDataEntedProperly) {
                                saveUpdateData();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object o) {
                            super.onPostExecute(o);
                                /*Snackbar snackbar = Snackbar
                                        .make(ac.findViewById(android.R.id.content), mContext.getString(R.string.msgDataSaveSuccessfully), Snackbar.LENGTH_LONG);
                                snackbar.setCallback(new Snackbar.Callback() {
                                    @Override
                                    public void onDismissed(Snackbar snackbar, int event) {
                                        super.onDismissed(snackbar, event);
                                        ac.finish();
                                    }
                                });
                                snackbar.show();*/

                        }
                    }.execute();
                    break;
                }
                case R.id.btnCancel:
                    finish();
                    break;
            }
        }
    };

    private void saveUpdateData() {
        new AsyncTask() {
            ServerResponse sr;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                objClientAssetService.setAstName(edServiceName.getText().toString());
                objClientAssetService.setAstNote(edServiceNote.getText().toString());
                objClientAssetService.setAstStatusId(String.valueOf(arrClientAssetInspectServiceStatus.get(spnStatus.getSelectedItemPosition()).getId()));
                objClientAssetService.setAstPerformedBy(String.valueOf(arrClientAdminUserEmployee.get(spnPerformBy.getSelectedItemPosition()).getEmpId()));
                objClientAssetService.setAstDateTime(calServiceDate.getTime());
                objClientAssetService.setAstServiceFirmName(edServiceFirmName.getText().toString());
                objClientAssetService.setAstCost(edCost.getText().toString());
                DataBase db = new DataBase(mContext);
                db.open();
                ContentValues cv = new ContentValues();
                cv.put("localViewStatus", ConstantVal.InspectionServiceStatus.DONE);
                db.update(DataBase.service_view_table, DataBase.service_view_int, "astId=" + objClientAssetService.getAstId(), cv);
                ContentValues cvInspect = new ContentValues();
                cvInspect.put("astName", objClientAssetService.getAstName());
                cvInspect.put("astDateTime", objClientAssetService.getAstDateTime().getTime());
                cvInspect.put("astCost", objClientAssetService.getAstCost());
                cvInspect.put("astNote", objClientAssetService.getAstNote());
                cvInspect.put("astServiceFirmName", objClientAssetService.getAstServiceFirmName());
                cvInspect.put("astPhoto", objClientAssetService.getPhoto());
                cvInspect.put("astInvoicePicture", objClientAssetService.getAstInvoicePicture());
                cvInspect.put("astStatusId", objClientAssetService.getAstStatusId());
                cvInspect.put("astPerformedBy", objClientAssetService.getAstPerformedBy());
                db.update(DataBase.service_table, DataBase.service_int, "astId=" + objClientAssetService.getAstId(), cvInspect);
                db.close();

                String account_id = Helper.getStringPreference(mContext, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                final String tokenId = Helper.getStringPreference(mContext, ConstantVal.TOKEN, "");
                String[] data = {String.valueOf(tokenId), account_id, objClientAssetService.getAstId(), objClientAssetService.getAstName(),
                        objClientAssetService.getAstPerformedBy(), objClientAssetService.getAstServiceFirmName(), objClientAssetService.getAstCost(),
                        Helper.convertDateToString(calServiceDate.getTime(), ConstantVal.DATE_FORMAT + " " + ConstantVal.TIME_FORMAT),
                        objClientAssetService.getAstNote(), objClientAssetService.getPhoto() == null ? "" : objClientAssetService.getPhoto(),
                        objClientAssetService.getAstInvoicePicture() == null ? "" : objClientAssetService.getAstInvoicePicture(), objClientAssetService.getAstStatusId()};
                URLMapping um = ConstantVal.updateServiceTransaction(mContext);
                HttpEngine objHttpEngine = new HttpEngine();
                sr = objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(), data, um.getParamNames(), um.isNeedToSync());
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (sr.getResponseCode().equals(ConstantVal.ServerResponseCode.NO_INTERNET)) {
                    Helper.displaySnackbar((AppCompatActivity) mContext, mContext.getString(R.string.msgSyncNoInternet)).setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            super.onDismissed(snackbar, event);
                            setResult(ConstantVal.INSPECTION_TRANSACTION_RESPONSE_CODE);
                            finish();
                        }
                    });
                } else if (sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                    finish();
                } else if (!sr.getResponseCode().equals(ConstantVal.ServerResponseCode.SESSION_EXPIRED)) {
                    Helper.displaySnackbar(ac, sr.getResponseCode()).setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            super.onDismissed(snackbar, event);
                            finish();
                        }
                    });
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantVal.REQUEST_TO_START_CAMERA_ACTIVITY && resultCode == RESULT_OK) {
            new AsyncTask() {
                Bitmap bmp;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    bmp = (Bitmap) data.getExtras().get("data");
                    String strBAse64Image = Helper.getEncoded64ImageStringFromBitmap(bmp);
                    objClientAssetService.setPhoto(strBAse64Image);
                    imgPicture.setImageResource(0);
                    imgPicture.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), bmp));
                }

                @Override
                protected Object doInBackground(Object[] params) {
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else if (requestCode == ConstantVal.REQUEST_TO_START_CAMERA_ACTIVITY1 && resultCode == RESULT_OK) {
            new AsyncTask() {
                Bitmap bmp;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    bmp = (Bitmap) data.getExtras().get("data");
                    String strBAse64Image = Helper.getEncoded64ImageStringFromBitmap(bmp);
                    objClientAssetService.setAstInvoicePicture(strBAse64Image);
                    imgInvoic.setImageResource(0);
                    imgInvoic.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), bmp));
                }

                @Override
                protected Object doInBackground(Object[] params) {
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else if (resultCode == ConstantVal.EXIT_RESPONSE_CODE) {
            ac.setResult(ConstantVal.EXIT_RESPONSE_CODE);
            finish();
        }
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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

}
