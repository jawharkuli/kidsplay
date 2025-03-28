package com.example.login;

import androidx.annotation.NonNull;

// Class data model
public class ClassInfo {
    private final int id;
    private final String name;

    public ClassInfo(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
