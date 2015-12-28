package jacketjie.astimes.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 评论内容实体
 * Created by Administrator on 2015/12/21.
 */
public class Comment  implements Parcelable{
    private String commentUserId;
    private String commentUserName;
    private String commentUserIconUrl;
    private String commentDate;
    private String commentDatil;

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getCommentDatil() {
        return commentDatil;
    }

    public void setCommentDatil(String commentDatil) {
        this.commentDatil = commentDatil;
    }

    public String getCommentUserIconUrl() {
        return commentUserIconUrl;
    }

    public void setCommentUserIconUrl(String commentUserIconUrl) {
        this.commentUserIconUrl = commentUserIconUrl;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getCommentUserName() {
        return commentUserName;
    }

    public void setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.commentUserId);
        dest.writeString(this.commentUserName);
        dest.writeString(this.commentUserIconUrl);
        dest.writeString(this.commentDate);
        dest.writeString(this.commentDatil);
    }

    public Comment() {
    }

    protected Comment(Parcel in) {
        this.commentUserId = in.readString();
        this.commentUserName = in.readString();
        this.commentUserIconUrl = in.readString();
        this.commentDate = in.readString();
        this.commentDatil = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
