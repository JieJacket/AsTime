package jacketjie.astimes.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jacketjie.astimes.R;

/**
 * Created by Administrator on 2015/12/9.
 */
public class MainForthFragment extends BaseFragment {
    private View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v == null){
            v = super.onCreateView(inflater,container,savedInstanceState);
            setContentView(R.layout.main_forth_fragment);
        }else {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null){
                parent.removeView(v);
            }
        }
        return  v;
    }
}
