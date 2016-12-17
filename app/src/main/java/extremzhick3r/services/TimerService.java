package extremzhick3r.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import extremzhick3r.view.TimerView;

public class TimerService extends Service{
    public static final String SERVICE = "extremzhick3r.timer";
    public static final String TIMER = "TIMER";

    private static Timer timer;
    private long startTime;

    public TimerService() {}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTime = System.currentTimeMillis();
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                publishResults(System.currentTimeMillis() - startTime);
            }
        }, 0, 100);

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        timer.cancel();
    }

    private void publishResults(long time) {
        Intent intent = new Intent(SERVICE);
        intent.putExtra(TIMER, time);
        sendBroadcast(intent);
    }
}
