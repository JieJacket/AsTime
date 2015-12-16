package jacketjie.astimes.views.activities;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;

import jacketjie.astimes.R;
import jacketjie.astimes.custom.swipeback.SwipeBackActivity;
import jacketjie.astimes.custom.swipeback.SwipeBackLayout;

/**
 * Created by Eric on 15/3/3.
 */
public class BaseActivity extends SwipeBackActivity {

    private SwipeBackLayout swipeBackLayout;
    private ImageView ivShadow;
    private View dialogView;

//    @Override
//    public void setContentView(int layoutResID) {
//        super.setContentView(getContainer());
//        View view = LayoutInflater.from(this).inflate(layoutResID, null);
//        swipeBackLayout.addView(view);
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        getSwipeBackLayout().setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }

    public void showDialog(){
        dialogView = findViewById(R.id.id_base_progressbar);
        if (dialogView != null)
            dialogView.setVisibility(View.VISIBLE);
    }

    public void hiddenDialog(){
        dialogView = findViewById(R.id.id_base_progressbar);
        if (dialogView != null)
            dialogView.setVisibility(View.GONE);
    }

}
