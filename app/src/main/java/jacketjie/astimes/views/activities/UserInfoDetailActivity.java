package jacketjie.astimes.views.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import de.hdodenhof.circleimageview.CircleImageView;
import jacketjie.astimes.AsTimeApp;
import jacketjie.astimes.R;
import jacketjie.astimes.greenDao.ATUser;
import jacketjie.astimes.greenDao.GreenDaoUtils;
import jacketjie.astimes.utils.PictureUtil;

/**
 * Created by wujie on 2015/12/12.
 */
public class UserInfoDetailActivity extends BaseActivity{
    private EditText userSignature;
    private EditText userNickName;
    private TextView userGendar;
    private static final int PICTURE = 0x123;
    private CircleImageView userIcon;
    private Button logoutBtn;
    private View iconContent,gendarContent;

    private ATUser updateUser;
    private String iconUril;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_detail);
        initViews();
        initDatas();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.personal_info_str);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        iconContent = findViewById(R.id.id_info_icon_content);
        gendarContent = findViewById(R.id.id_info_gendar_content);
        userIcon = (CircleImageView) findViewById(R.id.id_info_icon);
        userNickName = (EditText) findViewById(R.id.id_info_nickname);
        userGendar = (TextView) findViewById(R.id.id_info_gendar);
        userSignature = (EditText) findViewById(R.id.id_info_signature);
        logoutBtn = (Button) findViewById(R.id.id_user_logout);

        iconContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picture = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(picture, PICTURE);
            }
        });
        gendarContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final Dialog dialog = new Dialog(UserInfoDetailActivity.this);
                dialog.setTitle("选择");
                View dialogView = getLayoutInflater().inflate(R.layout.gendar_select_layout,null);
                dialogView.findViewById(R.id.id_info_gendar_female).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userGendar.setText("女");
                        dialog.dismiss();
                    }
                });
                dialogView.findViewById(R.id.id_info_gendar_male).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userGendar.setText("男");
                        dialog.dismiss();
                    }
                });
                dialog.setContentView(dialogView);
                dialog.show();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ATUser user = AsTimeApp.getCurATUser();
                user.setIsActiveUser(false);
                GreenDaoUtils.insertOrUpdateUser(getApplicationContext(), user);
                AsTimeApp.setCurATUser(null);
                LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(UserInfoDetailActivity.this);
                Intent intent = new Intent("UPDATE_USER_RECEIVER");
                lbm.sendBroadcast(intent);
                UserInfoDetailActivity.this.onBackPressed();
            }
        });
    }



    private void initDatas() {
        updateUser = AsTimeApp.getCurATUser();
        userNickName.setText(updateUser.getUserNickName());
        if(updateUser.getUserGender()!= null){
            userGendar.setText(updateUser.getUserGender() == 0?"女":"男");
        }
        if (TextUtils.isEmpty(updateUser.getUserIcon())){
            userIcon.setImageResource(R.drawable.as_time_icon);
        }else{
            ImageLoader.getInstance().displayImage(updateUser.getUserIcon(), userIcon);
        }
        userSignature.setText(TextUtils.isEmpty(updateUser.getUserSignature())?"":updateUser.getUserSignature());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.info_save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_save) {
            updateUser.setUserNickName(userNickName.getText().toString().trim());
            updateUser.setUserSignature(userSignature.getText().toString().trim());
            String gendar = userGendar.getText().toString().trim();
            if (!TextUtils.isEmpty(gendar)){
                updateUser.setUserGender("男".equals(gendar)?1:0);
            }
            if (!TextUtils.isEmpty(iconUril)){
                updateUser.setUserIcon(iconUril);
            }
            new UpdateUser().execute(updateUser);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE && resultCode == RESULT_OK && data != null) {
            String currentCoverUrl = PictureUtil.getPath(this, data.getData());
            iconUril = ImageDownloader.Scheme.FILE.wrap(currentCoverUrl);
            ImageLoader.getInstance().displayImage(iconUril, userIcon);
        }
    }

    /**
     * 请求数据
     */
    class UpdateUser extends AsyncTask<ATUser, Void, ATUser> {

        @Override
        protected ATUser doInBackground(ATUser... params) {
            ATUser updateUser = params[0];
            GreenDaoUtils.insertOrUpdateUser(getApplicationContext(), updateUser);
            AsTimeApp.setCurATUser(updateUser);
            return null;
        }

        @Override
        protected void onPostExecute(ATUser result) {
            super.onPostExecute(result);
            LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(UserInfoDetailActivity.this);
            Intent intent = new Intent("UPDATE_USER_RECEIVER");
            lbm.sendBroadcast(intent);
            UserInfoDetailActivity.this.onBackPressed();
        }
    }
}
