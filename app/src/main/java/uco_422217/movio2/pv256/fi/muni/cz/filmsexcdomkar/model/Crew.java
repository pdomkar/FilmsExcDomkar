package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Petr on 27. 10. 2016.
 */

public class Crew implements Parcelable {
    @SerializedName("job")
    private String mJob;
    @SerializedName("name")
    private String mName;

    public Crew(String job, String name) {
        mJob = job;
        mName = name;
    }

    public String getJob() {
        return mJob;
    }

    public void setJob(String job) {
        mJob = job;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mJob);
        dest.writeString(this.mName);

    }

    public Crew() {
    }

    protected Crew(Parcel in) {
        this.mJob = in.readString();
        this.mName = in.readString();
    }

    public static final Creator<Crew> CREATOR = new Creator<Crew>() {
        @Override
        public Crew createFromParcel(Parcel source) {
            return new Crew(source);
        }

        @Override
        public Crew[] newArray(int size) {
            return new Crew[size];
        }
    };

    @Override
    public String toString() {
        return super.toString() + "Crew: job: " + mJob + ", name: " + mName;
    }
}