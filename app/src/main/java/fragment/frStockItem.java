package fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.lab360io.jobio.officeApp.R;
import com.lab360io.jobio.officeApp.acSearchItemByBarcodeScanner;
import com.xwray.fontbinding.FontCache;

import java.util.ArrayList;

import adapter.AddedItemAdapter;
import entity.ClientItemMaster;
import permission.CameraPermission;
import utility.ConstantVal;
import utility.DotProgressBar;
import utility.Helper;

/**
 * Created by SAI on 10/5/2016.
 */
public class frStockItem extends Fragment implements View.OnClickListener {
    Helper objHelper = new Helper();
    ArrayList<ClientItemMaster> arrClientItemMaster;
    Fragment contextFragment;
    FragmentActivity frActivity;
    Context mContext;
    ListView lvlItem;
    DotProgressBar dot_progress_bar;
    RelativeLayout lyNoContent, lyMainContent;
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
        lvlItem = (ListView) view.findViewById(R.id.lvlItem);
        btnSearchItem = (FloatingActionButton) view.findViewById(R.id.btnSearchItem);
        btnSearchItem.setOnClickListener(this);
        dot_progress_bar = (DotProgressBar) view.findViewById(R.id.dot_progress_bar);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        new AsyncTask() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                arrClientItemMaster = ClientItemMaster.getDataFromDatabase(mContext, null, "");
            }

            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (arrClientItemMaster != null && arrClientItemMaster.size() >= 0) {
                    lyMainContent.setVisibility(View.VISIBLE);
                    lyNoContent.setVisibility(View.GONE);
                    AddedItemAdapter adp = new AddedItemAdapter(mContext, arrClientItemMaster);
                    lvlItem.setAdapter(adp);
                } else {
                    lyMainContent.setVisibility(View.GONE);
                    lyNoContent.setVisibility(View.VISIBLE);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearchItem:
                CameraPermission objCameraPermission = new CameraPermission((AppCompatActivity) getActivity());
                if (objCameraPermission.isHavePermission()) {
                    Intent i = new Intent(getActivity(), acSearchItemByBarcodeScanner.class);
                    startActivityForResult(i, ConstantVal.EXIT_REQUEST_CODE);
                } else {
                    objCameraPermission.askForPermission();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ConstantVal.EXIT_RESPONSE_CODE) {
            getActivity().setResult(ConstantVal.EXIT_RESPONSE_CODE);
            getActivity().finish();
        }
    }

    /*@Override
    public void onStart() {
        super.onStart();
        objHelper.registerSessionTimeoutBroadcast((AppCompatActivity)getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        objHelper.unRegisterSesionTimeOutBroadcast((AppCompatActivity) getActivity());
    }*/
}
