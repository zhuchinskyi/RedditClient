package sample.reddit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ChildEntity implements Parcelable {
    @SerializedName("num_comments")
    private int commentsNumber;
    private String title;
    private String author;
    @SerializedName("created_utc")
    private long createdUtc;
    private String thumbnail;
    private Preview preview;

    public Preview getPreview() {
        return preview;
    }

    public int getCommentsNumber() {
        return commentsNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public long getCreatedUtc() {
        return createdUtc;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public ChildEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.commentsNumber);
        dest.writeString(this.title);
        dest.writeString(this.author);
        dest.writeLong(this.createdUtc);
        dest.writeString(this.thumbnail);
        dest.writeParcelable(this.preview, flags);
    }

    protected ChildEntity(Parcel in) {
        this.commentsNumber = in.readInt();
        this.title = in.readString();
        this.author = in.readString();
        this.createdUtc = in.readLong();
        this.thumbnail = in.readString();
        this.preview = in.readParcelable(Preview.class.getClassLoader());
    }

    public static final Creator<ChildEntity> CREATOR = new Creator<ChildEntity>() {
        @Override
        public ChildEntity createFromParcel(Parcel source) {
            return new ChildEntity(source);
        }

        @Override
        public ChildEntity[] newArray(int size) {
            return new ChildEntity[size];
        }
    };
}
