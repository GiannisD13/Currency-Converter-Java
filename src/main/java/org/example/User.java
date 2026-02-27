package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {
    private String username;
    private String password;
    private List<String> favorites;


    public User(String username, String password) {
        this.username= username;
        this.password= password;
        this.favorites = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getFavorites() {
        return favorites;
    }

    public void setFavourites(List<String> favorites) {
        this.favorites = favorites;

    }

}
