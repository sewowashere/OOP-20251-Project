package com.airline.models;

import com.airline.core.dao.CSVConvertible;

public class User implements CSVConvertible {
    protected String username;
    protected String password;
    protected String role; // "ADMIN" veya "PASSENGER"

    public User() {}

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Override
    public String toCSV() {
        return username + "," + password + "," + role;
    }

    @Override
    public void fromCSV(String row) {
        String[] data = row.split(",");
        if (data.length >= 3) {
            this.username = data[0];
            this.password = data[1];
            this.role = data[2];
        }
    }

    @Override
    public String getId() {
        return username;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}