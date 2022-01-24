package com.example.giveit;

public class User {

    String fullname;
    String phone;

    public User() {
    }

    public User(String fullname, String phone) {
        this.fullname = fullname;
        this.phone = phone;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
