package com.example.yelp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RequestQueue requestQueue;
        StringRequest stringRequest;
        String url = "https://api-dot-business-search-reserve-081998.uw.r.appspot.com/businesses?b_id="+this.businessID;
        View v = inflater.inflate(R.layout.fragment_details,container,true);
        addressValue = v.findViewById(R.id.addressValue);
        categoryValue = v.findViewById(R.id.categoryValue);
        priceValue = v.findViewById(R.id.priceValue);
        phNoValue = v.findViewById(R.id.phNoValue);
        statusValue = v.findViewById(R.id.statusValue);
        visitYelpValue = v.findViewById(R.id.visitYelpMore);


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
                    Log.d("all_DAta", address+priceRange+phNo+status+category+disp_url+photos_urls.toString());

                    addressValue.setText(address);
                    priceValue.setText(priceRange);
                    phNoValue.setText(phNo);
                    statusValue.setText(status);
                    categoryValue.setText(category);
                    visitYelpValue.setText("Business Link");
                    Log.d("all_DAta", addressValue.getText().toString());


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
        return inflater.inflate(R.layout.fragment_details, container, false);
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