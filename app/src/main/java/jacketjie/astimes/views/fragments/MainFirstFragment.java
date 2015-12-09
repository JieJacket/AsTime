package jacketjie.astimes.views.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jacketjie.astimes.R;

/**
 * Created by Administrator on 2015/12/9.
 */
public class MainFirstFragment extends BaseFragment {
    private ViewGroup dispalyView;

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
        if (dispalyView == null){
            dispalyView = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
            setContentView(R.layout.main_first_fragment);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideProgressBar();
                }
            }, 10000);
        }else{
            ViewGroup parent = (ViewGroup) dispalyView.getParent();
            if (parent != null){
                parent.removeView(dispalyView);
            }
        }
        return dispalyView;
    }

    @Override
    public void setContentView(int res) {
        super.setContentView(res);
    }

    class loadAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            hideProgressBar();
        }
    }
}
