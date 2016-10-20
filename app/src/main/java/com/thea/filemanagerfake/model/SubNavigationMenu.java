package com.thea.filemanagerfake.model;

import android.widget.ImageView;

/**
 * Created by Thea on 10/18/2016.
 */

public class SubNavigationMenu {
    private int resID;
    private String nameMenu;

    public SubNavigationMenu(int resID, String nameMenu) {
        this.resID = resID;
        this.nameMenu = nameMenu;
    }

    public int getResID() {
        return resID;
    }

    public String getNameMenu() {
        return nameMenu;
    }
}
