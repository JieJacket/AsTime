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
        if (displayView == null){
            displayView = (ViewGroup) inflater.inflate(R.layout.base_fragment_layout,null);
            dialogView = displayView.findViewById(R.id.id_base_progressbar);
        }else{
            ViewGroup parent = (ViewGroup) displayView.getParent();
            if (parent != null){
                parent.removeView(displayView);
            }
        }
        return displayView;
    }
    public void setContentView(int res){
        View v = getActivity().getLayoutInflater().inflate(res,null);
        if (v != null && displayView!=null) {
            displayView.addView(v,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            dialogView.bringToFront();
        }
    }
    /*public View onCreateView(int resId){
        FractionTranslateLayout ftl = new FractionTranslateLayout(getActivity());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ftl.setLayoutParams(lp);
        dialogView = getActivity().getLayoutInflater().inflate(R.layout.progressbar_layout,null);
        ftl.addView(dialogView);
        View content = getActivity().getLayoutInflater().inflate(resId,null);
        ftl.addView(content);
        return ftl;
    }*/
    public void showProgress(){
        if (dialogView != null)
            dialogView.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(){
        if (dialogView != null)
            dialogView.setVisibility(View.GONE);
    }

}
