package com.thea.filemanagerfake.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.thea.filemanagerfake.R;
import com.thea.filemanagerfake.adapter.ListItemAdapter;
import com.thea.filemanagerfake.adapter.MenuNavigationAdapter;
import com.thea.filemanagerfake.manager.ListItemManager;
import com.thea.filemanagerfake.manager.MenuNavigationManager;
import com.thea.filemanagerfake.manager.ListViewItemType;
import com.thea.filemanagerfake.model.Item;
import com.thea.filemanagerfake.model.ListViewItem;
import com.thea.filemanagerfake.model.ScrimInsetsFrameLayout;
import com.thea.filemanagerfake.model.SubNavigationMenu;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final long KILOBYTE = 1024;
    private static final int MY_PERMISSIONS_REQUEST = 1;

    private DrawerLayout dlMainLayout;
    private ScrimInsetsFrameLayout scrimFLMenuNavigation;
    private ListView lvMenuNavigation;
    private ListView lvFile;
    private ImageView btn_open_menu;

    private ListItemManager listItemManager;

    private MenuNavigationAdapter menuNavigationAdapter;
    private ListItemAdapter listItemAdapter;

    private int heightScreen;
    private int widthScreen;

    private ArrayList<ListViewItem> listFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWidthHeightScreen();
        checkPermission();

        initializeComponents();
    }

    private void initializeComponents() {
        listItemManager = new ListItemManager();
        menuNavigationAdapter = new MenuNavigationAdapter();

        dlMainLayout = (DrawerLayout) findViewById(R.id.dl_drawerlayout);

        scrimFLMenuNavigation = (ScrimInsetsFrameLayout) findViewById(R.id.scriminsetsframelayout);
        DrawerLayout.LayoutParams scrimLayoutParams = (DrawerLayout.LayoutParams) scrimFLMenuNavigation.getLayoutParams();
        int widthOfSideNavBar = widthScreen - convertDpToPx(56);
        if (widthOfSideNavBar > convertDpToPx(320)) {
            int widthScrim = convertDpToPx(320);
            scrimLayoutParams.width = widthScrim;
        } else {
            scrimLayoutParams.width = widthOfSideNavBar;
        }
        scrimFLMenuNavigation.setLayoutParams(scrimLayoutParams);

        lvMenuNavigation = (ListView) findViewById(R.id.lv_menu_navigation);
        lvMenuNavigation.setAdapter(menuNavigationAdapter);
        lvMenuNavigation.setOnItemClickListener(this);

        btn_open_menu = (ImageView) findViewById(R.id.btn_open_menu);
        btn_open_menu.setOnClickListener(this);

        listItemManager = new ListItemManager(android.os.Environment.getExternalStorageDirectory() + "");
        listItemAdapter = new ListItemAdapter(listItemManager);

        lvFile = (ListView) findViewById(R.id.lv_file);
        lvFile.setOnItemClickListener(this);
        lvFile.setAdapter(listItemAdapter);


        File[] files = new File(android.os.Environment.getExternalStorageDirectory() + "").listFiles();
        Log.d("---------------------", files.length + "");

        for (File file : files) {


//            String dateFormat = "dd/MM/yyyy HH:mm:ss";
//
//            if (file.isDirectory()) {
//                File[] file2 = new File(file.getAbsoluteFile().getPath()).listFiles();
//                Log.d("Number item ", file2.length+"");
//                for (File file3 : file2) {
//                    Date lastModified = new Date(file3.lastModified());
//                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//                    String formattedDateString = formatter.format(lastModified);
//                    Log.d("----------22222", file3.getPath().toString().substring(file3.getPath().lastIndexOf("/") + 1) + "-----------" + formattedDateString + " -------------" + capacityFile(file3));
//
//                }
//            }
            Log.d("----------22222", file.getPath().toString().substring(file.getPath().lastIndexOf("/") + 1) + "-----------" + capacityFile(file));
            Log.d("------------111111", file.getPath());
        }

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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        switch (adapterView.getId()) {
            case R.id.lv_menu_navigation:
                MenuNavigationManager menuNM = MenuNavigationManager.getInstance();
                Object listObject = menuNM.getItemMenuNavigation(position).getObject();

                if (menuNavigationAdapter.getItemViewType(position) == ListViewItemType.CONTENT_MENU_NAVIGATION) {
                    SubNavigationMenu subMenu = (SubNavigationMenu) listObject;
                    switch (subMenu.getNameMenu().toUpperCase()) {
                        case "HOME":
                            Log.d("============", subMenu.getNameMenu());
                            dlMainLayout.closeDrawers();
                            break;
                        case "INTERNAL STORAGE":
                            Log.d("============", subMenu.getNameMenu());
                            dlMainLayout.closeDrawers();
                            break;
                        case "IMAGES":
                            Log.d("============", subMenu.getNameMenu());
                            dlMainLayout.closeDrawers();
                            break;
                        case "VIDEOS":
                            Log.d("============", subMenu.getNameMenu());
                            dlMainLayout.closeDrawers();
                            break;
                        case "MUSIC":
                            Log.d("============", subMenu.getNameMenu());
                            dlMainLayout.closeDrawers();
                            break;
                        case "COMPRESSED":
                            Log.d("============", subMenu.getNameMenu());
                            dlMainLayout.closeDrawers();
                            break;
                        case "DOCUMENTS":
                            Log.d("============", subMenu.getNameMenu());
                            dlMainLayout.closeDrawers();
                            break;
                        case "DOWNLOADS":
                            Log.d("============", subMenu.getNameMenu());
                            dlMainLayout.closeDrawers();
                            break;
                        default:
                            break;
                    }
                }
                break;

            case R.id.lv_file:
                Item item = (Item) listItemManager.getListViewItemFile(position).getObject();
                if (listItemAdapter.getItemViewType(position) == ListViewItemType.FOLDER_ITEM) {
                    Log.d("Path==========", item.getPath());
                   setAdapterForListViewFile(item.getPath());
                }
                break;

            default:
                break;
        }
    }

    private void setAdapterForListViewFile(String filePath) {
        listItemManager = new ListItemManager(filePath);
        listItemManager.createListItem();
        listItemAdapter = new ListItemAdapter(listItemManager);
        lvFile.setAdapter(listItemAdapter);
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open_menu:
                dlMainLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
    }


    private void getWidthHeightScreen() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        heightScreen = displaymetrics.heightPixels;
        widthScreen = displaymetrics.widthPixels;
    }

    private int convertDpToPx(int dp) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) (scale * dp + 0.5f);
        return pixels;
    }

    private String roundTwoDecimalsToString(float f) {
        return String.format("%.2f", f);
    }

    private void checkPermission() {
        String[] listPermission = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        boolean isOn = false;

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
            isOn = true;
        }

        if (isOn) {
            ActivityCompat.requestPermissions(this, listPermission, MY_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            for (int i = 0; i < grantResults.length; i++) {
                switch (i) {
                    case 0:
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(this)
                                    .setMessage("You have denied the READ_EXTERNAL_STORAGE permission" +
                                            " to File manager. Without this permisson File manager can't" +
                                            " function properly.\n\n" +
                                            "Please grant this permission the next time the app starts or" +
                                            " go to permission settings in the app manager.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                        }
                                    })
                                    .create();
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.show();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private boolean isImageFile(String filePath) {
        if (filePath.endsWith(".jpg")
                || filePath.endsWith(".png")
                || filePath.endsWith(".jpeg")
                || filePath.endsWith(".gif"))
        // Add other formats as desired
        {
            return true;
        }
        return false;
    }

    /**
     * This can be used to filter files.
     */
    private class ImageFileFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                for (File file1 : file.listFiles()) {
                    if (file1.isDirectory()) {
                        new ImageFileFilter();
                    } else if (isImageFile(file1.getAbsolutePath())) {
                        return true;
                    }
                }
            } else if (isImageFile(file.getAbsolutePath())) {
                return true;
            }
            return false;
        }
    }
}
