package fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lab360io.jobio.officeApp.R;
import com.lab360io.jobio.officeApp.acEmpCustomerSupplierSelection;
import com.xwray.fontbinding.FontCache;

import utility.DotProgressBar;
import utility.Helper;

/**
 * Created by SAI on 3/9/2017.
 */
public class frAllUsers extends Fragment {
    Helper objHelper = new Helper();
    Fragment contextFragment;
    FragmentActivity frActivity;
    Context mContext;
    ListView lvlUser;
    RelativeLayout lyNoContent;
    LinearLayout lyMainContent;
    String userType;
    DotProgressBar dot_progress_bar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            userType = bundle.getString(acEmpCustomerSupplierSelection.USER_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        contextFragment = this;
        frActivity = getActivity();
        FontCache.getInstance(mContext).addFont("Ubuntu", "Ubuntu-C.ttf");
        View view = DataBindingUtil.inflate(inflater, R.layout.fr_user, null, true).getRoot();
        dot_progress_bar = (DotProgressBar) view.findViewById(R.id.dot_progress_bar);
        lyMainContent = (LinearLayout) view.findViewById(R.id.lyMainContent);
        lyNoContent = (RelativeLayout) view.findViewById(R.id.lyNoContent);
        lvlUser = (ListView) view.findViewById(R.id.lvlUser);
        TextView txt = (TextView) view.findViewById(R.id.txtTest);
        txt.setText(userType);
        //mContext.registerReceiver(objAssetListBroadcast, new IntentFilter(ConstantVal.BroadcastAction.ASSET_LIST));
        return view;
    }
}
