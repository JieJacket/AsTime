package jacketjie.astimes.views.activities;

import android.os.Bundle;

import jacketjie.astimes.R;
import jacketjie.astimes.custom.SwipeBackActivity;
import jacketjie.astimes.custom.SwipeBackLayout;

/**
 * Created by Administrator on 2015/12/9.
 */
public class TextBackActivity extends SwipeBackActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_forth_fragment);
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);
    }
}
