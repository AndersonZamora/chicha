package com.example.lasesteras.adapterPersonal;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lasesteras.R;
import com.example.lasesteras.model.employeesModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class personalAdapter extends RecyclerView.Adapter<personalAdapter.viewHolder> {

    List<employeesModel> modelList;
    Context mContext;
    String user;
    Dialog dialogAdd;
    Spinner spinner_id;
    private final DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
    private TextInputLayout fullName_add, email_add, cellular_add, username_add, password_add;

    public personalAdapter(List<employeesModel> modelList, Context mContext, String user) {
        this.modelList = modelList;
        this.mContext = mContext;
        this.user = user;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_list_personal, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        employeesModel model = modelList.get(position);
        holder.name_personal.setText(model.getFullName());
        holder.rol_personal.setText(model.getRole());
        holder.card_view_id.setOnClickListener(v -> {

            initDialog();
            dialogAdd.setCancelable(false);
            dialogAdd.show();

            fullName_add = dialogAdd.findViewById(R.id.fullName_add);
            email_add = dialogAdd.findViewById(R.id.email_add);
            cellular_add = dialogAdd.findViewById(R.id.cellular_add);
            username_add = dialogAdd.findViewById(R.id.username_add);
            username_add.setEnabled(false);
            password_add = dialogAdd.findViewById(R.id.password_add);

            TextView role_view = dialogAdd.findViewById(R.id.role_view);
            RelativeLayout relative_layout_spinner = dialogAdd.findViewById(R.id.relative_layout_spinner);
            Button save_item = dialogAdd.findViewById(R.id.save_item);
            Button delete_item = dialogAdd.findViewById(R.id.delete_item);

            save_item.setBackgroundColor(Color.GREEN);
            delete_item.setVisibility(View.VISIBLE);
            delete_item.setBackgroundColor(Color.RED);

            if (!model.getUsername().equals(user)) {

                save_item.setBackgroundColor(Color.GREEN);
                delete_item.setVisibility(View.VISIBLE);
                delete_item.setBackgroundColor(Color.RED);

                Objects.requireNonNull(fullName_add.getEditText()).setText(model.getFullName());
                Objects.requireNonNull(email_add.getEditText()).setText(model.getEmail());
                Objects.requireNonNull(cellular_add.getEditText()).setText(model.getCellular());
                Objects.requireNonNull(username_add.getEditText()).setText(model.getUsername());
                Objects.requireNonNull(password_add.getEditText()).setText(model.getPassword());
                initSpinner(model.getRole());

            } else {

                role_view.setVisibility(View.VISIBLE);
                fullName_add.setVisibility(View.GONE);
                email_add.setVisibility(View.GONE);
                cellular_add.setVisibility(View.GONE);
                username_add.setVisibility(View.GONE);
                password_add.setVisibility(View.GONE);
                relative_layout_spinner.setVisibility(View.GONE);
                save_item.setVisibility(View.GONE);
                delete_item.setVisibility(View.GONE);
            }

            dialogAdd.findViewById(R.id.dialog_close_add).setOnClickListener(v1 -> dialogAdd.dismiss());
            delete_item.setOnClickListener(v14 -> {
                mReference.child("employees").child(model.getUsername()).removeValue().addOnSuccessListener(aVoid -> Toast.makeText(mContext, "ELIMINADO", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(mContext, "OCURRIO UN ERROR INTENTAR DE NUEVO", Toast.LENGTH_SHORT).show());
                dialogAdd.dismiss();
            });
            save_item.setOnClickListener(v13 -> postInformation(model));
        });
    }

    private void postInformation(employeesModel model) {

        String item = spinner_id.getSelectedItem().toString();
        String name = Objects.requireNonNull(fullName_add.getEditText()).getText().toString().trim();
        String email = Objects.requireNonNull(email_add.getEditText()).getText().toString().trim();
        String cellular = Objects.requireNonNull(cellular_add.getEditText()).getText().toString().trim();
        String username = Objects.requireNonNull(username_add.getEditText()).getText().toString().replace(" ", "").trim();
        String password = Objects.requireNonNull(password_add.getEditText()).getText().toString().replace(" ", "").trim();

        String itemU = model.getRole();
        String nameU = model.getFullName();
        String emailU = model.getEmail();
        String cellularU = model.getCellular();
        String usernameU = model.getUsername();
        String passwordU = model.getPassword();

            if (!validateName(name) | !validateEmail(email) | !validateCellular(cellular) | !validateUsername(username) | !validatePassword(password)) {
            return;
        }

        if (validaNameUpdate(name, nameU, usernameU) | validateEmailUpdate(email, emailU, usernameU)
                | validateCellularUpdate(cellular, cellularU, usernameU) | validatePasswordUpdate(password, passwordU, usernameU)
                | validateRol(item, itemU, usernameU)) {
            dialogAdd.dismiss();
        }
        dialogAdd.dismiss();

    }


    @Override
    public int getItemCount() {
        return modelList.size();
    }

    static class viewHolder extends RecyclerView.ViewHolder {
        TextView name_personal;
        TextView rol_personal;
        CardView card_view_id;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            name_personal = itemView.findViewById(R.id.name_personal);
            rol_personal = itemView.findViewById(R.id.rol_personal);
            card_view_id = itemView.findViewById(R.id.card_view_id);
        }
    }

    private void initDialog() {

        dialogAdd = new Dialog(mContext);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        //noinspection deprecation
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogAdd.getWindow().getAttributes());

        int dialogWindowWidth = (int) (displayWidth * 0.9f);
        int dialogWindowHeight = (int) (displayHeight * 0.85f);

        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialogAdd.setContentView(R.layout.dialog_add_personal);
        dialogAdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAdd.getWindow().setAttributes(layoutParams);
    }

    private void initSpinner(String value) {

        List<String> list = new ArrayList<>();

        switch (value) {
            case "waitress":
                list.add(value);
                list.add("cook");
                list.add("administrator");
                break;
            case "cook":
                list.add(value);
                list.add("waitress");
                list.add("administrator");

                break;
            case "administrator":
                list.add(value);
                list.add("cook");
                list.add("waitress");
                break;
        }

        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(
                mContext, android.R.layout.simple_spinner_item, list
        );

        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_id = dialogAdd.findViewById(R.id.spinner_id);
        spinner_id.setAdapter(mAdapter);
    }

    //VALIDATE
    private boolean validateName(String validate) {

        if (validate.isEmpty()) {
            fullName_add.setError(mContext.getString(R.string.val_empty));
            return false;
        } else {

            fullName_add.setError(null);
            fullName_add.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail(String validate) {
        if (validate.isEmpty()) {
            email_add.setError(mContext.getString(R.string.val_empty));
            return false;
        } else {
            email_add.setError(null);
            email_add.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateCellular(String validate) {
        if (validate.isEmpty()) {
            cellular_add.setError(mContext.getString(R.string.val_empty));
            return false;
        } else {
            cellular_add.setError(null);
            cellular_add.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUsername(String validate) {
        if (validate.isEmpty()) {
            username_add.setError(mContext.getString(R.string.val_empty));
            return false;
        } else {
            username_add.setError(null);
            username_add.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword(String validate) {
        if (validate.isEmpty()) {
            password_add.setError(mContext.getString(R.string.val_empty));
            return false;
        } else {
            password_add.setError(null);
            password_add.setErrorEnabled(false);
            return true;
        }
    }

    //UPDATE
    private boolean validaNameUpdate(String validate, String exist, String child) {

        if (!validate.equals(exist)) {
            mReference.child("employees").child(child).child("fullName").setValue(validate).addOnSuccessListener(aVoid -> Toast.makeText(mContext, "Nombre actualizado", Toast.LENGTH_LONG).show());
            fullName_add.setError(null);
            fullName_add.setErrorEnabled(false);
            return true;
        }
        return false;
    }

    private boolean validateEmailUpdate(String validate, String exist, String child) {

        if (!validate.equals(exist)) {

            for (employeesModel a : modelList) {
                if (a.getEmail().equals(validate)) {
                    email_add.setError(mContext.getString(R.string.existsEmail));
                    return false;
                }
            }
            mReference.child("employees").child(child).child("email").setValue(validate).addOnSuccessListener(aVoid -> Toast.makeText(mContext, "Email actualizado", Toast.LENGTH_LONG).show());
            email_add.setError(null);
            email_add.setErrorEnabled(false);
            return true;
        } else {
            return false;
        }
    }

    private boolean validateCellularUpdate(String validate, String exist, String child) {

        if (!validate.equals(exist)) {

            for (employeesModel a : modelList) {
                if (a.getEmail().equals(validate)) {
                    cellular_add.setError(mContext.getString(R.string.existsCellular));
                    return false;
                }
            }
            mReference.child("employees").child(child).child("cellular").setValue(validate).addOnSuccessListener(aVoid -> Toast.makeText(mContext, "Celular actualizado", Toast.LENGTH_LONG).show());
            cellular_add.setError(null);
            cellular_add.setErrorEnabled(false);
            return true;
        } else {
            return false;
        }
    }

    private boolean validatePasswordUpdate(String validate, String exist, String child) {

        if (!validate.equals(exist)) {
            mReference.child("employees").child(child).child("password").setValue(validate).addOnSuccessListener(aVoid -> Toast.makeText(mContext, "Contraseña actualizada", Toast.LENGTH_LONG).show());
            password_add.setError(null);
            password_add.setErrorEnabled(false);
            return true;
        } else {
            return false;
        }
    }

    private boolean validateRol(String validate, String exist, String child) {

        if (!validate.equals(exist)) {
            mReference.child("employees").child(child).child("role").setValue(validate).addOnSuccessListener(aVoid -> Toast.makeText(mContext, "Rol actualizada", Toast.LENGTH_LONG).show());
            return true;
        } else {
            return false;
        }
    }
}
