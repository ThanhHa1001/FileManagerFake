package com.thea.filemanagerfake.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thea.filemanagerfake.R;
import com.thea.filemanagerfake.manager.ListViewItemType;
import com.thea.filemanagerfake.model.Item;
import com.thea.filemanagerfake.model.ListViewItem;

import java.util.ArrayList;

/**
 * Created by Thea on 10/21/2016.
 */

public class AddNewFolderDialog extends Dialog implements View.OnClickListener {

    private TextView txtWarningFolder;
    private EditText edtInputNameFolder;
    private Button btnCancelFolder;
    private Button btnOkFolder;
    private ArrayList<ListViewItem> listViewItems;
    private ArrayList<String> arraysName;

    private OnReceiveNameFolderListener onReceiveNameFolderListener;

    public AddNewFolderDialog(Context context, ArrayList<ListViewItem> listViewItems) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_new_folder);
        this.listViewItems = listViewItems;

        initializeComponents();
    }

    private void initializeComponents() {
        txtWarningFolder = (TextView) findViewById(R.id.txt_warning_folder);
        edtInputNameFolder = (EditText) findViewById(R.id.edt_input_name_folder);
        edtInputNameFolder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (checkName(edtInputNameFolder.getText().toString())) {
                    txtWarningFolder.setText("A folder with the same name already exists");
                    txtWarningFolder.setVisibility(View.VISIBLE);
                    btnOkFolder.setEnabled(false);
                    btnOkFolder.setTextColor(0xffBDBDBD);
                } else {
                    txtWarningFolder.setVisibility(View.GONE);
                    btnOkFolder.setEnabled(true);
                    btnOkFolder.setTextColor(0xffE91E63);
                }

            }
        });
        btnCancelFolder = (Button) findViewById(R.id.btn_cancel_folder);
        btnOkFolder = (Button) findViewById(R.id.btn_ok_folder);

        btnCancelFolder.setOnClickListener(this);
        btnOkFolder.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTitle("Folder");
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        arraysName = new ArrayList<>();
        getListNameFolder();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok_folder:
                String name = edtInputNameFolder.getText().toString();
                if (name.equals("")) {
                    txtWarningFolder.setText("Please input a name for folder");
                    txtWarningFolder.setVisibility(View.VISIBLE);
                    break;
                }
                onReceiveNameFolderListener.onReceiveNameFolderListener(name);
                dismiss();
                break;
            case R.id.btn_cancel_folder:
                dismiss();
                break;
            default:
                break;
        }
    }

    public interface OnReceiveNameFolderListener {
        void onReceiveNameFolderListener(String name);
    }

    public void setOnReceiveNameFolder(OnReceiveNameFolderListener listener) {
        onReceiveNameFolderListener = listener;
    }

    private ArrayList<String> getListNameFolder() {
        Item item;
        for (int i = 0; i < listViewItems.size(); i++) {
            if (listViewItems.get(i).getItemType() == ListViewItemType.FOLDER_ITEM) {
                item = (Item) listViewItems.get(i).getObject();
                arraysName.add(item.getName().toString());
            }
        }
        return arraysName;
    }

    private boolean checkName(String strName) {
        int sizeList = arraysName.size();
        for (int  i = 0; i < sizeList; i++) {
            if (arraysName.get(i).toUpperCase().equals(strName.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}
