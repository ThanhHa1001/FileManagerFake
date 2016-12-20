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
import android.widget.Toast;

import com.thea.filemanagerfake.App;
import com.thea.filemanagerfake.R;
import com.thea.filemanagerfake.manager.ListViewItemType;
import com.thea.filemanagerfake.model.Item;
import com.thea.filemanagerfake.model.ListViewItem;

import java.util.ArrayList;

/**
 * Created by Thea on 10/21/2016.
 */

public class AddNewFileDialog extends Dialog implements View.OnClickListener {

    private TextView txtWarningFile;
    private EditText edtInputNameFile;
    private Button btnCancelFile;
    private Button btnOkFile;
    private ArrayList<ListViewItem> listViewItems;
    private ArrayList<String> arraysName;

    private OnReceiveNameFileListener onReceiveNameFileListener;

    public AddNewFileDialog(Context context, ArrayList<ListViewItem> listViewItems) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_new_file);
        this.listViewItems = listViewItems;

        initializeComponents();

    }

    private void initializeComponents() {
        txtWarningFile = (TextView) findViewById(R.id.txt_warning_file);
        edtInputNameFile = (EditText) findViewById(R.id.edt_input_name_file);
        edtInputNameFile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtWarningFile.setText("A file with the same name already exists");
                if (checkName(edtInputNameFile.getText().toString())) {
                    txtWarningFile.setVisibility(View.VISIBLE);
                    btnOkFile.setEnabled(false);
                    btnOkFile.setTextColor(0xffBDBDBD);
                } else {
                    txtWarningFile.setVisibility(View.GONE);
                    btnOkFile.setEnabled(true);
                    btnOkFile.setTextColor(0xffE91E63);
                }
            }
        });

        btnCancelFile = (Button) findViewById(R.id.btn_cancel_file);
        btnOkFile = (Button) findViewById(R.id.btn_ok_file);

        btnCancelFile.setOnClickListener(this);
        btnOkFile.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
//        setTitle("File");
        arraysName = new ArrayList<>();
        getNameListFile();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok_file:
                String name = edtInputNameFile.getText().toString();
                if (name.equals("")) {
                    txtWarningFile.setText("Please input a name for file");
                    txtWarningFile.setVisibility(View.VISIBLE);
                    break;
                }
                onReceiveNameFileListener.onReceiveNameFileListener(name);
                dismiss();
                break;
            case R.id.btn_cancel_file:
                dismiss();
                break;
            default:
                break;
        }
    }


    public interface OnReceiveNameFileListener {
        void onReceiveNameFileListener(String name);
    }

    public void setOnReceiveNameFile(OnReceiveNameFileListener listener) {
        onReceiveNameFileListener = listener;
    }

    private ArrayList<String> getNameListFile() {
        Item item;
        for (int i = 0; i < listViewItems.size(); i++) {
            if (listViewItems.get(i).getItemType() != ListViewItemType.FOLDER_ITEM) {
                item = (Item) listViewItems.get(i).getObject();
                arraysName.add(item.getName().toString());
            }
        }
        return arraysName;
    }

    private boolean checkName(String strName) {
        int sizeList = arraysName.size();
        for (int i = 0; i < sizeList; i++) {
            if (arraysName.get(i).toUpperCase().equals(strName.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}
