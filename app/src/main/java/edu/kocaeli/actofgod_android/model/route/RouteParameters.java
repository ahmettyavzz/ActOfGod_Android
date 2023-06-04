package edu.kocaeli.actofgod_android.model.route;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RouteParameters implements Serializable {
    @SerializedName("originLatitude")
    private Double originLatitude;
    @SerializedName("originLongitude")
    private Double originLongitude;
    @SerializedName("destinationLatitude")
    private Double destinationLatitude;
    @SerializedName("destinationLongitude")
    private Double destinationLongitude;

    public RouteParameters() {
    }

    public RouteParameters(Double originLatitude,
                           Double originLongitude,
                           Double destinationLatitude,
                           Double destinationLongitude) {
        this.originLatitude = originLatitude;
        this.originLongitude = originLongitude;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
    }

    public Double getOriginLatitude() {
        return originLatitude;
    }

    public void setOriginLatitude(Double originLatitude) {
        this.originLatitude = originLatitude;
    }

    public Double getOriginLongitude() {
        return originLongitude;
    }

    public void setOriginLongitude(Double originLongitude) {
        this.originLongitude = originLongitude;
    }

    public Double getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(Double destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public Double getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(Double destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }
}
