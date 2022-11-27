package com.example.yelp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yelp.R;
import com.example.yelp.Business_Details;
import com.squareup.picasso.Picasso;

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
            itemView.setOnClickListener(this);
            srNo = itemView.findViewById(R.id.name);
            busiName = itemView.findViewById(R.id.rating);
            rating = itemView.findViewById(R.id.review);
            distanceTo = itemView.findViewById(R.id.date);
            busiImage = itemView.findViewById(R.id.busiImage);
        }

        @Override
        public void onClick(View view) {
//            To Extract the id of the business
//            Get the position of the view first and then find the business in the business array
            int position_business = getBindingAdapterPosition();
            JSONObject business = businessList.get(position_business);
            try {
                String businessID = business.getString("id");
//                Toast.makeText(context, businessID, Toast.LENGTH_SHORT).show();
                Intent businessIntent = new Intent(context, Business_Details.class);
                businessIntent.putExtra("businessID", businessID);
                context.startActivity(businessIntent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Log.d("click", String.valueOf(business));
        }
    }
}
