package com.example.lasesteras.adapter.adapterViewPager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class viewPagerAdapter extends FragmentStateAdapter {


    ArrayList<Fragment> arrayList = new ArrayList<>();

    public viewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        return arrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addFragment(Fragment fragment) {
        arrayList.add(fragment);
    }
}
