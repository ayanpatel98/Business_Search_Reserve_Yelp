package com.example.yelp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.yelp.adapter.RecycViewReviewAdapter;
import com.example.yelp.adapter.RecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReviewsFragment extends Fragment {

    private String businessID;
    public ReviewsFragment(String businessID) {
        this.businessID = businessID;
    }

    private RecyclerView recyclerView;
    private RecycViewReviewAdapter recycViewReviewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reviews,container,true);
        recyclerView = view.findViewById(R.id.recycViewReview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RequestQueue requestQueue;
        StringRequest stringRequest;
        String url = "https://api-dot-business-search-reserve-081998.uw.r.appspot.com/businesses/reviews?b_id="+this.businessID;
        // RequestQueue initialized
        requestQueue = Volley.newRequestQueue(requireContext());

        // String Request initialized
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject business_reviews_json = new JSONObject(response);
                    JSONArray reviews_only = business_reviews_json.getJSONArray("response");

                    recycViewReviewAdapter = new RecycViewReviewAdapter(getContext(), reviews_only);
                    recyclerView.setAdapter(recycViewReviewAdapter);
                    recyclerView.setNestedScrollingEnabled(false);

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
        return inflater.inflate(R.layout.fragment_reviews, container, false);
    }
}