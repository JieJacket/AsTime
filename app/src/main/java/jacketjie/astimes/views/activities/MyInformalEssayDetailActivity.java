package jacketjie.astimes.views.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import jacketjie.astimes.R;
import jacketjie.astimes.adapter.InformalEssayListAdapter;
import jacketjie.astimes.custom.AutoLoadMoreListView;
import jacketjie.astimes.greenDao.ATInformalEssay;
import jacketjie.astimes.greenDao.GreenDaoUtils;
import jacketjie.astimes.views.fragments.MainSecondFragment;

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

    private LocalBroadcastManager lbm;
    private BroadcastReceiver recevie = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MainSecondFragment.UPDATE_ESSAY_LIST_ACTION.equals(intent.getAction())){
//                Toast.makeText(getApplicationContext(), "需要更新", Toast.LENGTH_LONG).show();
                showDialog();
                new LoadAllDataTask(true).execute();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_second_fragment);
        lbm = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter(MainSecondFragment.UPDATE_ESSAY_LIST_ACTION);
        lbm.registerReceiver(recevie, filter);
        showDialog();
        initViews();
        setEventListener();
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InformalEssayActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setEventListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ATInformalEssay essay = mDatas.get(position);
                Intent intent = new Intent(getApplicationContext(), SimpleWebViewActivity.class);
                intent.putExtra("ATINFORMALESSAY_DETAILS", essay);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MyInformalEssayDetailActivity.this).setTitle("删除记录").setMessage("确认删除？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GreenDaoUtils.deleteEssay(getApplicationContext(),mDatas.get(position));
                        mDatas.remove(position);
                        essayListAdapter.notifyDataSetChanged();

                    }
                }).setNegativeButton("取消", null).create().show();
                return true;
            }
        });

//        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
//                switch (index) {
//                    case 0:
//                        mDatas.remove(position);
//                        essayListAdapter.notifyDataSetChanged();
//                        GreenDaoUtils.deleteEssay(getApplicationContext(),mDatas.get(position));
//                        break;
//                }
//            }
//        });
//
//        SwipeMenuCreator creator = new SwipeMenuCreator() {
//
//            @Override
//            public void create(SwipeMenu menu) {
//                // Create different menus depending on the view type
//                // create "delete" item
//                SwipeMenuItem deleteItem = new SwipeMenuItem(
//                        getApplicationContext());
//                // set item background
//                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
//                        0x3F, 0x25)));
//                // set item width
//                deleteItem.setWidth(dp2px(90));
//                // set a icon
//                deleteItem.setIcon(R.drawable.ic_delete);
//                // add to menu
//                menu.addMenuItem(deleteItem);
//            }
//        };
//
//        listView.setMenuCreator(creator);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadAllDataTask(true).execute();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lbm.unregisterReceiver(recevie);
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
            refreshLayout.setRefreshing(false);
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
