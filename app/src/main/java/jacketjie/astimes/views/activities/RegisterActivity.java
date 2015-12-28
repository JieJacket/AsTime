package jacketjie.astimes.views.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.download.ImageDownloader;

import jacketjie.astimes.AsTimeApp;
import jacketjie.astimes.R;
import jacketjie.astimes.greenDao.ATUser;
import jacketjie.astimes.greenDao.GreenDaoUtils;
import jacketjie.astimes.utils.HttpUtils;

/**
 * Created by Administrator on 2015/12/16.
 */
public class RegisterActivity extends BaseActivity{
    private Button loginBtn;
    private EditText userNameEdit,passwordEdit,repeatPassEdit;
    private String REGISTER_URL ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        initViews();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.register_str);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loginBtn = (Button) findViewById(R.id.id_user_login);
        userNameEdit = (EditText) findViewById(R.id.id_username);
        passwordEdit = (EditText) findViewById(R.id.id_password);
        repeatPassEdit = (EditText) findViewById(R.id.id_password_repeat);
        REGISTER_URL = getString(R.string.as_times_address) + getString(R.string.register_address);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(userNameEdit.getText().toString().trim())) {
                    Toast.makeText(RegisterActivity.this, R.string.login_is_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passwordEdit.getText().toString().trim())) {
                    Toast.makeText(RegisterActivity.this, R.string.passwot_is_empty, Toast.LENGTH_SHORT).show();
                    return;
                }else if(!repeatPassEdit.getText().toString().trim().equals(passwordEdit.getText().toString().trim())){
                    Toast.makeText(RegisterActivity.this, R.string.passwot_isnotequal_repeat, Toast.LENGTH_SHORT).show();
                    return;
                }
                new RegisterTask().execute(userNameEdit.getText().toString().trim(), passwordEdit.getText().toString().trim());
            }
        });
    }

    /**
     * 注册
     */
    class RegisterTask extends AsyncTask<String,Void,ATUser> {
        @Override
        protected ATUser doInBackground(String... params) {
            String userName = params[0];
            String password = params[1];
            StringBuffer sb = new StringBuffer();
            sb.append("username=").append(userName).append("&").append("password=").append(password);
            String result = HttpUtils.doPost(REGISTER_URL,sb.toString());
            ATUser user = null;
            if (!GreenDaoUtils.isUserHadExisted(getApplicationContext(),userName)){
                user = new ATUser();
                user.setUserName(userName);
                user.setUserNickName(userName);
                user.setUserPassword(password);
                user.setIsActiveUser(true);
                user.setUserIcon(ImageDownloader.Scheme.DRAWABLE.wrap(R.drawable.as_time_icon + ""));
                GreenDaoUtils.insertOrUpdateUser(getApplicationContext(), user);
                AsTimeApp.setCurATUser(user);
            }
            return user;
        }

        @Override
        protected void onPostExecute(ATUser result) {
            super.onPostExecute(result);
            if (result == null){
                Toast.makeText(getApplicationContext(),R.string.user_had_existed,Toast.LENGTH_SHORT).show();
                return;
            }
            setResult(RESULT_OK);
            RegisterActivity.this.onBackPressed();
        }
    }
}
