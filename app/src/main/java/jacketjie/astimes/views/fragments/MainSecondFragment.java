package jacketjie.astimes.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import jacketjie.astimes.R;
import jacketjie.astimes.views.activities.TextBackActivity;

/**
 * Created by Administrator on 2015/12/9.
 */
public class MainSecondFragment extends Fragment {
    private View displayView;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (displayView == null){
            displayView = inflater.inflate(R.layout.main_second_fragment,container,false);
            initViews(displayView);
        }else {
            ViewGroup parent = (ViewGroup) displayView.getParent();
            if (parent != null){
                parent.removeView(displayView);
            }
        }
        return displayView;
    }

    private void initViews(View v) {
        String[]mDatas = new String[20];
        for (int i=0;i<20;i++){
            mDatas[i] = "测试"+i;
        }
        listView = (ListView) v.findViewById(R.id.id_list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,mDatas);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),TextBackActivity.class);
                startActivity(intent);
            }
        });
    }
}
