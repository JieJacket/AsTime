package jacketjie.astimes.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import jacketjie.astimes.R;
import jacketjie.astimes.utils.interfaces.OnLoadMoreListener;

/**
 * 可以自动加载更多的listview
 * Created by Administrator on 2015/12/16.
 */
public class AutoLoadMoreListView extends ListView implements AbsListView.OnScrollListener {
    /**
     * 加载更多FooterView
     */
    private View footerView;
    /**
     * 加载时显示的label
     */
    private String loadMoreLabel;
    /**
     * 监听器
     */
    private OnLoadMoreListener onLoadMoreListener;
    /**
     * footerview是否已添加
     */
    private boolean hasAddFooterView;
    /**
     * 是否监听滑动加载更多
     */
    private boolean isLoadMoreEnable =true;

    /**
     * 设置加载更多的listener
     * @param onLoadMoreListener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public AutoLoadMoreListView(Context context) {
        super(context);
    }

    public AutoLoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AutoLoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AutoLoadMoreListView,0,0);
        try {
            loadMoreLabel = ta.getString(R.styleable.AutoLoadMoreListView_loadMoreLabel);
//            progressBarStyle = ta.getDimensionPixelOffset(R.styleable.AutoLoadMoreListView_loadMoreStyle, R.style.MyLoadMoreProgressBarStyle);
            footerView = LayoutInflater.from(context).inflate(R.layout.default_load_more_footerview, null);
            TextView label = (TextView) footerView.findViewById(R.id.id_load_label);
            ProgressBar pb = (ProgressBar) footerView.findViewById(R.id.id_load_pb);
            label.setText(TextUtils.isEmpty(loadMoreLabel)?context.getResources().getString(R.string.default_loading_str):loadMoreLabel);
            this.setOnScrollListener(this);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            ta.recycle();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (getAdapter() != null){
            if (getAdapter().getCount() == 0 || visibleItemCount >= totalItemCount){
                return;
            }
            if (firstVisibleItem + visibleItemCount== getAdapter().getCount() ){
                if (onLoadMoreListener != null && !hasAddFooterView && isLoadMoreEnable){
                    addFooterView(footerView, null, false);
                    hasAddFooterView = true;
                    onLoadMoreListener.onLoadMoreListener();
                }
            }
        }
    }

    public void setOnLoadMoreComplete(){
        removeFooterView(footerView);
        hasAddFooterView = false;
    }

    /**
     * 设置是否需要加载更多
     * @param isLoadMoreEnable
     */
    public void setLoadMoreListenerEnable(boolean isLoadMoreEnable){
        this.isLoadMoreEnable = isLoadMoreEnable;
    }
}
