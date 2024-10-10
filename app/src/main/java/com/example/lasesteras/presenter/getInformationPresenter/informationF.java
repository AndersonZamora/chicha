package com.example.lasesteras.presenter.getInformationPresenter;

import com.example.lasesteras.model.tableModel;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public interface informationF {

    interface Callback {

       void onSuccess(ArrayList<tableModel> tableModels);

        void onFailure(String errorMsgS);
    }

    void getListTable(DatabaseReference reference, Callback callback);
}
