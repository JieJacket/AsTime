package jacketjie.astimes.views.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import jacketjie.astimes.R;
import jacketjie.astimes.adapter.Essay;
import jacketjie.astimes.custom.BaseActivity;

/**
 * 美文详情
 * Created by Administrator on 2015/12/11.
 */
public class EssayDetailsActivity extends BaseActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.essay_detail_layout);
        showDialog();
        initViews();
        new LoadDataTask().execute("");
    }

    private void initViews() {
        Essay essay = getIntent().getParcelableExtra("ESSAY_DETAIL");
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle(essay.getEssayName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    class LoadDataTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            hiddenDialog();
        }
    }
}
