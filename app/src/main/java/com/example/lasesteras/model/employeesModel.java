package com.example.lasesteras.model;

import java.io.Serializable;

public class employeesModel implements Serializable {

    String fullName;
    String cellular;
    String email;
    String password;
    String Username;
    String role;


    public employeesModel() {

    }

    public employeesModel(String fullName, String cellular, String email, String password, String username,String role) {
        this.fullName = fullName;
        this.cellular = cellular;
        this.email = email;
        this.password = password;
        Username = username;
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCellular() {
        return cellular;
    }

    public void setCellular(String cellular) {
        this.cellular = cellular;
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

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
