package jacketjie.astimes.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import jacketjie.astimes.R;

/**
 * Created by wujie on 2015/12/12.
 */
public class AsTimeBufferActivity extends AppCompatActivity{
    private Handler handler ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.as_time_buffer_layout);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(AsTimeBufferActivity.this,AsTimeMainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.buffer_right_in,R.anim.buffer_left_out);
                finish();
            }
        },3000);
    }
}
