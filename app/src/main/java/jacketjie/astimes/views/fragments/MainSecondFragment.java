package jacketjie.astimes.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jacketjie.astimes.R;
import jacketjie.astimes.adapter.MainSecondListAdapter;
import jacketjie.astimes.custom.AutoLoadMoreListView;
import jacketjie.astimes.model.EssayDetail;
import jacketjie.astimes.utils.HttpUtils;
import jacketjie.astimes.utils.interfaces.OnLoadMoreListener;
import jacketjie.astimes.views.activities.InformalEssayActivity;
import jacketjie.astimes.views.activities.InformalEssayDetailsActivity;
import jacketjie.astimes.views.activities.RecordsDetailsActivity;

/**
 * Created by Administrator on 2015/12/9.
 */
public class MainSecondFragment extends BaseFragment {
    private View displayView;
    private AutoLoadMoreListView listView;
    private SwipeRefreshLayout refreshLayout;
    private FloatingActionButton fab;
    private ImageView backImage;
    private TextView topTitle;
    private List<EssayDetail> mDatas;
    private MainSecondListAdapter secondListAdapter;

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
//            new LoadMoreDataTask().execute("");
            new LoadDataTask(false).execute(getString(R.string.essay_type_list_address));
        }else {
            ViewGroup parent = (ViewGroup) displayView.getParent();
            if (parent != null){
                parent.removeView(displayView);
            }
        }
        return displayView;
    }

    private void initViews(View v) {
        listView = (AutoLoadMoreListView) v.findViewById(R.id.id_list_view);
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.id_refresh_layout);
        backImage = (ImageView) v.findViewById(R.id.action_back);
        topTitle = (TextView) v.findViewById(R.id.action_title);
        fab = (FloatingActionButton) v.findViewById(R.id.id_fab);
        refreshLayout.setColorSchemeColors(R.color.colorPrimary);

    }

    private void initDatas() {
        mDatas = new ArrayList<EssayDetail>();
        secondListAdapter = new MainSecondListAdapter(getActivity(), mDatas, R.layout.main_second_item);
        listView.setAdapter(secondListAdapter);
    }

    private void setEventListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),RecordsDetailsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.sendEmptyMessageDelayed(0x456, 3000);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(fab,"Test",Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), InformalEssayActivity.class);
                startActivity(intent);
            }

        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), InformalEssayDetailsActivity.class);
                EssayDetail essayDetail = mDatas.get(position);
                intent.putExtra("DETAILS", essayDetail.getDetailTitle());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });

        listView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMoreListener() {
                new LoadDataTask(true).execute(getString(R.string.essay_type_list_address));
            }
        });
    }


    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x123:
//                    for (int i=0;i< 20;i++){
//                        mDatas.add( "测试"+i);
//                    }
//                    adapter.notifyDataSetChanged();

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


    /**
     * 请求数据
     */
    class LoadDataTask extends AsyncTask<String, Void, String> {

        private boolean isLoadMore;

        public LoadDataTask(boolean isLoadMore) {
            this.isLoadMore = isLoadMore;
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String id = "1";
            url += id;
            String result = HttpUtils.doGet(url);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hiddenDialog(displayView);
            if (TextUtils.isEmpty(result)) {
                return;
            }
            try {
                JSONObject jo = new JSONObject(result);
                int ret = jo.getInt("code");
                if (ret == 200) {
//                    JSONArray ja = jo.getJSONArray("data");
                    JSONObject object = jo.getJSONObject("data");
                    List<EssayDetail> results = new ArrayList<EssayDetail>();
                    Iterator iterator = object.keys();
                    while (iterator.hasNext()) {
                        EssayDetail essay = new EssayDetail();
                        String key = (String) iterator.next();
                        if (!TextUtils.isEmpty(key)) {
                            JSONObject o = object.getJSONObject(key);
                            essay.setDetailUrl(TextUtils.isEmpty(o.getString("cover")) ? "" : o.getString("cover"));
                            essay.setDetailTitle(TextUtils.isEmpty(o.getString("title")) ? "" : o.getString("title"));
                            essay.setDetailType(TextUtils.isEmpty(o.getString("type")) ? "" : o.getString("type"));
                            essay.setDetailDate(TextUtils.isEmpty(o.getString("date")) ? "" : o.getString("date"));
                            essay.setDetailId(key);
                            results.add(essay);
                        }
                    }
                    if (isLoadMore){
                        refreshLayout.setRefreshing(false);
                    }else{
                        mDatas.clear();
                    }
                    mDatas.addAll(results);
                    secondListAdapter.notifyDataSetChanged();
//                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
