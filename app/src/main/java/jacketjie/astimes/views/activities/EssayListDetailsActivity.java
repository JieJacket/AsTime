package jacketjie.astimes.views.activities;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import jacketjie.astimes.R;
import jacketjie.astimes.model.EssayDetail;
import jacketjie.astimes.utils.HttpUtils;
import jacketjie.astimes.utils.StatusBarUtil;

/**
 * 美文详情
 * Created by Administrator on 2015/12/11.
 */
public class EssayListDetailsActivity extends BaseActivity {

    private Toolbar toolbar;
    private WebView webView;
    private SwipeRefreshLayout refreshLayout;
    private String ESSAY_LIST_DETAIL_URL;
    private String IMAGE_LIST_DETAIL_URL;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.showStatusBar(this, R.color.colorPrimary);
        setContentView(R.layout.essay_list_detail_layout);
        showDialog();
        initViews();
    }

    private void initViews() {
        final EssayDetail essay = getIntent().getParcelableExtra("ESSAY_DETAIL");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        webView = (WebView) findViewById(R.id.id_essay_content);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadDataTask().execute(getString(R.string.essay_type_list_address), essay.getDetailId());
            }
        });

        webView.clearHistory();
        webView.clearCache(true);//清除Cache
        webView.getSettings().setAllowFileAccess(true);// 允许访问文件
        webView.getSettings().setSupportZoom(true);//支持缩放
        webView.getSettings().setBuiltInZoomControls(true); //支持缩放
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }//webview控件 设置适应屏
        //        webView.setInitialScale(150);//初始缩放比例100就是不变，100以下就是缩小
        webView.getSettings().setDisplayZoomControls(false);// 设置不显示缩放按钮
        if (essay!=null){
            toolbar.setTitle(essay.getDetailTitle());
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            type = essay.getType();
            switch (type){
                case 0:
                    ESSAY_LIST_DETAIL_URL = getString(R.string.as_times_address) + getString(R.string.article_list_detail_address);
                    new LoadDataTask().execute(ESSAY_LIST_DETAIL_URL, essay.getDetailId());
                    break;
                case 1:

                break;
                case 2:
                    IMAGE_LIST_DETAIL_URL = getString(R.string.as_times_address) + getString(R.string.image_list_detail_address);
                    new LoadDataTask().execute(IMAGE_LIST_DETAIL_URL, essay.getDetailId());

                    break;
            }
        }
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
                    String title = object.getString("title");
                    String content = object.getString("text");
                    String url = object.getString("cover");
                    toolbar.setTitle(title);
                    webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
//                    essayContent.loadUrl(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
