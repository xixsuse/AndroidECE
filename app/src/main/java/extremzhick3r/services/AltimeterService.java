package extremzhick3r.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.TextView;

public class AltimeterService extends Service implements SensorEventListener {

    //variables
    public static final String SERVICE = "extremzhick3r.altimeter";
    public static final String ALTIMETER = "ALTIMETER";

    private SensorManager sensorManager = null;
    private Sensor sensorAlt = null;

    float pressureNow;
    float altitude;

    //function
    public AltimeterService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //create sensor managers
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)!= null)
        {
            sensorAlt = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
            // Register sensor altitude
            sensorManager.registerListener(this, sensorAlt, SensorManager.SENSOR_DELAY_UI);
        }
        else{
            publishResults((float)0.0);
        }
        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent se) {
        pressureNow = se.values[0];
        altitude = sensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE,pressureNow);

        publishResults((float) altitude);
    }

    private void publishResults(float altitude) {
        Intent intent = new Intent(SERVICE);
        intent.putExtra(ALTIMETER, altitude);
        sendBroadcast(intent);
    }


}
