package com.thea.filemanagerfake.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thea.filemanagerfake.App;
import com.thea.filemanagerfake.R;
import com.thea.filemanagerfake.manager.ListItemManager;
import com.thea.filemanagerfake.manager.ListViewItemType;
import com.thea.filemanagerfake.model.Item;
import com.thea.filemanagerfake.model.ListViewItem;

import java.util.ArrayList;

/**
 * Created by Thea on 10/19/2016.
 */

public class ListItemAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ListItemManager listItemManager;
    private ArrayList<ListViewItem> listViewItems;

//    public ListItemAdapter(ListItemManager listItemManager) {
//        inflater = LayoutInflater.from(App.getContext());
//        this.listItemManager = listItemManager;
//    }

    public ListItemAdapter(ArrayList<ListViewItem> listViewItems) {
        inflater = LayoutInflater.from(App.getContext());
        this.listViewItems = listViewItems;
    }

    @Override
    public int getCount() {
        return listViewItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return ListViewItemType.getNumberItemType();
    }

    @Override
    public int getItemViewType(int position) {
        return listViewItems.get(position).getItemType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = (Item) listViewItems.get(position).getObject();

        switch (getItemViewType(position)) {
            case ListViewItemType.COMPRESSED_ITEM:
                CompressedHolder compressedHolder;

                if (convertView == null) {
                    compressedHolder = new CompressedHolder();
                    convertView = inflater.inflate(R.layout.item_compressed, parent, false);
                    compressedHolder.txtNameCompressed = (TextView) convertView.findViewById(R.id.txt_name_compressed);
                    compressedHolder.txtLastModifiedCompressed = (TextView) convertView.findViewById(R.id.txt_lastModified_compressed);
                    compressedHolder.txtCapacityCompressed = (TextView) convertView.findViewById(R.id.txt_capacity_compressed);
                    convertView.setTag(compressedHolder);
                } else {
                    compressedHolder = (CompressedHolder) convertView.getTag();
                }
                compressedHolder.txtNameCompressed.setText(item.getName());
                compressedHolder.txtLastModifiedCompressed.setText(item.getLastModified());
                compressedHolder.txtCapacityCompressed.setText(item.getLastModified());
                return convertView;

            case ListViewItemType.DOCUMENT_ITEM:
                DocumentHolder documentHolder;

                if (convertView == null) {
                    documentHolder = new DocumentHolder();
                    convertView = inflater.inflate(R.layout.item_document, parent, false);
                    documentHolder.txtNameDocument = (TextView) convertView.findViewById(R.id.txt_name_document);
                    documentHolder.txtLastModifiedDocument = (TextView) convertView.findViewById(R.id.txt_capacity_document);
                    documentHolder.txtCapacityDocument = (TextView) convertView.findViewById(R.id.txt_capacity_document);
                    convertView.setTag(documentHolder);
                } else {
                    documentHolder = (DocumentHolder) convertView.getTag();
                }
                documentHolder.txtNameDocument.setText(item.getName());
                documentHolder.txtLastModifiedDocument.setText(item.getLastModified());
                documentHolder.txtCapacityDocument.setText(item.getCapacity());
                return convertView;

            case ListViewItemType.FILE_ITEM:
                FileHolder fileHolder;

                if (convertView == null) {
                    fileHolder = new FileHolder();
                    convertView = inflater.inflate(R.layout.item_file, parent, false);
                    fileHolder.txtNameFile = (TextView) convertView.findViewById(R.id.txt_name_file);
                    fileHolder.txtLastModifiedFile = (TextView) convertView.findViewById(R.id.txt_lastModified_file);
                    fileHolder.txtCapacityFile = (TextView) convertView.findViewById(R.id.txt_capacity_file);
                    convertView.setTag(fileHolder);
                } else {
                    fileHolder = (FileHolder) convertView.getTag();
                }

                fileHolder.txtNameFile.setText(item.getName());
                fileHolder.txtLastModifiedFile.setText(item.getLastModified());
                fileHolder.txtCapacityFile.setText(item.getCapacity());
                return convertView;

            case ListViewItemType.FOLDER_ITEM:
                FolderHolder folderHolder;

                if (convertView == null) {
                    folderHolder = new FolderHolder();
                    convertView = inflater.inflate(R.layout.item_folder, parent, false);
                    folderHolder.txtNameFolder = (TextView) convertView.findViewById(R.id.txt_name_folder);
                    folderHolder.txtLastModified = (TextView) convertView.findViewById(R.id.txt_lastModified_folder);
                    folderHolder.txtNumberItemFolder = (TextView) convertView.findViewById(R.id.txt_number_item_folder);
                    convertView.setTag(folderHolder);
                } else {
                    folderHolder = (FolderHolder) convertView.getTag();
                }
                folderHolder.txtNameFolder.setText(item.getName());
                folderHolder.txtLastModified.setText(item.getLastModified());
                folderHolder.txtNumberItemFolder.setText(item.getNumberItem());
                return convertView;

            case ListViewItemType.IMAGE_ITEM:
                ImageHolder imageHolder;
                if (convertView == null) {
                    imageHolder = new ImageHolder();
                    convertView = inflater.inflate(R.layout.item_image, parent, false);
                    imageHolder.imgImage = (ImageView) convertView.findViewById(R.id.img_icon_image);
                    imageHolder.txtNameImage = (TextView) convertView.findViewById(R.id.txt_name_image);
                    imageHolder.txtLastModifiedImage = (TextView) convertView.findViewById(R.id.txt_lastModified_image);
                    imageHolder.txtCapacityImage = (TextView) convertView.findViewById(R.id.txt_capacity_image);
                    convertView.setTag(imageHolder);
                } else {
                    imageHolder = (ImageHolder) convertView.getTag();
                }
                Glide
                        .with(App.getContext())
                        .load(item.getPathImage())
                        .centerCrop()
                        .placeholder(R.drawable.ic_image_teal_48dp)
                        .into(imageHolder.imgImage);
                imageHolder.txtNameImage.setText(item.getName());
                imageHolder.txtLastModifiedImage.setText(item.getLastModified());
                imageHolder.txtCapacityImage.setText(item.getCapacity());
                return convertView;

            case ListViewItemType.MOVIE_ITEM:
                MovieHolder movieHolder;
                if (convertView == null) {
                    movieHolder = new MovieHolder();
                    convertView = inflater.inflate(R.layout.item_movie, parent, false);
                    movieHolder.txtNameMovie = (TextView) convertView.findViewById(R.id.txt_name_movie);
                    movieHolder.txtLastModifiedMovie = (TextView) convertView.findViewById(R.id.txt_lastModified_movie);
                    movieHolder.txtCapacityMovie = (TextView) convertView.findViewById(R.id.txt_capacity_movie);
                    convertView.setTag(movieHolder);
                } else {
                    movieHolder = (MovieHolder) convertView.getTag();
                }
                movieHolder.txtNameMovie.setText(item.getName());
                movieHolder.txtLastModifiedMovie.setText(item.getLastModified());
                movieHolder.txtCapacityMovie.setText(item.getCapacity());
                return convertView;

            case ListViewItemType.MUSIC_ITEM:
                MusicHolder musicHolder;
                if (convertView == null) {
                    musicHolder = new MusicHolder();
                    convertView = inflater.inflate(R.layout.item_music, parent, false);
                    musicHolder.txtNameMusic = (TextView) convertView.findViewById(R.id.txt_name_music);
                    musicHolder.txtLastModifiedMusic = (TextView) convertView.findViewById(R.id.txt_lastModified_music);
                    musicHolder.txtCapacityMusic = (TextView) convertView.findViewById(R.id.txt_capacity_music);
                    convertView.setTag(musicHolder);
                } else {
                    musicHolder = (MusicHolder) convertView.getTag();
                }
                musicHolder.txtNameMusic.setText(item.getName());
                musicHolder.txtLastModifiedMusic.setText(item.getLastModified());
                musicHolder.txtCapacityMusic.setText(item.getCapacity());
                return convertView;
            default:
                break;
        }

        return null;
    }

    private class CompressedHolder {
        TextView txtNameCompressed;
        TextView txtLastModifiedCompressed;
        TextView txtCapacityCompressed;
    }

    private class DocumentHolder {
        TextView txtNameDocument;
        TextView txtLastModifiedDocument;
        TextView txtCapacityDocument;
    }

    private class FileHolder {
        TextView txtNameFile;
        TextView txtLastModifiedFile;
        TextView txtCapacityFile;
    }

    private class FolderHolder {
        TextView txtNameFolder;
        TextView txtLastModified;
        TextView txtNumberItemFolder;
    }

    private class ImageHolder {
        ImageView imgImage;
        TextView txtNameImage;
        TextView txtLastModifiedImage;
        TextView txtCapacityImage;
    }

    private class MovieHolder {
        TextView txtNameMovie;
        TextView txtLastModifiedMovie;
        TextView txtCapacityMovie;
    }

    private class MusicHolder {
        TextView txtNameMusic;
        TextView txtLastModifiedMusic;
        TextView txtCapacityMusic;
    }
}
