package sample.reddit;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import sample.reddit.model.ChildItem;

class ListState implements ListContract.State, Parcelable {
    private final List<ChildItem> listItems;
    private final int firstVisiblePosition;
    private final String tagLastSlice;

    public ListState(List<ChildItem> listItems, int firstVisiblePosition, String tagLastSlice) {
        this.listItems = listItems;
        this.firstVisiblePosition = firstVisiblePosition;
        this.tagLastSlice = tagLastSlice;
    }

    @Override
    public List<ChildItem> getListItems() {
        return listItems;
    }

    @Override
    public int getFirstVisibleItemPosition() {
        return firstVisiblePosition;
    }

    public String getTagLastSlice() {
        return tagLastSlice;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.listItems);
        dest.writeInt(this.firstVisiblePosition);
        dest.writeString(this.tagLastSlice);
    }

    ListState(Parcel in) {
        this.listItems = in.createTypedArrayList(ChildItem.CREATOR);
        this.firstVisiblePosition = in.readInt();
        this.tagLastSlice = in.readString();
    }

    public static final Parcelable.Creator<ListState> CREATOR = new Parcelable.Creator<ListState>() {
        @Override
        public ListState createFromParcel(Parcel source) {
            return new ListState(source);
        }

        @Override
        public ListState[] newArray(int size) {
            return new ListState[size];
        }
    };
}
