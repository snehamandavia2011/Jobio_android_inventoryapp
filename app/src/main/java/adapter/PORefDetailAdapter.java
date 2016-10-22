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
import com.lab360io.jobio.inventoryapp.acEditPOReferenceDetail;

import java.util.ArrayList;

import asyncmanager.asyncLoadCommonData;
import entity.ClientItemMaster1;
import entity.ClientPORefDetail;
import utility.ConstantVal;
import utility.Helper;

/**
 * Created by SAI on 10/20/2016.
 */
public class PORefDetailAdapter extends BaseAdapter {
    Typeface ubuntuL, ubuntuM;
    Context ctx;
    ArrayList<ClientPORefDetail> arrClientPORefDetail;
    int selStockTransactionStatus, selStockTransactionReason;
    String referenceType, refId, fromId, toId, fromType, toType;

    private class ViewHolder {
        LinearLayout lyClickableLayout;
        TextView txtItemName, txtQty, txtCost, txtPrice;
        ImageView imgItem;
    }


    public PORefDetailAdapter(Context ctx, ArrayList<ClientPORefDetail> arrClientPORefDetail, int selStockTransactionStatus,
                              int selStockTransactionReason, String referenceType, String refId, String fromId, String toId, String fromType, String toType) {
        this.ctx = ctx;
        this.arrClientPORefDetail = arrClientPORefDetail;
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
            convertView = mInflater.inflate(R.layout.po_detail_list_item, null);
            holder = new ViewHolder();
            holder.txtItemName = (TextView) convertView.findViewById(R.id.txtItemName);
            holder.txtQty = (TextView) convertView.findViewById(R.id.txtQty);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            holder.txtCost = (TextView) convertView.findViewById(R.id.txtCost);
            holder.imgItem = (ImageView) convertView.findViewById(R.id.imgItem);
            holder.lyClickableLayout = (LinearLayout) convertView.findViewById(R.id.lyClickableLayout);
            holder.txtPrice.setTypeface(ubuntuL);
            holder.txtQty.setTypeface(ubuntuL);
            holder.txtCost.setTypeface(ubuntuL);
            holder.txtItemName.setTypeface(ubuntuM);
            holder.txtQty.setSelected(true);
            holder.txtPrice.setSelected(true);
            holder.txtItemName.setSelected(true);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ClientPORefDetail objClientPORefDetail = arrClientPORefDetail.get(position);

        if (!objClientPORefDetail.isPhotoLoaded()) {
            ClientItemMaster1 objClientItemMaster1 = new ClientItemMaster1();
            objClientItemMaster1.setPhoto("");
            objClientItemMaster1.setUuid(objClientPORefDetail.getPodItem_id());
            new asyncLoadCommonData(ctx).loadItemPhotoById(holder.imgItem, objClientItemMaster1, imgClick);
        }
        holder.txtItemName.setText(objClientPORefDetail.getImItem_name());
        holder.txtQty.setText(ctx.getString(R.string.strQuantity) + " :" + objClientPORefDetail.getPodQty());
        holder.txtPrice.setText(ctx.getString(R.string.strPrice) + " :" + Helper.getCurrencySymbol() + objClientPORefDetail.getPodPrice());
        holder.txtCost.setText(ctx.getString(R.string.strCost) + " :" + Helper.getCurrencySymbol() + objClientPORefDetail.getPodCost());
        holder.lyClickableLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, acEditPOReferenceDetail.class);
                i.putExtra("selStockTransactionStatus", selStockTransactionStatus);
                i.putExtra("selStockTransactionReason", selStockTransactionReason);
                i.putExtra("referenceType", referenceType);
                i.putExtra("refId", refId);
                i.putExtra("fromId", fromId);
                i.putExtra("toId", toId);
                i.putExtra("fromType", fromType);
                i.putExtra("toType", toType);
                i.putExtra("objClientPORefDetail", objClientPORefDetail);
                /*i.putExtra("itemName", objClientPORefDetail.getImItem_name());
                i.putExtra("quantity", objClientPORefDetail.getPodQty());
                i.putExtra("cost", objClientPORefDetail.getPodCost());
                i.putExtra("price", objClientPORefDetail.getPodPrice());*/
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


