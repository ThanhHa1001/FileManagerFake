package com.thea.filemanagerfake.model;

/**
 * Created by Thea on 10/18/2016.
 */

public class ListViewItem {
    private int itemType;
    private Object object;

    public ListViewItem(int itemType) {
        this.itemType = itemType;
    }

    public ListViewItem(int itemType, Object object) {
        this.itemType = itemType;
        this.object = object;
    }

    public int getItemType() {
        return itemType;
    }

    public Object getObject() {
        return object;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
