package com.example.lasesteras.presenterAdmin.tablePresenterA;


public interface tableInterfaceA {

    interface Callback {

        void onSuccess();

        void onFailure(String errorMsgS);
    }

    void getListHds(Callback callback);
}
