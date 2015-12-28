package jacketjie.astimes.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/11.
 */
public class EssayDetail implements Parcelable {
    private String detailId;
    private String detailTitle;
    private String detailSub;
    private String detailUrl;
    private String detailType;
    private String detailDate;
    private int type;
//    private String detailId;
//    private String detailTitle;
//    private String detailSub;
//    private String detailUrl;
//    private String detailType;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.detailId);
        dest.writeString(this.detailTitle);
        dest.writeString(this.detailSub);
        dest.writeString(this.detailUrl);
        dest.writeString(this.detailType);
        dest.writeString(this.detailDate);
        dest.writeInt(this.type);
    }

    public EssayDetail() {
    }

    protected EssayDetail(Parcel in) {
        this.detailId = in.readString();
        this.detailTitle = in.readString();
        this.detailSub = in.readString();
        this.detailUrl = in.readString();
        this.detailType = in.readString();
        this.detailDate = in.readString();
        this.type = in.readInt();
    }

    public static final Creator<EssayDetail> CREATOR = new Creator<EssayDetail>() {
        public EssayDetail createFromParcel(Parcel source) {
            return new EssayDetail(source);
        }

        public EssayDetail[] newArray(int size) {
            return new EssayDetail[size];
        }
    };

    public String getDetailDate() {
        return detailDate;
    }

    public void setDetailDate(String detailDate) {
        this.detailDate = detailDate;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getDetailSub() {
        return detailSub;
    }

    public void setDetailSub(String detailSub) {
        this.detailSub = detailSub;
    }

    public String getDetailTitle() {
        return detailTitle;
    }

    public void setDetailTitle(String detailTitle) {
        this.detailTitle = detailTitle;
    }

    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
