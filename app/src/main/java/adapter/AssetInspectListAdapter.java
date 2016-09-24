package adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lab360io.jobio.inventoryapp.R;
import com.lab360io.jobio.inventoryapp.acInspectTransaction;
import com.thomsonreuters.rippledecoratorview.RippleDecoratorView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import entity.ClientAssetInspect;
import fragment.ListHeader;
import utility.Helper;
import utility.Logger;
import utility.ConstantVal;

/**
 * Created by SAI on 2/18/2016.
 */

public class AssetInspectListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<ListHeader> listDataHeader;
    private HashMap<String, List<ClientAssetInspect>> listDataChild;
    Helper objHelper = new Helper();
    Fragment contextFragment;
    Typeface ubuntuL, ubuntuC, ubuntuM;

    public AssetInspectListAdapter(Context context, Fragment contextFragment, List<ListHeader> listDataHeader,
                                   HashMap<String, List<ClientAssetInspect>> listChildData) {
        this.mContext = context;
        this.contextFragment = contextFragment;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
        ubuntuL = Helper.getUbuntuL(mContext);
        ubuntuC = Helper.getUbuntuC(mContext);
        ubuntuM = Helper.getUbuntuM(mContext);
    }

    @Override
    public int getGroupCount() {
        try {
            return this.listDataHeader.size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            return this.listDataChild.get(this.listDataHeader.get(groupPosition).getHeader())
                    .size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        try {
            return this.listDataHeader.get(groupPosition);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.debug("Error in getGroup:" + e.getMessage() + ":" + listDataHeader.size());
            return null;
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        try {
            return this.listDataChild.get(this.listDataHeader.get(groupPosition).getHeader())
                    .get(childPosition);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.debug("Error in getChild" + e.getMessage() + listDataChild.size());
            return null;
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private class ViewHolderHeader {
        TextView txtDate, txtCount;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderHeader holderHeader = null;
        ListHeader objListHeader = (ListHeader) getGroup(groupPosition);
        if (convertView == null) {
            holderHeader = new ViewHolderHeader();
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.service_inspect_header, null);
            //convertView = DataBindingUtil.inflate(infalInflater, R.layout.job_list_group_header, null, true).getRoot();
            holderHeader.txtDate = (TextView) convertView.findViewById(R.id.txtHeader);
            holderHeader.txtCount = (TextView) convertView.findViewById(R.id.txtNotificationCount);
            holderHeader.txtDate.setTypeface(ubuntuC);
            holderHeader.txtCount.setTypeface(ubuntuL);
            convertView.setTag(holderHeader);
        } else {
            holderHeader = (ViewHolderHeader) convertView.getTag();
        }

        /*TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.txtHeader);*/
        //String strDate = Helper.getTextFromDate(mContext, arr.get(0).getObjClientJobMaster().getScheduledStartDateTime());
        holderHeader.txtDate.setText(objListHeader.getHeader());
        if (objListHeader.getNewJobCount() > 0) {
            holderHeader.txtCount.setVisibility(View.VISIBLE);
            holderHeader.txtCount.setText("" + objListHeader.getNewJobCount());
        } else {
            holderHeader.txtCount.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolderChild {
        TextView txtStatus, txtAssetName, txtStatusSer;
        Button btnDone;
        RippleDecoratorView rplDone;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ClientAssetInspect objClientAssetInspect = (ClientAssetInspect) getChild(groupPosition, childPosition);
        ViewHolderChild holderChild;
        if (convertView == null) {
            holderChild = new ViewHolderChild();
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.inspect_list_item, null);
            holderChild.txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
            holderChild.txtAssetName = (TextView) convertView.findViewById(R.id.txtAssetName);
            holderChild.txtStatusSer = (TextView) convertView.findViewById(R.id.txtStatusSer);
            holderChild.rplDone = (RippleDecoratorView) convertView.findViewById(R.id.rplDone);
            holderChild.btnDone = (Button) convertView.findViewById(R.id.btnDone);
            holderChild.btnDone.setTypeface(ubuntuL);
            holderChild.txtAssetName.setTypeface(ubuntuM);
            holderChild.txtStatusSer.setTypeface(ubuntuC);
            holderChild.txtStatus.setTypeface(ubuntuL);
            convertView.setTag(holderChild);
        } else {
            holderChild = (ViewHolderChild) convertView.getTag();
        }
        if (objClientAssetInspect.getViewStatus() == ConstantVal.InspectionServiceStatus.NEW) {
            holderChild.txtStatus.setTextAppearance(mContext, R.style.styDescSmallRed);
        } else {
            holderChild.txtStatus.setTextAppearance(mContext, R.style.styDescSmallTile);
        }
        holderChild.txtStatus.setText(ConstantVal.InspectionServiceStatus.getStatusName(mContext, objClientAssetInspect.getViewStatus()));
        holderChild.txtStatus.setSelected(true);
        try {
            Logger.debug("getAitStatusId: " + objClientAssetInspect.getAitStatusId());
            holderChild.txtStatusSer.setText(ConstantVal.assetServiceInspectionStatus.getStatusName(mContext, Integer.parseInt(objClientAssetInspect.getAitStatusId())));
            if (Integer.parseInt(objClientAssetInspect.getAitStatusId()) != (ConstantVal.assetServiceInspectionStatus.NOT_ATTENDED_YET)) {
                holderChild.rplDone.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holderChild.txtAssetName.setText(objClientAssetInspect.getAmAsset_name());
        holderChild.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, acInspectTransaction.class);
                i.putExtra("aitId", objClientAssetInspect.getAitId());
                contextFragment.startActivityForResult(i, ConstantVal.INSPECTION_TRANSACTION_REQUEST_CODE);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
