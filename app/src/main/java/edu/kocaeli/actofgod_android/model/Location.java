package edu.kocaeli.actofgod_android.model;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("name")
    private String name;
    @SerializedName("latitude")
    private Double latitude;
    @SerializedName("capacity")
    private Long capacity;
    @SerializedName("districtId")
    private Long districtId;

    public Location(String name, Double latitude, Long capacity, Long districtId) {
        this.name = name;
        this.latitude = latitude;
        this.capacity = capacity;
        this.districtId = districtId;
    }

    public Location() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", capacity=" + capacity +
                ", districtId=" + districtId +
                '}';
    }
}