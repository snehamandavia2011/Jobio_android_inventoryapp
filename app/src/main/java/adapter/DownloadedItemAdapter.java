package adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stackio.jobio.officeApp.R;
import com.stackio.jobio.officeApp.acStock;

import java.util.ArrayList;

import asyncmanager.asyncLoadCommonData;
import entity.ClientItemMaster1;
import utility.Helper;
import utility.Logger;

/**
 * Created by SAI on 9/14/2016.
 */
public class DownloadedItemAdapter extends BaseAdapter {
    Typeface ubuntuL, ubuntuM;
    Context ctx;
    ArrayList<ClientItemMaster1> arrClientItem;

    private class ViewHolder {
        TextView txtItemNameType, txtSpecification, txtMonthlyDemand, txtMinQtyForRestock, txtAvailQty;
        ImageView imgItem, imgNotification;
    }


    public DownloadedItemAdapter(Context ctx, ArrayList<ClientItemMaster1> arrClientItem) {
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
            convertView = mInflater.inflate(R.layout.downloaded_item_list_item, null);
            holder = new ViewHolder();
            holder.txtItemNameType = (TextView) convertView.findViewById(R.id.txtItemNameType);
            holder.txtSpecification = (TextView) convertView.findViewById(R.id.txtSpecification);
            holder.txtMonthlyDemand = (TextView) convertView.findViewById(R.id.txtMonthlyDemand);
            holder.txtMinQtyForRestock = (TextView) convertView.findViewById(R.id.txtMinQtyForRestock);
            holder.txtAvailQty = (TextView) convertView.findViewById(R.id.txtAvailQty);
            holder.imgItem = (ImageView) convertView.findViewById(R.id.imgItem);
            holder.imgNotification = (ImageView) convertView.findViewById(R.id.imgNotification);
            holder.txtMinQtyForRestock.setTypeface(ubuntuL);
            holder.txtAvailQty.setTypeface(ubuntuL);
            holder.txtMonthlyDemand.setTypeface(ubuntuL);
            holder.txtSpecification.setTypeface(ubuntuL);
            holder.txtItemNameType.setTypeface(ubuntuM);
            holder.txtSpecification.setSelected(true);
            holder.txtItemNameType.setSelected(true);
            holder.txtMinQtyForRestock.setSelected(true);
            holder.txtAvailQty.setSelected(true);
            holder.txtMonthlyDemand.setSelected(true);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //position = getItemViewType(position);
        final ClientItemMaster1 objClientItemMaster1 = arrClientItem.get(position);
        if (!objClientItemMaster1.isImageLoaded()) {
            new asyncLoadCommonData(ctx).loadItemPhotoById(holder.imgItem, objClientItemMaster1, imgClick);
        }
        holder.txtItemNameType.setText(objClientItemMaster1.getItem_name() + " [" + objClientItemMaster1.getCategory_name() + "]");
        holder.txtSpecification.setText(objClientItemMaster1.getSpecification().equals("") ? ctx.getString(R.string.msgDescNotAvail).toUpperCase() : objClientItemMaster1.getSpecification());
        int minQty, availQty;
        try {
            availQty = Integer.parseInt(objClientItemMaster1.getAvailable_qty());
        } catch (Exception e) {
            availQty = 0;
            Logger.writeToCrashlytics(e);
        }
        holder.txtAvailQty.setText(ctx.getString(R.string.strAvailQty) + ": " + availQty);
        holder.txtMonthlyDemand.setText(ctx.getString(R.string.strMonthlyDemand) + ": " + objClientItemMaster1.getMonthly_demand());
        try {
            minQty = Integer.parseInt(objClientItemMaster1.getMin_qty_for_restock());
        } catch (Exception e) {
            minQty = 0;
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
        }
        holder.txtMinQtyForRestock.setText(ctx.getString(R.string.strMinQtyForRestock) + ": " + minQty);
        if (availQty <= minQty) {
            holder.imgNotification.setImageResource(R.drawable.ic_red_flag);
        } else {
            holder.imgNotification.setImageResource(R.drawable.ic_green_flag);
        }
        return convertView;
    }

    View.OnClickListener imgClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bitmap bmp = (Bitmap) v.getTag();
            Helper.openImageZoomDialog(acStock.mContext, bmp);
        }
    };

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
}

