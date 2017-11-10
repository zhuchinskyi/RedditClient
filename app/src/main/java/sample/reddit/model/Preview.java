package sample.reddit.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Preview implements Parcelable {
    private  ArrayList<Image> images;

    public ArrayList<Image> getImages() {
        return images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.images);
    }

    public Preview() {
    }

    protected Preview(Parcel in) {
        this.images = new ArrayList<>();
        in.readList(this.images, Image.class.getClassLoader());
    }

    public static final Parcelable.Creator<Preview> CREATOR = new Parcelable.Creator<Preview>() {
        @Override
        public Preview createFromParcel(Parcel source) {
            return new Preview(source);
        }

        @Override
        public Preview[] newArray(int size) {
            return new Preview[size];
        }
    };
}
