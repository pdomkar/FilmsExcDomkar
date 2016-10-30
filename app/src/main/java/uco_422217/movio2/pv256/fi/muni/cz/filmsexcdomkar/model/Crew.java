package uco_422217.movio2.pv256.fi.muni.cz.filmsexcdomkar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Petr on 27. 10. 2016.
 */

public class Crew {
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
    public String toString() {
        return super.toString() + "Crew: job: " + mJob + ", name: " + mName;
    }
}