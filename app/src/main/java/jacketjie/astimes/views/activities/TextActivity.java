package jacketjie.astimes.views.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import jacketjie.astimes.R;
import jacketjie.astimes.custom.refreshlayout.CustomRefreshLayout;

/**
 * Created by Administrator on 2015/12/29.
 */
public class TextActivity extends AppCompatActivity {
    private String []test = {"优美散文", "短篇小说", "美文日赏", "青春碎碎念", "左岸阅读", "慢文艺", "诗歌精选", "经典语录", "陪你颠沛流离", "花边阅读", "终点书栈", "译言", "佳人阅读", "美文社", "悦旅行", "读美文", "青年周摘", "二更食堂", "不止读书", "读者投稿"};
    private ListView listView;
    private CustomRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        listView = (ListView) findViewById(R.id.id_listview);
        refreshLayout = (CustomRefreshLayout) findViewById(R.id.id_refresh);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,test);
        listView.setAdapter(adapter);
    }
}
