package com.sinhvien.doan;

import android.provider.ContactsContract;

import com.google.firebase.auth.FirebaseUser;

public class User {
    private String email;
    private String username;
    private String role;
    public String uid;

    public User() {
        // Constructor rỗng cần thiết cho Firestore
    }

    public User(String email, String role, String username, String uid) {
        this.email = email;
        this.role = role;
        this.username = username;
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getuid() {
        return uid;
    }

    public void setuid(String uid) {
        this.uid = uid;
    }
}