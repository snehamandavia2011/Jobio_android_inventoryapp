package adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lab360io.jobio.inventoryapp.R;

import java.util.ArrayList;

import asyncmanager.asyncLoadCommonData;
import entity.ClientItemMaster1;
import entity.ClientJobInvoiceRefDetail;
import entity.ClientPORefDetail;
import utility.Helper;

/**
 * Created by SAI on 10/20/2016.
 */
public class PORefDetailAdapter extends BaseAdapter {
    Typeface ubuntuL, ubuntuM;
    Context ctx;
    ArrayList<ClientPORefDetail> arrClientPORefDetail;

    private class ViewHolder {
        LinearLayout lyClickableLayout;
        TextView txtItemName, txtStatus, txtQty, txtPrice, txtBarcode, txtExpireDate;
        ImageView imgItem;
    }


    public PORefDetailAdapter(Context ctx, ArrayList<ClientPORefDetail> arrClientPORefDetail) {
        this.ctx = ctx;
        this.arrClientPORefDetail = arrClientPORefDetail;
        ubuntuL = Helper.getUbuntuL(ctx);
        ubuntuM = Helper.getUbuntuM(ctx);
    }

    @Override
    public int getCount() {
        return arrClientPORefDetail.size();
    }

    @Override
    public Object getItem(int position) {
        return arrClientPORefDetail.get(position);
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
            convertView = mInflater.inflate(R.layout.invoice_job_po_detail_list_item, null);
            holder = new ViewHolder();
            holder.txtItemName = (TextView) convertView.findViewById(R.id.txtItemName);
            holder.txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
            holder.txtQty = (TextView) convertView.findViewById(R.id.txtQty);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            holder.txtBarcode = (TextView) convertView.findViewById(R.id.txtBarcode);
            holder.txtExpireDate = (TextView) convertView.findViewById(R.id.txtExpireDate);
            holder.imgItem = (ImageView) convertView.findViewById(R.id.imgItem);
            holder.lyClickableLayout = (LinearLayout) convertView.findViewById(R.id.lyClickableLayout);
            holder.txtBarcode.setTypeface(ubuntuL);
            holder.txtExpireDate.setTypeface(ubuntuL);
            holder.txtPrice.setTypeface(ubuntuL);
            holder.txtQty.setTypeface(ubuntuL);
            holder.txtStatus.setTypeface(ubuntuL);
            holder.txtItemName.setTypeface(ubuntuM);
            holder.txtExpireDate.setSelected(true);
            holder.txtBarcode.setSelected(true);
            holder.txtQty.setSelected(true);
            holder.txtPrice.setSelected(true);
            holder.txtItemName.setSelected(true);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ClientPORefDetail objClientPORefDetail = arrClientPORefDetail.get(position);
        holder.txtBarcode.setText(ctx.getString(R.string.strBarcode) + " :" + ctx.getString(R.string.strNA));
        holder.txtBarcode.setPaintFlags(holder.txtBarcode.getPaintFlags() & ~Paint.UNDERLINE_TEXT_FLAG);
        holder.txtBarcode.setClickable(false);


        if (!objClientPORefDetail.isPhotoLoaded()) {
            ClientItemMaster1 objClientItemMaster1 = new ClientItemMaster1();
            objClientItemMaster1.setPhoto("");
            objClientItemMaster1.setUuid(objClientPORefDetail.getPodItem_id());
            new asyncLoadCommonData(ctx).loadItemPhotoById(holder.imgItem, objClientItemMaster1, imgClick);
        }
        holder.txtItemName.setText(objClientPORefDetail.getImItem_name());
        holder.txtQty.setText(ctx.getString(R.string.strQuantity) + " :" + objClientPORefDetail.getPodQty());
        holder.txtPrice.setText(ctx.getString(R.string.strPrice) + " :" + objClientPORefDetail.getPodPrice());
        holder.txtExpireDate.setText(ctx.getString(R.string.strDateExpire) + " :" + ctx.getString(R.string.strNoExpiry));
        holder.txtStatus.setText(objClientPORefDetail.getPodOrder_status());
        return convertView;
    }

    View.OnClickListener imgClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bitmap bmp = (Bitmap) v.getTag();
            Helper.openImageZoomDialog(ctx, bmp);
        }
    };

    View.OnClickListener barClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String strBarcode = v.getTag().toString();
            Helper.openBarcodeDialog(ctx, strBarcode);
        }
    };
}


