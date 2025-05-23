package com.sinhvien.doan;

public class Favorite {
    private String id;
    private String name;
    private String description;
    private String imageResource;

    public Favorite(String id, String name, String description, String imageResource) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageResource = imageResource;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }
}