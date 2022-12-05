package com.example.yelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.example.yelp.adapter.RecyclerViewAdapter;

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
    public TextView noResultTable ;
//    public Object[] categoryObject;
    public TextView catTitle;
    public List<String> keywordDropdown = new ArrayList<String>();
    public ArrayAdapter<String> autocompleteAdapter;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onResume() {
        super.onResume();

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Reset the theme to tht original
        // because in the AndroidManifest.xml we had set the theme to custom theme "SplashCustom", ans we created the theme in values->themes->themes.xml
        setTheme(R.style.Theme_Yelp);
        setContentView(R.layout.activity_main);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                findViewById(R.id.splashLayout).setVisibility(View.GONE);
            }
        }, 2300);

        submitButton = findViewById(R.id.submitButton);
        clearButton = findViewById(R.id.clearButton);
        keyword = findViewById(R.id.keyWord);
        distance = findViewById(R.id.distance);
        location = findViewById(R.id.location);
        detectLocation = findViewById(R.id.detectLocation);
        categorySelected =  findViewById(R.id.categoryDropdown);
        catTitle = findViewById(R.id.catTitle);
        noResultTable = findViewById(R.id.noResultTable);
        noResultTable.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                retrieveData(s.toString());
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

        if (detectLocation.isChecked()){
//                Hide the Location Field
            location.getText().clear();
            location.setVisibility(View.INVISIBLE);
        }
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
                else if(TextUtils.isEmpty(distance.getText())){
                    distance.setError( "This field is required!" );
                }
                else if(TextUtils.isEmpty(location.getText()) && location.isShown()){
                    location.setError( "This field is required!" );
                }
                else {
                    getLocationFromGoogle();
                }
            }
        });

//        Clear Click Listener
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectLocation.setChecked(false);
                latitude = "";
                longitude = "";
//                    Make location when auto detect is unchecked
                location.setVisibility(View.VISIBLE);
                noResultTable.setVisibility(View.GONE);
                location.setText("");
                distance.setText("");
                keyword.setText("");
                categorySelected.setAdapter(adapter);

                recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this,  new ArrayList<JSONObject>());
                recyclerView.setAdapter(recyclerViewAdapter);

            }
        });

//        Required Field Symbol
        showEditTextsAsMandatory ( keyword, location);
        showTextViewsAsMandatory ( catTitle);
    }


    protected void retrieveData(String s){
        RequestQueue requestQueue;
        StringRequest stringRequest;
        // RequestQueue initialized
        requestQueue = Volley.newRequestQueue(this);
        List<String> items = new ArrayList<String>();
        String text = s.toString();
        String url = "https://api-dot-business-search-reserve-081998.uw.r.appspot.com/autocomplete?text="+text;

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

                    if (results.length()==0){
                        items.add(" ");
                    }

                    autocompleteAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_item, items);
                    keywordDropdown = items;
                    Log.d("auto", keywordDropdown.toString());
//                    autocompleteAdapter.notifyDataSetChanged();
//                    keyword.setAdapter(autocompleteAdapter);
                    keyword.setThreshold(1);

//                    final Handler handler = new Handler(Looper.getMainLooper());

                    keyword.setAdapter(autocompleteAdapter);
                    autocompleteAdapter.notifyDataSetChanged();
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
        String radiusPayload;
        ArrayList<JSONObject> businesses = new ArrayList<JSONObject>();
        noResultTable.setVisibility(View.GONE);

        if (!distance.getText().toString().trim().isEmpty()){
            radiusPayload = String.valueOf(Integer.parseInt(distance.getText().toString().trim())*1609);
        }
        else{
            radiusPayload = "";
        }
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

        String url = "https://api-dot-business-search-reserve-081998.uw.r.appspot.com/businesses/search?term="+keyword.getText().toString()+"&latitude="+latitude+"&longitude="+longitude+"&categories="+catPayload+"&radius="+radiusPayload;

        // RequestQueue initialized
        requestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject tableData = new JSONObject(response);
                    JSONArray responseData = tableData.getJSONArray("response");
                    if (responseData.length()>0){
                        for (int i=0; i<responseData.length();i++){
                            businesses.add(responseData.getJSONObject(i));
                        }
                        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, businesses);
                        recyclerView.setAdapter(recyclerViewAdapter);
                        recyclerView.setNestedScrollingEnabled(false);
                    }
                    else {
                        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, new ArrayList<>());
                        recyclerView.setAdapter(recyclerViewAdapter);
                        recyclerView.setNestedScrollingEnabled(false);
                        noResultTable.setVisibility(View.VISIBLE);
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
                            getTable(keywordValid, distanceValid, locationValid);
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


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                inflater.inflate(R.menu.main_menu, menu);
            }
        }, 1400);
        // return true so that the menu pop up is opened
        return true;
    }

    // this event will enable the back
    // function to the button on press
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.reservation:
                Intent reserveIntent = new Intent(this, ReserveScreen.class);
                startActivity(reserveIntent);
                // User chose the "Facebook" item, show the app settings UI...
                // method to redirect to provided link
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com/sharer/sharer.php?u="+more_info));
//                startActivity(browserIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}