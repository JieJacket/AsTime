package jacketjie.astimes.views.fragments;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wujie on 2015/12/13.
 */
public class WeiYu implements Parcelable {
    private String id;
    private String userName;
    private String userSignature;
    private String userIcon;
    private String date;
    private String content;
    private String imageUrl;
    private boolean hasShared;
    private boolean hasCommented;
    private boolean hasPrised;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userIcon);
        dest.writeString(this.userName);
        dest.writeString(this.userSignature);
        dest.writeString(this.date);
        dest.writeString(this.content);
        dest.writeString(this.imageUrl);
        dest.writeInt(hasShared == true ? 1 : 0);
        dest.writeInt(hasCommented == true ? 1 : 0);
        dest.writeInt(hasPrised == true ? 1 : 0);
    }

    public WeiYu() {
    }

    protected WeiYu(Parcel in) {
        this.id = in.readString();
        this.userName = in.readString();
        this.userSignature = in.readString();
        this.content = in.readString();
        this.userIcon = in.readString();
        this.date = in.readString();
        this.imageUrl = in.readString();
        this.hasShared = in.readInt() == 1 ? true : false;
        this.hasCommented = in.readInt() == 1 ? true : false;
        this.hasPrised = in.readInt() == 1 ? true : false;
    }

    public static final Creator<WeiYu> CREATOR = new Creator<WeiYu>() {
        public WeiYu createFromParcel(Parcel source) {
            return new WeiYu(source);
        }

        public WeiYu[] newArray(int size) {
            return new WeiYu[size];
        }
    };

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSignature() {
        return userSignature;
    }

    public void setUserSignature(String userSignature) {
        this.userSignature = userSignature;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isHasShared() {
        return hasShared;
    }

    public void setHasShared(boolean hasShared) {
        this.hasShared = hasShared;
    }

    public boolean isHasCommented() {
        return hasCommented;
    }

    public void setHasCommented(boolean hasCommented) {
        this.hasCommented = hasCommented;
    }

    public boolean isHasPrised() {
        return hasPrised;
    }

    public void setHasPrised(boolean hasPrised) {
        this.hasPrised = hasPrised;
    }
}
