package jacketjie.astimes.views.activities;

import android.os.Bundle;

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

    }
}
