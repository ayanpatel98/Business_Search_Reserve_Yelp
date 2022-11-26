package com.example.yelp.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.yelp.Details;
import com.example.yelp.MapFragment;
import com.example.yelp.ReviewsFragment;

public class VPAdapter extends FragmentStateAdapter {
    String businessID;

    public VPAdapter(@NonNull FragmentActivity fragmentActivity, String businessID) {
        super(fragmentActivity);
        this.businessID = businessID;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
//                Log.d("vp", "0");
                return new Details(this.businessID);
            case 1:
//                Log.d("vp", "1");
                return new MapFragment(this.businessID);
            case 2:
//                Log.d("vp", "2");
                return new ReviewsFragment(this.businessID);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
