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
public class PictureFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ViewGroup displayView;
    private View dialogView;
    private GridView essayGridView;
    private CommonGridViewAdapter essayAdapter;
    private List<Essay> mDatas;
    private List<String> imageUrls;

    private String[] titles = {"摄影作品", "每日美图", "唯美图片", "可爱图片", "文字图片", "欧美图片", "情侣图片", "动漫图片", "明星图片", "唯美大图", "创意图片", "壹心理治愈图"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (displayView == null){
            displayView = (ViewGroup) inflater.inflate(R.layout.picture_fragment_layout,container,false);
            dialogView = displayView.findViewById(R.id.id_base_progressbar);
            init(displayView);
        }else{
            ViewGroup parent = (ViewGroup) displayView.getParent();
            if (parent != null){
                parent.removeView(displayView);
            }
        }
        return displayView;
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


    public void setContentView(int res){
        View v = getActivity().getLayoutInflater().inflate(res,null);
        if (v != null && displayView!=null) {
            displayView.addView(v,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            dialogView.bringToFront();
        }
    }
    /*public View onCreateView(int resId){
        FractionTranslateLayout ftl = new FractionTranslateLayout(getActivity());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ftl.setLayoutParams(lp);
        dialogView = getActivity().getLayoutInflater().inflate(R.layout.progressbar_layout,null);
        ftl.addView(dialogView);
        View content = getActivity().getLayoutInflater().inflate(resId,null);
        ftl.addView(content);
        return ftl;
    }*/
    public void showProgress(){
        if (dialogView != null)
            dialogView.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar(){
        if (dialogView != null)
            dialogView.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Essay essay = mDatas.get(position);
        Intent intent = new Intent(getActivity(), EssayDetailsActivity.class);
        intent.putExtra("ESSAY_DETAIL",essay);
        startActivity(intent);

    }
}
