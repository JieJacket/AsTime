package jacketjie.astimes.views.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jacketjie.astimes.R;
import jacketjie.astimes.adapter.WeiYuListAdapter;

/**
 * Created by Administrator on 2015/12/9.
 */
public class MainThirdFragment extends BaseFragment {
    private View v;
    private WeiYuListAdapter listAdapter;
    private List<WeiYu> mDatas;
    private ListView weiYuListView;
    private SwipeRefreshLayout refresh;
    private int count = 0;

    private String[] DEFAULT_USER_NAMES = {"结束就是开始", "屁股决定脑袋", "脑袋决定生活", "生存还是毁灭", "活着还是死亡", "这是个问题", "你觉得呢", "结束还是开始"};
    private String[] DEFAULT_USER_CONTENT = {ImageDownloader.Scheme.ASSETS.wrap("1.jpg"), ImageDownloader.Scheme.ASSETS.wrap("2.jpg"), ImageDownloader.Scheme.ASSETS.wrap("3.jpg"), ImageDownloader.Scheme.ASSETS.wrap("4.jpg"), ImageDownloader.Scheme.ASSETS.wrap("5.jpg"), ImageDownloader.Scheme.ASSETS.wrap("6.jpg"), ImageDownloader.Scheme.ASSETS.wrap("7.jpg"), ImageDownloader.Scheme.ASSETS.wrap("8.jpg"), ImageDownloader.Scheme.ASSETS.wrap("8.jpg")};
    private String[] DEFAULT_CONTENT = {"真理惟一可靠的标准就是永远自相符合",
            "土地是以它的肥沃和收获而被估价的；才能也是土地，不过它生产的不是粮食，而是真理。如果只能滋生瞑想和幻想的话，即使再大的才能也只是砂地或盐池，那上面连小草也长不出来的",
            "我需要三件东西：爱情友谊和图书。然而这三者之间何其相通！炽热的爱情可以充实图书的内容，图书又是人们最忠实的朋友", "时间是一切财富中最宝贵的财富。",
            "世界上一成不变的东西，只有“任何事物都是在不断变化的”这条真理。",
            "真正的科学家应当是个幻想家；谁不是幻想家，谁就只能把自己称为实践家。",
            "爱情原如树叶一样，在人忽视里绿了，在忍耐里露出蓓蕾。", "友谊是一棵可以庇荫的树。"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.main_third_fragment, container, false);
//            v = super.onCreateView(inflater,container,savedInstanceState);
//            setContentView(R.layout.main_third_fragment);
            showDialog(v);
            initView(v);
            initDatas();
        } else {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null) {
                parent.removeView(v);
            }
        }
        return v;
    }

    private void initDatas() {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm", Locale.getDefault());
        mDatas = new ArrayList<WeiYu>();
        for (int i = 0; i < 20; i++) {
            WeiYu weiYu = new WeiYu();
            weiYu.setDate(format.format(new Date()));
            weiYu.setUserName(DEFAULT_USER_NAMES[i % DEFAULT_USER_NAMES.length]);
            if (i % 2 == 0) {
                weiYu.setContent(DEFAULT_CONTENT[i % DEFAULT_CONTENT.length]);
            } else {
                weiYu.setImageUrl(DEFAULT_USER_CONTENT[i % DEFAULT_USER_CONTENT.length]);
            }
            mDatas.add(weiYu);
        }
        listAdapter = new WeiYuListAdapter(getActivity(), mDatas, R.layout.main_third_item);
        weiYuListView.setAdapter(listAdapter);
        hiddenDialog(v);
    }

    private void initView(View v) {
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) v.findViewById(
                R.id.collapse_toolbar);

        collapsingToolbar.setTitleEnabled(false);

        refresh = (SwipeRefreshLayout) v.findViewById(R.id.id_main_third_refresh);
        weiYuListView = (ListView) v.findViewById(R.id.id_weiyu_content);
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getStringArray(R.array.main_tabs_name)[2]);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadDataTask().execute((count++)+"");
            }
        });
    }


    public void showDialog(View v) {
        View dialogView = v.findViewById(R.id.id_base_progressbar);
        if (dialogView != null) {
            dialogView.setVisibility(View.VISIBLE);
            dialogView.bringToFront();
        }
    }

    public void hiddenDialog(View v) {
        View dialogView = v.findViewById(R.id.id_base_progressbar);
        if (dialogView != null)
            dialogView.setVisibility(View.GONE);
    }

    class LoadDataTask extends AsyncTask<String,Void,String>{
        private int mark;
        @Override
        protected String doInBackground(String... params) {
            try {
                mark = Integer.valueOf(params[0]);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            refresh.setRefreshing(false);
            SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm", Locale.getDefault());
            mDatas.clear();
            if (mark % 2 == 0){
                for (int i = 1; i <= 20; i++) {
                    WeiYu weiYu = new WeiYu();
                    weiYu.setDate(format.format(new Date()));
                    weiYu.setUserName(DEFAULT_USER_NAMES[i % DEFAULT_USER_NAMES.length]);
                    if (i % 2 == 0) {
                        weiYu.setContent(DEFAULT_CONTENT[i % DEFAULT_CONTENT.length]);
                    } else {
                        weiYu.setImageUrl(DEFAULT_USER_CONTENT[i % DEFAULT_USER_CONTENT.length]);
                    }
                    mDatas.add(weiYu);
                }
            }else{
                for (int i = 0; i <  20; i++) {
                    WeiYu weiYu = new WeiYu();
                    weiYu.setDate(format.format(new Date()));
                    weiYu.setUserName(DEFAULT_USER_NAMES[i % DEFAULT_USER_NAMES.length]);
                    if (i % 2 == 0) {
                        weiYu.setContent(DEFAULT_CONTENT[i % DEFAULT_CONTENT.length]);
                    } else {
                        weiYu.setImageUrl(DEFAULT_USER_CONTENT[i % DEFAULT_USER_CONTENT.length]);
                    }
                    mDatas.add(weiYu);
                }
            }
            listAdapter.notifyDataSetChanged();
        }
    }
}
