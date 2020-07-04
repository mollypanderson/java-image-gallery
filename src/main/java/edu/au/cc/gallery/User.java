package edu.au.cc.gallery;

public class User {
    private String username;
    private String password;
    private String fullName;

    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String u) {
        username = u;
    }

    public String getpassword() {

        return password;
    }

    public void setpassword(String p) {
        password = p;
    }

    public String getFullName() {

        return fullName;
    }

    public void setFullName(String fn) {
        fullName = fn;
    }

    @Override
    public String toString() {
        return "User with Username: " + username + "  password: " + password + "  full name: " + fullName;
    }
}

