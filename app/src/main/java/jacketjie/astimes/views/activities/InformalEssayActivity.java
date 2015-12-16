package jacketjie.astimes.views.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jacketjie.astimes.R;
import jacketjie.astimes.model.EssayDetail;
import jacketjie.astimes.utils.HttpUtils;
import jacketjie.astimes.utils.PictureUtil;
import jacketjie.astimes.utils.ScreenUtils;
import jacketjie.astimes.utils.StatusBarUtil;

/**
 * 美文详情
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
        coverImage.setImageResource(R.drawable.default_informal_essay);
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
        toolbar.setTitle("发心情");

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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_send) {
            String content = essayEdit.getText().toString();
            Spanned spanned = SpannableString.valueOf(TextUtils.isEmpty(content)?"":content);
            Html.toHtml(spanned);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
}
