package com.lab360io.jobio.inventoryapp;

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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import com.lab360io.jobio.inventoryapp.R;
import com.xwray.fontbinding.FontCache;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import entity.ClientAsset;
import entity.ClientAssetInspectServiceStatus;
import entity.InspectTransaction;
import io.fabric.sdk.android.services.concurrency.AsyncTask;
import me.zhanghai.android.materialedittext.MaterialEditText;
import utility.ConstantVal;
import utility.DataBase;
import utility.Helper;
import utility.Logger;

public class acInspectTransaction extends AppCompatActivity {
    ArrayList<ClientAssetInspectServiceStatus> arrClientAssetInspectServiceStatus = new ArrayList<>();
    InspectTransaction objInspectTransaction = new InspectTransaction();
    boolean isDataEntedProperly = true;
    DateFormat dateFormat = new SimpleDateFormat("dd MMM yyy");
    DateFormat timeFormate = new SimpleDateFormat("hh:mm");
    Date dtCurrentDate = new Date();
    TextView txtAssetName, txtAssignedTo;
    MaterialEditText edInspectionName, edInspectionDate, edInspectionNote, edInspectionTime;
    ImageButton btnTakePic;
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
        objHelper.setActionBar(ac, mContext.getString(R.string.strAssetDetail));
        setData();
    }


    private void setData() {
        new AsyncTask() {
            String asset_id = "", assignedT0EmpName = "";

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
                btnTakePic = (ImageButton) findViewById(R.id.btnTakePic);
                spnStatus = (Spinner) findViewById(R.id.spnStatus);
                btnSave = (Button) findViewById(R.id.btnSave);
                btnCancel = (Button) findViewById(R.id.btnCancel);
                btnCancel.setOnClickListener(handleClick);
                btnSave.setOnClickListener(handleClick);
                edInspectionDate.setOnClickListener(handleClick);
                edInspectionTime.setOnClickListener(handleClick);
                btnTakePic.setOnClickListener(handleClick);
                if (ac.getIntent().getExtras() != null) {
                    asset_id = ac.getIntent().getStringExtra("assetId");
                }
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                arrClientAssetInspectServiceStatus = ConstantVal.assetServiceInspectionStatus.getStatusArr(mContext);
                ClientAsset objClientAsset = ClientAsset.getDataFromDatabase(mContext, asset_id).get(0);
                objInspectTransaction.setAssetId(asset_id);
                objInspectTransaction.setAssetName(objClientAsset.getAmAsset_name());
                objInspectTransaction.setAssetBarcode(objClientAsset.getAmBarcode_no());
                objInspectTransaction.setAssignedTo(objClientAsset.getAmInspection_aasigned_employee());
                objInspectTransaction.setAssignedDate(objClientAsset.getAmNext_inspection_date());
                objInspectTransaction.setIsPresent("Yes");
                DataBase db = new DataBase(mContext);
                db.open();
                Cursor curName = db.fetch(DataBase.adminuser_employee_table, DataBase.adminuser_employee_int, "empId='" + objClientAsset.getAmInspection_aasigned_employee() + "'");
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
                ArrayAdapter<ClientAssetInspectServiceStatus> adpSpinner = new ArrayAdapter<ClientAssetInspectServiceStatus>(mContext, R.layout.spinner_item, arrClientAssetInspectServiceStatus);
                spnStatus.setAdapter(adpSpinner);
                txtAssetName.setText(objInspectTransaction.getAssetName());
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
                            objInspectTransaction.setDate_time(calInspectionDate.getTime());
                            Logger.debug(objInspectTransaction.getDate_time().toString());
                        }
                    }, calInspectionDate.get(Calendar.YEAR), calInspectionDate.get(Calendar.MONTH), calInspectionDate.get(Calendar.DAY_OF_MONTH));
                    dp.show();
                    break;
                }

                case R.id.edInspectionTime: {
                    final Dialog tp = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            calInspectionDate.set(Calendar.HOUR, hourOfDay);
                            calInspectionDate.set(Calendar.MINUTE, minute);
                            edInspectionTime.setText(timeFormate.format(calInspectionDate.getTime()));
                            objInspectTransaction.setDate_time(calInspectionDate.getTime());
                            Logger.debug(objInspectTransaction.getDate_time().toString());
                        }
                    }, calInspectionDate.get(Calendar.HOUR), calInspectionDate.get(Calendar.MINUTE), true);
                    tp.show();
                    break;
                }
                case R.id.btnTakePic: {
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
                            if (isDataEntedProperly) {
                                ac.finish();
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
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                objInspectTransaction.setInspectionName(edInspectionName.getText().toString());
                objInspectTransaction.setNote(edInspectionNote.getText().toString());
                objInspectTransaction.setStatusId(String.valueOf(arrClientAssetInspectServiceStatus.get(spnStatus.getSelectedItemPosition()).getId()));
                objInspectTransaction.setDate_time(Helper.convertStringToDate(edInspectionDate.getText().toString(), ConstantVal.DATE_FORMAT+" "+ConstantVal.TIME_FORMAT));
                objInspectTransaction.display();
                DataBase db = new DataBase(mContext);
                db.open();
                ContentValues cv = new ContentValues();
                cv.put("localViewStatus", ConstantVal.InspectionServiceStatus.DONE);
                cv.put("serStatus", objInspectTransaction.getStatusId());
                db.update(DataBase.inspect_view_table, "localViewStatus=?", new String[]{String.valueOf(ConstantVal.InspectionServiceStatus.NEW) + " or " + String.valueOf(ConstantVal.InspectionServiceStatus.PENDING)}, cv);
                db.close();
                return null;
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
                    objInspectTransaction.setPhoto(strBAse64Image);
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
