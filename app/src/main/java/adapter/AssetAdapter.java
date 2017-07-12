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
import com.stackio.jobio.officeApp.acAssetDetail;

import java.util.ArrayList;

import entity.ClientAsset;
import utility.ConstantVal;
import utility.Helper;

/**
 * Created by SAI on 9/14/2016.
 */
public class AssetAdapter extends BaseAdapter {
    Typeface ubuntuL,ubuntuM;
    Context ctx;
    ArrayList<ClientAsset> arrClientAsset;

    private class ViewHolder {
        TextView txtAssetName, txtAssetDesc;
        RelativeLayout lyAsset;
    }


    public AssetAdapter(Context ctx, ArrayList<ClientAsset> arrClientAsset) {
        this.ctx = ctx;
        this.arrClientAsset = arrClientAsset;
        ubuntuL = Helper.getUbuntuL(ctx);
        ubuntuM = Helper.getUbuntuM(ctx);
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
            holder.lyAsset = (RelativeLayout) convertView.findViewById(R.id.lyAsset);
            holder.txtAssetDesc = (TextView) convertView.findViewById(R.id.txtAssetDesc);
            holder.txtAssetName = (TextView) convertView.findViewById(R.id.txtAssetNameType);
            holder.txtAssetDesc.setTypeface(ubuntuL);
            holder.txtAssetName.setTypeface(ubuntuM);
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
                ((AppCompatActivity)ctx).startActivityForResult(i, ConstantVal.EXIT_REQUEST_CODE);
            }
        });
        return convertView;
    }
}

