package jacketjie.astimes.views.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jacketjie.astimes.R;
import jacketjie.astimes.custom.AnimFragmentTabHost;
import jacketjie.astimes.utils.ScreenUtils;
import jacketjie.astimes.utils.StatusBarUtil;
import jacketjie.astimes.views.fragments.MainFirstFragment;
import jacketjie.astimes.views.fragments.MainForthFragment;
import jacketjie.astimes.views.fragments.MainSecondFragment;
import jacketjie.astimes.views.fragments.MainThirdFragment;

/**
 * Created by Administrator on 2015/12/10.
 */
public class AsTimeMainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private AnimFragmentTabHost fragmentTabHost;
    private String[] tabNames;
    private MainFirstFragment firstFragment;
    private MainSecondFragment secondFragment;
    private MainThirdFragment thirdFragment;
    private MainForthFragment forthFragment;
    private int curentId = 0;
    private int[] defaultMainTabsRes = {R.drawable.main_pager_default, R.drawable.my_records_default, R.drawable.wei_yu_default, R.drawable.myself_default};
    private int[] selectedMainTabsRes = {R.drawable.main_pager, R.drawable.my_records, R.drawable.wei_yu, R.drawable.myself};
    private int DEFAULT_TAB_COLOR, SELECTED_TAB_COLOR;
    private List<Fragment> fragments;
    private Class[] tabsFragment = {MainFirstFragment.class, MainSecondFragment.class, MainThirdFragment.class, MainForthFragment.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.showStatusBar(this, R.color.colorPrimary);
        setContentView(R.layout.activity_main);
        initViews();
        initDatas();
        setEventListener();
    }

    /**
     * 初始化界面
     */
    private void initViews() {
        fragmentTabHost = (AnimFragmentTabHost) findViewById(R.id.id_tabhost);
//        tabLayout = (TabLayout) findViewById(R.id.id_main_tabs);
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        fragments = new ArrayList<Fragment>();
        if (firstFragment == null) {
            firstFragment = new MainFirstFragment();
            fragments.add(firstFragment);
        }
        if (secondFragment == null) {
            secondFragment = new MainSecondFragment();
            fragments.add(secondFragment);
        }
        if (thirdFragment == null) {
            thirdFragment = new MainThirdFragment();
            fragments.add(thirdFragment);
        }
        if (forthFragment == null) {
            forthFragment = new MainForthFragment();
            fragments.add(forthFragment);
        }
        DEFAULT_TAB_COLOR = getResources().getColor(R.color.main_tab_default_color);
        SELECTED_TAB_COLOR = getResources().getColor(R.color.colorPrimary);
        tabNames = getResources().getStringArray(R.array.main_tabs_name);
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        for (int i = 0; i < tabNames.length; i++) {
            TabHost.TabSpec tabSpec = fragmentTabHost.newTabSpec(tabNames[i]).setIndicator(customTab(i));
            fragmentTabHost.addTab(tabSpec, tabsFragment[i], null);
        }
        fragmentTabHost.getTabWidget().setDividerDrawable(null);
//        for (int i = 0; i < tabNames.length; i++) {
//            TabLayout.Tab tab = tabLayout.newTab();
//            tab.setCustomView(customTab(i));
//            tabLayout.addTab(tab);
//        }
//        tabLayout.setSelectedTabIndicatorHeight(0);
        resetTabStatus(curentId);
    }

    /**
     * 设置事件
     */
    private void setEventListener() {
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                curentId = fragmentTabHost.getCurrentTab();
                resetTabStatus(curentId);
            }
        });
//
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                curentId = tab.getPosition();
//                fragmentTabHost.setCurrentTab(curentId);
//                resetTabStatus(curentId);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }

    /**
     * 自定义tab样式
     *
     * @param index
     * @return
     */
    private View customTab(int index) {
        View view = getLayoutInflater().inflate(R.layout.custom_tab_view, null);
        TextView tab = (TextView) view.findViewById(R.id.id_single_tab);
        Drawable topDrawable = getResources().getDrawable(defaultMainTabsRes[index]);
        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
        tab.setCompoundDrawables(null, topDrawable, null, null);
        tab.setText(tabNames[index]);
        return view;
    }

    public int getTabsHeight() {
        return ScreenUtils.getViewHeight(fragmentTabHost.getTabWidget()) + ScreenUtils.getViewHeight(findViewById(R.id.id_top_layout));
    }

    /**
     * 重置tab的状态
     *
     * @param pos
     */
    private void resetTabStatus(int pos) {
        TabWidget tabWidget =  fragmentTabHost.getTabWidget();
        for (int i = 0; i < tabNames.length; i++) {
            View tab = tabWidget.getChildAt(i);
            TextView singleTab = (TextView) tab.findViewById(R.id.id_single_tab);
            if (pos == i) {
                Drawable selectDrawable = getResources().getDrawable(selectedMainTabsRes[i]);
                selectDrawable.setBounds(0, 0, selectDrawable.getMinimumWidth(), selectDrawable.getMinimumHeight());
                singleTab.setTextColor(SELECTED_TAB_COLOR);
                singleTab.setCompoundDrawables(null, selectDrawable, null, null);
            } else {
                Drawable defauleDrawable = getResources().getDrawable(defaultMainTabsRes[i]);
                defauleDrawable.setBounds(0, 0, defauleDrawable.getMinimumWidth(), defauleDrawable.getMinimumHeight());
                singleTab.setTextColor(DEFAULT_TAB_COLOR);
                singleTab.setCompoundDrawables(null, defauleDrawable, null, null);
            }
        }
//        for (int i=0;i<tabLayout.getTabCount();i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            TextView singleTab = (TextView) tab.getCustomView().findViewById(R.id.id_single_tab);
//            if (pos == i){
//                Drawable selectDrawable = getResources().getDrawable(selectedMainTabsRes[i]);
//                selectDrawable.setBounds(0, 0, selectDrawable.getMinimumWidth() / 2, selectDrawable.getMinimumHeight() / 2);
//                singleTab.setTextColor(SELECTED_TAB_COLOR);
//                singleTab.setCompoundDrawables(null,selectDrawable,null,null);
//            }else{
//                Drawable defauleDrawable = getResources().getDrawable(defaultMainTabsRes[i]);
//                defauleDrawable.setBounds(0, 0, defauleDrawable.getMinimumWidth() / 2, defauleDrawable.getMinimumHeight() / 2);
//                singleTab.setTextColor(DEFAULT_TAB_COLOR);
//                singleTab.setCompoundDrawables(null, defauleDrawable, null, null);
//            }
//        }
    }

    private void setDefaultFragment() {
        if (firstFragment == null) {
            firstFragment = new MainFirstFragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.id_main_content, firstFragment);
        transaction.commit();
    }

}