package fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.getbase.floatingactionbutton.FloatingActionButton;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lab360io.jobio.inventoryapp.R;
import com.lab360io.jobio.inventoryapp.acSearchAssetByQR;
import com.xwray.fontbinding.FontCache;

import java.util.ArrayList;

import adapter.AssetAdapter;
import entity.ClientAsset;
import entity.ClientAssetOwner;
import utility.ConstantVal;
import utility.DataBase;
import utility.DotProgressBar;


/**
 * Created by SAI on 9/12/2016.
 */
public class frAssetsAsset extends Fragment {
    Fragment contextFragment;
    FragmentActivity frActivity;
    Context mContext;
    ListView lvlAsset;
    DotProgressBar dot_progress_bar;
    RelativeLayout lyNoContent, lyMainContent;
    ArrayList<ClientAsset> arrClientAsset = null;
    FloatingActionButton btnSearchAsset;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        contextFragment = this;
        frActivity = getActivity();
        FontCache.getInstance(mContext).addFont("Ubuntu", "Ubuntu-C.ttf");
        lyNoContent = (RelativeLayout) getActivity().findViewById(R.id.lyNoContent);
        View view = DataBindingUtil.inflate(inflater, R.layout.frasset_asset, null, true).getRoot();
        lyMainContent = (RelativeLayout) view.findViewById(R.id.lyMainContent);
        lvlAsset = (ListView) view.findViewById(R.id.lvlAsset);
        btnSearchAsset = (FloatingActionButton) view.findViewById(R.id.btnSearchAsset);
        dot_progress_bar = (DotProgressBar) view.findViewById(R.id.dot_progress_bar);
        mContext.registerReceiver(objAssetListBroadcast, new IntentFilter(ConstantVal.BroadcastAction.ASSET_LIST));
        setData();
        return view;
    }

    private void setData() {
        new AsyncTask() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (dot_progress_bar != null) {
                    dot_progress_bar.setVisibility(View.VISIBLE);
                }
                lvlAsset.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                            btnSearchAsset.setVisibility(View.GONE);
                        } else {
                            btnSearchAsset.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    }
                });

                btnSearchAsset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mContext, acSearchAssetByQR.class);
                        startActivity(i);
                    }
                });
            }

            @Override
            protected Object doInBackground(Object[] params) {
                arrClientAsset = ClientAsset.getDataFromDatabase(mContext, null);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (dot_progress_bar != null) {
                    try {
                        dot_progress_bar.setVisibility(View.GONE);
                        ((ViewGroup) dot_progress_bar.getParent()).removeView(dot_progress_bar);
                    }catch (Exception e){}
                }
                if (arrClientAsset != null) {
                    lyMainContent.setVisibility(View.VISIBLE);
                    lvlAsset.setAdapter(new AssetAdapter(mContext, arrClientAsset));
                } else {
                    lyNoContent.setVisibility(View.GONE);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(objAssetListBroadcast);
    }


    private BroadcastReceiver objAssetListBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };
}

