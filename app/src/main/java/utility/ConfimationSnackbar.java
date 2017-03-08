package utility;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.lab360io.jobio.officeApp.R;

/**
 * Created by SNEHA MANDAVIA on 5/18/2016.
 */
public class ConfimationSnackbar {
    AppCompatActivity ac;
    TSnackbar snackbar;
    int toastType;

    public ConfimationSnackbar(AppCompatActivity ac, int toastType) {
        this.toastType = toastType;
        this.ac = ac;
        snackbar = TSnackbar.make(ac.findViewById(android.R.id.content), "", TSnackbar.LENGTH_INDEFINITE);
    }

    public void showSnackBar(String message, String positiveText, String negativeText, View.OnClickListener clkPosititive, View.OnClickListener clkNegative) {
        TSnackbar.SnackbarLayout layout = (TSnackbar.SnackbarLayout) snackbar.getView();
        layout.setBackgroundResource(toastType);
        LayoutInflater mInflater = (LayoutInflater) ac.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View snackView = DataBindingUtil.inflate(mInflater, R.layout.custom_snackbar_confirmation, null, true).getRoot();
        TextView imageView = (TextView) snackView.findViewById(R.id.txtText);
        imageView.setText(message);
        Button btnPositive = (Button) snackView.findViewById(R.id.btnPositive);
        Button btnNegative = (Button) snackView.findViewById(R.id.btnNegative);
        btnPositive.setText(positiveText);
        btnNegative.setText(negativeText);
        btnPositive.setOnClickListener(clkPosititive);
        if (clkNegative == null) {
            btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
        } else {
            btnNegative.setOnClickListener(clkNegative);
        }

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        snackbar.show();
    }

    public void dismissSnackBar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }
}
