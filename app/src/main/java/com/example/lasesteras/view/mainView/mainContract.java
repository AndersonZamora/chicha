package com.example.lasesteras.view.mainView;

import com.example.lasesteras.model.tableModel;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class mainContract {

    public interface setInfo {

        void attachView(attachViewTable attachViewTable);

        void getListTable(DatabaseReference mReference);
    }

    public interface attachViewTable {

        void showProgressBar();

        void hideProgressBar();

        void showError();

        void setListTableMain(ArrayList<tableModel> listTable);

    }

}
