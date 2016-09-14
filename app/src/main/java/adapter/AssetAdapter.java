package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lab360io.jobio.inventoryapp.R;
import com.lab360io.jobio.inventoryapp.acAssetDetail;

import java.util.ArrayList;

import entity.ClientAsset;
import utility.Helper;

/**
 * Created by SAI on 9/14/2016.
 */
public class AssetAdapter extends BaseAdapter {
    Typeface ubuntuC, ubuntuL;
    Context ctx;
    ArrayList<ClientAsset> arrClientAsset;

    private class ViewHolder {
        TextView txtAssetName, txtAssetDesc;
        LinearLayout lyAsset;
    }


    public AssetAdapter(Context ctx, ArrayList<ClientAsset> arrClientAsset) {
        this.ctx = ctx;
        this.arrClientAsset = arrClientAsset;
        ubuntuC = Helper.getUbuntuC(ctx);
        ubuntuL = Helper.getUbuntuL(ctx);

    }

    @Override
    public int getCount() {
        return arrClientAsset.size();
    }

    @Override
    public Object getItem(int position) {
        return arrClientAsset.get(position);
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
            convertView = mInflater.inflate(R.layout.asset_item, null);
            holder = new ViewHolder();
            holder.lyAsset = (LinearLayout) convertView.findViewById(R.id.lyAsset);
            holder.txtAssetDesc = (TextView) convertView.findViewById(R.id.txtAssetDesc);
            holder.txtAssetName = (TextView) convertView.findViewById(R.id.txtAssetNameType);
            holder.txtAssetDesc.setTypeface(ubuntuL);
            holder.txtAssetName.setTypeface(ubuntuC);
            holder.txtAssetDesc.setSelected(true);
            holder.txtAssetName.setSelected(true);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ClientAsset objClientAsset = arrClientAsset.get(position);
        holder.txtAssetName.setText(objClientAsset.getAmAsset_name() + "[" + objClientAsset.getActmCategory_name() + "]");
        holder.txtAssetDesc.setText(objClientAsset.getAmDescription().equals("") ? ctx.getString(R.string.msgDescNotAvail).toUpperCase() : objClientAsset.getAmDescription());
        holder.lyAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, acAssetDetail.class);
                i.putExtra("AssetId",objClientAsset.getAoAsset_id());
                ctx.startActivity(i);
            }
        });
        return convertView;
    }
}

