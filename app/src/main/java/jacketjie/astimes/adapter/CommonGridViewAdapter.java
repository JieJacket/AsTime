package jacketjie.astimes.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.List;

import jacketjie.astimes.R;
import jacketjie.astimes.model.Essay;
import jacketjie.astimes.utils.ScreenUtils;

/**
 * GridView 的适配器
 * Created by Administrator on 2015/12/11.
 */
public class CommonGridViewAdapter extends CommonAdapter<Essay> {
    private final int imageWith;
    private ImageSize imageSize;

    public CommonGridViewAdapter(Context context, List<Essay> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        int width = ScreenUtils.getScreenWidth(context);
        imageWith = (width - 15 * ScreenUtils.getDensityDpi(context) / 160) / 2;
        imageSize = new ImageSize(imageWith, (int) (imageWith * 1.0f / 1.6));
    }

    @Override
    public void convert(ViewHolder helper, Essay item) {
        ViewGroup.LayoutParams lp = helper.getConvertView().getLayoutParams();
        lp.width = imageWith;
        lp.height = (int) (imageWith * 1.f / 1.6);
        helper.getConvertView().setLayoutParams(lp);
        helper.setText(R.id.id_essay_item_title, item.getEssayName());
        helper.setImageByImageLoader(R.id.id_essay_item_image, item.getDisplayUrl());
    }
}
