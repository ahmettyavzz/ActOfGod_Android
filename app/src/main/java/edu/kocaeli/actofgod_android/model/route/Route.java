package edu.kocaeli.actofgod_android.model.route;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Route implements Serializable {
    @SerializedName("distance")
    private int distance;
    @SerializedName("duration")
    private int duration;

    public Route() {
    }

    public Route(int distance, int duration) {
        this.distance = distance;
        this.duration = duration;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
