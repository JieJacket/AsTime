package jacketjie.astimes.views.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jacketjie.astimes.AsTimeApp;
import jacketjie.astimes.R;
import jacketjie.astimes.adapter.MySelectionsAdapter;
import jacketjie.astimes.custom.DividerItemDecoration;
import jacketjie.astimes.greenDao.ATUser;
import jacketjie.astimes.model.Selection;
import jacketjie.astimes.utils.interfaces.OnRecyclerOnItemClickListener;
import jacketjie.astimes.views.activities.LoginActivity;
import jacketjie.astimes.views.activities.MyInformalEssayDetailActivity;
import jacketjie.astimes.views.activities.UserInfoDetailActivity;

/**
 * Created by Administrator on 2015/12/9.
 */
public class MainForthFragment extends BaseFragment {
    private View v;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private RecyclerView recyclerView;
    private MySelectionsAdapter adapter;
    private View personInfoView;

    private CircleImageView personalInfoIcon;

    private TextView personalInfoName;

    private TextView personalSignature;

    private LocalBroadcastManager lbm;

    private UpdateUseInfoReceiver updateUseInfoReceiver = null;

    private int[] selectionRes = {R.drawable.personal_info_1, R.drawable.personal_info_2, R.drawable.personal_info_3, R.drawable.personal_info_4, R.drawable.personal_info_5};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateUseInfoReceiver = new UpdateUseInfoReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("UPDATE_USER_RECEIVER");
        lbm = LocalBroadcastManager.getInstance(getActivity());
        lbm.registerReceiver(updateUseInfoReceiver, filter);
    }

    /**
     * 更新用户信息广播
     */
    class UpdateUseInfoReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("UPDATE_USER_RECEIVER".equals(action)) {
                updateUserInfo();
            }
        }
    }

    private void updateUserInfo() {
        ATUser user = AsTimeApp.getCurATUser();
        if (user != null){
            personalInfoName.setText(TextUtils.isEmpty(user.getUserNickName())?"AsTime":user.getUserNickName());
            if (TextUtils.isEmpty(user.getUserIcon())){
                personalInfoIcon.setImageResource(R.drawable.as_time_icon);
            }else{
                ImageLoader.getInstance().displayImage(user.getUserIcon(),personalInfoIcon);
            }
            if (TextUtils.isEmpty(user.getUserSignature())){
                personalSignature.setText(R.string.default_signature);
            }else {
                personalSignature.setText(user.getUserSignature());
            }
        }else {
            personalInfoName.setText("AsTime");
            personalInfoIcon.setImageResource(R.drawable.as_time_icon);
            personalSignature.setText(R.string.default_signature);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.main_forth_fragment, container, false);
            setupToolbar(v);
            setupRecyclerView(v);
            setupCollapsingToolbar(v);
            initInfo(v);
        } else {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null) {
                parent.removeView(v);
            }
        }
        return v;
    }

    private void initInfo(View v) {
        personInfoView = v.findViewById(R.id.id_login);
        personalInfoIcon = (CircleImageView) v.findViewById(R.id.id_my_icon);
        personalInfoName = (TextView) v.findViewById(R.id.id_name);
        personalSignature = (TextView) v.findViewById(R.id.id_user_signature);
        personInfoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AsTimeApp.getCurATUser() == null){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
                }else{
                    Intent intent = new Intent(getActivity(),UserInfoDetailActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);

                }
            }
        });
        ATUser user = AsTimeApp.getCurATUser();
        if (user != null) {
            if (!TextUtils.isEmpty(user.getUserIcon())) {
                ImageLoader.getInstance().displayImage(user.getUserIcon(), personalInfoIcon);
            }
            personalInfoName.setText(TextUtils.isEmpty(user.getUserName()) ? "" : user.getUserName());
            if (!TextUtils.isEmpty(user.getUserSignature())) {
                personalSignature.setText(user.getUserSignature());
            }
        }

    }

    private void setupToolbar(View v) {
        collapsingToolbar = (CollapsingToolbarLayout) v.findViewById(
                R.id.collapse_toolbar);

        collapsingToolbar.setTitleEnabled(false);
    }

    private void setupRecyclerView(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.id_recyclerview);
        final List<Selection> selections = new ArrayList<Selection>();
        String[] strings = getResources().getStringArray(R.array.main_forth_selections);
        for (int i = 0; i < strings.length; i++) {
            Selection selection = new Selection();
            selection.setTitle(strings[i]);
            selection.setResId(selectionRes[i]);
            selections.add(selection);
        }
        adapter = new MySelectionsAdapter(getActivity(), selections);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnRecyclerOnItemClickListener() {
            @Override
            public void onItemListener(View view, int position) {
                    if (AsTimeApp.getCurATUser() != null){
                        switch (position){
                            case 0:
                                break;
                            case 1:
                                break;
                            case 2:
                                Intent intent = new Intent(getActivity(),MyInformalEssayDetailActivity.class);
                                startActivity(intent);
                                break;
                            case 3:
                                break;
                            case 4:
                                break;
                        }
                    }
            }
        });
    }

    private void setupCollapsingToolbar(View v) {
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(null);
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getStringArray(R.array.main_tabs_name)[3]);
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lbm.unregisterReceiver(updateUseInfoReceiver);
    }
}
