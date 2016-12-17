package extremzhick3r.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class AltimeterService extends Service implements SensorEventListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //variables
    public static final String SERVICE = "extremzhick3r.altimeter";
    public static final String ALTIMETER = "ALTIMETER";

    private SensorManager sensorManager = null;
    private Sensor sensorAlt = null;

    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;

    public AltimeterService() {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Create sensor managers
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        sensorAlt = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if(sensorAlt != null)
            sensorManager.registerListener(this, sensorAlt, SensorManager.SENSOR_DELAY_UI);
        else {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();
        }

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        googleApiClient.disconnect();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void onSensorChanged(SensorEvent se) {
        float altitude = sensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, se.values[0]);

        publishResults(altitude);
    }


    @Override
    public void onLocationChanged(Location location) {
        publishResults((float) location.getAltitude());
    }

    private void publishResults(float altitude) {
        Intent intent = new Intent(SERVICE);
        intent.putExtra(ALTIMETER, altitude);
        sendBroadcast(intent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, locationRequest, this);
        }
        catch (SecurityException e) { e.printStackTrace(); }

    }

    @Override public void onConnectionSuspended(int i) {}
    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}
}
