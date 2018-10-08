package com.example.g91go.whatsfordinner;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


import static com.example.g91go.whatsfordinner.MainActivity.EXTRA_MESSAGE;

public class LocalSearchActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private final String ACCESS = "8VS7RT3JKGxL4O2Z0iUm0LUAHp1OGGu99hWVr71lSckP_hQe0N_pPCBTMc5SHHn957rFRTf-HB1UsXunBFTMuEFYDNrnwp5WuzTi2VpCJThnDySUtBX46d0Usrj3WXYx";
    private String longitude, latitude;
    private ListView lv;
    private LocationManager locationManager;
    private Location loc;
    private int sortValue;
    private String sort;


    ArrayList<HashMap<String, String>> businessList;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_search);

        businessList = new ArrayList<>();
        Intent intent = getIntent();
        lv = (ListView) findViewById(R.id.list);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        sortValue = intent.getIntExtra("SORT_SELECTION", 1);

        if(sortValue == 1){
            sort ="distance";
        }
        else if(sortValue == 2){
            sort = "rating";
        }
        else if(sortValue==3){
            sort ="review_count";
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        new YelpBusinessSearch().execute(sort,message);
    }


    public class YelpBusinessSearch extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute(){
            loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            latitude = Double.toString(loc.getLatitude());
            longitude = Double.toString(loc.getLongitude());
        }
        @Override
        protected Void doInBackground(String... arg0) {
            HTTPHandler sh = new HTTPHandler();

            String endpoint = "https://api.yelp.com/v3/businesses/search?sort_by="+arg0[0] + "&term=" + arg0[1] + "&latitude=" + latitude +"&longitude=" + longitude+"&Authorization=Bearer "+ACCESS;
           // String endpoint = "https://api.yelp.com/v3/businesses/search?term=" + arg0[0] + "&latitude=33.841584&longitude=-118.147974";

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(endpoint);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray businesses = jsonObj.getJSONArray("businesses");

                    // looping through All businesses
                    for (int i = 0; i < businesses.length(); i++) {
                        JSONObject b = businesses.getJSONObject(i);

                        String name =  b.getString("name");
                        String rating =  b.getString("rating");
                        String url =  b.getString("url");
                        String phone = b.getString("phone");
                        String distance = b.getString("distance");

                        JSONObject l= b.getJSONObject("location");
                        String address1 = l.getString("address1");
                        String zipCode = l.getString("zip_code");

                        HashMap<String, String> business = new HashMap<>();

                        business.put("name", name);
                        business.put("rating", rating);
                        business.put("phone",phone);
                        business.put("url", url);
                        business.put("distance", distance);
                        business.put("address1", address1);
                        business.put("zip_code", zipCode);

                        businessList.add(business);

                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            ListAdapter adapter = new SimpleAdapter(
                    LocalSearchActivity.this, businessList,
                    R.layout.list_item, new String[]{"name", "rating",
                    "url", "address1", "zip_code","distance"}, new int[]{R.id.name,
                    R.id.rating, R.id.url, R.id.address1, R.id.zip_code, R.id.distance});
            lv.setAdapter(adapter);
        }

    }
}


