package com.thea.filemanagerfake.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import com.thea.filemanagerfake.dialog.AddNewFolderDialog;
import com.thea.filemanagerfake.manager.ListItemManager;
import com.thea.filemanagerfake.manager.MenuNavigationManager;
import com.thea.filemanagerfake.manager.ListViewItemType;
import com.thea.filemanagerfake.model.Item;
import com.thea.filemanagerfake.model.ListViewItem;
import com.thea.filemanagerfake.model.ScrimInsetsFrameLayout;
import com.thea.filemanagerfake.model.SubNavigationMenu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener,
        Toolbar.OnMenuItemClickListener, AdapterView.OnItemLongClickListener {

    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int MY_PERMISSIONS_REQUEST = 1;
    private static final int STATE_NONE = 2;
    private static final int STATE_PREV = 3;
    private static final int STATE_NEXT = 4;
    private static final String INTERNAL_STORAGE = android.os.Environment.getExternalStorageDirectory().toString();

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
    private LinearLayout llOptionCut;
    private LinearLayout llOptionCopy;
    private ImageView imgBtnCancelCut;
    private TextView txtNameFileCut;
    private ImageView imgBtnPasteCut;
    private ImageView imgBtnCancelCopy;
    private TextView txtNameFileCopy;
    private ImageView imgBtnPasteCopy;
    private LinearLayout llForFolderEmpty;
    private ImageView imgForFolderEmpty;
    private TextView txtForFolderEmpty;

    private ListItemManager listItemManager;

    private MenuNavigationAdapter menuNavigationAdapter;
    private ListItemAdapter listItemAdapter;

    private ArrayList<ListViewItem> listFile;
    private ArrayList<String> arrayListPath;
    private ArrayList<String> arrayListNameImage;
    private ArrayList<String> arrayListNameMovie;
    private ArrayList<String> arrayListNameMusic;
    private ArrayList<String> arrayListNameDocument;
    private ArrayList<String> arrayListNameCompressed;
    private ArrayList<String> arrayListNameFile;
    private ArrayList<String> arrayListNameFolder;

    private boolean doubleBackToExitPressedOne;
    private boolean permissionGranted = false;

    private String pathItemSelected;
    private int typeOfItem;

    private int widthScreen;
    private int heightScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
//        } else {
//            permissionGranted = true;
//        }

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

        llOptionCut = (LinearLayout) findViewById(R.id.ll_option_cut);
        imgBtnCancelCut = (ImageView) findViewById(R.id.img_btn_cancel_cut);
        imgBtnCancelCut.setOnClickListener(this);
        txtNameFileCut = (TextView) findViewById(R.id.txt_name_file_cut);
        imgBtnPasteCut = (ImageView) findViewById(R.id.img_btn_paste_cut);
        imgBtnPasteCut.setOnClickListener(this);

        llOptionCopy = (LinearLayout) findViewById(R.id.ll_option_copy);
        imgBtnCancelCopy = (ImageView) findViewById(R.id.img_btn_cancel_copy);
        imgBtnCancelCopy.setOnClickListener(this);
        txtNameFileCopy = (TextView) findViewById(R.id.txt_name_file_copy);
        imgBtnPasteCopy = (ImageView) findViewById(R.id.img_btn_paste_copy);
        imgBtnPasteCopy.setOnClickListener(this);

        llForFolderEmpty = (LinearLayout) findViewById(R.id.ll_for_folder_empty);
        imgForFolderEmpty = (ImageView) findViewById(R.id.img_icon_for_folder_empty);
        txtForFolderEmpty = (TextView) findViewById(R.id.txt_for_folder_empty);

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.new_file:
//                AddNewFileDialog addNewFileDialog = new AddNewFileDialog(this, listFile);
//                addNewFileDialog.show();
//                addNewFileDialog.setOnReceiveNameFile(new AddNewFileDialog.OnReceiveNameFileListener() {
//                    @Override
//                    public void onReceiveNameFileListener(String name) {
//                        File file = new File(txtPathFile.getHint().toString() + "/" + name);
//                        try {
//                            file.createNewFile();
//                            updateListViewFile(txtPathFile.getHint().toString());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                break;
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
        lvFile.setSelector(android.R.color.white);
        if (adapterView.getId() == R.id.lv_menu_navigation) {
            MenuNavigationManager menuNM = MenuNavigationManager.getInstance();
            Object listObject = menuNM.getItemMenuNavigation(position).getObject();
            String linkFile;
            if (menuNavigationAdapter.getItemViewType(position) == ListViewItemType.CONTENT_MENU_NAVIGATION) {
                SubNavigationMenu subMenu = (SubNavigationMenu) listObject;
                switch (subMenu.getNameMenu().toUpperCase()) {
                    case "HOME":
                        setAdapterForListViewFile(INTERNAL_STORAGE);
                        updateTxtPathFile(STATE_NONE, "Internal storage", INTERNAL_STORAGE);
                        dlMainLayout.closeDrawers();
                        break;
                    case "INTERNAL STORAGE":
                        setAdapterForListViewFile(INTERNAL_STORAGE);
                        updateTxtPathFile(STATE_NONE, "Internal storage", INTERNAL_STORAGE);
                        dlMainLayout.closeDrawers();
                        break;
                    case "PICTURES":
                        Log.d("============", subMenu.getNameMenu());
                        linkFile = INTERNAL_STORAGE + "/Pictures";
                        updateTxtPathFile(STATE_NONE, "Pictures", linkFile);
                        setAdapterForListViewFile(linkFile);
                        if (listFile.size() == 0) {
                            lvFile.setVisibility(View.GONE);
                            llForFolderEmpty.setVisibility(View.VISIBLE);
                            changeUIForFolderEmpty(R.drawable.ic_image_white_48dp_2x, "Nothing in Pictures");
                        } else {
                            lvFile.setVisibility(View.VISIBLE);
                            llForFolderEmpty.setVisibility(View.GONE);
                        }
                        dlMainLayout.closeDrawers();
                        break;
                    case "MOVIES":
                        linkFile = INTERNAL_STORAGE + "/Movies";
                        updateTxtPathFile(STATE_NONE, "Movies", linkFile);
                        setAdapterForListViewFile(linkFile);
                        if (listFile.size() == 0) {
                            lvFile.setVisibility(View.GONE);
                            llForFolderEmpty.setVisibility(View.VISIBLE);
                            changeUIForFolderEmpty(R.drawable.ic_movie_creation_white_48dp_2x, "Nothing in Movies");
                        } else {
                            lvFile.setVisibility(View.VISIBLE);
                            llForFolderEmpty.setVisibility(View.GONE);
                        }
                        dlMainLayout.closeDrawers();
                        break;
                    case "MUSIC":
                        linkFile = INTERNAL_STORAGE + "/Music";
                        updateTxtPathFile(STATE_NONE, "Music", linkFile);
                        setAdapterForListViewFile(linkFile);
                        if (listFile.size() == 0) {
                            lvFile.setVisibility(View.GONE);
                            llForFolderEmpty.setVisibility(View.VISIBLE);
                            changeUIForFolderEmpty(R.drawable.ic_library_music_white_48dp_2x, "Nothing in Music");
                        } else {
                            lvFile.setVisibility(View.VISIBLE);
                            llForFolderEmpty.setVisibility(View.GONE);
                        }
                        dlMainLayout.closeDrawers();
                        break;
                    case "DOWNLOADS":
                        linkFile = INTERNAL_STORAGE + "/Download";
                        updateTxtPathFile(STATE_NONE, "Download", linkFile);
                        setAdapterForListViewFile(linkFile);
                        if (listFile.size() == 0) {
                            lvFile.setVisibility(View.GONE);
                            llForFolderEmpty.setVisibility(View.VISIBLE);
                            changeUIForFolderEmpty(R.drawable.ic_file_download_white_48dp_2x, "Nothing in Downloads");
                        } else {
                            lvFile.setVisibility(View.VISIBLE);
                            llForFolderEmpty.setVisibility(View.GONE);
                        }
                        dlMainLayout.closeDrawers();
                        break;
                    default:
                        break;
                }
            }
        } else if (adapterView.getId() == R.id.lv_file) {
            Intent intent;
            Uri uri;
            File url;
            Item item = (Item) listItemManager.getListViewItemFile(position).getObject();
            url = new File(item.getPath().toString());
            switch (listItemAdapter.getItemViewType(position)) {
                case ListViewItemType.FOLDER_ITEM:
                    updateTxtPathFile(STATE_NEXT, item.getPath().toString().substring(item.getPath().lastIndexOf("/") + 1), item.getPath());
                    setAdapterForListViewFile(item.getPath());
                    break;
                case ListViewItemType.DOCUMENT_ITEM:
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    uri = Uri.fromFile(new File(item.getPath().toString()));
                    if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                        // Word document
                        intent.setDataAndType(uri, "application/msword");
                    } else if (url.toString().contains(".pdf")) {
                        // PDF file
                        intent.setDataAndType(uri, "application/pdf");
                    } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                        // Powerpoint file
                        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                    } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                        // Excel file
                        intent.setDataAndType(uri, "application/vnd.ms-excel");
                    } else if (url.toString().contains(".txt")) {
                        // Text file
                        intent.setDataAndType(uri, "text/plain");
                    }
                    startActivity(intent);
                    break;
                case ListViewItemType.IMAGE_ITEM:
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    uri = Uri.fromFile(new File(item.getPath().toString()));
                    intent.setDataAndType(uri, "image/*");
                    startActivity(intent);
                    break;
                case ListViewItemType.MOVIE_ITEM:
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    uri = Uri.fromFile(new File(item.getPath().toString()));
                    intent.setDataAndType(uri, "video/*");
                    startActivity(intent);
                    break;
                case ListViewItemType.MUSIC_ITEM:
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    uri = Uri.fromFile(new File(item.getPath().toString()));
                    intent.setDataAndType(uri, "audio/*");
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
        getNameFileAndFolder();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        llOption.setVisibility(View.VISIBLE);
        lvFile.setSelector(android.R.color.darker_gray);

        Item item = (Item) listItemManager.getListViewItemFile(position).getObject();
        pathItemSelected = item.getPath();
        typeOfItem = listItemManager.getListViewItemFile(position).getItemType();
        return true;
    }

    @Override
    public void onClick(View view) {
        String nameFileSelected = "";
        switch (view.getId()) {
            case R.id.btn_open_menu:
                dlMainLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.ll_btn_cut:
                nameFileSelected = pathItemSelected.substring(pathItemSelected.lastIndexOf("/") + 1);
                txtNameFileCut.setText(nameFileSelected);
                llOption.setVisibility(View.GONE);
                llOptionCopy.setVisibility(View.GONE);
                llOptionCut.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_btn_copy:
                nameFileSelected = pathItemSelected.substring(pathItemSelected.lastIndexOf("/") + 1);
                txtNameFileCopy.setText(nameFileSelected);
                llOption.setVisibility(View.GONE);
                llOptionCopy.setVisibility(View.VISIBLE);
                llOptionCut.setVisibility(View.GONE);
                break;
            case R.id.ll_btn_delete:
                deleteFiles(pathItemSelected);
                setAdapterForListViewFile(txtPathFile.getHint().toString());
                llOption.setVisibility(View.GONE);
                break;
            case R.id.img_btn_cancel_cut:
                llOption.setVisibility(View.VISIBLE);
                llOptionCopy.setVisibility(View.GONE);
                llOptionCut.setVisibility(View.GONE);
                break;
            case R.id.img_btn_paste_cut:
                nameFileSelected = pathItemSelected.substring(pathItemSelected.lastIndexOf("/") + 1);
                if (checkNameFile(nameFileSelected)
                        || checkNameImage(nameFileSelected)
                        || checkNameMovie(nameFileSelected)
                        || checkNameMusic(nameFileSelected)
                        || checkNameDocument(nameFileSelected)
                        || checkNameCompressed(nameFileSelected)) {
                    String message = "File " + nameFileSelected + " already exist. Do you want to overwrite it?";
                    final String finalNameFileSelected = nameFileSelected;
                    AlertDialog alertDialog = new AlertDialog.Builder(this)
                            .setCancelable(true)
                            .setTitle("Cut")
                            .setMessage(message)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    cutFile(pathItemSelected, txtPathFile.getHint().toString() + "/" + finalNameFileSelected);
                                    setAdapterForListViewFile(txtPathFile.getHint().toString());
                                    llOptionCut.setVisibility(View.GONE);
                                    llOption.setVisibility(View.GONE);
                                    Toast.makeText(MainActivity.this,
                                            "Cut successful!",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    llOptionCut.setVisibility(View.GONE);
                                    llOption.setVisibility(View.GONE);
                                }
                            })
                            .create();
                    //touch home button no exit dialog
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.show();
                    TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                    textView.setTextSize(13);
                } else {
                    cutFile(pathItemSelected, txtPathFile.getHint().toString() + "/" + nameFileSelected);
                    setAdapterForListViewFile(txtPathFile.getHint().toString());
                    llOptionCut.setVisibility(View.GONE);
                    llOption.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this,
                            "Cut successful!",
                            Toast.LENGTH_SHORT
                    ).show();
                }
                setAdapterForListViewFile(txtPathFile.getHint().toString());
                break;
            case R.id.img_btn_cancel_copy:
                llOption.setVisibility(View.VISIBLE);
                llOptionCopy.setVisibility(View.GONE);
                llOptionCut.setVisibility(View.GONE);
                break;
            case R.id.img_btn_paste_copy:
                nameFileSelected = pathItemSelected.substring(pathItemSelected.lastIndexOf("/") + 1);
                if (checkNameFolder(nameFileSelected)) {
                    String message = "Folder " + nameFileSelected + " already exist. Do you want to overwrite it?";
                    final String finalNameFileSelected = nameFileSelected;
                    AlertDialog alertDialog = new AlertDialog.Builder(this)
                            .setCancelable(true)
                            .setTitle("Copy")
                            .setMessage(message)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    copyFiles(pathItemSelected, txtPathFile.getHint().toString() + "/" + finalNameFileSelected);
                                    llOptionCopy.setVisibility(View.GONE);
                                    llOption.setVisibility(View.GONE);
                                    Toast.makeText(MainActivity.this,
                                            "Copied successful!",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    llOptionCopy.setVisibility(View.GONE);
                                    llOption.setVisibility(View.GONE);
                                }
                            })
                            .create();
                    //touch home button no exit dialog
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.show();
                    TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                    textView.setTextSize(13);
                } else if (checkNameFile(nameFileSelected)
                        || checkNameImage(nameFileSelected)
                        || checkNameMovie(nameFileSelected)
                        || checkNameMusic(nameFileSelected)
                        || checkNameDocument(nameFileSelected)
                        || checkNameCompressed(nameFileSelected)) {
                    String message = "File " + nameFileSelected + " already exist. Do you want to overwrite it?";
                    final String finalNameFileSelected = nameFileSelected;
                    AlertDialog alertDialog = new AlertDialog.Builder(this)
                            .setCancelable(true)
                            .setTitle("Copy")
                            .setMessage(message)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    copyFiles(pathItemSelected, txtPathFile.getHint().toString() + "/" + finalNameFileSelected);
                                    llOptionCopy.setVisibility(View.GONE);
                                    llOption.setVisibility(View.GONE);
                                    Toast.makeText(MainActivity.this,
                                            "Copied successful!",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    llOptionCopy.setVisibility(View.GONE);
                                    llOption.setVisibility(View.GONE);
                                }
                            })
                            .create();
                    //touch home button no exit dialog
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.show();
                    TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                    textView.setTextSize(13);
                } else {
                    copyFiles(pathItemSelected, txtPathFile.getHint().toString() + "/" + nameFileSelected);
                    llOptionCopy.setVisibility(View.GONE);
                    llOption.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this,
                            "Copied successful!",
                            Toast.LENGTH_SHORT
                    ).show();
                }
                setAdapterForListViewFile(txtPathFile.getHint().toString());
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Toast toast = Toast.makeText(this,
                "Press back again to exit app",
                Toast.LENGTH_SHORT
        );
        String strPath;
        if (doubleBackToExitPressedOne) {
            super.onBackPressed();
            toast.cancel();
            return;
        } else if (llOption.getVisibility() == View.VISIBLE) {
            llOption.setVisibility(View.GONE);
            lvFile.setSelector(android.R.color.transparent);
            return;
        } else {
            if (llForFolderEmpty.getVisibility() == View.VISIBLE) {
                llForFolderEmpty.setVisibility(View.GONE);
                lvFile.setVisibility(View.VISIBLE);
            }
            strPath = txtPathFile.getHint().toString().substring(0, txtPathFile.getHint().toString().lastIndexOf("/"));
            if (strPath.equals(INTERNAL_STORAGE)) {
                updateTxtPathFile(STATE_NONE, "Internal storage", strPath);
            } else if (!txtPathFile.getText().equals("Internal storage")) {
                updateTxtPathFile(STATE_PREV, strPath.substring(strPath.lastIndexOf("/") + 1), strPath);
            } else {
                strPath = INTERNAL_STORAGE;
                doubleBackToExitPressedOne = true;
                toast.show();
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

    private void setAdapterForListViewFile(String filePath) {
        listFile = listItemManager.createListItem(filePath);
        listItemAdapter = new ListItemAdapter(listFile);
        lvFile.setAdapter(listItemAdapter);
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

    private void copyFile(String inputPath, String outputPath) {
        try {
            File afile = new File(inputPath);
            File bfile = new File(outputPath);
            InputStream in = new FileInputStream(afile);
            OutputStream out = new FileOutputStream(bfile);

            if (!bfile.exists()) {
                bfile.createNewFile();
            }

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }

            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyFiles(String sourceLocation, String targetLocation) {
        try {
            File afile = new File(sourceLocation);
            File bfile = new File(targetLocation);
            if (afile.isDirectory()) {
                if (!bfile.exists()) {
                    bfile.mkdir();
                }
                File[] files = afile.listFiles();
                for (File f : files) {
                    copyFiles(f.getPath(), targetLocation + "/" + f.getName());
                }
            } else {
                copyFile(sourceLocation, targetLocation + afile.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void cutFile(String inputPath, String outputPath) {
        try {
            File afile = new File(inputPath);
            File bfile = new File(outputPath);
            InputStream in = new FileInputStream(afile);
            OutputStream out = new FileOutputStream(bfile);

            if (!bfile.exists()) {
                bfile.createNewFile();
            }

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }

            in.close();
            out.close();
            afile.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteFiles(String pathFile) {
        File file = new File(pathFile);
        if (file.isDirectory()) {
            File[] listFile = file.listFiles();
            for (File f : listFile) {
                deleteFiles(f.getPath());
            }
        }
        try {
            file.delete();
        } catch (Exception e) {

        }

    }

    private void changeUIForFolderEmpty(int resID, String name) {
        imgForFolderEmpty.setImageResource(resID);
        txtForFolderEmpty.setText(name);
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

    private void getNameFileAndFolder() {
        arrayListNameImage = new ArrayList<>();
        arrayListNameMusic = new ArrayList<>();
        arrayListNameMovie = new ArrayList<>();
        arrayListNameDocument = new ArrayList<>();
        arrayListNameCompressed = new ArrayList<>();
        arrayListNameFile = new ArrayList<>();
        arrayListNameFolder = new ArrayList<>();

        for (int i = 0; i < listFile.size(); i++) {
            Item item = (Item) listFile.get(i).getObject();
            int type = listFile.get(i).getItemType();
            if (type == ListViewItemType.FOLDER_ITEM) {
                arrayListNameFolder.add(item.getName());
            } else if (type == ListViewItemType.FILE_ITEM) {
                arrayListNameFile.add(item.getName());
            } else if (type == ListViewItemType.IMAGE_ITEM) {
                arrayListNameImage.add(item.getName());
            } else if (type == ListViewItemType.MUSIC_ITEM) {
                arrayListNameMusic.add(item.getName());
            } else if (type == ListViewItemType.MOVIE_ITEM) {
                arrayListNameMovie.add(item.getName());
            } else if (type == ListViewItemType.DOCUMENT_ITEM) {
                arrayListNameDocument.add(item.getName());
            } else if (type == ListViewItemType.COMPRESSED_ITEM) {
                arrayListNameCompressed.add(item.getName());
            }
        }
    }

    private boolean checkNameFolder(String nameFolder) {
        if (!arrayListNameFolder.isEmpty()) {
            for (int i = 0; i < arrayListNameFolder.size(); i++) {
                if (arrayListNameFolder.get(i).equals(nameFolder)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkNameFile(String nameFile) {
        if (!arrayListNameFile.isEmpty()) {
            for (int i = 0; i < arrayListNameFile.size(); i++) {
                if (arrayListNameFile.get(i).equals(nameFile)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkNameImage(String nameImage) {
        if (!arrayListNameImage.isEmpty()) {
            for (int i = 0; i < arrayListNameImage.size(); i++) {
                if (arrayListNameImage.get(i).equals(nameImage)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkNameMusic(String nameMusic) {
        if (!arrayListNameMusic.isEmpty()) {
            for (int i = 0; i < arrayListNameMusic.size(); i++) {
                if (arrayListNameMusic.get(i).equals(nameMusic)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkNameMovie(String nameMovie) {
        if (!arrayListNameMovie.isEmpty()) {
            for (int i = 0; i < arrayListNameMovie.size(); i++) {
                if (arrayListNameMovie.get(i).equals(nameMovie)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkNameDocument(String nameDocument) {
        if (!arrayListNameDocument.isEmpty()) {
            for (int i = 0; i < arrayListNameDocument.size(); i++) {
                if (arrayListNameDocument.get(i).equals(nameDocument)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkNameCompressed(String nameCompressed) {
        if (!arrayListNameCompressed.isEmpty()) {
            for (int i = 0; i < arrayListNameCompressed.size(); i++) {
                if (arrayListNameCompressed.get(i).equals(nameCompressed)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapterForListViewFile(txtPathFile.getHint().toString());
    }
}
