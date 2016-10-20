package com.thea.filemanagerfake.manager;

import com.thea.filemanagerfake.R;
import com.thea.filemanagerfake.model.ListViewItem;
import com.thea.filemanagerfake.model.MainNavigationMenu;
import com.thea.filemanagerfake.model.SubNavigationMenu;

import java.util.ArrayList;

/**
 * Created by Thea on 10/18/2016.
 */

public class MenuNavigationManager {

    private static MenuNavigationManager instance;

    public static MenuNavigationManager getInstance(){
        if (instance == null) {
            instance = new MenuNavigationManager();
        }
        return instance;
    }

    private ArrayList<ListViewItem> menuNavigation;

    public MenuNavigationManager() {
        initializeComponents();
    }

    private void initializeComponents() {
        menuNavigation = new ArrayList<>();

        menuNavigation.add(new ListViewItem(ListViewItemType.HEADER_NAVIGATION));

        menuNavigation.add(new ListViewItem(ListViewItemType.HEADER_MENU_NAVIGATION,
                new MainNavigationMenu("Home")));
        menuNavigation.add(new ListViewItem(ListViewItemType.CONTENT_MENU_NAVIGATION,
                new SubNavigationMenu(R.drawable.ic_home_white_48dp, "Home")));

        menuNavigation.add(new ListViewItem(ListViewItemType.HEADER_MENU_NAVIGATION,
                new MainNavigationMenu("Storages")));
        menuNavigation.add(new ListViewItem(ListViewItemType.CONTENT_MENU_NAVIGATION,
                new SubNavigationMenu(R.drawable.ic_phone_android_white_48dp, "Internal Storage")));

        menuNavigation.add(new ListViewItem(ListViewItemType.HEADER_MENU_NAVIGATION,
                new MainNavigationMenu("Colections")));
        menuNavigation.add(new ListViewItem(ListViewItemType.CONTENT_MENU_NAVIGATION,
                new SubNavigationMenu(R.drawable.ic_image_white_48dp, "Images")));
        menuNavigation.add(new ListViewItem(ListViewItemType.CONTENT_MENU_NAVIGATION,
                new SubNavigationMenu(R.drawable.ic_movie_white_48dp, "Videos")));
        menuNavigation.add(new ListViewItem(ListViewItemType.CONTENT_MENU_NAVIGATION,
                new SubNavigationMenu(R.drawable.ic_music_note_white_48dp, "Music")));
        menuNavigation.add(new ListViewItem(ListViewItemType.CONTENT_MENU_NAVIGATION,
                new SubNavigationMenu(R.drawable.ic_compressed_white_48dp, "Compressed")));
        menuNavigation.add(new ListViewItem(ListViewItemType.CONTENT_MENU_NAVIGATION,
                new SubNavigationMenu(R.drawable.ic_document_white_48dp, "Documents")));
        menuNavigation.add(new ListViewItem(ListViewItemType.CONTENT_MENU_NAVIGATION,
                new SubNavigationMenu(R.drawable.ic_file_download_white_48dp, "Downloads")));
    }

    public int getCountItem() {
        return menuNavigation.size();
    }

    public ListViewItem getItemMenuNavigation(int position) {
        return menuNavigation.get(position);
    }
}
