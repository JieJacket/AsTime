package jacketjie.astimes.views.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jacketjie.astimes.R;
import jacketjie.astimes.model.Essay;
import jacketjie.astimes.adapter.EssayDetailListAdapter;
import jacketjie.astimes.custom.BaseActivity;
import jacketjie.astimes.model.EssayDetail;
import jacketjie.astimes.utils.HttpUtils;
import jacketjie.astimes.utils.StatusBarUtil;

/**
 * 美文详情
 * Created by Administrator on 2015/12/11.
 */
public class EssayDetailsActivity extends BaseActivity {

    private Toolbar toolbar;
    private ListView essayContent;
    private EssayDetailListAdapter essayDetailListAdapter;
    private List<EssayDetail> mDatas;
private SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.showStatusBar(this,R.color.colorPrimary);
        setContentView(R.layout.essay_detail_layout);
        showDialog();
        initViews();
    }

    private void initViews() {
        final Essay essay = getIntent().getParcelableExtra("ESSAY_DETAIL");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        essayContent = (ListView) findViewById(R.id.id_essay_content);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadDataTask().execute(getString(R.string.essay_type_list_address),essay.getEssayId());
            }
        });
        toolbar.setTitle(essay.getEssayName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mDatas = new ArrayList<EssayDetail>();
        essayDetailListAdapter = new EssayDetailListAdapter(this,mDatas,R.layout.main_second_item);
        essayContent.setAdapter(essayDetailListAdapter);
        new LoadDataTask().execute(getString(R.string.essay_type_list_address),essay.getEssayId());
    }

    /**
     * 请求数据
     */
    class LoadDataTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String id = params[1];
            url += id;
            String result = HttpUtils.doGet(url);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hiddenDialog();
            refreshLayout.setRefreshing(false);
            if (TextUtils.isEmpty(result))
                return;
            try {
                JSONObject jo = new JSONObject(result);
                int ret = jo.getInt("code");
                if (ret == 200){
//                    JSONArray ja = jo.getJSONArray("data");
                    JSONObject object = jo.getJSONObject("data");
                    List<EssayDetail> results = new ArrayList<EssayDetail>();
                    Iterator iterator = object.keys();
                    while (iterator.hasNext()){
                        EssayDetail essay = new EssayDetail();
                        String key = (String) iterator.next();
                        if (!TextUtils.isEmpty(key)){
                            JSONObject o = object.getJSONObject(key);
                            essay.setDetailUrl(TextUtils.isEmpty(o.getString("cover"))?"":o.getString("cover"));
                            essay.setDetailTitle(TextUtils.isEmpty(o.getString("title"))?"":o.getString("title"));
                            essay.setDetailType(TextUtils.isEmpty(o.getString("type"))?"":o.getString("type"));
                            essay.setDetailDate(TextUtils.isEmpty(o.getString("date"))?"":o.getString("date"));
                            essay.setDetailId(key);
                            results.add(essay);
                        }
                    }
                    mDatas.clear();
                    mDatas.addAll(results);
                    essayDetailListAdapter.notifyDataSetChanged();
//                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
