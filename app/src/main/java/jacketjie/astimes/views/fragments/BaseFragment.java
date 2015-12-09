package jacketjie.astimes.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jacketjie.astimes.R;

/**
 * Created by Administrator on 2015/12/9.
 */
public class BaseFragment extends Fragment {

    private ViewGroup displayView;
    private View dialogView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        displayView = (ViewGroup) inflater.inflate(R.layout.base_fragment_layout,container,false);
        dialogView = displayView.findViewById(R.id.id_base_progressbar);
        return displayView;
    }
    public void setContentView(int res){
        View v = getActivity().getLayoutInflater().inflate(res,null);
        if (v != null && displayView!=null) {
            displayView.addView(v);
            dialogView.bringToFront();
        }
    }
    public void showProgress(){
        if (dialogView != null)
            dialogView.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(){
        if (dialogView != null)
            dialogView.setVisibility(View.GONE);
    }

}
