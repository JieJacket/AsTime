package jacketjie.astimes.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2015/12/11.
 */
public class Essay implements Parcelable {
    private int essayId;
    private String essayName;
    private String displayUrl;
    private List<String> gallery;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.essayId);
        dest.writeString(this.essayName);
        dest.writeString(this.displayUrl);
        dest.writeStringList(this.gallery);
    }

    public Essay() {
    }

    protected Essay(Parcel in) {
        this.essayId = in.readInt();
        this.essayName = in.readString();
        this.displayUrl = in.readString();
        this.gallery = in.createStringArrayList();
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

    public int getEssayId() {
        return essayId;
    }

    public void setEssayId(int essayId) {
        this.essayId = essayId;
    }

    public String getEssayName() {
        return essayName;
    }

    public void setEssayName(String essayName) {
        this.essayName = essayName;
    }

    public List<String> getGallery() {
        return gallery;
    }

    public void setGallery(List<String> gallery) {
        this.gallery = gallery;
    }
}
