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
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lab360io.jobio.inventoryapp.R;
import com.xwray.fontbinding.FontCache;

import utility.DotProgressBar;


/**
 * Created by SAI on 9/12/2016.
 */
public class frAssetsService extends Fragment {
    Fragment contextFragment;
    FragmentActivity frActivity;
    Context mContext;
    ListView lvlService;
    DotProgressBar dot_progress_bar;
    RelativeLayout lyNoContent, lyMainContent;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        contextFragment = this;
        frActivity = getActivity();
        FontCache.getInstance(mContext).addFont("Ubuntu", "Ubuntu-C.ttf");
        View view = DataBindingUtil.inflate(inflater, R.layout.frasset_asset, null, true).getRoot();
        lyMainContent = (RelativeLayout) view.findViewById(R.id.lyMainContent);
        lyNoContent = (RelativeLayout) view.findViewById(R.id.lyNoContent);
        lvlService = (ListView) view.findViewById(R.id.lvlService);
        dot_progress_bar = (DotProgressBar) view.findViewById(R.id.dot_progress_bar);
        return view;
    }
}

