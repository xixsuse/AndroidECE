package com.example.cleme.test_boussole;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import extremzhick3r.R;

public class MainActivity extends AppCompatActivity implements LocationListener {

    Button buttonGauche;
    Button buttonDroite;
    ImageView boussole;
    TextView latitude;
    TextView longitude;
    LocationManager locationManager;
    Location locationPos;
    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boussole = (ImageView) findViewById(R.id.boussoleImage);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        buttonGauche.setOnClickListener(buttonListenerGauche);
        buttonDroite.setOnClickListener(buttonListenerDroite);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("Position","Sortie ");
            return;
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0 );
        }

        locationPos = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(locationPos == null){
            locationPos= locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if(locationPos != null){
            longitude.setText("longitude :"+ Double.toString(locationPos.getLongitude() ) );
            latitude.setText("latitude :"+ Double.toString(locationPos.getLatitude() ) );
        }
        else {
            longitude.setText("longitude : 0.00");
            latitude.setText("latitude : 0.00");
        }
    }

    public View.OnClickListener buttonListenerGauche = new View.OnClickListener() {
        public void onClick(View v) {
            x-=30;
            boussole.setRotation(x);
        }
    };

    public View.OnClickListener buttonListenerDroite = new View.OnClickListener() {
        public void onClick(View v) {
            x+=30;
            boussole.setRotation(x);
        }
    };


    @Override
    public void onLocationChanged(Location location) {
        longitude.setText("longitude :"+ Double.toString(location.getLongitude() ) );
        latitude.setText("latitude :"+ Double.toString(location.getLatitude() ) );
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
