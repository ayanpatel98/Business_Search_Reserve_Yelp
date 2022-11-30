package com.example.yelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    String busiName;
    String more_info;
    ArrayList<JSONObject> details_map_reviews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String[] arr = new String[]{"BUSINESS DETAILS", "MAP LOCATION", "REVIEWS"};
        details_map_reviews = new ArrayList<JSONObject>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

//        Get the ID of the corresponding business
        businessIntent = getIntent();
        businessID = businessIntent.getStringExtra("businessID");
        lati = businessIntent.getStringExtra("latitude");
        longi = businessIntent.getStringExtra("longitude");
        busiName = businessIntent.getStringExtra("name");
        more_info = businessIntent.getStringExtra("more_info");

        busiTabLayout = findViewById(R.id.busiTabLayout);
        viewpager = findViewById(R.id.busiViewpager);

//        Title of the business activity
        setTitle(busiName);

//        call all the data and sent prepare the data to send to all 3 fragments from business_details actvity
        FragmentStateAdapter vpAdapter = new VPAdapter(this, businessID, lati, longi);
        viewpager.setAdapter(vpAdapter);
        new TabLayoutMediator(busiTabLayout, viewpager, ((tab, position)-> tab.setText(arr[position]))).attach();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.business_details_bar, menu);
        // return true so that the menu pop up is opened
        return true;
    }

    // this event will enable the back
    // function to the button on press
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.facebook:
                // User chose the "Facebook" item, show the app settings UI...
                // method to redirect to provided link
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com/sharer/sharer.php?u="+more_info));
                startActivity(browserIntent);
                return true;

            case R.id.twitter:
                // User chose the "Twitter" action, mark the current item
                Intent browserIntentTwitter = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet/?text=Check "+busiName+" on Yelp.%0A"+more_info));
                startActivity(browserIntentTwitter);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}