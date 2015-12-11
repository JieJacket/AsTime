package jacketjie.astimes.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.util.ArrayList;
import java.util.List;

import jacketjie.astimes.R;
import jacketjie.astimes.adapter.CommonGridViewAdapter;
import jacketjie.astimes.adapter.Essay;
import jacketjie.astimes.views.activities.EssayDetailsActivity;

/**
 * Created by Administrator on 2015/12/9.
 */
public class RadioStationFragment extends Fragment implements AdapterView.OnItemClickListener {

    private GridView essayGridView;
    private CommonGridViewAdapter essayAdapter;
    private List<Essay> mDatas;
    private List<String> imageUrls;

    private String[] titles = {"心理FM", "邻居的耳朵", "陌生人广播", "柠檬香香", "聆听世界的声音", "吻安七分MHz", "旮旯里的豆豆", "蔷薇岛屿网络电台", "童心未泯的文艺甜", "娜娜情感语录", "岁月如歌"};
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.essay_fragment_layout, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        essayGridView = (GridView) view.findViewById(R.id.id_essay_content);
        mDatas = new ArrayList<Essay>();
        imageUrls = new ArrayList<String>();
        for (int i = 1; i <= titles.length; i++) {
            imageUrls.add(ImageDownloader.Scheme.ASSETS.wrap("test_icon.jpg"));
        }
        for (int i = 0; i < titles.length; i++) {
            Essay essay = new Essay();
            essay.setEssayId(i);
            essay.setDisplayUrl(imageUrls.get(i));
            essay.setEssayName(titles[i]);
            essay.setGallery(imageUrls);
            mDatas.add(essay);
        }
        essayAdapter = new CommonGridViewAdapter(getActivity(), mDatas, R.layout.essay_grid_item);
        essayGridView.setAdapter(essayAdapter);
        essayGridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Essay essay = mDatas.get(position);
        Intent intent = new Intent(getActivity(), EssayDetailsActivity.class);
        intent.putExtra("ESSAY_DETAIL", essay);
        startActivity(intent);
    }
}
