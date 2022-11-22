package com.example.yelp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public String latitude = "";
    public String longitude = "";
    public Button submitButton;
    public Button clearButton;
    public AutoCompleteTextView keyword;
    public EditText distance;
    public EditText location;
    public CheckBox detectLocation;
    public Spinner categorySelected ;
//    public Object[] categoryObject;
    public TextView catTitle;
    public List<String> keywordDropdown = new ArrayList<String>();
    public ArrayAdapter<String> autocompleteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        submitButton = findViewById(R.id.submitButton);
        clearButton = findViewById(R.id.clearButton);
        keyword = findViewById(R.id.keyWord);
        distance = findViewById(R.id.distance);
        location = findViewById(R.id.location);
        detectLocation = findViewById(R.id.detectLocation);
        categorySelected =  findViewById(R.id.categoryDropdown);
        catTitle = findViewById(R.id.catTitle);

        String[] fruits = {"Apple", "Banana", "Cherry", "Date", "Grape", "Kiwi", "Mango", "Pear"};//Creating the instance of ArrayAdapter containing list of fruit names
        autocompleteAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, keywordDropdown);
//        Instance of Autocomplete
//        keyword.setThreshold(1);//will start working from first character
        keyword.setAdapter(autocompleteAdapter);//setting the adapter data into the AutoCompleteTextView

        keyword.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //retrieve data s
            }

            public void afterTextChanged(Editable s) {
                retrieveData(s);
            }
        });

        String[] items = new String[]
            {
                    "Default",
                    "Arts and Entertainment",
                    "Health and Medical",
                    "Hotels and Travel",
                    "Food",
                    "Professional Services"
            };
//        Categories Dropdown Values
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        categorySelected.setAdapter(adapter);


//        to know whether the autodetect is checked or not
        detectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!detectLocation.isChecked()){
                    latitude = "";
                    longitude = "";
//                    Make location when auto detect is unchecked
                    location.setVisibility(View.VISIBLE);
                }
                else{
                    setFromIpInfo();
                }
            }
        });

//        Submit Click Listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( TextUtils.isEmpty(keyword.getText())){
                    keyword.setError( "This field is required!" );
                }
                else if(TextUtils.isEmpty(location.getText()) && location.isShown()){
                    location.setError( "This field is required!" );
                }
                else {
                    getLocationFromGoogle();
                }
            }
        });

//        Required Field Symbol
        showEditTextsAsMandatory ( keyword, location);
        showTextViewsAsMandatory ( catTitle);
    }

    protected void retrieveData(Editable s){
        RequestQueue requestQueue;
        StringRequest stringRequest;
        // RequestQueue initialized
        requestQueue = Volley.newRequestQueue(this);
        List<String> items = new ArrayList<String>();
        String text = s.toString();
        String url = "https://api-dot-business-search-reserve-081998.uw.r.appspot.com/autocomplete?text="+text;

        Log.d("auto", url);
        // String Request initialized
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject autocompleteResponse = new JSONObject(response);
                    JSONArray results = autocompleteResponse.getJSONArray("response");
                    for (int i = 0; i<results.length(); i++){
                        items.add(results.getJSONObject(i).getString("text"));
                    }

                    autocompleteAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_item, items);
                    keywordDropdown = items;
                    Log.d("auto", keywordDropdown.toString());
//                    autocompleteAdapter.notifyDataSetChanged();
                    keyword.setAdapter(autocompleteAdapter);
//                    keyword.setThreshold(1);
                    keyword.setAdapter(autocompleteAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "No results found!", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    protected void getTable(String keywordValid, String distanceValid, String locationValid){
        RequestQueue requestQueue;
        StringRequest stringRequest;
        String catPayload = "";
        String currentCategory = categorySelected.getSelectedItem().toString();
        int radiusPayload = Integer.parseInt(distance.getText().toString())*1609;
        switch (currentCategory) {
            case "Default":
                catPayload = "All";
                break;
            case "Arts and Entertainment":
                catPayload = "arts";
                break;
            case "Health and Medical":
                catPayload = "health";
                break;
            case "Hotels and Travel":
                catPayload = "hotelstravel";
                break;
            case "Food":
                catPayload = "food";
                break;
            case "Professional Services":
                catPayload = "professional";
                break;
        }
        Log.d("current", keyword.getText().toString()+radiusPayload+longitude+latitude+catPayload);

        String url = "https://api-dot-business-search-reserve-081998.uw.r.appspot.com/businesses/search?term="+keyword.getText().toString()+"&latitude="+latitude+"&longitude="+longitude+"&categories="+catPayload+"&radius="+radiusPayload;

        // RequestQueue initialized
        requestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject tableData = new JSONObject(response);
                    Log.d( "current :" , tableData.getString("response") + url);
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

    protected void getLocationFromGoogle(){
        String keywordValid = keyword.getText().toString();
        String distanceValid = distance.getText().toString();
        String locationValid = location.getText().toString();
        if (!detectLocation.isChecked()){

            RequestQueue requestQueue;
            StringRequest stringRequest;
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+locationValid+"&key=AIzaSyCxyrgZ_Jw6ZFo4vG3AAPnsAk6LHfimJS8";

            requestQueue = Volley.newRequestQueue(this);
            // String Request initialized
            stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject geoLocation = new JSONObject(response);
//                    the param of String.split accept a regular expression.
                        if (!geoLocation.getString("status").equals("OK")){
                            longitude = "";
                            latitude = "";
                            Toast.makeText(MainActivity.this, "Please Enter Correct Location!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            JSONArray jsnarr = geoLocation.getJSONArray("results");
                            JSONObject geometry = jsnarr.getJSONObject(0);
                            JSONObject geoLatlong = geometry.getJSONObject("geometry").getJSONObject("location");
                            latitude = geoLatlong.getString("lat");
                            longitude = geoLatlong.getString("lng");
                            Toast.makeText(MainActivity.this, "Correct Location!", Toast.LENGTH_SHORT).show();
                            getTable(keywordValid, distanceValid, locationValid);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("error", String.valueOf(error));
                }
            });

            requestQueue.add(stringRequest);
        }
        else {
            getTable(keywordValid, distanceValid, locationValid);
        }
    }

    protected void setFromIpInfo(){
        RequestQueue requestQueue;
        StringRequest stringRequest;
        String url = "https://ipinfo.io?token=196ec65b0b0406";
        // RequestQueue initialized
        requestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonIpinfo = new JSONObject(response);
//                    the param of String.split accept a regular expression.
                    String[] latLong = jsonIpinfo.getString("loc").split("\\,");
                    latitude = latLong[0];
                    longitude = latLong[1];
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                
//                Hide the Location Field
                location.getText().clear();
                location.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                latitude = "";
                longitude = "";
                Toast.makeText(MainActivity.this, "Location Not Detected!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);

    }

//    Methods to insert mandatory symbol
//        *
    protected void showEditTextsAsMandatory(EditText... ets){
        for (EditText et: ets){
            String hint = et.getHint ().toString ();

            et.setHint ( Html.fromHtml ( hint + " <font color=\"#ff0000\">" + "* " + "</font>"  ) );
        }
    }

    protected void showTextViewsAsMandatory(TextView... tvs){
        for ( TextView tv : tvs ){
            String text = tv.getText ().toString ();
            tv.setText ( Html.fromHtml (  text + " <font color=\"#ff0000\">" + "* " + "</font>" ) );
        }
    }
}