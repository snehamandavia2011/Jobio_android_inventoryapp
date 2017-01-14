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

import com.lab360io.jobio.officeApp.R;
import com.lab360io.jobio.officeApp.acItemDetail;

import java.util.ArrayList;
import java.util.Date;

import entity.ClientItemMaster;
import utility.ConstantVal;
import utility.Helper;

/**
 * Created by SAI on 9/14/2016.
 */
public class AddedItemAdapter extends BaseAdapter {
    Typeface ubuntuL, ubuntuM;
    Context ctx;
    ArrayList<ClientItemMaster> arrClientItem;

    private class ViewHolder {
        TextView txtItemName, txtItemDesc, txtLastUpdated;
        RelativeLayout ly;
    }


    public AddedItemAdapter(Context ctx, ArrayList<ClientItemMaster> arrClientItem) {
        this.ctx = ctx;
        this.arrClientItem = arrClientItem;
        ubuntuL = Helper.getUbuntuL(ctx);
        ubuntuM = Helper.getUbuntuM(ctx);
    }

    @Override
    public int getCount() {
        return arrClientItem.size();
    }

    @Override
    public Object getItem(int position) {
        return arrClientItem.get(position);
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
            convertView = mInflater.inflate(R.layout.added_item_list_item, null);
            holder = new ViewHolder();
            holder.ly = (RelativeLayout) convertView.findViewById(R.id.ly);
            holder.txtItemDesc = (TextView) convertView.findViewById(R.id.txtItemDesc);
            holder.txtItemName = (TextView) convertView.findViewById(R.id.txtItemName);
            holder.txtLastUpdated = (TextView) convertView.findViewById(R.id.txtLastUpdated);
            holder.txtItemDesc.setTypeface(ubuntuL);
            holder.txtLastUpdated.setTypeface(ubuntuL);
            holder.txtItemName.setTypeface(ubuntuM);
            holder.txtItemDesc.setSelected(true);
            holder.txtItemName.setSelected(true);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ClientItemMaster objClientItemMaster = arrClientItem.get(position);
        holder.txtItemName.setText(objClientItemMaster.getItem_name() + " [" + objClientItemMaster.getCategory_name() + "]");
        holder.txtItemDesc.setText(objClientItemMaster.getSpecification().equals("") ? ctx.getString(R.string.msgDescNotAvail).toUpperCase() : objClientItemMaster.getSpecification());
        String date = Helper.convertDateToString(new Date(Long.parseLong(objClientItemMaster.getLast_update_date_time())), ConstantVal.DATE_FORMAT + " " + ConstantVal.TIME_FORMAT);
        holder.txtLastUpdated.setText(ctx.getString(R.string.strLastUpdated) + ": " + date);
        holder.ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, acItemDetail.class);
                i.putExtra("itemUUId", objClientItemMaster.getUuid());
                i.putExtra("needToSyncFromServer", true);
                i.putExtra("barcode", "");
                ((AppCompatActivity) ctx).startActivityForResult(i, ConstantVal.EXIT_REQUEST_CODE);
            }
        });
        return convertView;
    }
}

