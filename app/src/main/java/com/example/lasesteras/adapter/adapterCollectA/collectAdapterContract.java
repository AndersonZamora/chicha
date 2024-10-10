package com.example.lasesteras.adapter.adapterCollectA;


import com.example.lasesteras.model.salesModel;
import com.example.lasesteras.model.tableModel;

public class collectAdapterContract {

    public interface contract {
        void attachViewC(attachViewCollect viewCollect);

        void saveCollect(tableModel model, salesModel sales, String date, tableModel recur);
    }

    public interface attachViewCollect {

        void shotProgressSaveMesa();

        void hideProgressSaveMesa();

        void shotProgressUpdateMesa();

        void hideProgressUpdateMesa();

        void toastView(String s);

        void hideDialog();
    }
}
