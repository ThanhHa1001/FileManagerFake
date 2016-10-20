package com.thea.filemanagerfake.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.thea.filemanagerfake.App;
import com.thea.filemanagerfake.R;
import com.thea.filemanagerfake.manager.MenuNavigationManager;
import com.thea.filemanagerfake.model.ListViewItem;
import com.thea.filemanagerfake.manager.ListViewItemType;
import com.thea.filemanagerfake.model.MainNavigationMenu;
import com.thea.filemanagerfake.model.SubNavigationMenu;

/**
 * Created by Thea on 10/18/2016.
 */

public class MenuNavigationAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private MenuNavigationManager menuNM;

    public MenuNavigationAdapter() {
        inflater = LayoutInflater.from(App.getContext());
        menuNM = MenuNavigationManager.getInstance();
    }

    @Override
    public int getCount() {
        return menuNM.getCountItem();
    }

    @Override
    public ListViewItem getItem(int position) {
        return menuNM.getItemMenuNavigation(position);
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
        return menuNM.getItemMenuNavigation(position).getItemType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object listObject = menuNM.getItemMenuNavigation(position).getObject();

        switch (getItemViewType(position)) {
            case ListViewItemType.HEADER_NAVIGATION:
                convertView = inflater.inflate(R.layout.header_main_navigation_menu, parent, false);
                return  convertView;

            case ListViewItemType.HEADER_MENU_NAVIGATION:
                MainNavigationMenu mainNavigationMenu = (MainNavigationMenu) listObject;
                HeaderMenuHolder headerMenuHolder;

                if (convertView == null) {
                    headerMenuHolder = new HeaderMenuHolder();
                    convertView = inflater.inflate(R.layout.header_item_navigation_menu, parent, false);
                    headerMenuHolder.txtNameHeaderMenu = (TextView) convertView.findViewById(R.id.txt_name_header_menu_navigation);
                    convertView.setTag(headerMenuHolder);
                } else {
                    headerMenuHolder = (HeaderMenuHolder) convertView.getTag();
                }
                headerMenuHolder.txtNameHeaderMenu.setText(mainNavigationMenu.getNameMenu());
                return convertView;

            case ListViewItemType.CONTENT_MENU_NAVIGATION:
                SubNavigationMenu subNavigationMenu = (SubNavigationMenu) listObject;
                ContentMenuHolder contentMenuHolder;
                if (convertView == null) {
                    contentMenuHolder = new ContentMenuHolder();
                    convertView = inflater.inflate(R.layout.item_navigation_menu, parent, false);
                    contentMenuHolder.imgIcon = (ImageView) convertView.findViewById(R.id.img_icon_folder);
                    contentMenuHolder.txtNameContentMenu = (TextView) convertView.findViewById(R.id.txt_name_folder);
                    convertView.setTag(contentMenuHolder);
                } else {
                    contentMenuHolder = (ContentMenuHolder) convertView.getTag();
                }
                contentMenuHolder.imgIcon.setImageResource(subNavigationMenu.getResID());
                contentMenuHolder.txtNameContentMenu.setText(subNavigationMenu.getNameMenu());
                return convertView;

            default:
                break;
        }
        return null;
    }

    public class HeaderMenuHolder {
        TextView txtNameHeaderMenu;
    }

    public class ContentMenuHolder {
        ImageView imgIcon;
        TextView txtNameContentMenu;
    }
}
