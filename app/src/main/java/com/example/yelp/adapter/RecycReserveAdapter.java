package com.example.yelp.adapter;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yelp.R;
import com.example.yelp.ReserveScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class RecycReserveAdapter  extends RecyclerView.Adapter<RecycReserveAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> storageList= new ArrayList<String>();
    String localRow;
    String[] values = new String[]{};
    View view;

    public RecycReserveAdapter(Context context, ArrayList<String> storageList) {
        this.context = context;
        this.storageList = storageList;
    }

    @NonNull
    @Override
    public RecycReserveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(this.context).inflate(R.layout.reserve_row, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull RecycReserveAdapter.ViewHolder holder, int position) {
        String localRow = storageList.get(position);
        String[] values = localRow.split("\\*");
        holder.srNo.setText(String.valueOf(position+1));
        holder.name.setText(values[1]);
        holder.dateValue.setText(values[2]);
        holder.timeValue.setText(values[3]);
        holder.email.setText(values[4]);
    }

    @Override
    public int getItemCount() {
        return storageList.size();
    }
    public void removeItem(int position) {
        SharedPreferences sharedPreferences=context.getSharedPreferences("MyPref",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        String tempRow = storageList.get(position);
        String key = tempRow.split("\\*")[0];
        editor.remove(key);
        storageList.remove(position);
        editor.apply();
//        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

//    public void restoreItem(String item, int position) {
////        data.add(position, item);
//        notifyItemInserted(position);
//    }

    public ArrayList<String> getData() {
        return storageList;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView srNo;
        public TextView name;
        public TextView dateValue;
        public TextView timeValue;
        public TextView email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            srNo = itemView.findViewById(R.id.sr_no);
            name = itemView.findViewById(R.id.b_name);
            dateValue = itemView.findViewById(R.id.date_id);
            timeValue = itemView.findViewById(R.id.time_id);
            email = itemView.findViewById(R.id.email_id);
        }

        @Override
        public void onClick(View view) {
        }
    }
}
