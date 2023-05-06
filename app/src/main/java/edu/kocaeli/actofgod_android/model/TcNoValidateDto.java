package edu.kocaeli.actofgod_android.model;

import com.google.gson.annotations.SerializedName;

public class TcNoValidateDto {
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("birthYear")
    private String birthYear;
    @SerializedName("tcNo")
    private String tcNo;

    public TcNoValidateDto(String firstName,
                           String lastName,
                           String birthYear,
                           String tcNo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
        this.tcNo = tcNo;
    }

    public TcNoValidateDto() {
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

    @Override
    public String toString() {
        return "TcNoValidateDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthYear='" + birthYear + '\'' +
                ", tcNo='" + tcNo + '\'' +
                '}';
    }
}
