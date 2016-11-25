package extremzhick3r.activities;

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
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import extremzhick3r.R;

public class CompassActivity  extends Activity  implements SensorEventListener {
    private SensorManager sensorManager = null;
    private ImageView compass;
    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];
    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];

    private Sensor sensorAcc = null;
    private Sensor sensorMagneto = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.compass = (ImageView) findViewById(R.id.boussoleImage);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagneto = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    protected void onPause() {
        // Unregister the listener
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register accelerometer
        sensorManager.registerListener(this, sensorAcc,
                SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        // Register magnetometer
        sensorManager.registerListener(this, sensorMagneto,
                SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Ignoring this for now

    }

    public void onSensorChanged(SensorEvent se) {
        if (se.sensor == sensorAcc)
            System.arraycopy(se.values, 0, mAccelerometerReading, 0, mAccelerometerReading.length);
        else if (se.sensor == sensorMagneto)
            System.arraycopy(se.values, 0, mMagnetometerReading, 0, mMagnetometerReading.length);

        if(mAccelerometerReading != null && mMagnetometerReading != null) {
            // Update orientation
            sensorManager.getRotationMatrix(mRotationMatrix, null, mAccelerometerReading, mMagnetometerReading);
            sensorManager.getOrientation(mRotationMatrix, mOrientationAngles);

            // Rotate compass
            compass.setRotation((float) Math.toDegrees(-mOrientationAngles[0]));
        }
    }
}
