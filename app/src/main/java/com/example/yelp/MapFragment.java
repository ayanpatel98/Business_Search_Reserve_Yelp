package com.example.yelp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private String businessID;
    private double latitude;
    private double longitude;
//    private FragmentContainerView maps;
    private GoogleMap mMap;

    public MapFragment(String businessID) {
        this.businessID = businessID;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map,container,true);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);


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
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
//        map = googleMap;
        Log.d("map", String.valueOf(latitude) +" "+ String.valueOf(longitude));
//        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }
//
//    @Override
//    public void onResume() {
//        maps.onResume();
//        super.onResume();
//    }
//
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        maps.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        maps.onDestroy();
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        maps.onLowMemory();
//    }
}