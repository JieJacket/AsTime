package jacketjie.astimes.views.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import jacketjie.astimes.R;
import jacketjie.astimes.adapter.InformalEssayListAdapter;
import jacketjie.astimes.custom.AutoLoadMoreListView;
import jacketjie.astimes.greenDao.ATInformalEssay;
import jacketjie.astimes.greenDao.GreenDaoUtils;

/**
 * Created by Administrator on 2015/12/17.
 */
public class MyInformalEssayDetailActivity extends BaseActivity{
    private AutoLoadMoreListView listView;
    private SwipeRefreshLayout refreshLayout;
    private FloatingActionButton fab;
    private List<ATInformalEssay> mDatas;
    private Toolbar toolbar;
    private InformalEssayListAdapter essayListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_second_fragment);
        showDialog();
        initViews();
        initDatas();
    }

    private void initDatas() {
        mDatas = new ArrayList<ATInformalEssay>();
        essayListAdapter = new InformalEssayListAdapter(this,mDatas,R.layout.main_second_item);
        listView.setAdapter(essayListAdapter);
        listView.setLoadMoreListenerEnable(false);
        new LoadAllDataTask(true).execute();
    }

    private void initViews() {
        listView = (AutoLoadMoreListView) findViewById(R.id.id_list_view);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_refresh_layout);
        fab = (FloatingActionButton) findViewById(R.id.id_fab);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("我的随笔");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        refreshLayout.setColorSchemeColors(R.color.colorPrimary);
    }

    /**
     * 加载数据
     */
    class LoadAllDataTask extends AsyncTask<Void,Void,List<ATInformalEssay>>{
        private boolean isPullDown;

        public LoadAllDataTask(boolean isPullDown) {
            this.isPullDown = isPullDown;
        }

        @Override
        protected List<ATInformalEssay> doInBackground(Void... params) {
            List<ATInformalEssay> result = GreenDaoUtils.getAllInformalEssayByDate(MyInformalEssayDetailActivity.this);
            return result;
        }

        @Override
        protected void onPostExecute(List<ATInformalEssay> atInformalEssays) {
            super.onPostExecute(atInformalEssays);
            hiddenDialog();
            listView.setOnLoadMoreComplete();
            if (atInformalEssays == null || atInformalEssays.size() == 0){
                return;
            }
            if (isPullDown){
                mDatas.clear();
            }
            mDatas.addAll(atInformalEssays);
            essayListAdapter.notifyDataSetChanged();
        }
    }
}
