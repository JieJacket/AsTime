package jacketjie.astimes.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.List;

import jacketjie.astimes.R;
import jacketjie.astimes.greenDao.ATComment;
import jacketjie.astimes.greenDao.ATUser;
import jacketjie.astimes.greenDao.GreenDaoUtils;
import jacketjie.astimes.utils.ScreenUtils;

/**
 * 微语评论的适配器
 * Created by Administrator on 2015/12/11.
 */
public class WeiyuCommentListAdapter extends CommonAdapter<ATComment> {
    private  Context context;
    private ImageSize imageSize;
    private ATUser user;
    public WeiyuCommentListAdapter(Context context, List<ATComment> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        int width = ScreenUtils.getScreenWidth(context);
        this.context = context;
    }

    @Override
    public void convert(ViewHolder helper, ATComment item) {
        ATUser  user = GreenDaoUtils.getUserForUserId(context,item.getCommentUserId());
        helper.setText(R.id.id_comment_content, item.getCommentDetail());
        helper.setText(R.id.id_comment_user_name,user.getUserNickName());
        helper.setText(R.id.id_comment_date, TextUtils.isEmpty(item.getCommentDate()) ? "" : item.getCommentDate());
        if (!TextUtils.isEmpty(user.getUserIcon())){
            helper.setImageByImageLoader(R.id.id_comment_user_icon,user.getUserIcon() );
        }
    }
}
