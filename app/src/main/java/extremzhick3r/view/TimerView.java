package extremzhick3r.view;

import android.content.Context;
import android.graphics.Canvas;
import android.icu.util.TimeUnit;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

public class TimerView extends TextView {

    private Runnable timer;
    private Handler handle;
    private long startTime;

    public TimerView(Context context, AttributeSet attrs, int defStyle) { super(context, attrs, defStyle); initialize(); }
    public TimerView(Context context, AttributeSet attrs) { super(context, attrs); initialize(); }
    public TimerView(Context context) { super(context); initialize(); }

    private void initialize() {
        handle = new Handler();
        timer = new Runnable() {
            @Override
            public void run() {
                TimerView.this.setTime(System.currentTimeMillis() - startTime);
                handle.postDelayed(this, 1000);
            }
        };

        this.setTime(0);
    }

    public void start() {
        startTime = System.currentTimeMillis();
        handle.postDelayed(timer, 1000);
    }
    public void stop() {
        handle.removeCallbacks(timer);
        this.setTime(0);
    }

    public void setTime(long time) {
        this.setText(milliToTimer(time));
    }

    public static String milliToTimer(long time) {
        int seconds = (int) (time / 1000) % 60 ;
        int minutes = (int) ((time / (1000*60)) % 60);
        int hours   = (int) (time / (1000*60*60));

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    protected void onDraw (Canvas canvas) { super.onDraw(canvas); }
}
