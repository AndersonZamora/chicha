package com.example.lasesteras.adapter.adapterTableA;

import android.app.Dialog;
import android.view.WindowManager;

import com.example.lasesteras.model.dishesModel;
import com.example.lasesteras.model.tableModel;
import com.google.firebase.database.DatabaseReference;

import java.util.List;


public class adapterTableContract {

    public interface window {

        void mWindowManager(Dialog dialog, WindowManager windowManager);

        void collect_table(tableModel mTablet, Dialog dialog, DatabaseReference mReference);

        void attachView(attachViewAdapterA viewAdapterA);
    }

    public interface attachViewAdapterA {

        void loadMesa();

        void closeLoadMesa();

        void errorLoadMesa(String error);

        void mWindowManager();

        //set info
        void Init(List<dishesModel> listModels, tableModel recur);
    }
}
