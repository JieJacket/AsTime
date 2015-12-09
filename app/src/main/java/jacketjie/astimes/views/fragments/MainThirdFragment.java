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
public class MainThirdFragment extends Fragment {
    private View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.main_third_fragment,container,false);
        if (v == null){
            v = inflater.inflate(R.layout.main_forth_fragment,container,false);
        }else {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null){
                parent.removeView(v);
            }
        }
        return v;
    }
}
