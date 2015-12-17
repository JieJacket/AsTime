package jacketjie.astimes.adapter;

import android.content.Context;

import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.List;

import jacketjie.astimes.AsTimeApp;
import jacketjie.astimes.R;
import jacketjie.astimes.greenDao.ATInformalEssay;
import jacketjie.astimes.utils.ScreenUtils;

/**
 * GridView 的适配器
 * Created by Administrator on 2015/12/11.
 */
public class InformalEssayListAdapter extends CommonAdapter<ATInformalEssay> {
    private ImageSize imageSize;

    public InformalEssayListAdapter(Context context, List<ATInformalEssay> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        int width = ScreenUtils.getScreenWidth(context);
    }

    @Override
    public void convert(ViewHolder helper, ATInformalEssay item) {
        helper.setText(R.id.id_second_item_title, item.getATIETitle());
        String name = "AsTime";
        if (AsTimeApp.getCurATUser()!=null){
            name = AsTimeApp.getCurATUser().getUserNickName();
        }
        helper.setText(R.id.id_second_item_name, name);
        helper.setText(R.id.id_second_item_date, item.getATIEReleaseDate());
        helper.setImageByImageLoader(R.id.id_second_item_icon, item.getATIEImageUrl());
    }
}
