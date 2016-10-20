package com.thea.filemanagerfake.manager;

/**
 * Created by Thea on 10/18/2016.
 */

public class ListViewItemType {
    public final static int HEADER_NAVIGATION = 0;
    public final static int HEADER_MENU_NAVIGATION = 1;
    public final static int CONTENT_MENU_NAVIGATION = 2;
    public final static int FOLDER_ITEM = 3;
    public final static int IMAGE_ITEM = 4;
    public final static int DOCUMENT_ITEM = 5;
    public final static int COMPRESSED_ITEM = 6;
    public final static int MUSIC_ITEM = 7;
    public final static int MOVIE_ITEM = 8;
    public final static int FILE_ITEM = 9;

    public static int getNumberItemType() {
        return 10;
    }
}
