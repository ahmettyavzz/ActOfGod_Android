package edu.kocaeli.actofgod_android.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpdateLocationDto implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("latitude")
    private Double latitude;
    @SerializedName("longitude")
    private Double longitude;
    @SerializedName("capacity")
    private Long capacity;
    @SerializedName("districtId")
    private Long districtId;

    public UpdateLocationDto(String name,
                             Double latitude,
                             Double logitude,
                             Long capacity,
                             Long districtId) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = logitude;
        this.capacity = capacity;
        this.districtId = districtId;
    }

    public UpdateLocationDto() {
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

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
