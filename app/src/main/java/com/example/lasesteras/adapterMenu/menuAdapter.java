package com.example.lasesteras.adapterMenu;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lasesteras.R;
import com.example.lasesteras.model.dishesModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;

public class menuAdapter extends RecyclerView.Adapter<menuAdapter.viewHolder> {


    private final List<dishesModel> modelList;
    private final Context mContext;
    private Dialog dialogEdit;
    TextInputLayout name_add, price_add;
    ImageView close;
    private DatabaseReference mReference;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch available_id;
    String postType;
    String typeState = "";

    public menuAdapter(List<dishesModel> modelList, Context mContext, String postType) {
        this.modelList = modelList;
        this.mContext = mContext;
        this.postType = postType;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_list_type, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        dishesModel model = modelList.get(position);
        holder.name_type.setText(model.getNamePlate());
        setInfo(holder, model);

        if (model.getAvailable().equals("TRUE")) {
            holder.available_type.setBackgroundColor(Color.GREEN);
        } else {
            holder.available_type.setBackgroundColor(Color.RED);
        }

        holder.linearLayout_re.setOnClickListener(v -> {

            mReference = FirebaseDatabase.getInstance().getReference();

            initDialog();
            dialogEdit.setCancelable(false);
            dialogEdit.show();

            name_add = dialogEdit.findViewById(R.id.name_add);
            price_add = dialogEdit.findViewById(R.id.price_add);
            available_id = dialogEdit.findViewById(R.id.available_id);
            available_id.setVisibility(View.VISIBLE);

            Objects.requireNonNull(name_add.getEditText()).setText(model.getNamePlate());
            Objects.requireNonNull(price_add.getEditText()).setText(model.getPricePlate());
            close = dialogEdit.findViewById(R.id.dialog_close_add);

            if (model.getAvailable().equals("TRUE")) {
                available_id.setChecked(true);
            }
            if (model.getAvailable().equals("FALSE")) {
                available_id.setChecked(false);
            }
            close.setOnClickListener(v1 -> dialogEdit.dismiss());

            Button save_item = dialogEdit.findViewById(R.id.save_item);
            save_item.setBackgroundColor(Color.GREEN);

            Button delete_item = dialogEdit.findViewById(R.id.delete_item);
            delete_item.setVisibility(View.VISIBLE);
            delete_item.setBackgroundColor(Color.RED);

            save_item.setOnClickListener(v12 -> postFieldInformation(model));
            delete_item.setOnClickListener(v14 -> {
                mReference.child(postType).child(model.getChildId()).removeValue().addOnSuccessListener(aVoid -> Toast.makeText(mContext, "ELIMINADO", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(mContext, "OCURRIO UN ERROR INTENTAR DE NUEVO", Toast.LENGTH_SHORT).show());
                dialogEdit.dismiss();
            });

            available_id.setOnClickListener(v13 -> {

                if (available_id.isChecked()) {
                    typeState = "TRUE";
                } else {
                    typeState = "FALSE";
                }
            });


        });
    }

    private void postFieldInformation(dishesModel model) {

        String name = Objects.requireNonNull(name_add.getEditText()).getText().toString();
        String price = Objects.requireNonNull(price_add.getEditText()).getText().toString();

        if (!name.equals(model.getNamePlate()) && !name.isEmpty()) {
            mReference.child(postType).child(model.getChildId()).child("namePlate").setValue(name);
            Toast.makeText(mContext, "NOMBRE ACTUALIZADO", Toast.LENGTH_LONG).show();
            dialogEdit.dismiss();
        }
        if (!price.equals(model.getPricePlate()) && !name.isEmpty()) {
            mReference.child(postType).child(model.getChildId()).child("pricePlate").setValue(price);
            Toast.makeText(mContext, "PRECIO ACTUALIZADO", Toast.LENGTH_LONG).show();
            dialogEdit.dismiss();
        }

        if (!typeState.equals("") && !model.getAvailable().equals(typeState)) {
            mReference.child(postType).child(model.getChildId()).child("available").setValue(typeState);
            Toast.makeText(mContext, "ESTADO ACTUALIZADO", Toast.LENGTH_LONG).show();
            dialogEdit.dismiss();
        }
        dialogEdit.dismiss();

    }

    @SuppressLint("SetTextI18n")
    private void setInfo(@NonNull viewHolder holder, dishesModel model) {
        holder.price_type.setText("Precio: S/" + model.getPricePlate());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    static class viewHolder extends RecyclerView.ViewHolder {
        TextView name_type;
        TextView price_type;
        Button available_type;
        CardView linearLayout_re;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            name_type = itemView.findViewById(R.id.name_type);
            price_type = itemView.findViewById(R.id.price_type);
            available_type = itemView.findViewById(R.id.available_type);
            linearLayout_re = itemView.findViewById(R.id.linearLayout_re);
        }
    }

    private void initDialog() {

        dialogEdit = new Dialog(mContext);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        //noinspection deprecation
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogEdit.getWindow().getAttributes());

        int dialogWindowWidth = (int) (displayWidth * 0.9f);
        int dialogWindowHeight = (int) (displayHeight * 0.6f);

        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;
        dialogEdit.setContentView(R.layout.dialog_add_item);
        dialogEdit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEdit.getWindow().setAttributes(layoutParams);
    }
}
