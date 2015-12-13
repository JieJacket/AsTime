package jacketjie.astimes.views.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import jacketjie.astimes.R;
import jacketjie.astimes.custom.swipeback.SwipeBackActivity;

/**
 * Created by wujie on 2015/12/12.
 */
public class LoginActivity extends SwipeBackActivity{

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
    }
}
