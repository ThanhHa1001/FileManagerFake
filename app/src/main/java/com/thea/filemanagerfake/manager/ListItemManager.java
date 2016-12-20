package com.thea.filemanagerfake.manager;

import android.util.Log;

import com.thea.filemanagerfake.model.Item;
import com.thea.filemanagerfake.model.ListViewItem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Thea on 10/18/2016.
 */

public class ListItemManager {
    private Date lastModifiedFile;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private String formattedDateString;
    private String numberItem;

    private String path;
    private ArrayList<ListViewItem> listItem;

    private static ListItemManager instance;

    public static ListItemManager getInstance (){
        if (instance == null) {
            instance = new ListItemManager();
        }
        return instance;
    }

    public ArrayList<ListViewItem> createListItem(String path) {
        listItem = new ArrayList<>();
        String filePath;
        String fileAbsolutePath;
        String name;
        File[] files = new File(path).listFiles();
        for (File f : files) {
            filePath = f.getPath();
            fileAbsolutePath = f.getAbsolutePath();
            name = filePath.substring(filePath.lastIndexOf("/") + 1);
            lastModifiedFile = new Date(f.lastModified());
            formattedDateString = formatter.format(lastModifiedFile);

            if (f.isDirectory() && FileFilter.isDirectory(fileAbsolutePath)) {
                numberItem = f.listFiles().length + " items";

                listItem.add(new ListViewItem(
                        ListViewItemType.FOLDER_ITEM,
                        new Item(filePath,
                                name,
                                formattedDateString,
                                numberItem,
                                capacityFile(f))));

            } else if (FileFilter.isImageFile(fileAbsolutePath)) {
                numberItem = "1";

                listItem.add(new ListViewItem(
                        ListViewItemType.IMAGE_ITEM,
                        new Item(filePath,
                                name,
                                formattedDateString,
                                numberItem,
                                capacityFile(f),
                                filePath)));

            } else if (FileFilter.isMusicFile(fileAbsolutePath)) {
                numberItem = "1";

                listItem.add(new ListViewItem(
                        ListViewItemType.MUSIC_ITEM,
                        new Item(filePath,
                                name,
                                formattedDateString,
                                numberItem,
                                capacityFile(f))));

            } else if (FileFilter.isVideoFile(fileAbsolutePath)) {
                numberItem = "1";

                listItem.add(new ListViewItem(
                        ListViewItemType.MOVIE_ITEM,
                        new Item(filePath,
                                name,
                                formattedDateString,
                                numberItem,
                                capacityFile(f))));

            } else if (FileFilter.isDocumentFile(fileAbsolutePath)) {
                numberItem = "1";

                listItem.add(new ListViewItem(
                        ListViewItemType.DOCUMENT_ITEM,
                        new Item(filePath,
                                name,
                                formattedDateString,
                                numberItem,
                                capacityFile(f))));

            } else if (FileFilter.isCompressedFile(fileAbsolutePath)) {
                numberItem = "1";

                listItem.add(new ListViewItem(
                        ListViewItemType.COMPRESSED_ITEM,
                        new Item(filePath,
                                name,
                                formattedDateString,
                                numberItem,
                                capacityFile(f))));

            } else if (FileFilter.isFile(fileAbsolutePath)) {
                numberItem = "1";

                listItem.add(new ListViewItem(
                        ListViewItemType.FILE_ITEM,
                        new Item(filePath,
                                name,
                                formattedDateString,
                                numberItem,
                                capacityFile(f))));
            }

        }
        return listItem;
    }

    public String capacityFile(File file) {
        float capacity = file.length();
        String capacityFile;
        if (capacity < 1024) {
            capacityFile = String.format("%.2f", capacity) + " B";
        } else {
            capacity = capacity / 1024;
            if (capacity < 1024) {
                capacityFile = String.format("%.2f", capacity) + " KB";
            } else if (capacity > 1024) {
                capacity = capacity / 1024;
                capacityFile = String.format("%.2f", capacity) + " MB";
            } else {
                capacity = capacity / 1024;
                capacityFile = String.format("%.2f", capacity) + " GB";
            }
        }
        return capacityFile;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getCount() {
        return listItem.size();
    }

    public ListViewItem getListViewItemFile(int position) {
        return listItem.get(position);
    }

}
