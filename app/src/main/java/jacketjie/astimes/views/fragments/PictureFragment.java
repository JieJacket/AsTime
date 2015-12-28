package jacketjie.astimes.views.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.nostra13.universalimageloader.core.download.ImageDownloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jacketjie.astimes.R;
import jacketjie.astimes.adapter.CommonGridViewAdapter;
import jacketjie.astimes.model.Essay;
import jacketjie.astimes.utils.HttpUtils;
import jacketjie.astimes.views.activities.AsTimeMainActivity;
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

    private String ARTICLES_URL;

    private LoadDataTask loadTask;

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
            int j = (i+2) % 11;
            imageUrls.add(ImageDownloader.Scheme.ASSETS.wrap((j+1) + ".jpg"));
        }
        for (int i = 0; i < titles.length; i++) {
            Essay essay = new Essay();
            essay.setEssayId(i+"");
            essay.setDisplayUrl(imageUrls.get(i));
            essay.setEssayName(titles[i]);
            mDatas.add(essay);
        }
        int height = ((AsTimeMainActivity)getActivity()).getTabsHeight();
        essayAdapter = new CommonGridViewAdapter(getActivity(), mDatas, height,R.layout.essay_grid_item);
        essayGridView.setAdapter(essayAdapter);
        essayGridView.setOnItemClickListener(this);

        ARTICLES_URL = getString(R.string.as_times_address) + getString(R.string.image_address);
        loadTask = new LoadDataTask();
        loadTask.execute(ARTICLES_URL);

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
        getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
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
    /**
     * 请求数据
     */
    class LoadDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String result = HttpUtils.doGet(url);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hiddenDialog(displayView);
            if (TextUtils.isEmpty(result))
                return;
            try {
                JSONObject jo = new JSONObject(result);
                int ret = jo.getInt("code");
                if (ret == 200) {
//                    JSONArray ja = jo.getJSONArray("data");
                    JSONArray array = jo.getJSONArray("data");
                    List<Essay> results = new ArrayList<Essay>();
                    if (array != null) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            Essay essay = new Essay();
                            essay.setEssayId(object.getString("id"));
                            essay.setEssayName(object.getString("title"));
                            essay.setDisplayUrl(object.getString("cover"));
                            essay.setType(2);
                            results.add(essay);
                        }
//                    Iterator iterator = object.keys();
//                    while (iterator.hasNext()){
//                        Essay essay = new Essay();
//                        String key = (String) iterator.next();
//                        if (!TextUtils.isEmpty(key)){
//                            JSONObject o = object.getJSONObject(key);
//                            essay.setDisplayUrl(TextUtils.isEmpty(o.getString("cover"))?"":o.getString("cover"));
//                            essay.setEssayName(TextUtils.isEmpty(o.getString("title"))?"":o.getString("title"));
//                            essay.setEssayId(key);
//                            results.add(essay);
//                        }
//                    }
//                    if (ja != null){
//                        for (int i=0;i<ja.length();i++){
//                            JSONObject object = ja.getJSONObject(i);
//                            Essay essay = new Essay();
//                            essay.setEssayId(object.getInt("id"));
//                            essay.setEssayName(object.getString("title"));
//                            essay.setDisplayUrl(object.getString("cover"));
//                            essay.setEssayContent(object.getString("content"));
//                            results.add(essay);
//                        }

                    }
                    mDatas.clear();
                    mDatas.addAll(results);
                    essayAdapter.notifyDataSetChanged();
//                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
