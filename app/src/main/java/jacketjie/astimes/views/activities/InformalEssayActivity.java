package jacketjie.astimes.views.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jacketjie.astimes.AsTimeApp;
import jacketjie.astimes.R;
import jacketjie.astimes.greenDao.ATInformalEssay;
import jacketjie.astimes.greenDao.GreenDaoUtils;
import jacketjie.astimes.model.EssayDetail;
import jacketjie.astimes.utils.HttpUtils;
import jacketjie.astimes.utils.PictureUtil;
import jacketjie.astimes.utils.ScreenUtils;
import jacketjie.astimes.utils.StatusBarUtil;
import jacketjie.astimes.views.fragments.MainSecondFragment;

/**
 * 写随笔
 * Created by Administrator on 2015/12/11.
 */
public class InformalEssayActivity extends BaseActivity {

    private static final int PICTURE = 0x123;
    private Toolbar toolbar;
    private ScrollView scrollView;

    private ImageView coverImage;
    private TextInputLayout titleInputLayout, essayInputLayout;
    private EditText titleEdit, essayEdit;
    private String currentCoverUrl;
    private int imageWidth;
    private ATInformalEssay lastEssay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.showStatusBar(this, R.color.colorPrimary);
        setContentView(R.layout.informal_essay_layout);
//        showDialog();
        initTopImage();
        initViews();
        setEventListener();
    }

    private void initTopImage() {
        coverImage = (ImageView) findViewById(R.id.id_essay_cover);
        int width = ScreenUtils.getScreenWidth(this);
        imageWidth = width - 20 * ScreenUtils.getDensityDpi(this) / 160;
        ViewGroup.LayoutParams lp = coverImage.getLayoutParams();
        lp.width = imageWidth;
        lp.height = (int) (imageWidth / 1.4);
    }

    private void setEventListener() {
        coverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(picture, PICTURE);
            }
        });
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("随便写写");

        titleInputLayout = (TextInputLayout) findViewById(R.id.id_essay_title_layout);
        titleEdit = (EditText) findViewById(R.id.id_essay_title);

        essayInputLayout = (TextInputLayout) findViewById(R.id.id_essay_layout);
        essayEdit = (EditText) findViewById(R.id.id_essay);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lastEssay = GreenDaoUtils.getLastNotSubmit(this);
        if (lastEssay != null){
            ImageLoader.getInstance().displayImage(lastEssay.getATIEImageUrl(), coverImage);
            titleEdit.setText(TextUtils.isEmpty(lastEssay.getATIETitle()) ? "" : Html.fromHtml(lastEssay.getATIETitle()));
            essayEdit.setText(TextUtils.isEmpty(lastEssay.getATIEText()) ? "" : Html.fromHtml(lastEssay.getATIEText()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (AsTimeApp.getCurATUser() == null){
            Toast.makeText(this,R.string.please_login_first,Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
        int id = item.getItemId();
        if (id == R.id.action_shared){
            saveOrSharedEssay(1);
        }else if(id == R.id.action_privated){
            saveOrSharedEssay(0);
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 保存信息
     */
    private void saveOrSharedEssay(int id) {
        if (lastEssay == null){
            lastEssay = new ATInformalEssay();
        }
        if (!TextUtils.isEmpty(currentCoverUrl)){
            lastEssay.setATIEImageUrl(ImageDownloader.Scheme.FILE.wrap(currentCoverUrl));
        }else{
            lastEssay.setATIEImageUrl(ImageDownloader.Scheme.ASSETS.wrap("default_informal_essay.jpg"));
        }
        SimpleDateFormat smd = new SimpleDateFormat("MM-dd HH:mm");
        lastEssay.setATIEReleaseDate(smd.format(new Date()));
        Spanned title = new SpannableString(titleEdit.getText().toString());
        lastEssay.setATIETitle(Html.toHtml(title));
        Spanned body = new SpannableString(essayEdit.getText().toString());
        lastEssay.setATIEText(Html.toHtml(body));
        lastEssay.setATIEShared(id);
        lastEssay.setATIEHasSubmit(1);
        GreenDaoUtils.insertOrUpdateInformalEssay(this,lastEssay);
        if (id == 1){
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MainSecondFragment.UPDATE_ESSAY_LIST_ACTION));
        }
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE && resultCode == RESULT_OK && data != null) {
            currentCoverUrl = PictureUtil.getPath(this, data.getData());
            ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(currentCoverUrl),coverImage);
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
            if (TextUtils.isEmpty(result))
                return;
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
//                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (lastEssay == null){
                    lastEssay = new ATInformalEssay();
                }
                if (lastEssay.getATIEHasSubmit() == 1)
                    return;
                SimpleDateFormat smd = new SimpleDateFormat("MM-dd HH:mm");
                lastEssay.setATIEReleaseDate(smd.format(new Date()));
                Spanned title = new SpannableString(titleEdit.getText().toString());
                lastEssay.setATIETitle(Html.toHtml(title));
                Spanned body = new SpannableString(essayEdit.getText().toString());
                lastEssay.setATIEText(Html.toHtml(body));
                lastEssay.setATIEShared(0);
                lastEssay.setATIEHasSubmit(0);
                if (!TextUtils.isEmpty(currentCoverUrl)){
                    lastEssay.setATIEImageUrl(ImageDownloader.Scheme.FILE.wrap(currentCoverUrl));
                }
                if (TextUtils.isEmpty(lastEssay.getATIEImageUrl())){
                    lastEssay.setATIEImageUrl(ImageDownloader.Scheme.ASSETS.wrap("default_informal_essay.jpg"));
                }
                GreenDaoUtils.insertOrUpdateInformalEssay(InformalEssayActivity.this, lastEssay);
            }
        }).start();
    }
}
