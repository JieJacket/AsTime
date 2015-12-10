package jacketjie.astimes.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2015/12/10.
 */
public class MainFirstViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public MainFirstViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }
    @Override
    public int getCount() {
        return  fragments.size();
    }
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
}
