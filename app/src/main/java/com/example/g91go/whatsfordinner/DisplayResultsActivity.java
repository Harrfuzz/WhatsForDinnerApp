package com.example.g91go.whatsfordinner;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static com.example.g91go.whatsfordinner.MainActivity.EXTRA_MESSAGE;


public class DisplayResultsActivity extends AppCompatActivity {
    private String message;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location loc;
    private int temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        message = intent.getStringExtra(EXTRA_MESSAGE);
        temp = intent.getIntExtra("SORT_SELECTION",1);

        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.textView_ResultFilter);
        textView.setText(message);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                String latitude = Double.toString(location.getLatitude());
                String longitude = Double.toString(location.getLongitude());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                }, 10);
                return;
            }
        }
        else{
            locationManager.requestLocationUpdates("gps", 1, 0, locationListener);
        }

    }


    public void onRequestPermissionResult(int requestCode, String[] permission, int[] grantResults){
        switch(requestCode) {
            case 10:
                if(grantResults.length>0&& grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    locationManager.requestLocationUpdates("gps", 1, 0, locationListener);
                return;
        }
    }



    public void searchActivity(View view){
       Intent searchParam = new Intent(this, LocalSearchActivity.class);
        searchParam.putExtra(EXTRA_MESSAGE, message);
        searchParam.putExtra("SORT_SELECTION", temp);
        startActivity(searchParam);
    }
}
