package com.example.lasesteras.presenterAdmin.tablePresenterA;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.example.lasesteras.R;
import com.example.lasesteras.model.tableModel;
import com.example.lasesteras.presenter.getInformationPresenter.informationF;
import com.example.lasesteras.presenter.getInformationPresenter.presenterInformation;
import com.example.lasesteras.viewAdministrator.tableView.tableContractA;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class tablePresenterA implements tableContractA.setInfo {
    private tableContractA.attachViewTableA attachViewTableA;
    private final presenterInformation mInformation;
    private final Context context;

    public tablePresenterA(presenterInformation mInformation, Context context) {
        this.mInformation = mInformation;
        this.context = context;
    }

    @Override
    public void attachView(tableContractA.attachViewTableA attachViewTable) {
        this.attachViewTableA = attachViewTable;
    }

    @Override
    public void getListTableA(DatabaseReference mReference) {
        attachViewTableA.showProgressBar();
        mInformation.getListTable(mReference, new informationF.Callback() {
            @Override
            public void onSuccess(ArrayList<tableModel> tableModels) {
                attachViewTableA.hideProgressBar();
                attachViewTableA.setListTableAd(tableModels);
            }

            @Override
            public void onFailure(String errorMsgS) {
                attachViewTableA.hideProgressBar();
                attachViewTableA.showError();
            }
        });
    }

    @Override
    public void saveTable(DatabaseReference mReference, tableModel model) {
        mReference.child("tableList").push().setValue(model).addOnCompleteListener(task -> attachViewTableA.showToast()).addOnFailureListener(e -> attachViewTableA.showToastError());
    }

    @Override
    public void mWindowManager(Dialog dialog) {
        DisplayMetrics displayMetrics = new DisplayMetrics();

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //noinspection deprecation
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());

        int dialogWindowWidth = (int) (displayWidth * 0.8f);
        int dialogWindowHeight = (int) (displayHeight * 0.4f);

        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialog.setContentView(R.layout.dialog_row_table);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setAttributes(layoutParams);
    }

    @Override
    public void mWindowManagerAdd(Dialog dialogAdd) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //noinspection deprecation
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogAdd.getWindow().getAttributes());

        int dialogWindowWidth = (int) (displayWidth * 0.6f);
        int dialogWindowHeight = (int) (displayHeight * 0.4f);

        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialogAdd.setContentView(R.layout.dialo_roow_add_table);
        dialogAdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAdd.getWindow().setAttributes(layoutParams);
    }
}
