package com.example.plannus.Objects;

public class User {
    public String fullName;
    public String age;
    public String email;
    public String password;

    public User() {

    }

    public User(String fullName, String age, String email, String password) {
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
