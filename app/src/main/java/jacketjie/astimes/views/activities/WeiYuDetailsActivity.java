package jacketjie.astimes.views.activities;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import jacketjie.astimes.R;
import jacketjie.astimes.model.WeiYu;
import jacketjie.astimes.utils.HttpUtils;
import jacketjie.astimes.utils.PictureUtil;
import jacketjie.astimes.utils.ScreenUtils;
import jacketjie.astimes.utils.StatusBarUtil;

/**
 * 美文详情
 * Created by Administrator on 2015/12/11.
 */
public class WeiYuDetailsActivity extends BaseActivity {

    private Toolbar toolbar;
    private SwipeRefreshLayout refreshLayout;
    private TextView userName,weiyuDate,userSignature;
    private ImageView userIcon,userGendar,weiyuContentImage;
    private TextView weiyuContent;
    private TextView fromClent;
    private ListView commentListView;
    private EditText commentEdit;
    private int targetWidth;

    public WeiYuDetailsActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.showStatusBar(this, R.color.colorPrimary);
        setContentView(R.layout.weiyu_ditail_layout);
//        showDialog();
        initViews();
        setDataAndEventListener();
    }

    private void initViews() {
//        final EssayDetail essay = getIntent().getParcelableExtra("DETAILS");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_refresh_layout);
        userName = (TextView) findViewById(R.id.id_weiyu_detail_username);
        userGendar = (ImageView) findViewById(R.id.id_weiyu_detail_user_gendar);
        weiyuDate = (TextView) findViewById(R.id.id_weiyu_detail_date);
        userSignature = (TextView) findViewById(R.id.id_weiyu_user_signature);
        userIcon = (ImageView) findViewById(R.id.id_weiyu_detail_icon);
        weiyuContent = (TextView) findViewById(R.id.id_weiyu_detail_content_text);
        weiyuContentImage = (ImageView) findViewById(R.id.id_weiyu_detail_content_image);
        fromClent = (TextView) findViewById(R.id.id_weiyu_from);
        commentListView = (ListView) findViewById(R.id.id_weiyu_comment_list);

        commentEdit = (EditText) findViewById(R.id.id_weiyu_send_comment);
//        new LoadDataTask().execute(getString(R.string.essay_type_list_detail_address), essay.getDetailId());
    }
    private void setDataAndEventListener() {
        WeiYu weiYu = getIntent().getParcelableExtra("WEIYU");
        toolbar.setTitle("心灵鸡汤");
        targetWidth = ScreenUtils.getScreenWidth(this) - (66+20) * ScreenUtils.getDensityDpi(this) / 160 ;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                new LoadDataTask().execute(getString(R.string.essay_type_list_address), essay.getDetailId());
            }
        });
        userName.setText(TextUtils.isEmpty(weiYu.getUserName()) ? "" : weiYu.getUserName());
        weiyuDate.setText(TextUtils.isEmpty(weiYu.getDate())?"":weiYu.getDate());
        userSignature.setText(TextUtils.isEmpty(weiYu.getUserSignature())?getString(R.string.default_signature):weiYu.getUserSignature());
        if (TextUtils.isEmpty(weiYu.getImageUrl())){
            weiyuContentImage.setVisibility(View.GONE);
        }else{
            weiyuContentImage.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().loadImage(weiYu.getImageUrl(), new SimpleImageLoadingListener(){
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    Bitmap bit = PictureUtil.decodebitmap(loadedImage,targetWidth);
                    weiyuContentImage.setImageBitmap(bit);
                }
            });
        }
        if (TextUtils.isEmpty(weiYu.getContent())){
            weiyuContent.setVisibility(View.GONE);
        }else{
            weiyuContent.setVisibility(View.VISIBLE);
            weiyuContent.setText(weiYu.getContent());
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
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
