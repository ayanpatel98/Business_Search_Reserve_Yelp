package com.example.yelp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MapFragment extends Fragment {
    private String businessID;
    private double latitude;
    private double longitude;

    public MapFragment(String businessID) {
        this.businessID = businessID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RequestQueue requestQueue;
        StringRequest stringRequest;
        String url = "https://api-dot-business-search-reserve-081998.uw.r.appspot.com/businesses?b_id="+this.businessID;
        // RequestQueue initialized
        requestQueue = Volley.newRequestQueue(requireContext());

        // String Request initialized
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject business_details_json = new JSONObject(response);
                    JSONObject map_only =  business_details_json.getJSONArray("response").getJSONObject(0).getJSONObject("coordinates");
                    latitude = map_only.getDouble("latitude");
                    longitude = map_only.getDouble("longitude");
                    Log.d("map", String.valueOf(latitude) +" "+ String.valueOf(longitude));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        requestQueue.add(stringRequest);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }
}