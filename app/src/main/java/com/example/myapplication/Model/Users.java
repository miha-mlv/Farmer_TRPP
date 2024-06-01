package com.example.myapplication.Model;

import java.io.Serializable;

public class Users implements Serializable {
    private String phone, name, password;
    public Users(){}
    public Users(String name, String phone, String password)
    {
        this.phone = phone;
        this.name = name;
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
