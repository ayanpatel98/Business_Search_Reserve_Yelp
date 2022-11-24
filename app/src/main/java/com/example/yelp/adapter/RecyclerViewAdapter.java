package com.example.yelp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yelp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
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
        holder.srNo.setText(String.valueOf(position+1));
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
            holder.distanceTo.setText(String.valueOf((Math.round(Float.parseFloat(business.getString("distance").trim()))/1609)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Image Load ONE WAY
        try {
            Picasso.get().load(business.getString("image_url")).into(holder.busiImage); // Add https otherwise t won't work
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        IMAGE LOAD, OTHER WAY, NOT WORKING
//        Bitmap myBitmap = BitmapFactory.decodeFile("https://i.imgur.com/DvpvklR.png");
//        holder.busiImage.setImageBitmap(myBitmap);
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView srNo;
        public ImageView busiImage;
        public TextView busiName;
        public TextView rating;
        public TextView distanceTo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            srNo = itemView.findViewById(R.id.srNo);
            busiName = itemView.findViewById(R.id.busiName);
            rating = itemView.findViewById(R.id.rating);
            distanceTo = itemView.findViewById(R.id.distanceTo);
            busiImage = itemView.findViewById(R.id.busiImage);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context, "Row Clicked", Toast.LENGTH_SHORT).show();
        }
    }
}
