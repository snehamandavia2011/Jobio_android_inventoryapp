package com.stackio.jobio.officeApp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.xwray.fontbinding.FontCache;

import coaching.CoachingPreference;
import coaching.QRCodeConfigurationScreen;
import me.zhanghai.android.materialedittext.MaterialTextInputLayout;
import permission.CameraPermission;
import permission.Constant;
import utility.Helper;


public class acManualQRCode extends AppCompatActivity {
    Button btnLetsStart;
    ImageButton btnScanQR;
    Button btnNext;
    EditText edQRCode;
    MaterialTextInputLayout lyQRCode;
    AppCompatActivity ac;
    Handler handler = new Handler();
    Helper objHelper = new Helper();
    CameraPermission objCameraPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.manual_qr_code);
        ac = this;
        btnLetsStart = (Button) findViewById(R.id.btnLetsStart);
        lyQRCode = (MaterialTextInputLayout) findViewById(R.id.lyQRCode);
        lyQRCode.setHintTextAppearance(R.style.stySubTitleWhite);
        btnScanQR = (ImageButton) findViewById(R.id.btnScanQR);
        edQRCode = (EditText) findViewById(R.id.edQRCode);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helper.isFieldBlank(edQRCode.getText().toString())) {
                    edQRCode.setError(getString(R.string.strEnterQRCode));
                    requestFocus(edQRCode);
                } else {
                    objHelper.verifyingQRcode(ac, handler, edQRCode.getText().toString(), new View[]{btnNext});
                }
            }
        });
        btnScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CoachingPreference.needToShowPrompt(ac, CoachingPreference.MANUAL_QR_CODE_SCAN_QR_CODE_BUTTON))
                    new QRCodeConfigurationScreen().showPromptOnClickScanQRCodeIcon(acManualQRCode.this);
                else {
                    if (objCameraPermission.isHavePermission()) {
                        Intent i = new Intent(getApplicationContext(), acQRCodeScanner.class);
                        startActivity(i);
                    } else {
                        objCameraPermission.askForPermission();
                    }
                }
            }
        });
        btnLetsStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://jobio.io/signup.php"));
                startActivity(i);
            }
        });
        //check coaching prompt open one time or not.
        //if never open, then open the prompt
        //On hide prompt, ask for camera permission (if required and not allow to access camera permission)
        //else
        //  ask for camera permission (if required and not allow to access camera permission)
        objCameraPermission = new CameraPermission(ac);
        if (CoachingPreference.needToShowPrompt(ac, CoachingPreference.MANUAL_QR_CODE_SCREEN))
            new QRCodeConfigurationScreen().showPromptWhileLoadActivity(acManualQRCode.this);
        else
            objCameraPermission.askForPermission();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (objHelper.dtDialog != null && objHelper.dtDialog.getVisibility() == View.VISIBLE)
            objHelper.dtDialog.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constant.PermissionRequestCode.CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                } else {
                    //not granted
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
