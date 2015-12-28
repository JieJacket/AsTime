package jacketjie.astimes.views.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jacketjie.astimes.R;
import jacketjie.astimes.adapter.EssayDetailListAdapter;
import jacketjie.astimes.model.Essay;
import jacketjie.astimes.model.EssayDetail;
import jacketjie.astimes.utils.HttpUtils;
import jacketjie.astimes.utils.StatusBarUtil;

/**
 * 美文详情
 * Created by Administrator on 2015/12/11.
 */
public class EssayDetailsActivity extends BaseActivity {

    private Toolbar toolbar;
    private ListView essayContentList;
    private EssayDetailListAdapter essayDetailListAdapter;
    private List<EssayDetail> mDatas;
    private SwipeRefreshLayout refreshLayout;
    private LoadDataTask loadDataTask;
    private String ESSAY_DEAIL_URL;
    private String IMAGE_DEAIL_URL;
    private int pager = 1;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.showStatusBar(this, R.color.colorPrimary);
        setContentView(R.layout.essay_detail_layout);
        showDialog();
        initViews();
        setEventListener();
    }

    private void setEventListener() {
        essayContentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EssayDetail essayDetail = mDatas.get(position);
                Intent intent = new Intent(EssayDetailsActivity.this, EssayListDetailsActivity.class);
                intent.putExtra("ESSAY_DETAIL", essayDetail);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
    }

    private void initViews() {
        final Essay essay = getIntent().getParcelableExtra("ESSAY_DETAIL");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        essayContentList = (ListView) findViewById(R.id.id_essay_content);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadDataTask().execute(getString(R.string.essay_type_list_address), essay.getEssayId());
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
        essayDetailListAdapter = new EssayDetailListAdapter(this, mDatas, R.layout.main_second_item);
        essayContentList.setAdapter(essayDetailListAdapter);
        type = essay.getType();
    switch (type){
        case 0:
            ESSAY_DEAIL_URL = getString(R.string.as_times_address) + getString(R.string.article_list_address);
            loadDataTask = new LoadDataTask();
            loadDataTask.execute(ESSAY_DEAIL_URL, essay.getEssayId());
            break;
        case 1:
            break;
        case 2:
            IMAGE_DEAIL_URL = getString(R.string.as_times_address) + getString(R.string.image_list_address);
            loadDataTask = new LoadDataTask();
            loadDataTask.execute(IMAGE_DEAIL_URL, essay.getEssayId());
            break;
    }
    }

    /**
     * 请求数据
     */
    class LoadDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuffer sb = new StringBuffer();
            String url = params[0];
            String id = params[1];
            sb.append(url).append(id).append("/").append(pager);
            String result = HttpUtils.doGet(sb.toString());
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
                if (ret == 200) {
                    JSONArray array = jo.getJSONArray("data");
                    String categroyType = jo.getString("type");
                    List<EssayDetail> results = new ArrayList<EssayDetail>();
                    if (array != null){
                        for (int i = 0;i < array.length();i++){
                            EssayDetail essaessayDetail = new EssayDetail();
                            JSONObject o = array.getJSONObject(i);
                            essaessayDetail.setDetailUrl(TextUtils.isEmpty(o.getString("cover")) ? "" : o.getString("cover"));
                            essaessayDetail.setDetailTitle(TextUtils.isEmpty(o.getString("title")) ? "" : o.getString("title"));
                            essaessayDetail.setDetailType(TextUtils.isEmpty(categroyType) ? "" : categroyType);
                            essaessayDetail.setDetailDate(TextUtils.isEmpty(o.getString("ctime")) ? "" : o.getString("ctime"));
                            essaessayDetail.setDetailId(TextUtils.isEmpty(o.getString("id")) ? "" : o.getString("id"));
                            essaessayDetail.setType(type);
                            results.add(essaessayDetail);
                        }
                    }
//                    JSONArray ja = jo.getJSONArray("data");
//                    JSONObject object = jo.getJSONObject("data");
//                    List<EssayDetail> results = new ArrayList<EssayDetail>();
//                    Iterator iterator = object.keys();
//                    while (iterator.hasNext()) {
//                        EssayDetail essay = new EssayDetail();
//                        String key = (String) iterator.next();
//                        if (!TextUtils.isEmpty(key)) {
//                            JSONObject o = object.getJSONObject(key);
//                            essay.setDetailUrl(TextUtils.isEmpty(o.getString("cover")) ? "" : o.getString("cover"));
//                            essay.setDetailTitle(TextUtils.isEmpty(o.getString("title")) ? "" : o.getString("title"));
//                            essay.setDetailType(TextUtils.isEmpty(o.getString("type")) ? "" : o.getString("type"));
//                            essay.setDetailDate(TextUtils.isEmpty(o.getString("ctime")) ? "" : o.getString("ctime"));
//                            essay.setDetailId(key);
//                            results.add(essay);
//                        }
//                    }
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
