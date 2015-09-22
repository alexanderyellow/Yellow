package com.yellow.adviceby.model;

/**
 * Created by SheykinAV on 21.09.2015.
 */
public class User {

    private int id;
    private String email;
    private String name;
    private String password;
    private boolean isConnected;
    private String connectionSource;

    public User(){}

    public User(int id, boolean isConnected, String connectionSource) {
        this.id = id;
        this.isConnected = isConnected;
        this.connectionSource = connectionSource;
    }

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConnectionSource() {
        return connectionSource;
    }

    public void setConnectionSource(String connectionSource) {
        this.connectionSource = connectionSource;
    }

    public boolean getIsConnected() {
        return isConnected;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }
}
