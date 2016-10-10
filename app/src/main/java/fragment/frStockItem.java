package fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.lab360io.jobio.inventoryapp.R;
import com.lab360io.jobio.inventoryapp.acSearchItemByBarcode;
import com.xwray.fontbinding.FontCache;

import java.util.ArrayList;

import entity.ClientAsset;
import utility.ConstantVal;
import utility.DotProgressBar;

/**
 * Created by SAI on 10/5/2016.
 */
public class frStockItem extends Fragment implements View.OnClickListener {
    Fragment contextFragment;
    FragmentActivity frActivity;
    Context mContext;
    ListView lvlAsset;
    DotProgressBar dot_progress_bar;
    RelativeLayout lyNoContent, lyMainContent;
    ArrayList<ClientAsset> arrClientItem = null;
    FloatingActionButton btnSearchItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        contextFragment = this;
        frActivity = getActivity();
        FontCache.getInstance(mContext).addFont("Ubuntu", "Ubuntu-C.ttf");
        View view = DataBindingUtil.inflate(inflater, R.layout.frstock_item, null, true).getRoot();
        lyMainContent = (RelativeLayout) view.findViewById(R.id.lyMainContent);
        lyNoContent = (RelativeLayout) view.findViewById(R.id.lyNoContent);
        lvlAsset = (ListView) view.findViewById(R.id.lvlAsset);
        btnSearchItem = (FloatingActionButton) view.findViewById(R.id.btnSearchItem);
        btnSearchItem.setOnClickListener(this);
        dot_progress_bar = (DotProgressBar) view.findViewById(R.id.dot_progress_bar);
        setData();
        return view;
    }

    private void setData() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {

                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearchItem:
                Intent i = new Intent(getActivity(), acSearchItemByBarcode.class);
                startActivityForResult(i, ConstantVal.SEARCH_ITEM_BY_BARCODE_REQUEST_CODE);
                break;
        }
    }
}
