package sample.reddit.model;

import android.os.Parcel;
import android.os.Parcelable;


public class ImageSource implements Parcelable {
    private  String url;
    private  int width;
    private  int height;

    public String getUrl() {
        return url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
    }

    public ImageSource() {
    }

    protected ImageSource(Parcel in) {
        this.url = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
    }

    public static final Parcelable.Creator<ImageSource> CREATOR = new Parcelable.Creator<ImageSource>() {
        @Override
        public ImageSource createFromParcel(Parcel source) {
            return new ImageSource(source);
        }

        @Override
        public ImageSource[] newArray(int size) {
            return new ImageSource[size];
        }
    };
}
