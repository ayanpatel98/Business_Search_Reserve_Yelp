package com.example.yelp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yelp.R;
import com.example.yelp.adapter.VPAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Business_Details extends AppCompatActivity {
    private TabLayout busiTabLayout;
    public ViewPager2 viewpager;
    Intent businessIntent;
    String businessID;
    String lati;
    String longi;
    ArrayList<JSONObject> details_map_reviews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String[] arr = new String[]{"BUSINESS DETAILS", "MAP LOCATION", "REVIEWS"};
        details_map_reviews = new ArrayList<JSONObject>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);

//        Get the ID of the corresponding business
        businessIntent = getIntent();
        businessID = businessIntent.getStringExtra("businessID");
        lati = businessIntent.getStringExtra("latitude");
        longi = businessIntent.getStringExtra("longitude");

        busiTabLayout = findViewById(R.id.busiTabLayout);
        viewpager = findViewById(R.id.busiViewpager);

//        call all the data and sent prepare the data to send to all 3 fragments from business_details actvity
        FragmentStateAdapter vpAdapter = new VPAdapter(this, businessID, lati, longi);
        viewpager.setAdapter(vpAdapter);
        new TabLayoutMediator(busiTabLayout, viewpager, ((tab, position)-> tab.setText(arr[position]))).attach();

    }
}