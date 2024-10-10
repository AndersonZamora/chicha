package com.example.lasesteras.presenter.mainPresenter;

import com.example.lasesteras.model.tableModel;
import com.example.lasesteras.presenter.getInformationPresenter.informationF;
import com.example.lasesteras.presenter.getInformationPresenter.presenterInformation;
import com.example.lasesteras.view.mainView.mainContract;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class presenterMain implements mainContract.setInfo {

    private mainContract.attachViewTable attachViewTable;
    private final presenterInformation mInformation;

    public presenterMain(presenterInformation mInformation) {
        this.mInformation = mInformation;
    }

    @Override
    public void attachView(mainContract.attachViewTable attachViewTable) {
        this.attachViewTable = attachViewTable;
    }

    @Override
    public void getListTable(DatabaseReference mReference) {

        attachViewTable.showProgressBar();
        mInformation.getListTable(mReference, new informationF.Callback() {
            @Override
            public void onSuccess(ArrayList<tableModel> tableModels) {
                attachViewTable.hideProgressBar();
                attachViewTable.setListTableMain(tableModels);
            }

            @Override
            public void onFailure(String errorMsgS) {
                attachViewTable.hideProgressBar();
                attachViewTable.showError();
            }
        });
    }
}
