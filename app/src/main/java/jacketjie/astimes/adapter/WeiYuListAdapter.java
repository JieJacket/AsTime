package jacketjie.astimes.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import jacketjie.astimes.R;
import jacketjie.astimes.utils.ScreenUtils;
import jacketjie.astimes.model.WeiYu;

/**
 * GridView 的适配器
 * Created by Administrator on 2015/12/11.
 */
public class WeiYuListAdapter extends CommonAdapter<WeiYu> {
    private final int imageWidth;
    private String default_signature;

    public WeiYuListAdapter(Context context, List<WeiYu> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        int width = ScreenUtils.getScreenWidth(context);
        imageWidth = width - 20 * ScreenUtils.getDensityDpi(context) / 160;

        default_signature = context.getResources().getString(R.string.default_signature);
    }

    @Override
    public void convert(ViewHolder helper, WeiYu item) {
        //分享
        ImageView shareTv = helper.getView(R.id.id_weiyu_share);
        shareTv.setTag(item);
        //评论
        ImageView commentTv = helper.getView(R.id.id_weiyu_comment);
        commentTv.setTag(item);
        //点赞
        ImageView priseTv = helper.getView(R.id.id_weiyu_prise);
        priseTv.setTag(item);
        //个人头像
//        helper.setImageByImageLoader(R.id.id_weiyu_icon, item.getUserIcon());
        //个人头像
        helper.setText(R.id.id_weiyu_user_name, TextUtils.isEmpty(item.getUserName()) ? "" : item.getUserName());
        //时间
        helper.setText(R.id.id_weiyu_date, item.getDate());
        //个性签名
        helper.setText(R.id.id_weiyu_user_signature, TextUtils.isEmpty(item.getUserSignature()) ? default_signature : item.getUserSignature());
        //微语内容
        if (TextUtils.isEmpty(item.getContent())) {
            helper.setVisiable(R.id.id_weiyu_detail_content, false);
        } else {
            helper.setVisiable(R.id.id_weiyu_detail_content, true);
            helper.setText(R.id.id_weiyu_detail_content, item.getContent());

        }
        //微语图片
        ImageView imageContent = helper.getView(R.id.id_weiyu_image);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imageContent.getLayoutParams();
        if (TextUtils.isEmpty(item.getImageUrl())) {
            helper.setVisiable(R.id.id_weiyu_image, false);
        } else {
            helper.setVisiable(R.id.id_weiyu_image, true);
            helper.setImageByImageLoader(R.id.id_weiyu_image, item.getImageUrl(), imageWidth);
        }
        //是否赞过
        if (item.isHasPrised()) {
            shareTv.setImageResource(R.drawable.icon_shared);
        } else {
            shareTv.setImageResource(R.drawable.icon_share);
        }
        //是否已经评论
        if (item.isHasCommented()) {
            commentTv.setImageResource(R.drawable.icon_commented);
        } else {
            commentTv.setImageResource(R.drawable.icon_comment);
        }
        //是否已经分享
        if (item.isHasShared()) {
            priseTv.setImageResource(R.drawable.icon_prised);
        } else {
            priseTv.setImageResource(R.drawable.icon_prise);
        }
        //设置点击事件
        setEventListener(priseTv);
    }

    private void setEventListener(final ImageView priseTv) {
        priseTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeiYu weiyu = (WeiYu) v.getTag();
                if (!weiyu.isHasPrised()) {
                   Animation anim =  AnimationUtils.loadAnimation(mContext,R.anim.prised_anim);
                    priseTv.setImageResource(R.drawable.icon_prised);
                    priseTv.startAnimation(anim);
                }
            }
        });
    }
}
