package jacketjie.astimes.views.activities;

import android.os.Bundle;
import android.widget.TextView;

import jacketjie.astimes.R;
import jacketjie.astimes.custom.BaseActivity;
import jacketjie.astimes.custom.SwipeBackLayout;

/**
 * Created by Administrator on 2015/12/9.
 */
public class TextBackActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_second_fragment);
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        ((TextView)findViewById(R.id.action_title)).setText("左滑后退");
    }
}
