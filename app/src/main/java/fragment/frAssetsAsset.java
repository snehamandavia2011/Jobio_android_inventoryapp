package fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lab360io.jobio.inventoryapp.R;
import com.xwray.fontbinding.FontCache;

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
        dot_progress_bar = (DotProgressBar) view.findViewById(R.id.dot_progress_bar);
        return view;
    }
}

