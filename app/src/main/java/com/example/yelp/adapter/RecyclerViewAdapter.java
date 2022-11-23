package com.example.yelp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yelp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<JSONObject> businessList;

    public RecyclerViewAdapter(Context context, ArrayList<JSONObject> businessList) {
        this.context = context;
        this.businessList = businessList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        JSONObject business = businessList.get(position);
        try {
            holder.busiName.setText(business.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            holder.rating.setText(business.getString("rating"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            holder.distanceTo.setText(String.valueOf((business.getString("distance"))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView busiImage;
        public TextView busiName;
        public TextView rating;
        public TextView distanceTo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            busiName = itemView.findViewById(R.id.busiName);
            rating = itemView.findViewById(R.id.rating);
            distanceTo = itemView.findViewById(R.id.distanceTo);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context, "Row Clicked", Toast.LENGTH_SHORT).show();
        }
    }
}
