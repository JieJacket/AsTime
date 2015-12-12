package jacketjie.astimes.views.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import jacketjie.astimes.R;
import jacketjie.astimes.views.activities.RecordsDetailsActivity;
import jacketjie.astimes.views.activities.TextBackActivity;

/**
 * Created by Administrator on 2015/12/9.
 */
public class MainSecondFragment extends BaseFragment {
    private View displayView;
    private ListView listView;
    private SwipeRefreshLayout refreshLayout;
    private FloatingActionButton fab;
    private ImageView backImage;
    private TextView topTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (displayView == null){
            displayView =inflater.inflate(R.layout.main_second_fragment,container,false);
            initViews(displayView);
            initDatas();
            setEventListener();
            showDialog(displayView);
            handler.sendEmptyMessageDelayed(0x123,3000);
        }else {
            ViewGroup parent = (ViewGroup) displayView.getParent();
            if (parent != null){
                parent.removeView(displayView);
            }
        }
        return displayView;
    }

    private void initViews(View v) {
        listView = (ListView) v.findViewById(R.id.id_list_view);
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.id_refresh_layout);
        backImage = (ImageView) v.findViewById(R.id.action_back);
        topTitle = (TextView) v.findViewById(R.id.action_title);
        fab = (FloatingActionButton) v.findViewById(R.id.id_fab);
        refreshLayout.setColorSchemeColors(R.color.colorPrimary);

    }

    private void initDatas() {
        topTitle.setText(getResources().getStringArray(R.array.main_tabs_name)[1]);
    }

    private void setEventListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),RecordsDetailsActivity.class);
                startActivity(intent);
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                        handler.sendEmptyMessageDelayed(0x456,3000);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(fab,"Test",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x123:
                    String[]mDatas = new String[20];
                    for (int i=0;i<20;i++){
                        mDatas[i] = "测试"+i;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,mDatas);
                    listView.setAdapter(adapter);
                    hiddenDialog(displayView);
                    break;
                case 0x456:
                    refreshLayout.setRefreshing(false);
                    break;
            }
        }
    };

    public void showDialog(View v){
        View dialogView = v.findViewById(R.id.id_base_progressbar);
        if (dialogView != null){
            dialogView.setVisibility(View.VISIBLE);
            dialogView.bringToFront();
        }
    }

    public void hiddenDialog(View v){
        View dialogView = v.findViewById(R.id.id_base_progressbar);
        if (dialogView != null)
            dialogView.setVisibility(View.GONE);
    }
}
