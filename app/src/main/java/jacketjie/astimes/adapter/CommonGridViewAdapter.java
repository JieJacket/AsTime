package jacketjie.astimes.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import jacketjie.astimes.R;
import jacketjie.astimes.model.Essay;
import jacketjie.astimes.utils.ScreenUtils;

/**
 * GridView 的适配器
 * Created by Administrator on 2015/12/11.
 */
public class CommonGridViewAdapter extends CommonAdapter<Essay> {
    private final int imageWith, imageHeight;
//    private ImageSize imageSize;

    public CommonGridViewAdapter(Context context, List<Essay> mDatas, int tabheight, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        int width = ScreenUtils.getScreenWidth(context);
        //（宽度为屏幕的宽度 - 左右中间的间隔）/ 2
        imageWith = (width - 15 * ScreenUtils.getDensityDpi(context) / 160) / 2;
        //（高度为屏幕的高度 - 状态栏高度 - APPlayout高度 - FragmentTabHost的TabWidget高度 -图片间隔*5 - 1)）/4
        imageHeight = (int) ((ScreenUtils.getScreenHeight(context) - ScreenUtils.getStatusHeight(context) - 24 * ScreenUtils.getDensityDpi(context) / 160 - tabheight) * 1.0f / 4);
//        imageSize = new ImageSize(imageWith, (int) (imageWith * 1.0f / 1.6));
    }

    @Override
    public void convert(ViewHolder helper, Essay item) {
        ImageView iv = helper.getView(R.id.id_essay_item_image);
        ViewGroup.LayoutParams lp = iv.getLayoutParams();
        lp.width = imageWith;
        lp.height = imageHeight;
        iv.setLayoutParams(lp);
        helper.setText(R.id.id_essay_item_title, item.getEssayName());
        helper.setImageByImageLoader(R.id.id_essay_item_image, item.getDisplayUrl());
    }
}
