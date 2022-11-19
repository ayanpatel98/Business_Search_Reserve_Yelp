package com.example.yelp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    public String latitude = "";
    public String longitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button submitButton = findViewById(R.id.submitButton);
        Button clearButton = findViewById(R.id.clearButton);
        EditText keyword = findViewById(R.id.keyWord);
        EditText distance = findViewById(R.id.distance);
        EditText location = findViewById(R.id.location);
        CheckBox detectLocation = findViewById(R.id.detectLocation);

        Spinner categories =  findViewById(R.id.categoryDropdown);
        String[] items = new String[]{"Default", "Arts and Entertainment", "Health and Medical", "Hotels and Travel", "Food", "Professional Services"};
//        Categories Dropdown Values
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        categories.setAdapter(adapter);

//        to know whether the autodetect is checked or not
        detectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!detectLocation.isChecked()){
                    latitude = "";
                    longitude = "";
                }
                else{
                    getFromIpInfo();
                }
//                Log.d("check", String.valueOf(detectLocation.isChecked()));
            }
        });

//        Submit Click Listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( TextUtils.isEmpty(keyword.getText())){
                    keyword.setError( "Keyword is required!" );
                }
                else if(TextUtils.isEmpty(distance.getText())){
                    distance.setError( "Distance is required!" );
                }
                else if(TextUtils.isEmpty(location.getText())){
                    location.setError( "Location is required!" );
                }
                else {
                    String keywordValid = keyword.getText().toString();
                    String distanceValid = distance.getText().toString();
                    String locationValid = location.getText().toString();
                    getTable(keywordValid, distanceValid, locationValid);
                }
            }
        });
    }

    protected void getTable(String keywordValid, String distanceValid, String locationValid){
        RequestQueue requestQueue;
        StringRequest stringRequest;
//        String url = "https://api-dot-business-search-reserve-081998.uw.r.appspot.com/";
        String url = "https://api-dot-business-search-reserve-081998.uw.r.appspot.com/autocomplete?text=pizza";
        // RequestQueue initializedr
        requestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d( "Response :" , response.toString());//display the response on screen
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        requestQueue.add(stringRequest);
    }

    protected void getFromIpInfo(){
        RequestQueue requestQueue;
        StringRequest stringRequest;
        String url = "https://ipinfo.io?token=196ec65b0b0406";
        // RequestQueue initializedr
        requestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d( "Long lat Response :" , response.toString());
                try {
                    JSONObject jsonIpinfo = new JSONObject(response);
                    String latLong = jsonIpinfo.getString("loc");
                    Log.d("jsonresp", latLong);
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

    }
}