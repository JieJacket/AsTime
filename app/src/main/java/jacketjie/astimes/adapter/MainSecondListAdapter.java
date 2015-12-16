package jacketjie.astimes.adapter;

import android.content.Context;

import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.List;

import jacketjie.astimes.R;
import jacketjie.astimes.model.EssayDetail;
import jacketjie.astimes.utils.ScreenUtils;

/**
 * GridView 的适配器
 * Created by Administrator on 2015/12/11.
 */
public class MainSecondListAdapter extends CommonAdapter<EssayDetail> {
    private ImageSize imageSize;

    public MainSecondListAdapter(Context context, List<EssayDetail> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        int width = ScreenUtils.getScreenWidth(context);
    }

    @Override
    public void convert(ViewHolder helper, EssayDetail item) {
        helper.setText(R.id.id_second_item_title, item.getDetailTitle());
        helper.setText(R.id.id_second_item_name, item.getDetailType());
        helper.setText(R.id.id_second_item_date, item.getDetailDate());
        helper.setImageByImageLoader(R.id.id_second_item_icon, item.getDetailUrl());
    }
}
