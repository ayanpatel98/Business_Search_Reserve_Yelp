package com.example.yelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yelp.adapter.RecycReserveAdapter;
import com.example.yelp.adapter.RecycViewReviewAdapter;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class ReserveScreen extends AppCompatActivity {

    private String businessID;
    private RecyclerView recyclerView;
    private TextView noBooking;
    private RecycReserveAdapter recycReserveAdapter;
    private Context context;
    private ArrayList<String> storageList= new ArrayList<String>();
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservation_screen);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        noBooking = findViewById(R.id.noBooking);
        noBooking.setVisibility(View.GONE);
//        noBooking.setVisibility(View.VISIBLE);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycViewReservation);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        SharedPreferences sharedPreferences=getSharedPreferences("MyPref",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String temp = entry.getKey()+"*"+entry.getValue().toString();
            storageList.add(temp);
        }

        if(sharedPreferences.getAll().isEmpty()){
            noBooking.setVisibility(View.VISIBLE);
        }

        recycReserveAdapter = new RecycReserveAdapter(this, storageList);
        recyclerView.setAdapter(recycReserveAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        enableSwipeToDeleteAndUndo();
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final String item = recycReserveAdapter.getData().get(position);

                recycReserveAdapter.removeItem(position);

                if (recycReserveAdapter.getItemCount()==0){
                    noBooking.setVisibility(View.VISIBLE);
                }


                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Removing Existing Reservation.", Snackbar.LENGTH_LONG);
//                snackbar.setAction("UNDO", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        recycReserveAdapter.restoreItem(item, position);
//                        recyclerView.scrollToPosition(position);
//                    }
//                });

//                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }


    // this event will enable the back
    // function to the button on press
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}