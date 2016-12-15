package extremzhick3r.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

public class CompassService extends Service  implements SensorEventListener {
    public static final String SERVICE = "extremzhick3r.compass";
    public static final String AZIMUTH = "AZIMUTH";

    private SensorManager sensorManager = null;

    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];
    private final float[] mOrientationAngles = new float[3];
    private final float[] mRotationMatrix = new float[9];

    private Sensor sensorAcc = null;
    private Sensor sensorMagneto = null;

    public CompassService() {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagneto = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Register accelerometer
        sensorManager.registerListener(this, sensorAcc,
                SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        // Register magnetometer
        sensorManager.registerListener(this, sensorMagneto,
                SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Ignoring this
    }

    @Override
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
            publishResults((float) Math.toDegrees(-mOrientationAngles[0]));
        }
    }

    private void publishResults(float azimuth) {
        Intent intent = new Intent(SERVICE);
        intent.putExtra(AZIMUTH, azimuth);
        sendBroadcast(intent);
    }
}
