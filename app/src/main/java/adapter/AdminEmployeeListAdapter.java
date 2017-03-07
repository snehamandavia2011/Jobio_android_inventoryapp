package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lab360io.jobio.officeApp.R;
import com.lab360io.jobio.officeApp.acMessageList;

import java.util.ArrayList;

import asyncmanager.asyncLoadCommonData;
import entity.ClientAdminUserEmployee;
import utility.CircleImageView;
import utility.ConstantVal;
import utility.Helper;


/**
 * Created by SAI on 6/17/2016.
 */
public class AdminEmployeeListAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<ClientAdminUserEmployee> arrClientAdminUserEmployee;
    Typeface ubuntuC, ubuntuL;

    private class ViewHolder {
        RelativeLayout rl;
        //ImageView status,profile_image;
        utility.CircleImageView status, profile_image;
        TextView txtEmployeeName, txtUserType, txtPhone, txtUnreadMessageCount;
    }

    public AdminEmployeeListAdapter(Context mContext, ArrayList<ClientAdminUserEmployee> arrClientAdminUserEmployee) {
        this.mContext = mContext;
        this.arrClientAdminUserEmployee = arrClientAdminUserEmployee;
        ubuntuC = Helper.getUbuntuC(mContext);
        ubuntuL = Helper.getUbuntuL(mContext);
    }

    @Override
    public int getCount() {
        return arrClientAdminUserEmployee.size();
    }

    @Override
    public Object getItem(int position) {
        return arrClientAdminUserEmployee.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            //convertView = DataBindingUtil.inflate(mInflater, R.layout.line_item_list_item, null, false).getRoot();
            convertView = mInflater.inflate(R.layout.admin_employee_list_item, null);
            holder = new ViewHolder();
            holder.rl = (RelativeLayout) convertView.findViewById(R.id.rl);
            holder.profile_image = (CircleImageView) convertView.findViewById(R.id.profile_image);
            holder.status = (CircleImageView) convertView.findViewById(R.id.status);
            holder.txtEmployeeName = (TextView) convertView.findViewById(R.id.txtEmployeeName);
            holder.txtUserType = (TextView) convertView.findViewById(R.id.txtUserType);
            holder.txtPhone = (TextView) convertView.findViewById(R.id.txtPhone);
            holder.txtUnreadMessageCount = (TextView) convertView.findViewById(R.id.txtUnreadMessageCount);
            holder.txtEmployeeName.setTypeface(ubuntuC);
            holder.txtUserType.setTypeface(ubuntuL);
            holder.txtPhone.setTypeface(ubuntuL);
            holder.txtUnreadMessageCount.setTypeface(ubuntuL);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ClientAdminUserEmployee objClientAdminUserEmployee = arrClientAdminUserEmployee.get(position);
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, acMessageList.class);
                i.putExtra("friendAdminUserId", objClientAdminUserEmployee.getAuId());
                i.putExtra("name", objClientAdminUserEmployee.getFirst_name() + " " + objClientAdminUserEmployee.getLast_name());
                ((AppCompatActivity) mContext).startActivityForResult(i, ConstantVal.EXIT_REQUEST_CODE);
                if (holder.txtUnreadMessageCount.getVisibility() == View.VISIBLE) {
                    arrClientAdminUserEmployee.get(position).setUnreadMessageCount(0);
                    //    holder.txtUnreadMessageCount.setVisibility(View.GONE);
                }
            }
        });
        holder.txtEmployeeName.setText(objClientAdminUserEmployee.getFirst_name() + " " + objClientAdminUserEmployee.getLast_name());
        holder.txtUserType.setText(objClientAdminUserEmployee.getDesignation_name());
        if (objClientAdminUserEmployee.getContact_no().equals("")) {
            holder.txtPhone.setText("(" + mContext.getString(R.string.strNA) + ")");
        } else {
            holder.txtPhone.setText("(" + objClientAdminUserEmployee.getContact_no() + ")");
        }
        //Logger.debug(objClientAdminUserEmployee.getFirst_name()+" "+objClientAdminUserEmployee.getUnreadMessageCount());
        if (objClientAdminUserEmployee.getUnreadMessageCount() > 0) {
            holder.txtUnreadMessageCount.setVisibility(View.VISIBLE);
            holder.txtUnreadMessageCount.setText("" + objClientAdminUserEmployee.getUnreadMessageCount());
        } else {
            holder.txtUnreadMessageCount.setVisibility(View.GONE);
        }
        if (objClientAdminUserEmployee.getIsOnLine().equals("N")) {
            holder.status.setImageResource(R.color.darkgrey);
        } else if (objClientAdminUserEmployee.getIsOnLine().equals("Y")) {
            holder.status.setImageResource(R.color.green);
        }
        if (!objClientAdminUserEmployee.isImageLoaded()) {
            new asyncLoadCommonData(mContext).loadAdminUserEmployeePhotoById(holder.profile_image, objClientAdminUserEmployee, imgClick, R.drawable.ic_field_engineer_grey);
        }
        return convertView;
    }

    View.OnClickListener imgClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bitmap bmp = (Bitmap) v.getTag();
            Helper.openImageZoomDialog(mContext, bmp);
        }
    };
}
