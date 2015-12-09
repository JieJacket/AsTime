package jacketjie.astimes.views.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jacketjie.astimes.R;
import jacketjie.astimes.views.fragments.MainFirstFragment;
import jacketjie.astimes.views.fragments.MainForthFragment;
import jacketjie.astimes.views.fragments.MainSecondFragment;
import jacketjie.astimes.views.fragments.MainThirdFragment;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private String[] tabNames;
    private MainFirstFragment firstFragment;
    private MainSecondFragment secondFragment;
    private MainThirdFragment thirdFragment;
    private MainForthFragment forthFragment;
    private int curentId = 0;
    private int[] defaultMainTabsRes = {R.drawable.ic_home_white,R.drawable.ic_home_white,R.drawable.ic_home_white,R.drawable.ic_home_white};
    private int[]selectedMainTabsRes = {R.drawable.ic_home_grary,R.drawable.ic_home_grary,R.drawable.ic_home_grary,R.drawable.ic_home_grary};
    private int DEFAULT_TAB_COLOR,SELECTED_TAB_COLOR;
    private List fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initDatas();
        setEventListener();
    }

    /**
     * 初始化界面
     */
    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.id_main_tabs);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        fragments = new ArrayList();
        fragments.add(firstFragment);
        fragments.add(secondFragment);
        fragments.add(thirdFragment);
        fragments.add(forthFragment);
        DEFAULT_TAB_COLOR = getResources().getColor(R.color.main_tab_default_color);
        SELECTED_TAB_COLOR = getResources().getColor(R.color.colorPrimary);
        tabNames = getResources().getStringArray(R.array.main_tabs_name);
        for (int i = 0; i < tabNames.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setCustomView(customTab(i));
            tabLayout.addTab(tab);
        }
        tabLayout.setSelectedTabIndicatorHeight(0);
        setDefaultFragment();
        resetTabStatus(curentId);
    }

    /**
     * 设置事件
     */
    private void setEventListener() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                resetTabStatus(pos);
                setContentByPos(pos);
                curentId = pos;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 自定义tab样式
     * @param index
     * @return
     */
    private View customTab(int index) {
        View view = getLayoutInflater().inflate(R.layout.custom_tab_view, null);
        TextView tab = (TextView) view.findViewById(R.id.id_single_tab);
        Drawable topDrawable = getResources().getDrawable(defaultMainTabsRes[index]);
        topDrawable.setBounds(0,0,topDrawable.getMinimumWidth()/2,topDrawable.getMinimumHeight()/2);
        tab.setCompoundDrawables(null,topDrawable,null,null);
        tab.setText(tabNames[index]);
        return view;
    }

    /**
     * 重置tab的状态
     * @param pos
     */
    private void resetTabStatus(int pos){
        for (int i=0;i<tabLayout.getTabCount();i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            TextView singleTab = (TextView) tab.getCustomView().findViewById(R.id.id_single_tab);
            if (pos == i){
                Drawable selectDrawable = getResources().getDrawable(selectedMainTabsRes[i]);
                selectDrawable.setBounds(0, 0, selectDrawable.getMinimumWidth() / 2, selectDrawable.getMinimumHeight() / 2);
                singleTab.setTextColor(SELECTED_TAB_COLOR);
                singleTab.setCompoundDrawables(null,selectDrawable,null,null);
            }else{
                Drawable defauleDrawable = getResources().getDrawable(defaultMainTabsRes[i]);
                defauleDrawable.setBounds(0, 0, defauleDrawable.getMinimumWidth() / 2, defauleDrawable.getMinimumHeight() / 2);
                singleTab.setTextColor(DEFAULT_TAB_COLOR);
                singleTab.setCompoundDrawables(null, defauleDrawable, null, null);
            }
        }
    }

    private void setDefaultFragment() {
        if (firstFragment == null) {
            firstFragment = new MainFirstFragment();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.id_main_content, firstFragment);
        transaction.commit();
    }

    /**
     * 点击tab切换fragment
     * @param pos
     */
    private void setContentByPos(int pos) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (curentId == pos)
            return;
        switch (pos) {
            case 0:
                if (firstFragment == null) {
                    firstFragment = new MainFirstFragment();
                }
                if (!firstFragment.isAdded()){
                    transaction.replace(R.id.id_main_content, firstFragment);
                    transaction.commit();
                }

                break;
            case 1:
                if (secondFragment == null) {
                    secondFragment = new MainSecondFragment();
                }
                if (!secondFragment.isAdded()){

                    transaction.replace(R.id.id_main_content, secondFragment);
                transaction.commit();
                }
                break;
            case 2:
                if (thirdFragment == null) {
                    thirdFragment = new MainThirdFragment();
                }
                if (!thirdFragment.isAdded()){
                transaction.replace(R.id.id_main_content, thirdFragment);
                transaction.commit();
                }
                break;
            case 3:
                if (forthFragment == null) {
                    forthFragment = new MainForthFragment();
                }
                if (!forthFragment.isAdded()){
                    transaction.replace(R.id.id_main_content, forthFragment);
                    transaction.commit();
                }
                break;
        }
    }

}
