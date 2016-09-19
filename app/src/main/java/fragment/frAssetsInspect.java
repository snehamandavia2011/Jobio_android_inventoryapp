package fragment;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lab360io.jobio.inventoryapp.R;
import com.lab360io.jobio.inventoryapp.acSearchAssetByQR;
import com.xwray.fontbinding.FontCache;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import adapter.AssetAdapter;
import adapter.AssetInspectListAdapter;
import asyncmanager.asyncAsset;
import entity.ClientAsset;
import entity.ClientAssetInspect;
import utility.ConstantVal;
import utility.DataBase;
import utility.DotProgressBar;
import utility.Helper;
import utility.Logger;


/**
 * Created by SAI on 9/12/2016.
 */
public class frAssetsInspect extends Fragment {
    AssetInspectListAdapter objAdapter;
    Handler handler = new Handler();
    int intTodayDateHeaderPosition, intFirstTodayorFutureDatePositionofChild, intFirstTodayorFutureDatePositionofGroup;
    boolean isFutureDatePositionFound = false;
    Fragment contextFragment;
    FragmentActivity frActivity;
    Context mContext;
    ExpandableListView lvlInspect;
    DotProgressBar dot_progress_bar;
    RelativeLayout lyNoContent, lyMainContent;
    ArrayList<ClientAssetInspect> arrClientAssetInspectInspect = null;
    ArrayList<ListHeader> listDataHeader;
    HashMap<String, List<ClientAssetInspect>> listDataChild;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        contextFragment = this;
        frActivity = getActivity();
        FontCache.getInstance(mContext).addFont("Ubuntu", "Ubuntu-C.ttf");
        View view = DataBindingUtil.inflate(inflater, R.layout.frasset_inspect, null, true).getRoot();
        lyMainContent = (RelativeLayout) view.findViewById(R.id.lyMainContent);
        lyNoContent = (RelativeLayout) view.findViewById(R.id.lyNoContent);
        lvlInspect = (ExpandableListView) view.findViewById(R.id.lvlInspect);
        dot_progress_bar = (DotProgressBar) view.findViewById(R.id.dot_progress_bar);
        mContext.registerReceiver(objInspectListBroadcast, new IntentFilter(ConstantVal.BroadcastAction.INSPECT_LIST));
        setData();
        return view;
    }

    private void setData() {
        setDataFromLocalDatabase();
        fetchDataFromServer();
    }

    private void fetchDataFromServer() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                new asyncAsset(mContext).getInspect();
                return null;
            }
        }.execute(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setDataFromLocalDatabase() {
        new AsyncTask() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (dot_progress_bar != null) {
                    dot_progress_bar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected Object doInBackground(Object[] params) {
                arrClientAssetInspectInspect = ClientAssetInspect.getDataFromDatabase(mContext);
                if (arrClientAssetInspectInspect != null && arrClientAssetInspectInspect.size() > 0)
                    prepareListDataAsPerExpandableListView();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (dot_progress_bar != null) {
                    try {
                        dot_progress_bar.setVisibility(View.GONE);
                        ((ViewGroup) dot_progress_bar.getParent()).removeView(dot_progress_bar);
                    } catch (Exception e) {
                    }
                }
                if (arrClientAssetInspectInspect != null && arrClientAssetInspectInspect.size() > 0) {
                    lyMainContent.setVisibility(View.VISIBLE);
                    lyNoContent.setVisibility(View.GONE);
                } else {
                    lyMainContent.setVisibility(View.GONE);
                    lyNoContent.setVisibility(View.VISIBLE);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void fillDataToExpandableListView(ArrayList<ListHeader> tempListDataHeader, HashMap<String, List<ClientAssetInspect>> tempListDataChild) {
        if (tempListDataHeader != null)
            this.listDataHeader = tempListDataHeader;
        if (tempListDataChild != null)
            this.listDataChild = tempListDataChild;
        handler.post(new Runnable() {
            @Override
            public void run() {
                lvlInspect.invalidate();
                objAdapter = new AssetInspectListAdapter(mContext, contextFragment, listDataHeader, listDataChild);
                lvlInspect.setAdapter(objAdapter);
                expandGroup();
            }
        });
    }

    private void expandGroup() {
        if (lvlInspect.getAdapter() != null) {
            int count = objAdapter.getGroupCount();
            for (int i = 0; i < count; i++) {
                lvlInspect.expandGroup(i);
            }
            lvlInspect.setSelectedGroup(intFirstTodayorFutureDatePositionofGroup);
        }
    }

    private void prepareListDataAsPerExpandableListView() {

        try {
            ArrayList<ListHeader> tempListDataHeader = new ArrayList<ListHeader>();
            HashMap<String, List<ClientAssetInspect>> tempListDataChild = new HashMap<String, List<ClientAssetInspect>>();
            int headerCount = 0;
            int mainCount = 0;
            ArrayList<ClientAssetInspect> chileData = null;
            Date dt1 = null;
            int newJobCount = 0;
            for (; mainCount < arrClientAssetInspectInspect.size(); ) {
                dt1 = arrClientAssetInspectInspect.get(mainCount).getAmNext_inspection_date();
                newJobCount = 0;
                chileData = new ArrayList<ClientAssetInspect>();
                for (; mainCount < arrClientAssetInspectInspect.size(); mainCount++) {
                    Date dt2 = arrClientAssetInspectInspect.get(mainCount).getAmNext_inspection_date();
                    Date dtTodayDate = new Date();
                    if (!isFutureDatePositionFound) {
                        if (dt2.getTime() >= dtTodayDate.getTime()) {
                            intFirstTodayorFutureDatePositionofChild++;
                            intFirstTodayorFutureDatePositionofGroup = headerCount;
                            isFutureDatePositionFound = true;
                        } else {
                            intFirstTodayorFutureDatePositionofChild++;
                        }
                    }
                    //Logger.debug("dt1:" + dt1.toString() + "        dt2:" + dt2.toString());
                    //Logger.debug("D1 d2 day month year" + dt1.getDate() + " " + dt2.getDate() + " " + dt1.getMonth() + " " + dt2.getMonth() + " " + dt1.getYear() + " " + dt2.getYear());
                    if ((dt1.getDate() == dt2.getDate()) && (dt1.getMonth() == dt2.getMonth()) && (dt1.getYear() == dt2.getYear())) {
                        if (arrClientAssetInspectInspect.get(mainCount).getViewStatus() == ConstantVal.InspectionServiceStatus.NEW)
                            newJobCount++;
                        chileData.add(arrClientAssetInspectInspect.get(mainCount));
                    } else {
                        //if (!isHeaderAdded) {
                        String strDate = Helper.getTextFromDate(mContext, dt1);
                        if (strDate.equals(mContext.getString(R.string.strToday))) {
                            intTodayDateHeaderPosition = headerCount;
                        }
                        tempListDataHeader.add(new ListHeader(strDate, newJobCount));
                        tempListDataChild.put(tempListDataHeader.get(headerCount).getHeader(), chileData);
                        headerCount++;
                        break;
                    }
                }
            }
            mainCount--;
            String strDate = Helper.getTextFromDate(mContext, dt1);
            if (strDate.equals(mContext.getString(R.string.strToday))) {
                intTodayDateHeaderPosition = headerCount;
            }
            tempListDataHeader.add(new ListHeader(strDate, newJobCount));
            tempListDataChild.put(tempListDataHeader.get(headerCount).getHeader(), chileData);
            fillDataToExpandableListView(tempListDataHeader, tempListDataChild);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(objInspectListBroadcast);
    }


    private BroadcastReceiver objInspectListBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setDataFromLocalDatabase();
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        //onstop set jobid is true.
        DataBase db = new DataBase(mContext);
        db.open();
        ContentValues cv = new ContentValues();
        cv.put("localViewStatus", ConstantVal.InspectionServiceStatus.PENDING);
        db.update(DataBase.inspect_view_table, "localViewStatus=?", new String[]{String.valueOf(ConstantVal.InspectionServiceStatus.NEW)}, cv);
        db.close();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantVal.INSPECTION_TRANSACTION_REQUEST_CODE && resultCode == ConstantVal.INSPECTION_TRANSACTION_RESPONSE_CODE) {
            setDataFromLocalDatabase();
        }
    }
}

