package jacketjie.astimes.views.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;

import jacketjie.astimes.R;
import jacketjie.astimes.greenDao.ATInformalEssay;

/**
 * Created by Administrator on 2015/12/17.
 */
public class SimpleWebViewActivity extends BaseActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_webview_detail);
        initViews();
        initDatas();
    }

    private void initViews() {
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
    }

    private void initDatas() {
        ATInformalEssay essay = (ATInformalEssay) getIntent().getSerializableExtra("ATINFORMALESSAY_DETAILS");
        String imageUrl = essay.getATIEImageUrl();
        String title = essay.getATIETitle();
        String text = essay.getATIEText();

        webView.clearHistory();
        webView.clearCache(true);//清除Cache
        webView.getSettings().setAllowFileAccess(true);// 允许访问文件
        webView.getSettings().setSupportZoom(true);//支持缩放
        webView.getSettings().setBuiltInZoomControls(true); //支持缩放
        //        webView.setInitialScale(150);//初始缩放比例100就是不变，100以下就是缩小
        webView.getSettings().setDisplayZoomControls(false);// 设置不显示缩放按钮
        //        webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);//WebView自适配

        StringBuffer html = new StringBuffer();
        if (!TextUtils.isEmpty(title)) {
            html.append("<head>").append(title).append("\n").append("</head>");
        }
        html.append("<body>");
                if (!TextUtils.isEmpty(imageUrl)) {
                    html.append("<img src=").append("\"").append("http://imagefiles.dfhon.com/imagefiles/2010/20101102/151049%20%281%29.jpg").append("\"").append("/>");
                }
                if (!TextUtils.isEmpty(text)){
                    html.append("<p2>").append(text).append("</p2>");
                }
        html.append("</body>");
        webView.loadDataWithBaseURL(null, html.toString(), "text/html", "utf-8", null);
    }
}
