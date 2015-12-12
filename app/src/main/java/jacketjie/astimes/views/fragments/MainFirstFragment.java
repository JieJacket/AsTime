package jacketjie.astimes.views.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jacketjie.astimes.R;
import jacketjie.astimes.adapter.MainFirstViewPagerAdapter;
import jacketjie.astimes.utils.ScreenUtils;

/**
 * Created by Administrator on 2015/12/9.
 */
public class MainFirstFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewGroup dispalyView;
    private ViewPager mViewPager;
    private String[] tabNames;
    private List<TextView> textViews;
    private LinearLayout tabsLinear;
    private LinearLayout tabSlider;
    private int currentPager = 0;
    private int offset;
    private MainFirstViewPagerAdapter adapter;
    private EssayFragment essayFragment;
    private RadioStationFragment radioStationFragment;
    private PictureFragment pictureFragment;
    private List<Fragment> fragments;

    private int sliderWidth;
    private int tabsWidth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.main_first_fragment,container,false);
//        return view;
//    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (dispalyView == null) {
            dispalyView = (ViewGroup) inflater.inflate(R.layout.main_first_fragment, container, false);
            initViews(dispalyView);
            initDatas();
        } else {
            ViewGroup parent = (ViewGroup) dispalyView.getParent();
            if (parent != null) {
                parent.removeView(dispalyView);
            }
        }
//        showProgress();
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                hideProgressBar();
//            }
//        }, 5000);
        return dispalyView;
    }

    private void initDatas() {
        tabNames = getResources().getStringArray(R.array.main_first_tabs_name);
        textViews = new ArrayList<TextView>();
        fragments = new ArrayList<Fragment>();
        if (essayFragment == null) {
            essayFragment = new EssayFragment();
            fragments.add(essayFragment);
        }
        if (radioStationFragment == null) {
            radioStationFragment = new RadioStationFragment();
            fragments.add(radioStationFragment);
        }
        if (pictureFragment == null) {
            pictureFragment = new PictureFragment();
            fragments.add(pictureFragment);
        }

        adapter = new MainFirstViewPagerAdapter(getChildFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);

        for (int i = 0; i < tabNames.length; i++) {
            TextView textView = new TextView(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            textView.setLayoutParams(lp);
            textView.setText(tabNames[i]);
            textView.setId(i);
            textView.setCompoundDrawablePadding(5);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            tabsLinear.addView(textView);
            textViews.add(textView);
            textView.setOnClickListener(this);
        }


        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabSlider.getLayoutParams();
        sliderWidth = (int) (tabsWidth * 1.0f / tabNames.length);
        lp.width = sliderWidth;
        tabSlider.setLayoutParams(lp);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {

        return super.onCreateAnimation(transit, enter, nextAnim);
    }


    private void initViews(ViewGroup dispalyView) {
        tabsLinear = (LinearLayout) dispalyView.findViewById(R.id.id_personal_title_content);
        mViewPager = (ViewPager) dispalyView.findViewById(R.id.id_first_view_pager);
        tabSlider = (LinearLayout) dispalyView.findViewById(R.id.fun_slider_line);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabsLinear.getLayoutParams();
        int screenW = ScreenUtils.getScreenWidth(getActivity());
        offset = (int) (20 * ScreenUtils.getDensityDpi(getActivity()) * 1.0f / 160);
        tabsWidth = (int) (screenW * 0.6);
        lp.width = tabsWidth;
        lp.leftMargin = offset;
        tabsLinear.setLayoutParams(lp);

        mViewPager.addOnPageChangeListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case 0:
                mViewPager.setCurrentItem(0);
                break;
            case 1:
                mViewPager.setCurrentItem(1);
                break;
            case 2:
                mViewPager.setCurrentItem(2);
                break;
        }
    }

    private void setTabAndPagerBuPos(int pos) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabSlider.getLayoutParams();
        lp.leftMargin = (int) (offset + sliderWidth * position + sliderWidth
                * positionOffset);
        tabSlider.setLayoutParams(lp);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
