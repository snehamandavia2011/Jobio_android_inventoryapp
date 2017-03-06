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
import entity.ClientAssetInspect;
import entity.ClientAssetInspectServiceStatus;
import entity.ClientRegional;
import io.fabric.sdk.android.services.concurrency.AsyncTask;
import me.zhanghai.android.materialedittext.MaterialEditText;
import utility.ConstantVal;
import utility.DataBase;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;
import utility.ServerResponse;
import utility.URLMapping;

public class acInspectTransaction extends AppCompatActivity {
    ArrayList<ClientAssetInspectServiceStatus> arrClientAssetInspectServiceStatus = new ArrayList<>();
    ClientAssetInspect objClientAssetInspect = new ClientAssetInspect();
    boolean isDataEntedProperly = true;
    DateFormat dateFormat;// = new SimpleDateFormat("dd MMM yyy");
    DateFormat timeFormate;// = new SimpleDateFormat("hh:mm");
    Date dtCurrentDate = new Date();
    TextView txtAssetName, txtAssignedTo;
    MaterialEditText edInspectionName, edInspectionDate, edInspectionNote, edInspectionTime;
    ImageButton btnUploadPic;
    ImageView imgPicture;
    Spinner spnStatus;
    Button btnCancel, btnSave;
    AppCompatActivity ac;
    Context mContext;
    Helper objHelper = new Helper();
    Calendar calInspectionDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.inspect_transaction);
        ac = this;
        mContext = this;
        objHelper.setActionBar(ac, mContext.getString(R.string.strInspect));
        dateFormat = new SimpleDateFormat(Helper.getStringPreference(mContext, ClientRegional.Fields.DATE_FORMAT, ConstantVal.DEVICE_DEFAULT_DATE_FORMAT));
        timeFormate = new SimpleDateFormat(Helper.getStringPreference(mContext, ClientRegional.Fields.TIME_FORMAT, ConstantVal.DEVICE_DEFAULT_TIME_FORMAT));
        Logger.debug(Helper.getStringPreference(mContext, ClientRegional.Fields.TIME_FORMAT, ConstantVal.DEVICE_DEFAULT_TIME_FORMAT));
        setData();
    }


    private void setData() {
        new AsyncTask() {
            String aitId = "", assignedT0EmpName = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                imgPicture = (ImageView) findViewById(R.id.imgPicture);
                txtAssetName = (TextView) findViewById(R.id.txtAssetName);
                txtAssignedTo = (TextView) findViewById(R.id.txtAssignedTo);
                edInspectionName = (MaterialEditText) findViewById(R.id.edInspectionName);
                edInspectionDate = (MaterialEditText) findViewById(R.id.edInspectionDate);
                edInspectionTime = (MaterialEditText) findViewById(R.id.edInspectionTime);
                edInspectionNote = (MaterialEditText) findViewById(R.id.edInspectionNote);
                btnUploadPic = (ImageButton) findViewById(R.id.btnUploadPic);
                spnStatus = (Spinner) findViewById(R.id.spnStatus);
                btnSave = (Button) findViewById(R.id.btnSave);
                btnCancel = (Button) findViewById(R.id.btnCancel);
                btnCancel.setOnClickListener(handleClick);
                btnSave.setOnClickListener(handleClick);
                edInspectionDate.setOnClickListener(handleClick);
                edInspectionTime.setOnClickListener(handleClick);
                btnUploadPic.setOnClickListener(handleClick);
                if (ac.getIntent().getExtras() != null) {
                    aitId = ac.getIntent().getStringExtra("aitId");
                }
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                arrClientAssetInspectServiceStatus = ConstantVal.assetServiceInspectionStatus.getStatusArr(mContext);
                objClientAssetInspect = ClientAssetInspect.getDataFromDatabase(mContext, aitId).get(0);
                //objClientAssetInspect.setAitAssetId(objClientAssetInspect.getAitId());
                //objClientAssetInspect.setAitAsset_name(objClientAssetInspect.getAmAsset_name());
                //objClientAssetInspect.setAitAssetBarcode(objClientAssetInspect.getAmBarcode_no());
                //objClientAssetInspect.setAitAssignedTo(objClientAssetInspect.getAmInspection_aasigned_employee());
                //objClientAssetInspect.setAitAssignedDate(objClientAssetInspect.getAmNext_inspection_date());
                objClientAssetInspect.setAitIsPresent("Yes");
                DataBase db = new DataBase(mContext);
                db.open();
                Cursor curName = db.fetch(DataBase.adminuser_employee_table, DataBase.adminuser_employee_int, "empId='" + objClientAssetInspect.getAmInspection_aasigned_employee() + "'");
                if (curName != null && curName.getCount() > 0) {
                    curName.moveToFirst();
                    assignedT0EmpName = curName.getString(3) + " " + curName.getString(4);
                }
                curName.close();
                db.close();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                ArrayAdapter<ClientAssetInspectServiceStatus> adpSpinner = new ArrayAdapter<ClientAssetInspectServiceStatus>(mContext, R.layout.spinner_item_no_padding, arrClientAssetInspectServiceStatus);
                adpSpinner.setDropDownViewResource(R.layout.spinner_item);
                spnStatus.setAdapter(adpSpinner);
                txtAssetName.setText(objClientAssetInspect.getAitAsset_name());
                txtAssignedTo.setText(assignedT0EmpName);
                edInspectionDate.setText(dateFormat.format(calInspectionDate.getTime()));
                edInspectionTime.setText(timeFormate.format(calInspectionDate.getTime()));
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    View.OnClickListener handleClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.edInspectionDate: {
                    final Dialog dp = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            view.setMinDate(dtCurrentDate.getTime());
                            calInspectionDate.set(Calendar.YEAR, year);
                            calInspectionDate.set(Calendar.MONTH, monthOfYear);
                            calInspectionDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            edInspectionDate.setText(dateFormat.format(calInspectionDate.getTime()));
                            objClientAssetInspect.setAitDateTime(calInspectionDate.getTime());
                        }
                    }, calInspectionDate.get(Calendar.YEAR), calInspectionDate.get(Calendar.MONTH), calInspectionDate.get(Calendar.DAY_OF_MONTH));
                    dp.show();
                    break;
                }

                case R.id.edInspectionTime: {
                    final Dialog tp = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            if (view.isShown()) {
                                calInspectionDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calInspectionDate.set(Calendar.MINUTE, minute);
                                edInspectionTime.setText(timeFormate.format(calInspectionDate.getTime()));
                                objClientAssetInspect.setAitDateTime(calInspectionDate.getTime());
                                Logger.debug(hourOfDay + " " + minute + " " + calInspectionDate.getTime());
                            }
                        }
                    }, calInspectionDate.get(Calendar.HOUR_OF_DAY), calInspectionDate.get(Calendar.MINUTE), true);
                    tp.show();
                    break;
                }
                case R.id.btnUploadPic: {
                    Intent cameraIntent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, ConstantVal.REQUEST_TO_START_CAMERA_ACTIVITY);
                    break;
                }
                case R.id.btnSave: {
                    new AsyncTask() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            isDataEntedProperly = true;
                            if (Helper.isFieldBlank(edInspectionName.getText().toString())) {
                                edInspectionName.setError(getString(R.string.msgEnterInspectionName));
                                requestFocus(edInspectionName);
                                isDataEntedProperly = false;
                            } else if (Helper.isFieldBlank(edInspectionNote.getText().toString())) {
                                edInspectionNote.setError(getString(R.string.msgEnterInspectionNote));
                                requestFocus(edInspectionNote);
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
                objClientAssetInspect.setAitName(edInspectionName.getText().toString());
                objClientAssetInspect.setAitNote(edInspectionNote.getText().toString());
                objClientAssetInspect.setAitStatusId(String.valueOf(arrClientAssetInspectServiceStatus.get(spnStatus.getSelectedItemPosition()).getId()));
                objClientAssetInspect.setAitDateTime(calInspectionDate.getTime());
                DataBase db = new DataBase(mContext);
                db.open();
                ContentValues cv = new ContentValues();
                cv.put("localViewStatus", ConstantVal.InspectionServiceStatus.DONE);
                db.update(DataBase.inspect_view_table, DataBase.inspect_view_int, "aitId=" + objClientAssetInspect.getAitId(), cv);
                String strIsPresent = "Y";
                ContentValues cvInspect = new ContentValues();
                cvInspect.put("aitName", objClientAssetInspect.getAitName());
                cvInspect.put("aitDateTime", objClientAssetInspect.getAitDateTime().getTime());
                cvInspect.put("aitNote", objClientAssetInspect.getAitNote());
                cvInspect.put("aitPhoto", objClientAssetInspect.getPhoto());
                cvInspect.put("aitStatusId", objClientAssetInspect.getAitStatusId());
                cvInspect.put("aitIsPresent", strIsPresent);
                db.update(DataBase.inspect_table, DataBase.inspect_int, "aitId=" + objClientAssetInspect.getAitId(), cvInspect);
                db.close();

                String account_id = Helper.getStringPreference(mContext, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
                final String tokenId = Helper.getStringPreference(mContext, ConstantVal.TOKEN, "");
                String[] data = {String.valueOf(tokenId), account_id, objClientAssetInspect.getAitId(), objClientAssetInspect.getAitName(),
                        Helper.convertDateToString(calInspectionDate.getTime(), ConstantVal.DATE_FORMAT + " " + ConstantVal.TIME_FORMAT),
                        objClientAssetInspect.getAitNote(), objClientAssetInspect.getPhoto() == null ? "" : objClientAssetInspect.getPhoto(), strIsPresent, objClientAssetInspect.getAitStatusId()};
                //Logger.debug(data.length + "len");
                URLMapping um = ConstantVal.updateInspectTransaction(mContext);
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
                            setResult(ConstantVal.INSPECTION_TRANSACTION_RESPONSE_CODE);
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
        if (resultCode == ConstantVal.EXIT_RESPONSE_CODE) {
            ac.setResult(ConstantVal.EXIT_RESPONSE_CODE);
            finish();
        }
        if (requestCode == ConstantVal.REQUEST_TO_START_CAMERA_ACTIVITY && resultCode == RESULT_OK) {
            new AsyncTask() {
                Bitmap bmp;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    bmp = (Bitmap) data.getExtras().get("data");
                    String strBAse64Image = Helper.getEncoded64ImageStringFromBitmap(bmp);
                    objClientAssetInspect.setPhoto(strBAse64Image);
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
