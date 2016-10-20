package com.thea.filemanagerfake.model;

/**
 * Created by Thea on 10/19/2016.
 */

public class Item {

    private String path;
    private String name;
    private String lastModified;
    private String numberItem;
    private String capacity;
    private String pathImage;

    public Item(String path, String name, String lastModified, String numberItem, String capacity) {
        this.path = path;
        this.name = name;
        this.lastModified = lastModified;
        this.numberItem = numberItem;
        this.capacity = capacity;
    }

    public Item(String path, String name, String lastModified, String numberItem, String capacity, String pathImage) {
        this.path = path;
        this.name = name;
        this.lastModified = lastModified;
        this.numberItem = numberItem;
        this.capacity = capacity;
        this.pathImage = pathImage;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getLastModified() {
        return lastModified;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getNumberItem() {
        return numberItem;
    }

    public String getPathImage() {
        return pathImage;
    }
}
