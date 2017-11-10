package sample.reddit.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable {
    private ImageSource source;
    private String id;

    public ImageSource getSource() {
        return source;
    }

    public Image() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.source, flags);
        dest.writeString(this.id);
    }

    protected Image(Parcel in) {
        this.source = in.readParcelable(ImageSource.class.getClassLoader());
        this.id = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public String getId() {
        return id;
    }
}
