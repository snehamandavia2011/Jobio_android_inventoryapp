package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stackio.jobio.officeApp.R;

import java.util.ArrayList;

import entity.ClientCustEmpSupplier;
import utility.ConstantVal;
import utility.Helper;

/**
 * Created by SAI on 9/14/2016.
 */
public class EmpCustomerSupplierAdapter extends BaseAdapter {
    Typeface ubuntuL;
    Context ctx;
    ArrayList<ClientCustEmpSupplier> arrClientCustEmpSupplier;

    private class ViewHolder {
        TextView txtUserName;
        RelativeLayout lyUserName;
    }


    public EmpCustomerSupplierAdapter(Context ctx, ArrayList<ClientCustEmpSupplier> arrClientCustEmpSupplier) {
        this.ctx = ctx;
        this.arrClientCustEmpSupplier = arrClientCustEmpSupplier;
        ubuntuL = Helper.getUbuntuL(ctx);
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
        final ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) ctx
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.emp_customer_suuplier_list_item, null);
            holder = new ViewHolder();
            holder.lyUserName = (RelativeLayout) convertView.findViewById(R.id.lyUserName);
            holder.txtUserName = (TextView) convertView.findViewById(R.id.txtUserName);
            holder.txtUserName.setTypeface(ubuntuL);
            holder.txtUserName.setSelected(true);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ClientCustEmpSupplier objClientCustEmpSupplier = arrClientCustEmpSupplier.get(position);
        holder.txtUserName.setText(objClientCustEmpSupplier.getName());
        holder.lyUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("selectedUser", objClientCustEmpSupplier);
                ((AppCompatActivity) ctx).setResult(ConstantVal.FROM_TO_USER_SELECTION_RESPONSE, i);
                ((AppCompatActivity) ctx).finish();
            }
        });
        return convertView;
    }
}

