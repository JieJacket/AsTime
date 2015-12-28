package jacketjie.astimes.views.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import jacketjie.astimes.AsTimeApp;
import jacketjie.astimes.R;
import jacketjie.astimes.utils.PictureUtil;
import jacketjie.astimes.utils.ScreenUtils;

/**
 * 图片详情查看
 * Created by Administrator on 2015/12/17.
 */
public class ImageDetailActivity extends BaseActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_image_layout);
        initViews();
    }

    private void initViews() {
        String title = getIntent().getStringExtra("IMAGE_TITLE");
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");
        imageView = (ImageView) findViewById(R.id.id_pop_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        ImageLoader.getInstance().loadImage(imageUrl, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                Bitmap bit = PictureUtil.decodebitmap(loadedImage, ScreenUtils.getScreenWidth(ImageDetailActivity.this));
                imageView.setImageBitmap(bit);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_detail, menu);
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
            case R.id.action_image_share:

                break;
            case R.id.action_image_save:

                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
