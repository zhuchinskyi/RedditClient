package sample.reddit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ChildItem implements Parcelable {
    @SerializedName("data")
    private ChildEntity childEntity;

    public ChildEntity getChildren() {
        return childEntity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.childEntity, flags);
    }

    protected ChildItem(Parcel in) {
        this.childEntity = in.readParcelable(ChildEntity.class.getClassLoader());
    }

    public static final Parcelable.Creator<ChildItem> CREATOR = new Parcelable.Creator<ChildItem>() {
        @Override
        public ChildItem createFromParcel(Parcel source) {
            return new ChildItem(source);
        }

        @Override
        public ChildItem[] newArray(int size) {
            return new ChildItem[size];
        }
    };
}
