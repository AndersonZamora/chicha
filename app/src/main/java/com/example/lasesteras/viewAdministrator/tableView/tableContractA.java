package com.example.lasesteras.viewAdministrator.tableView;

import android.app.Dialog;

import com.example.lasesteras.model.tableModel;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class tableContractA {

    public interface setInfo {

        void attachView(attachViewTableA attachViewTable);

        void getListTableA(DatabaseReference mReference);

        void saveTable(DatabaseReference mReference, tableModel model);

        void mWindowManager(Dialog dialog);

        void mWindowManagerAdd(Dialog dialog);

    }

    public interface attachViewTableA {

        void showProgressBar();

        void hideProgressBar();

        void showToast();

        void showToastError();

        void showError();

        void setListTableAd(ArrayList<tableModel> listTable);
    }
}
