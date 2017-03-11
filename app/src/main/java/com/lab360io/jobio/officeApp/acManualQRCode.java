package com.lab360io.jobio.officeApp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.xwray.fontbinding.FontCache;

import me.zhanghai.android.materialedittext.MaterialEditText;
import utility.ConstantVal;
import utility.Helper;


public class acManualQRCode extends AppCompatActivity {
    ImageButton btnScanQR;
    FloatingActionButton btnNext;
    EditText edQRCode;
    AppCompatActivity ac;
    Handler handler = new Handler();
    Helper objHelper = new Helper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.startFabric(this);
        FontCache.getInstance(getApplicationContext()).addFont("Ubuntu", "Ubuntu-C.ttf");
        DataBindingUtil.setContentView(this, R.layout.manual_qr_code);
        ac = this;

        btnScanQR = (ImageButton) findViewById(R.id.btnScanQR);
        edQRCode = (EditText) findViewById(R.id.edQRCode);
        btnNext = (FloatingActionButton) findViewById(R.id.btnNext);
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
                Intent i = new Intent(getApplicationContext(), acQRCodeScanner.class);
                startActivity(i);
            }
        });
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ConstantVal.EXIT_RESPONSE_CODE) {
            ac.setResult(ConstantVal.EXIT_RESPONSE_CODE);
            finish();
        }
    }
}
