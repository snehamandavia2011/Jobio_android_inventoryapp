package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lab360io.jobio.inventoryapp.R;
import com.lab360io.jobio.inventoryapp.acEditJobInvoiceReferenceDetail;

import java.util.ArrayList;

import asyncmanager.asyncLoadCommonData;
import entity.ClientItemMaster1;
import entity.ClientJobInvoiceRefDetail;
import utility.ConstantVal;
import utility.Helper;
import utility.Logger;

/**
 * Created by SAI on 10/20/2016.
 */
public class JobInvoiceRefDetailAdapter extends BaseAdapter {
    Typeface ubuntuL, ubuntuM;
    Context ctx;
    ArrayList<ClientJobInvoiceRefDetail> arrClientJobInvoiceRefDetail;
    int selStockTransactionStatus, selStockTransactionReason;
    String referenceType, refId, fromId, toId, fromType, toType;

    private class ViewHolder {
        LinearLayout lyClickableLayout;
        TextView txtItemName, txtStatus, txtQty, txtPrice, txtBarcode, txtExpireDate;
        ImageView imgItem;
    }


    public JobInvoiceRefDetailAdapter(Context ctx, ArrayList<ClientJobInvoiceRefDetail> arrClientJobInvoiceRefDetail, int selStockTransactionStatus,
                                      int selStockTransactionReason, String referenceType, String refId, String fromId, String toId, String fromType, String toType) {
        this.ctx = ctx;
        this.arrClientJobInvoiceRefDetail = arrClientJobInvoiceRefDetail;
        ubuntuL = Helper.getUbuntuL(ctx);
        ubuntuM = Helper.getUbuntuM(ctx);
        this.selStockTransactionReason = selStockTransactionReason;
        this.selStockTransactionStatus = selStockTransactionStatus;
        this.referenceType = referenceType;
        this.refId = refId;
        this.fromId = fromId;
        this.toId = toId;
        this.fromType = fromType;
        this.toType = toType;
    }

    @Override
    public int getCount() {
        return arrClientJobInvoiceRefDetail.size();
    }

    @Override
    public Object getItem(int position) {
        return arrClientJobInvoiceRefDetail.get(position);
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
            convertView = mInflater.inflate(R.layout.invoice_job_detail_list_item, null);
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

        final ClientJobInvoiceRefDetail objClientJobInvoiceRefDetail = arrClientJobInvoiceRefDetail.get(position);
        if (!objClientJobInvoiceRefDetail.getItBarcode().equals("")) {
            holder.txtBarcode.setText(ctx.getString(R.string.strBarcode) + ": " + objClientJobInvoiceRefDetail.getItBarcode());
            holder.txtBarcode.setPaintFlags(holder.txtBarcode.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            holder.txtBarcode.setOnClickListener(barClick);
            holder.txtBarcode.setTag(objClientJobInvoiceRefDetail.getItBarcode());
            holder.txtBarcode.setSelected(true);
        } else {
            holder.txtBarcode.setText(ctx.getString(R.string.strBarcode) + ": " + ctx.getString(R.string.strNA));
            holder.txtBarcode.setPaintFlags(holder.txtBarcode.getPaintFlags() & ~Paint.UNDERLINE_TEXT_FLAG);
            holder.txtBarcode.setClickable(false);
        }

        if (!objClientJobInvoiceRefDetail.isPhotoLoaded()) {
            ClientItemMaster1 objClientItemMaster1 = new ClientItemMaster1();
            objClientItemMaster1.setPhoto(objClientJobInvoiceRefDetail.getImPhoto());
            objClientItemMaster1.setUuid(objClientJobInvoiceRefDetail.getItItem_id());
            new asyncLoadCommonData(ctx).loadItemPhotoById(holder.imgItem, objClientItemMaster1, imgClick);
        }
        holder.txtItemName.setText(objClientJobInvoiceRefDetail.getImItem_name());
        holder.txtQty.setText(ctx.getString(R.string.strQuantity) + " :" + objClientJobInvoiceRefDetail.getItQty());
        holder.txtPrice.setText(ctx.getString(R.string.strPrice) + " :" + Helper.getCurrencySymbol() + objClientJobInvoiceRefDetail.getItPrice());
        String strExpiryDate = objClientJobInvoiceRefDetail.getItExpiry().equals("0000-00-00") ? ctx.getString(R.string.strNoExpiry) : objClientJobInvoiceRefDetail.getItExpiry();
        Logger.debug(strExpiryDate + " " + objClientJobInvoiceRefDetail.getItExpiry());
        holder.txtExpireDate.setText(ctx.getString(R.string.strDateExpire) + " :" + strExpiryDate);
        holder.txtStatus.setText(objClientJobInvoiceRefDetail.getStsmAction_name());
        holder.lyClickableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, acEditJobInvoiceReferenceDetail.class);
                i.putExtra("selStockTransactionStatus", selStockTransactionStatus);
                i.putExtra("selStockTransactionReason", selStockTransactionReason);
                i.putExtra("referenceType", referenceType);
                i.putExtra("refId", refId);
                i.putExtra("fromId", fromId);
                i.putExtra("toId", toId);
                i.putExtra("fromType", fromType);
                i.putExtra("toType", toType);
                i.putExtra("objClientJobInvoiceRefDetail",objClientJobInvoiceRefDetail);
                /*i.putExtra("itemName", objClientJobInvoiceRefDetail.getImItem_name());
                i.putExtra("quantity", objClientJobInvoiceRefDetail.getItQty());
                i.putExtra("price", objClientJobInvoiceRefDetail.getItPrice());
                i.putExtra("expiryDate", objClientJobInvoiceRefDetail.getItExpiry());
                i.putExtra("barcode", objClientJobInvoiceRefDetail.getItBarcode());*/
                ((AppCompatActivity) ctx).startActivityForResult(i, ConstantVal.EXIT_RESPONSE_CODE);
            }
        });
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


