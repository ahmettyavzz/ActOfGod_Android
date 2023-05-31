package edu.kocaeli.actofgod_android.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class PersonDto implements Serializable {
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("birthYear")
    private String birthYear;
    @SerializedName("tcNo")
    private String tcNo;
    @SerializedName("androidId")
    private String androidId;

    public PersonDto(String firstName, String lastName, String birthYear, String tcNo, String androidId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
        this.tcNo = tcNo;
        this.androidId = androidId;
    }

    public PersonDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getTcNo() {
        return tcNo;
    }

    public void setTcNo(String tcNo) {
        this.tcNo = tcNo;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDto personDto = (PersonDto) o;
        return Objects.equals(firstName, personDto.firstName) && Objects.equals(lastName, personDto.lastName) && Objects.equals(birthYear, personDto.birthYear) && Objects.equals(tcNo, personDto.tcNo) && Objects.equals(androidId, personDto.androidId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, birthYear, tcNo, androidId);
    }
}
