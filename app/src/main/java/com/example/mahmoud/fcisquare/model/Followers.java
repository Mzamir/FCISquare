package com.example.mahmoud.fcisquare.model;

/**
 * Created by Mahmoud on 3/27/2016.
 */
public class Followers {
    String name, button, email;

    public Followers(String name, String button, String email) {
        this.name = name;
        this.button = button;
        this.email = email;
    }

    public Followers(String name, String button) {
        this.name = name;
        this.button = button;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }
}
