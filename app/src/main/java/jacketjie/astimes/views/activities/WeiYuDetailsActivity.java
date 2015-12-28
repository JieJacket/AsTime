package jacketjie.astimes.views.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jacketjie.astimes.AsTimeApp;
import jacketjie.astimes.R;
import jacketjie.astimes.adapter.WeiyuCommentListAdapter;
import jacketjie.astimes.custom.EditTextWithDrawable;
import jacketjie.astimes.custom.ListViewForScrollView;
import jacketjie.astimes.greenDao.ATComment;
import jacketjie.astimes.greenDao.ATUser;
import jacketjie.astimes.greenDao.GreenDaoUtils;
import jacketjie.astimes.model.WeiYu;
import jacketjie.astimes.utils.HttpUtils;
import jacketjie.astimes.utils.KeyBoardUtils;
import jacketjie.astimes.utils.PictureUtil;
import jacketjie.astimes.utils.ScreenUtils;
import jacketjie.astimes.utils.StatusBarUtil;
import jacketjie.astimes.utils.interfaces.OnEditTextDrawableClickListener;

/**
 * 微语详情
 * Created by Administrator on 2015/12/11.
 */
public class WeiYuDetailsActivity extends BaseActivity {

    private ScrollView scrollView;
    private Toolbar toolbar;
    private TextView userName, weiyuDate, userSignature;
    private ImageView userIcon, userGendar, weiyuContentImage, priseImage;
    private TextView weiyuContent;
    private TextView fromClent;
    private ListViewForScrollView commentListView;
    private EditTextWithDrawable commentEdit;
    private int targetWidth;
    private List<ATComment> comments;
    private WeiyuCommentListAdapter commentListAdapter;
    private WeiYu weiYu;

    public WeiYuDetailsActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.showStatusBar(this, R.color.colorPrimary);
        setContentView(R.layout.weiyu_ditail_layout);
//        showDialog();
        initViews();
        initDatas();
        setEventListener();
    }

    private void initViews() {
//        final EssayDetail essay = getIntent().getParcelableExtra("DETAILS");
        scrollView = (ScrollView) findViewById(R.id.id_detail_content);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        userName = (TextView) findViewById(R.id.id_weiyu_detail_username);
        userGendar = (ImageView) findViewById(R.id.id_weiyu_detail_user_gendar);
        weiyuDate = (TextView) findViewById(R.id.id_weiyu_detail_date);
        userSignature = (TextView) findViewById(R.id.id_weiyu_user_signature);
        userIcon = (ImageView) findViewById(R.id.id_weiyu_detail_icon);
        weiyuContent = (TextView) findViewById(R.id.id_weiyu_detail_content_text);
        weiyuContentImage = (ImageView) findViewById(R.id.id_weiyu_detail_content_image);
        fromClent = (TextView) findViewById(R.id.id_weiyu_from);
        commentListView = (ListViewForScrollView) findViewById(R.id.id_weiyu_comment_list);
        priseImage = (ImageView) findViewById(R.id.id_weiyu_detail_content_prise);
        commentEdit = (EditTextWithDrawable) findViewById(R.id.id_weiyu_send_comment);
//        new LoadDataTask().execute(getString(R.string.essay_type_list_detail_address), essay.getDetailId());
        scrollView.smoothScrollTo(0, 0);
    }

    private void initDatas() {
        weiYu = getIntent().getParcelableExtra("WEIYU");
        toolbar.setTitle("心灵鸡汤");
        targetWidth = ScreenUtils.getScreenWidth(this) - (66 + 20) * ScreenUtils.getDensityDpi(this) / 160;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userName.setText(TextUtils.isEmpty(weiYu.getUserName()) ? "" : weiYu.getUserName());
        weiyuDate.setText(TextUtils.isEmpty(weiYu.getDate()) ? "" : weiYu.getDate());
        userSignature.setText(TextUtils.isEmpty(weiYu.getUserSignature()) ? getString(R.string.default_signature) : weiYu.getUserSignature());
        if (TextUtils.isEmpty(weiYu.getImageUrl())) {
            weiyuContentImage.setVisibility(View.GONE);
        } else {
            weiyuContentImage.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().loadImage(weiYu.getImageUrl(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    Bitmap bit = PictureUtil.decodebitmap(loadedImage, targetWidth);
                    weiyuContentImage.setImageBitmap(bit);
                }
            });
        }
        if (TextUtils.isEmpty(weiYu.getContent())) {
            weiyuContent.setVisibility(View.GONE);
        } else {
            weiyuContent.setVisibility(View.VISIBLE);
            weiyuContent.setText(weiYu.getContent());
        }

        comments = GreenDaoUtils.getAllComments(getApplicationContext(), weiYu.getId());
        commentListAdapter = new WeiyuCommentListAdapter(this, comments, R.layout.weiyu_detail_comment_item);
        commentListView.setAdapter(commentListAdapter);
    }

    /**
     * 设置监听事件
     */
    private void setEventListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        commentEdit.setOnEditTextDrawableClickListener(new OnEditTextDrawableClickListener() {
            @Override
            public void onDrawableLeftClickListener() {
                Toast.makeText(getApplicationContext(), "click left", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDrawableRightClickListener() {
                ATUser curUser = AsTimeApp.getCurATUser();
                if (curUser == null) {
                    Toast.makeText(getApplicationContext(), R.string.please_login_first, Toast.LENGTH_SHORT).show();
                    return;
                }
                String content = commentEdit.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(getApplicationContext(), R.string.comment_is_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                ATComment comment = new ATComment();

                String date = new SimpleDateFormat("MM-dd HH:mm").format(new Date());
                comment.setCommentDate(date);
                comment.setCommentDetail(content);
                comment.setCommentUserId(curUser.getUserId());
                comment.setWeiYuId(weiYu.getId());
                comments.add(comment);
                commentListAdapter.notifyDataSetChanged();
                commentEdit.clear();
                commentEdit.clearFocus();
                GreenDaoUtils.addComment(getApplicationContext(), comment);
                KeyBoardUtils.closeKeybord(commentEdit, WeiYuDetailsActivity.this);
            }
        });

        priseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.prised_anim);
                priseImage.setImageResource(R.drawable.icon_prised);
                priseImage.startAnimation(anim);
            }
        });
        weiyuContentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ImageDetailActivity.class);
                intent.putExtra("IMAGE_TITLE", weiYu.getUserName());
                intent.putExtra("IMAGE_URL", weiYu.getImageUrl());
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

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
