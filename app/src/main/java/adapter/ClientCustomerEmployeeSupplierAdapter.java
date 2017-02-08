package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lab360io.jobio.officeApp.R;
import com.lab360io.jobio.officeApp.acAssetDetail;

import java.util.ArrayList;

import entity.ClientAsset;
import entity.ClientCustEmpSupplier;
import utility.ConstantVal;
import utility.Helper;

/**
 * Created by SAI on 2/7/2017.
 */
public class ClientCustomerEmployeeSupplierAdapter extends BaseAdapter {
    Context ctx;
    ArrayList<ClientCustEmpSupplier> arrClientCustEmpSupplier;


    public ClientCustomerEmployeeSupplierAdapter(Context ctx, ArrayList<ClientCustEmpSupplier> arrClientCustEmpSupplier) {
        this.ctx = ctx;
        this.arrClientCustEmpSupplier = arrClientCustEmpSupplier;
    }

    @Override
    public int getCount() {
        return arrClientCustEmpSupplier.size();
    }

    @Override
    public Object getItem(int position) {
        return arrClientCustEmpSupplier.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) ctx
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TextView txt = (TextView) mInflater.inflate(R.layout.spinner_item_no_padding, null);
        setTextView(position,txt);
        return txt;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) ctx
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TextView txt = (TextView) mInflater.inflate(R.layout.spinner_item, null);
        setTextView(position,txt);
        return txt;
    }

    private TextView setTextView(int position,TextView txt){
        txt.setText(arrClientCustEmpSupplier.get(position).getName());
        String name = arrClientCustEmpSupplier.get(position).getName();
        String type = arrClientCustEmpSupplier.get(position).getType();
        String text = "";
        if (type.equals(ConstantVal.UserType.CUSTOMER))
            text = "<font color=#0AA89E>" + name + "&nbsp;-&nbsp;</font> <font color=#FFA07A>" + type + "</font>";
        else if (type.equals(ConstantVal.UserType.EMPLOYEE))
            text = "<font color=#0AA89E>" + name + "&nbsp;-&nbsp;</font> <font color=#7CFC00>" + type + "</font>";
        else if (type.equals(ConstantVal.UserType.SUPPLIER))
            text = "<font color=#0AA89E>" + name + "&nbsp;-&nbsp;</font> <font color=#DDA0DD>" + type + "</font>";
        txt.setText(Html.fromHtml(text));
        return txt;
    }
}
