package com.thea.filemanagerfake.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thea.filemanagerfake.R;
import com.thea.filemanagerfake.adapter.ListItemAdapter;
import com.thea.filemanagerfake.adapter.MenuNavigationAdapter;
import com.thea.filemanagerfake.dialog.AddNewFileDialog;
import com.thea.filemanagerfake.dialog.AddNewFolderDialog;
import com.thea.filemanagerfake.manager.ListItemManager;
import com.thea.filemanagerfake.manager.MenuNavigationManager;
import com.thea.filemanagerfake.manager.ListViewItemType;
import com.thea.filemanagerfake.model.Item;
import com.thea.filemanagerfake.model.ListViewItem;
import com.thea.filemanagerfake.model.ScrimInsetsFrameLayout;
import com.thea.filemanagerfake.model.SubNavigationMenu;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener,
        Toolbar.OnMenuItemClickListener, AdapterView.OnItemLongClickListener {

    private static final int MY_PERMISSIONS_REQUEST = 1;
    private static final int STATE_NONE = 2;
    private static final int STATE_PREV = 3;
    private static final int STATE_NEXT = 4;
    private static final long KILOBYTE = 1024;
    private static final String INTERNAL_STORAGE = android.os.Environment.getExternalStorageDirectory().toString();

    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private DrawerLayout dlMainLayout;
    private ScrimInsetsFrameLayout scrimFLMenuNavigation;
    private ListView lvMenuNavigation;
    private ListView lvFile;
    private ImageView btnOpenmenu;
    private TextView txtPathFile;
    private Toolbar toolbar;
    private LinearLayout llOption;
    private LinearLayout llBtnCut;
    private LinearLayout llBtnCopy;
    private LinearLayout llBtnDelete;

    private ListItemManager listItemManager;

    private MenuNavigationAdapter menuNavigationAdapter;
    private ListItemAdapter listItemAdapter;

    private int heightScreen;
    private int widthScreen;

    private ArrayList<ListViewItem> listFile;
    private ArrayList<String> arrayListPath;

    private boolean doubleBackToExitPressedOne;
    private boolean permissionGranted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else {
            permissionGranted = true;
        }

        getWidthHeightScreen();
        initializeToolbar();
        initializeComponents();
    }

    private void initializeToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.setOnMenuItemClickListener(this);
    }

    public void checkPermission() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermissions();
        } else {
            permissionGranted = true;
        }
    }

    private void requestStoragePermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, MY_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listFile = listItemManager.createListItem(INTERNAL_STORAGE);
                            listItemAdapter = new ListItemAdapter(listFile);
                            lvFile.setAdapter(listItemAdapter);
                        }
                    });
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
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void initializeComponents() {
        doubleBackToExitPressedOne = false;

        arrayListPath = new ArrayList<>();
        listItemManager = ListItemManager.getInstance();
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

        btnOpenmenu = (ImageView) findViewById(R.id.btn_open_menu);
        btnOpenmenu.setOnClickListener(this);

        txtPathFile = (TextView) findViewById(R.id.txt_path_file);
        updateTxtPathFile(STATE_NONE, "Internal storage", INTERNAL_STORAGE);

        lvFile = (ListView) findViewById(R.id.lv_file);
        lvFile.setOnItemClickListener(this);
        lvFile.setOnItemLongClickListener(this);

        if (permissionGranted) {
            listFile = listItemManager.createListItem(INTERNAL_STORAGE);
            listItemAdapter = new ListItemAdapter(listFile);
            lvFile.setAdapter(listItemAdapter);
        }

        llOption = (LinearLayout) findViewById(R.id.ll_option);
        llBtnCut = (LinearLayout) findViewById(R.id.ll_btn_cut);
        llBtnCut.setOnClickListener(this);
        llBtnCopy = (LinearLayout) findViewById(R.id.ll_btn_copy);
        llBtnCopy.setOnClickListener(this);
        llBtnDelete = (LinearLayout) findViewById(R.id.ll_btn_delete);
        llBtnDelete.setOnClickListener(this);
//        File[] files = new File(android.os.Environment.getExternalStorageDirectory() + "").listFiles();
//        Log.d("---------------------", files.length + "");
//
//        for (File file : files) {
//
//
////            String dateFormat = "dd/MM/yyyy HH:mm:ss";
////
////            if (file.isDirectory()) {
////                File[] file2 = new File(file.getAbsoluteFile().getPath()).listFiles();
////                Log.d("Number item ", file2.length+"");
////                for (File file3 : file2) {
////                    Date lastModified = new Date(file3.lastModified());
////                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
////                    String formattedDateString = formatter.format(lastModified);
////                    Log.d("----------22222", file3.getPath().toString().substring(file3.getPath().lastIndexOf("/") + 1) + "-----------" + formattedDateString + " -------------" + capacityFile(file3));
////
////                }
////            }
//            Log.d("----------22222", file.getPath().toString().substring(file.getPath().lastIndexOf("/") + 1) + "-----------" + capacityFile(file));
//            Log.d("------------111111", file.getPath());
//        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_file:
                AddNewFileDialog addNewFileDialog = new AddNewFileDialog(this, listFile);
                addNewFileDialog.show();
                addNewFileDialog.setOnReceiveNameFile(new AddNewFileDialog.OnReceiveNameFileListener() {
                    @Override
                    public void onReceiveNameFileListener(String name) {
                        File file = new File(txtPathFile.getHint().toString() + "/" + name);
                        try {
                            file.createNewFile();
                            updateListViewFile(txtPathFile.getHint().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case R.id.new_folder:
                AddNewFolderDialog addNewFolderDialog = new AddNewFolderDialog(this, listFile);
                addNewFolderDialog.show();
                addNewFolderDialog.setOnReceiveNameFolder(new AddNewFolderDialog.OnReceiveNameFolderListener() {
                    @Override
                    public void onReceiveNameFolderListener(String name) {
                        File file = new File(txtPathFile.getHint().toString() + "/" + name);
                        file.mkdir();
                        updateListViewFile(txtPathFile.getHint().toString());
                    }
                });
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        switch (adapterView.getId()) {
            case R.id.lv_menu_navigation:
                MenuNavigationManager menuNM = MenuNavigationManager.getInstance();
                Object listObject = menuNM.getItemMenuNavigation(position).getObject();

                String linkFile;
                if (menuNavigationAdapter.getItemViewType(position) == ListViewItemType.CONTENT_MENU_NAVIGATION) {
                    SubNavigationMenu subMenu = (SubNavigationMenu) listObject;
                    switch (subMenu.getNameMenu().toUpperCase()) {
                        case "HOME":
                            Log.d("============", subMenu.getNameMenu());

                            dlMainLayout.closeDrawers();
                            break;
                        case "INTERNAL STORAGE":
                            Log.d("============", subMenu.getNameMenu());
                            setAdapterForListViewFile(INTERNAL_STORAGE);
                            updateTxtPathFile(STATE_NONE, "Internal storage", INTERNAL_STORAGE);
                            dlMainLayout.closeDrawers();
                            break;
                        case "PICTURES":
                            Log.d("============", subMenu.getNameMenu());
                            dlMainLayout.closeDrawers();
                            break;
                        case "MOVIES":
                            Log.d("============", subMenu.getNameMenu());
                            linkFile = INTERNAL_STORAGE + "/Movies";
                            updateTxtPathFile(STATE_NONE, "Movies", linkFile);
                            setAdapterForListViewFile(linkFile);
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
                            linkFile = INTERNAL_STORAGE + "/Download";
                            updateTxtPathFile(STATE_NONE, "Download", linkFile);
                            setAdapterForListViewFile(linkFile);
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
                    updateTxtPathFile(STATE_NEXT, item.getPath().toString().substring(item.getPath().lastIndexOf("/") + 1), item.getPath());
                    setAdapterForListViewFile(item.getPath());
                }
                break;

            default:
                break;
        }
    }

    private void setAdapterForListViewFile(String filePath) {
        listFile = listItemManager.createListItem(filePath);
        listItemAdapter = new ListItemAdapter(listFile);
        lvFile.setAdapter(listItemAdapter);
    }

    @Override
    public void onBackPressed() {
        String strPath;
        if (doubleBackToExitPressedOne) {
            super.onBackPressed();
            return;
        } else if (llOption.getVisibility() == View.VISIBLE) {
            llOption.setVisibility(View.GONE);
            return;
        } else {
            strPath = txtPathFile.getHint().toString().substring(0, txtPathFile.getHint().toString().lastIndexOf("/"));
            if (strPath.equals(INTERNAL_STORAGE)) {
                updateTxtPathFile(STATE_NONE, "Internal storage", strPath);
            } else if (!txtPathFile.getText().equals("Internal storage")){
                updateTxtPathFile(STATE_PREV, strPath.substring(strPath.lastIndexOf("/") + 1), strPath);
            } else {
                strPath = INTERNAL_STORAGE;
                doubleBackToExitPressedOne = true;
                Toast.makeText(this,
                        "Press back again to exit app",
                        Toast.LENGTH_SHORT
                ).show();
             new Handler().postDelayed(new Runnable() {
                 @Override
                 public void run() {
                     doubleBackToExitPressedOne = false;
                 }
             }, 2000);
            }
            setAdapterForListViewFile(strPath);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open_menu:
                dlMainLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.ll_btn_cut:
                break;
            case R.id.ll_btn_copy:
                break;
            case R.id.ll_btn_delete:
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        llOption.setVisibility(View.VISIBLE);
        return true;
    }

    private void updateListViewFile(String strPath) {
        listFile = listItemManager.createListItem(strPath);
        listItemAdapter = new ListItemAdapter(listFile);
        lvFile.setAdapter(listItemAdapter);
    }

    private void updateTxtPathFile(int state, String name, String strPath) {
        if (state == STATE_NONE) {
            arrayListPath.clear();
            arrayListPath.add(name);
        } else if (state == STATE_PREV) {
            arrayListPath.remove(arrayListPath.size() - 1);
        } else if (state == STATE_NEXT) {
            arrayListPath.add(name);
        }

        int length = arrayListPath.size();
        txtPathFile.setHint(strPath);
        String str = arrayListPath.get(0);
        for (int i = 1; i < length; i++) {
            str += " / " + arrayListPath.get(i);
        }
        txtPathFile.setText(str);
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
