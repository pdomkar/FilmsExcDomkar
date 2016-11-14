package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Petr on 27. 10. 2016.
 */

public class Cast implements Parcelable {
    @SerializedName("character")
    private String mCharacter;
    @SerializedName("name")
    private String mName;
    @SerializedName("profile_path")
    private String mProfilePath;

    public Cast(String character, String name, String profilePath) {
        mCharacter = character;
        mName = name;
        mProfilePath = profilePath;
    }

    public String getCharacter() {
        return mCharacter;
    }

    public void setCharacter(String character) {
        mCharacter = character;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getProfilePath() {
        return mProfilePath;
    }

    public void setProfilePath(String profilePath) {
        mProfilePath = profilePath;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mCharacter);
        dest.writeString(this.mName);
        dest.writeString(this.mProfilePath);

    }

    public Cast() {
    }

    protected Cast(Parcel in) {
        this.mCharacter = in.readString();
        this.mName = in.readString();
        this.mProfilePath = in.readString();
    }

    public static final Creator<Cast> CREATOR = new Creator<Cast>() {
        @Override
        public Cast createFromParcel(Parcel source) {
            return new Cast(source);
        }

        @Override
        public Cast[] newArray(int size) {
            return new Cast[size];
        }
    };

    @Override
    public String toString() {
        return super.toString() + "Cast: character: " + mCharacter + ", name: " + mName + ", path: " + mProfilePath;
    }
}
