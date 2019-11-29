package com.example.myapplication;

public class DoctorInfo {
    private String email;
    private String hospital;
    private String location;
    private String name;
    private String photoUrl;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public DoctorInfo(String name, String hospital, String location, String email, String photoUrl) {
        this.name = name;
        this.hospital = hospital;
        this.location = location;
        this.email = email;
        this.photoUrl = photoUrl;
    }
}

