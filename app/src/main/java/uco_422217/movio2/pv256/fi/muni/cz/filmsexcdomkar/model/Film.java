package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Petr on 6. 10. 2016.
 *
 * https://www.themoviedb.org/documentation/api/discover
 * https://www.themoviedb.org/account/domki9/api
 * https://developers.themoviedb.org/3/discover
 */

public class Film implements Parcelable{

    @SerializedName("id")
    private long mId;
    @SerializedName("original_title")
    private String mTitle;
    @SerializedName("release_date")
    private String mReleaseDate;
    @SerializedName("poster_path")
    private String mCoverPath;
    @SerializedName("backdrop_path")
    private String mBackdropPath;
    @SerializedName("vote_average")
    private float mVoteAverage;
    @SerializedName("overview")
    private String mOverview;

    public Film(long id, String title, String releaseDate, String coverPath, String backdropPath, float voteAverage, String overview) {
        mId = id;
        mTitle = title;
        mReleaseDate = releaseDate;
        mCoverPath = coverPath;
        mBackdropPath = backdropPath;
        mVoteAverage = voteAverage;
        mOverview = overview;
    }

    public long getId() {
        return mId;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String getCoverPath() {
        return mCoverPath;
    }

    public void setCoverPath(String coverPath) {
        mCoverPath = coverPath;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        mBackdropPath = backdropPath;
    }

    public float getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        mVoteAverage = voteAverage;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeString(this.mReleaseDate);
        dest.writeString(this.mCoverPath);
        dest.writeString(this.mTitle);
        dest.writeString(this.mBackdropPath);
        dest.writeFloat(this.mVoteAverage);
        dest.writeString(this.mOverview);
    }

    public Film() {
    }

    protected Film(Parcel in) {
        this.mId = in.readLong();
        this.mReleaseDate = in.readString();
        this.mCoverPath = in.readString();
        this.mTitle = in.readString();
        this.mBackdropPath = in.readString();
        this.mVoteAverage = in.readFloat();
        this.mOverview = in.readString();
    }

    public static final Creator<Film> CREATOR = new Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel source) {
            return new Film(source);
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };

    @Override
    public String toString() {
        return super.toString() + "Film: id: " + mId + ", title: "+mTitle+", VoteAverage: " + mVoteAverage + ", Reease date: " + mReleaseDate + ", Overview: " + mOverview;
    }
}
