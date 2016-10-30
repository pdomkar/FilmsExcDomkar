package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Petr on 27. 10. 2016.
 */

public class Cast {
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
    public String toString() {
        return super.toString() + "Cast: character: " + mCharacter + ", name: " + mName + ", path: " + mProfilePath;
    }
}
