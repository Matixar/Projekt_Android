package com.example.projektandroid;

public class Grade {
    private int id;
    private String name;
    private float value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Grade(int id) {
        this.id = id;
        this.name = "ocena"+id;

    }

    public Grade(int id, float value) {
        this.name = "ocena"+id;
        this.value = value;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }


}
