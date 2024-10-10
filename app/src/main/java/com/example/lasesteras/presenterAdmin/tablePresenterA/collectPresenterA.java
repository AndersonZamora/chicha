package com.example.lasesteras.presenterAdmin.tablePresenterA;

import android.util.Log;

import com.example.lasesteras.adapter.adapterCollectA.collectAdapterContract;
import com.example.lasesteras.model.salesModel;
import com.example.lasesteras.model.tableModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class collectPresenterA implements collectAdapterContract.contract {

    private collectAdapterContract.attachViewCollect viewCollect;

    @Override
    public void attachViewC(collectAdapterContract.attachViewCollect viewCollect) {
        this.viewCollect = viewCollect;
    }

    @Override
    public void saveCollect(tableModel model, salesModel sales, String date, tableModel recur) {

        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
        viewCollect.shotProgressSaveMesa();
        mReference.child("sales").child(date).push().setValue(sales).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                viewCollect.hideProgressSaveMesa();
                viewCollect.shotProgressUpdateMesa();
                model.setTableStatus("FALSE");
                model.setAttended("FALSE");
                model.setTableName(recur.getTableName());
                model.setModels(null);

                mReference.child("tableList").child(recur.getChildId()).setValue(model).addOnCompleteListener(task12 -> {

                    if (task12.isSuccessful()) {
                        viewCollect.hideProgressUpdateMesa();
                        viewCollect.hideDialog();
                        viewCollect.toastView("Venta guardada -- Mesa Actualizada");
                    } else {
                        viewCollect.hideProgressUpdateMesa();
                        viewCollect.toastView("Actualizar mesa manualmente ocurrió un error");
                    }
                });

            } else {
                viewCollect.toastView("COMPRUEBE SU CONEXION A INTERNET, OCURRIO UN ERROR EN EL PROCESO");
                viewCollect.hideProgressSaveMesa();
                viewCollect.hideDialog();
            }
        });
    }
}
