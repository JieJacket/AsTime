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

import jacketjie.astimes.AsTimeApp;
import jacketjie.astimes.R;
import jacketjie.astimes.greenDao.ATUser;
import jacketjie.astimes.greenDao.GreenDaoUtils;

/**
 * Created by wujie on 2015/12/12.
 */
public class LoginActivity extends BaseActivity{
    private Button loginBtn;
    private EditText userNameEdit,passwordEdit;
    private TextView registerTV;
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
                new CheckLoginTask().execute(userNameEdit.getText().toString().trim(), passwordEdit.getText().toString().trim());
            }
        });
        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent, 0x123);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if (requestCode == 0x123){
           if (resultCode == RESULT_OK){
               LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
               Intent intent = new Intent("UPDATE_USER_RECEIVER");
               lbm.sendBroadcast(intent);
               LoginActivity.this.finish();
           }
       }
    }

    /**
     * 检查登录状态
     */
    class CheckLoginTask extends AsyncTask<String,Void,ATUser>{
        @Override
        protected ATUser doInBackground(String... params) {
            String userName = params[0];
            String password = params[1];
            ATUser user = GreenDaoUtils.getUserFromLogin(getApplicationContext(), userName, password);
            if (user != null){
                user.setIsActiveUser(true);
                GreenDaoUtils.insertOrUpdate(getApplicationContext(), user);
            }
            return user;
        }

        @Override
        protected void onPostExecute(ATUser result) {
            super.onPostExecute(result);
            if (result == null){
                Toast.makeText(getApplicationContext(),R.string.invalid_user_str,Toast.LENGTH_SHORT).show();
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
