package com.example.yelp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yelp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecycViewReviewAdapter  extends RecyclerView.Adapter<RecycViewReviewAdapter.ViewHolder> {
    private Context context;
    private JSONArray reviewsList;
    JSONObject current_review;

    public RecycViewReviewAdapter(Context context, JSONArray reviewsList) {
        this.context = context;
        this.reviewsList = reviewsList;
    }

    @NonNull
    @Override
    public RecycViewReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.review_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycViewReviewAdapter.ViewHolder holder, int position) {
        try {
            current_review = reviewsList.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            holder.name.setText(current_review.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            holder.rating.setText(current_review.getString("rating"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            holder.review.setText(current_review.getString("text"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            holder.date.setText(current_review.getString("time_created"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return reviewsList.length();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name;
        public TextView rating;
        public TextView review;
        public TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name = itemView.findViewById(R.id.name);
            rating = itemView.findViewById(R.id.rating);
            review = itemView.findViewById(R.id.review);
            date = itemView.findViewById(R.id.date);
        }

        @Override
        public void onClick(View view) {
        }
    }
}
