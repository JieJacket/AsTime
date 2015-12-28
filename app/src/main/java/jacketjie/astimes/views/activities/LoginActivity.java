package jacketjie.astimes.views.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import jacketjie.astimes.AsTimeApp;
import jacketjie.astimes.R;
import jacketjie.astimes.greenDao.ATUser;
import jacketjie.astimes.greenDao.GreenDaoUtils;
import jacketjie.astimes.utils.HttpUtils;

/**
 * Created by wujie on 2015/12/12.
 */
public class LoginActivity extends BaseActivity {
    private Button loginBtn;
    private EditText userNameEdit, passwordEdit;
    private TextView registerTV;
    private String LOGIN_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initViews();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.login);
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
        registerTV = (TextView) findViewById(R.id.id_user_register);
        LOGIN_URL = getString(R.string.as_times_address) + getString(R.string.login_address);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(userNameEdit.getText().toString().trim())) {
                    Toast.makeText(LoginActivity.this, R.string.login_is_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passwordEdit.getText().toString().trim())) {
                    Toast.makeText(LoginActivity.this, R.string.passwot_is_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                new LoginTask().execute(userNameEdit.getText().toString().trim(), passwordEdit.getText().toString().trim());
            }
        });
        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 0x123);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x123) {
            if (resultCode == RESULT_OK) {
                LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
                Intent intent = new Intent("UPDATE_USER_RECEIVER");
                lbm.sendBroadcast(intent);
                LoginActivity.this.finish();
            }
        }
    }

    /**
     * 登录
     */
    class LoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String userName = params[0];
            String password = params[1];
            StringBuffer sb = new StringBuffer();
            sb.append("username=").append(userName).append("&password=").append(password);
            String result = HttpUtils.doPost(LOGIN_URL, sb.toString());
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.isEmpty(s)) {
                return;
            }
            try {
                JSONObject jo = new JSONObject(s);
                int ret = jo.getInt("code");
                switch (ret) {
                    case 200:
                        JSONObject object = jo.getJSONObject("data");
                        if (object != null) {
                            final ATUser user = new ATUser();
                            user.setUserId(object.getString("uid"));
                            user.setUserNickName(object.getString("nickname"));
                            user.setUserName(object.getString("username"));
                            user.setUserIcon(object.getString("face"));
                            user.setUserSignature(object.getString("autograph"));
                            user.setIsActiveUser(true);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    GreenDaoUtils.insertOrUpdateUser(getApplicationContext(), user);
                                }
                            }).start();
                            AsTimeApp.setCurATUser(user);
                            LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(LoginActivity.this);
                            Intent intent = new Intent("UPDATE_USER_RECEIVER");
                            lbm.sendBroadcast(intent);
                            LoginActivity.this.onBackPressed();
                        }
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检查登录状态
     */
    class CheckLoginTask extends AsyncTask<String, Void, ATUser> {
        @Override
        protected ATUser doInBackground(String... params) {
            String userName = params[0];
            String password = params[1];
            StringBuffer sb = new StringBuffer();
            sb.append("username=").append(userName).append("&password=").append(password);
            String result = HttpUtils.doPost(LOGIN_URL, sb.toString());
//            ATUser user = GreenDaoUtils.getUserFromLogin(getApplicationContext(), userName, password);
//            if (user != null){
//                user.setIsActiveUser(true);
//                GreenDaoUtils.insertOrUpdateUser(getApplicationContext(), user);
//            }
            ATUser user = new ATUser();
            return user;
        }

        @Override
        protected void onPostExecute(ATUser result) {
            super.onPostExecute(result);
            if (result == null) {
                Toast.makeText(getApplicationContext(), R.string.invalid_user_str, Toast.LENGTH_SHORT).show();
                return;
            }
            AsTimeApp.setCurATUser(result);
            LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(LoginActivity.this);
            Intent intent = new Intent("UPDATE_USER_RECEIVER");
            lbm.sendBroadcast(intent);
            LoginActivity.this.onBackPressed();
        }
    }
}
