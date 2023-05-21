package com.example.i190417_i190468_i190260.Models;

public class Users {
    String name;
    String email;
    String password;
    String deviceID;

    public Users(){};

    public Users(String name, String email, String password, String deviceID) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.deviceID = deviceID;
    }

    public Users(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
}
