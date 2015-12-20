package jacketjie.astimes.views.activities;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import jacketjie.astimes.AsTimeApp;
import jacketjie.astimes.R;
import jacketjie.astimes.greenDao.ATInformalEssay;
import jacketjie.astimes.greenDao.GreenDaoUtils;

/**
 * Created by Administrator on 2015/12/17.
 */
public class SimpleWebViewActivity extends BaseActivity {
    private WebView webView;
    private boolean hasPrised;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_webview_detail);
        initViews();
        initDatas();
    }

    private void initViews() {
        showDialog();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("我的随笔");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        webView = (WebView) findViewById(R.id.id_webview);

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
        //        webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);//WebView自适配
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_essay_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (AsTimeApp.getCurATUser() == null){
            Toast.makeText(this, R.string.please_login_first, Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
        int id = item.getItemId();
        switch (id){
            case R.id.action_essay_shared:

                break;
            case R.id.action_essay_comment:

                break;
            case R.id.action_essay_prised:
                if (hasPrised){
                    item.setIcon(R.drawable.ic_essay_prise);
                    hasPrised = false;
                }else {
                    item.setIcon(R.drawable.ic_essay_prised);
                    hasPrised = true;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initDatas() {
        showDialog();
        ATInformalEssay essay = (ATInformalEssay) getIntent().getSerializableExtra("ATINFORMALESSAY_DETAILS");
        new LoadDataTask().execute(essay.getATIEId());
        String imageUrl = essay.getATIEImageUrl();
        String title = essay.getATIETitle();
        String text = essay.getATIEText();


        StringBuffer html = new StringBuffer();
        html.append("<html>").append("<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>");
        html.append("<body>");
        if (!TextUtils.isEmpty(title)) {
            html.append("<h2>").append(Html.fromHtml(title)).append("</h2>").append("<br/>");
        }
        if (!TextUtils.isEmpty(imageUrl)) {
            html.append("<img src=").append("\"").append(imageUrl).append("\"").append("/>").append("<br/>");
        }
        if (!TextUtils.isEmpty(text)) {
            html.append("<p style=\"line-height:100%\">").append(Html.fromHtml(text)).append("</p>");
        }
        html.append("</body>").append("</html>");

    }

    /**
     * 加载数据
     */
    class LoadDataTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            ATInformalEssay essay =  GreenDaoUtils.getLastNotByATIEId(getApplicationContext(),id);
            String imageUrl = essay.getATIEImageUrl();
            String title = essay.getATIETitle();
            String text = essay.getATIEText();


            StringBuffer html = new StringBuffer();
            html.append("<html>").append("<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>");
            html.append("<body>");
            if (!TextUtils.isEmpty(title)) {
                html.append("<h2>").append(Html.fromHtml(title)).append("</h2>").append("<br/>");
            }
            if (!TextUtils.isEmpty(imageUrl)) {
                html.append("<img src=").append("\"").append(imageUrl).append("\"").append("/>").append("<br/>");
            }
            if (!TextUtils.isEmpty(text)) {
                html.append("<h4>").append(Html.fromHtml(text)).append("</h4>");
            }
            html.append("</body>").append("</html>");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return html.toString();
        }

        @Override
        protected void onPostExecute(String essay) {
            super.onPostExecute(essay);
            webView.loadDataWithBaseURL(null, essay, "text/html", "utf-8", null);
            hiddenDialog();
        }
    }
}
