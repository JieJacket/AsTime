package jacketjie.astimes.views.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import jacketjie.astimes.R;
import jacketjie.astimes.utils.HttpUtils;
import jacketjie.astimes.utils.StatusBarUtil;

/**
 * 美文详情
 * Created by Administrator on 2015/12/11.
 */
public class InformalEssayDetailsActivity extends BaseActivity {

    private Toolbar toolbar;
    private WebView essayContent;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.showStatusBar(this, R.color.colorPrimary);
        setContentView(R.layout.essay_list_detail_layout);
        showDialog();
        initViews();
    }

    private void initViews() {
//        final EssayDetail essay = getIntent().getParcelableExtra("DETAILS");
        String title = getIntent().getStringExtra("DETAILS");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        essayContent = (WebView) findViewById(R.id.id_essay_content);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                new LoadDataTask().execute(getString(R.string.essay_type_list_address), essay.getDetailId());
            }
        });
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        new LoadDataTask().execute(getString(R.string.essay_type_list_detail_address), essay.getDetailId());
    }

    /**
     * 请求数据
     */
    class LoadDataTask extends AsyncTask<String, Void, String> {

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
                if (ret == 200) {
//                    JSONArray ja = jo.getJSONArray("data");
                    JSONObject object = jo.getJSONObject("data");
                    String url = object.getString("url");
                    essayContent.loadUrl(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
