package jacketjie.astimes.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/12/11.
 */
public class Essay implements Parcelable {
    private String essayId;
    private String essayName;
    private String displayUrl;
    private String essayContent;
    /**
     * 0:美文；1：电台；2：图片
     */
    private int type;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.essayId);
        dest.writeString(this.essayName);
        dest.writeString(this.displayUrl);
        dest.writeString(this.essayContent);
        dest.writeInt(this.type);
    }

    public Essay() {
    }

    protected Essay(Parcel in) {
        this.essayId = in.readString();
        this.essayName = in.readString();
        this.displayUrl = in.readString();
        this.essayContent = in.readString();
        this.type = in.readInt();
    }

    public static final Creator<Essay> CREATOR = new Creator<Essay>() {
        public Essay createFromParcel(Parcel source) {
            return new Essay(source);
        }

        public Essay[] newArray(int size) {
            return new Essay[size];
        }
    };

    public String getDisplayUrl() {
        return displayUrl;
    }

    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }

    public String getEssayContent() {
        return essayContent;
    }

    public void setEssayContent(String essayContent) {
        this.essayContent = essayContent;
    }

    public String getEssayId() {
        return essayId;
    }

    public void setEssayId(String essayId) {
        this.essayId = essayId;
    }

    public String getEssayName() {
        return essayName;
    }

    public void setEssayName(String essayName) {
        this.essayName = essayName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Essay{" +
                "displayUrl='" + displayUrl + '\'' +
                ", essayId='" + essayId + '\'' +
                ", essayName='" + essayName + '\'' +
                ", essayContent='" + essayContent + '\'' +
                ", type=" + type +
                '}';
    }
}
