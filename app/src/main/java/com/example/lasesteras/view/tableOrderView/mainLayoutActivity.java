package com.example.lasesteras.view.tableOrderView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lasesteras.R;
import com.example.lasesteras.adapter.adapterViewPager.viewPagerAdapter;
import com.example.lasesteras.adapter.viewPagerAdapterOrder;
import com.example.lasesteras.model.dishesModel;
import com.example.lasesteras.model.tableModel;
import com.example.lasesteras.view.tableOrderView.tableLayout.beverages_fragment;
import com.example.lasesteras.view.tableOrderView.tableLayout.food_fragment;
import com.example.lasesteras.view.tableOrderView.tableLayout.item_fragment;
import com.example.lasesteras.view.tableOrderView.tableLayout.order_fragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class mainLayoutActivity extends AppCompatActivity {

    private ViewPager2 vp_horizontal;
    private List<dishesModel> listOrder;
    private DatabaseReference mReference;
    private tableModel nameTable;
    private static final String SHARED_PREFERENCES = "prefLogin";
    private static final String KEY_DATE = "dateKey";
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        mReference = FirebaseDatabase.getInstance().getReference();

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String date = preferences.getString(KEY_DATE, null);
        TextView dateApp_dashboard = findViewById(R.id.dateApp_dashboard);
        dateApp_dashboard.setText(date);

        TextView fullName_table = findViewById(R.id.fullName_ord);
        String key = preferences.getString(KEY_USERNAME, null);
        fullName_table.setText(key);

        nameTable = (tableModel) getIntent().getSerializableExtra("model");
        assert nameTable != null;
        listOrder = new ArrayList<>();
        getListOrder();

        TextView name = findViewById(R.id.nameTable);
        name.setText(nameTable.getTableName());

        ViewPager2 vp_vertical = findViewById(R.id.vp_vertical);
        vp_horizontal = findViewById(R.id.vp_horizontal);

        TabLayout tabLayout = findViewById(R.id.tab_layout);


        viewPagerAdapter mPagerAdapter = new viewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPagerAdapterOrder mPagerAdapterOrder = new viewPagerAdapterOrder(getSupportFragmentManager(), getLifecycle());

        mPagerAdapterOrder.addFragment(new order_fragment(nameTable));
        vp_vertical.setAdapter(mPagerAdapterOrder);

        mPagerAdapter.addFragment(new food_fragment(listOrder));
        mPagerAdapter.addFragment(new beverages_fragment(listOrder));
        mPagerAdapter.addFragment(new item_fragment(listOrder));


        vp_horizontal.setClipToPadding(false);
        vp_horizontal.setClipChildren(false);
        vp_horizontal.setOffscreenPageLimit(3);
        vp_horizontal.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        vp_horizontal.setAdapter(mPagerAdapter);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(8));
        transformer.addTransformer((page, position) -> {
            float v = 1 - Math.abs(position);
            page.setScaleY(0.8f + v * 0.2f);
        });
        vp_horizontal.setPageTransformer(transformer);

        orderView1(tabLayout);
    }

    private void orderView1(TabLayout tabLayout) {
        new TabLayoutMediator(tabLayout, vp_horizontal, (tab, position) -> {

            switch (position) {
                case 0:
                    tab.setText("Menu");
                    break;
                case 1:
                    tab.setText("Bebidas");
                    break;
                case 2:
                    tab.setText("Extras");
                    break;
            }

        }).attach();
    }

    void getListOrder() {

        ProgressDialog mDialog = new ProgressDialog(mainLayoutActivity.this);
        mDialog.setMessage("Obteniendo lista de ordenes...");
        mDialog.setCancelable(false);
        mDialog.show();

        mReference.child("tableList").child(nameTable.getChildId()).child("order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    listOrder.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        dishesModel model = dataSnapshot.getValue(dishesModel.class);
                        assert model != null;
                        model.setNamePlate(model.getNamePlate());
                        model.setPricePlate(model.getPricePlate());
                        model.setQuantity(model.getQuantity());
                        model.setTotal(model.getTotal());
                        model.setTotalQuantity(model.getTotalQuantity());
                        model.setPlateState(model.getPlateState());
                        listOrder.add(model);
                    }
                }
                mDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mainLayoutActivity.this, "OCURRIO UN ERROR", Toast.LENGTH_LONG).show();
                mDialog.dismiss();
            }
        });

    }
}