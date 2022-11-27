package com.example.yelp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.Objects;


public class Details extends Fragment {

    private String businessID;
    public Details(String businessID) {
        this.businessID = businessID;
    }
    private String address;
    private String priceRange;
    private String phNo;
    private String status;
    private String category;
    private String disp_url;
    private JSONArray photos_urls;

    private TextView addressValue;
    private TextView categoryValue;
    private TextView priceValue;
    private TextView phNoValue;
    private TextView statusValue;
    private TextView visitYelpValue;
    private LinearLayout photoContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RequestQueue requestQueue;
        StringRequest stringRequest;
        String url = "https://api-dot-business-search-reserve-081998.uw.r.appspot.com/businesses?b_id="+this.businessID;
        View view = inflater.inflate(R.layout.fragment_details,container,true);
        addressValue = view.findViewById(R.id.addressValue);
        categoryValue = view.findViewById(R.id.categoryValue);
        priceValue = view.findViewById(R.id.priceValue);
        phNoValue = view.findViewById(R.id.phNoValue);
        statusValue = view.findViewById(R.id.statusValue);
        visitYelpValue = view.findViewById(R.id.visitYelpMore);
        photoContainer = view.findViewById(R.id.photoContainer);

        // RequestQueue initialized
        requestQueue = Volley.newRequestQueue(requireContext());
        // String Request initialized
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject business_details_json = new JSONObject(response);
                    JSONObject details_only = business_details_json.getJSONArray("response").getJSONObject(0);
                    address = getAddress(details_only.getJSONArray("display_address"));
                    priceRange = details_only.getString("price");
                    phNo = details_only.getString("display_phone");
                    status = details_only.getString("is_open_now");
                    category = getCategories(details_only.getJSONArray("categories"));
                    disp_url = details_only.getString("more_info");
                    photos_urls = details_only.getJSONArray("photos");

                    if (address.equals("")) {
                        addressValue.setText("N/A");
                    } else {
                        addressValue.setText(address);
                    }

                    if (priceRange.equals("")) {
                        priceValue.setText("N/A");
                    } else {
                        priceValue.setText(priceRange);
                    }

                    if (phNo.equals("")) {
                        phNoValue.setText("N/A");
                    } else {
                        phNoValue.setText(phNo);
                    }

                    if (status.equals("noStatus")) {
                        statusValue.setText("N/A");
                    } else if (status=="true") {
                        statusValue.setText("Open");
                        statusValue.setTextColor(Color.GREEN);
                    } else if (status=="false") {
                        statusValue.setText("Closed");
                        statusValue.setTextColor(Color.RED);
                    }

                    if (category.equals("")) {
                        categoryValue.setText("N/A");
                    } else {
                        categoryValue.setText(category);
                    }
                    // method to redirect to provided link
                    visitYelpValue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(disp_url));
                            startActivity(browserIntent);
                        }
                    });

                    for(int i=0; i<photos_urls.length();i++){
                        ImageView iv = new ImageView(getContext());
                        Picasso.get().load(photos_urls.getString(i)).into(iv);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(800, 1000);
                        lp.setMargins(100, 0, 100, 0);
                        lp.gravity = Gravity.CENTER;
                        iv.setLayoutParams(lp);
                        photoContainer.addView(iv);
                    }


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


    //    Get Address in the form of string
    protected String getAddress(JSONArray address) throws JSONException {
        String new_address = "";
        for (int i =0; i<address.length();i++){
            new_address += address.getString(i)+" ";
        }
        return new_address.trim();
    }

    //    Get Categories in the form of string
    protected String getCategories(JSONArray category) throws JSONException {
        String new_category = "";
        for (int i =0; i<category.length();i++){
            if(i==category.length()-1){
                new_category += category.getJSONObject(i).getString("title");
            }
            else{
                new_category += category.getJSONObject(i).getString("title")+" | ";
            }
        }
        return new_category.trim();
    }
}