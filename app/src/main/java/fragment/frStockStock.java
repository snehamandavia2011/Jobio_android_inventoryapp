package fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.lab360io.jobio.inventoryapp.R;
import com.xwray.fontbinding.FontCache;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import adapter.AssetAdapter;
import adapter.DownloadedItemAdapter;
import entity.BusinessAccountdbDetail;
import entity.ClientAsset;
import entity.ClientEmployeeMaster;
import entity.ClientItemMaster1;
import parser.parseAssetIOwn;
import parser.parseItemMaster;
import utility.ConstantVal;
import utility.DotProgressBar;
import utility.Helper;
import utility.HttpEngine;
import utility.Logger;
import utility.ServerResponse;
import utility.URLMapping;

/**
 * Created by SAI on 10/5/2016.
 */
public class frStockStock extends Fragment {
    DotProgressBar dot_progress_bar;
    RelativeLayout lyNoInternet, lyNoContent, lyMainContent;
    ListView lvlStock;
    boolean isNetworkAvail;
    Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        isNetworkAvail = new HttpEngine().isNetworkAvailable(mContext);
        FontCache.getInstance(mContext).addFont("Ubuntu", "Ubuntu-C.ttf");
        View view = DataBindingUtil.inflate(inflater, R.layout.frstock_stock, null, true).getRoot();
        lyMainContent = (RelativeLayout) view.findViewById(R.id.lyMainContent);
        lyNoContent = (RelativeLayout) view.findViewById(R.id.lyNoContent);
        lyNoInternet = (RelativeLayout) view.findViewById(R.id.lyNoInternet);
        dot_progress_bar = (DotProgressBar) view.findViewById(R.id.dot_progress_bar);
        lvlStock = (ListView) view.findViewById(R.id.lvlStock);
        getData();
        return view;
    }

    private void getData() {
        new AsyncTask() {
            ArrayList<ClientItemMaster1> arrClientItemMaster1 = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dot_progress_bar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Object doInBackground(Object[] params) {
                if (isNetworkAvail)
                    arrClientItemMaster1 = getItemList();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (isNetworkAvail) {
                    if (arrClientItemMaster1 != null && arrClientItemMaster1.size() > 0) {
                        Collections.sort(arrClientItemMaster1, new Comparator<ClientItemMaster1>() {
                            @Override
                            public int compare(ClientItemMaster1 lhs, ClientItemMaster1 rhs) {
                                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                                int lQ, rQ;
                                try {

                                    lQ = Integer.parseInt(lhs.getMin_qty_for_restock()) - Integer.parseInt(lhs.getAvailable_qty());
                                    rQ = Integer.parseInt(rhs.getMin_qty_for_restock()) - Integer.parseInt(rhs.getAvailable_qty());
                                } catch (Exception e) {
                                    lQ = 0;
                                    rQ = 0;
                                }
                                return lQ > rQ ? -1 : (lQ < rQ) ? 1 : 0;
                            }
                        });
                        lvlStock.setAdapter(new DownloadedItemAdapter(mContext, arrClientItemMaster1));
                        lyNoInternet.setVisibility(View.GONE);
                        lyMainContent.setVisibility(View.VISIBLE);
                        lyNoContent.setVisibility(View.GONE);
                    } else {
                        lyNoInternet.setVisibility(View.GONE);
                        lyMainContent.setVisibility(View.GONE);
                        lyNoContent.setVisibility(View.VISIBLE);
                    }
                } else {
                    lyNoInternet.setVisibility(View.VISIBLE);
                    lyMainContent.setVisibility(View.GONE);
                    lyNoContent.setVisibility(View.GONE);
                }
                dot_progress_bar.setVisibility(View.GONE);
                ((ViewGroup) dot_progress_bar.getParent()).removeView(dot_progress_bar);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ConstantVal.EXIT_RESPONSE_CODE) {
            getActivity().setResult(ConstantVal.EXIT_RESPONSE_CODE);
            getActivity().finish();
        }
    }

    private ArrayList<ClientItemMaster1> getItemList() {
        HttpEngine objHttpEngine = new HttpEngine();
        ArrayList<ClientItemMaster1> arrClientItemMaster = null;
        final String tokenId = Helper.getStringPreference(mContext, ConstantVal.TOKEN, "");
        String account_id = Helper.getStringPreference(mContext, BusinessAccountdbDetail.Fields.ACCOUNT_ID, "");
        URLMapping um = ConstantVal.getItemTransactionList(mContext);
        ServerResponse objServerRespose = objHttpEngine.getDataFromWebAPI(mContext, um.getUrl(),
                new String[]{tokenId, account_id}, um.getParamNames(), um.isNeedToSync());
        String result = objServerRespose.getResponseString();
        String responseCode = objServerRespose.getResponseCode();
        if (result != null && !result.equals("") && responseCode == ConstantVal.ServerResponseCode.SUCCESS) {
            arrClientItemMaster = ClientItemMaster1.parseList(result);
        }
        return arrClientItemMaster;
    }
}
