package edu.kocaeli.actofgod_android.model.route;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Route implements Serializable {
    @SerializedName("distance")
    private String distance;
    @SerializedName("duration")
    private String duration;

    public Route() {
    }

    public Route(String distance, String duration) {
        this.distance = distance;
        this.duration = duration;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
