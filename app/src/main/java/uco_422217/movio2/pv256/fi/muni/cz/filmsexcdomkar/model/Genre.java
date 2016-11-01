package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Petr on 6. 10. 2016.
 */

public class Genre implements Parcelable {
    @SerializedName("id")
    private Long mId;
    @SerializedName("name")
    private String mName;
    private boolean mShow = true;

    public Genre(Long id, String name, boolean show) {
        mId = id;
        mName = name;
        mShow = show;
    }

    public Genre(Long id, String name) {
        this(id, name, true);
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean isShow() {
        return mShow;
    }

    public void setShow(boolean show) {
        mShow = show;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeString(this.mName);
    }

    public Genre() {
    }

    protected Genre(Parcel in) {
        this.mId = in.readLong();
        this.mName = in.readString();
    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel source) {
            return new Genre(source);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };

}
