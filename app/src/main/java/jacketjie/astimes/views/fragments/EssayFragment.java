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
 * Created by Administrator on 2015/12/10.
 * 美文Fragment
 */
public class EssayFragment extends Fragment implements AdapterView.OnItemClickListener {
    private GridView essayGridView;
    private CommonGridViewAdapter essayAdapter;
    private List<Essay> mDatas;
    private List<String> imageUrls;

    private String[] titles = {"优美散文", "短篇小说", "美文日赏", "青春碎碎念", "左岸阅读", "慢文艺", "诗歌精选", "经典语录", "陪你颠沛流离", "花边阅读", "终点书栈", "译言", "佳人阅读", "美文社", "悦旅行", "读美文", "青年周摘", "二更食堂", "不止读书", "读者投稿"};
    private View view;

    private String ARTICLES_URL;

    private LoadDataTask loadTask;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.essay_fragment_layout, container, false);
            showDialog(view);
            init(view);
            initDatas();
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        return view;
    }

    private void init(View view) {
        essayGridView = (GridView) view.findViewById(R.id.id_essay_content);
        mDatas = new ArrayList<Essay>();
        int height = ((AsTimeMainActivity) getActivity()).getTabsHeight();
        essayAdapter = new CommonGridViewAdapter(getActivity(), mDatas, height, R.layout.essay_grid_item);
        essayGridView.setAdapter(essayAdapter);
        essayGridView.setOnItemClickListener(this);
    }

    private void initDatas() {
        ARTICLES_URL = getString(R.string.as_times_address) + getString(R.string.article_address);
        loadTask = new LoadDataTask();
        loadTask.execute(ARTICLES_URL);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Essay essay = mDatas.get(position);
        Intent intent = new Intent(getActivity(), EssayDetailsActivity.class);
        intent.putExtra("ESSAY_DETAIL", essay);
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
            hiddenDialog(view);
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
                            essay.setType(0);
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
