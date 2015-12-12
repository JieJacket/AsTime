package jacketjie.astimes.views.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import jacketjie.astimes.R;
import jacketjie.astimes.custom.swipeback.SwipeBackActivity;
import jacketjie.astimes.utils.StatusBarUtil;

/**
 * Created by wujie on 2015/12/12.
 */
public class RecordsDetailsActivity extends SwipeBackActivity{

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.showStatusBar(this, R.color.colorPrimary);
        setContentView(R.layout.records_details_layout);
        initViews();
    }

    private void initViews() {
            toolbar = (Toolbar) findViewById(R.id.toolbar);

            toolbar.setTitle("测试");
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
