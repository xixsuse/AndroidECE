package extremzhick3r.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import extremzhick3r.R;
import extremzhick3r.activities.MainActivity;
import extremzhick3r.services.LocationService;
import extremzhick3r.services.TimerService;
import extremzhick3r.view.TimerView;


public class HomeFragment extends Fragment {

    private boolean isStarted;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();

        if(receiver != null)
            getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(receiver != null)
            this.getActivity().registerReceiver(receiver, new IntentFilter(TimerService.SERVICE));
    }

    @Override
    public void onStart() {
        super.onStart();

        final MainActivity mainActivity = ((MainActivity)HomeFragment.this.getActivity());
        final Button startstop = (Button) this.getActivity().findViewById(R.id.startstop);
        isStarted = mainActivity.getState();

        if(isStarted)
            startstop.setText("Stop");
        else
            startstop.setText("Start");

        startstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isStarted)
                    start();
                else
                    stop();

                isStarted = !isStarted;
            }
        });
    }

    public void start() {
        MainActivity mainActivity = ((MainActivity)HomeFragment.this.getActivity());
        Button startstop = (Button) mainActivity.findViewById(R.id.startstop);

        mainActivity.startHike();
        startstop.setText("Stop");

        mainActivity.timerIntent = new Intent(this.getActivity(), TimerService.class);
        mainActivity.startService(mainActivity.timerIntent);
    }

    public void stop() {
        MainActivity mainActivity = ((MainActivity)HomeFragment.this.getActivity());
        Button startstop = (Button) mainActivity.findViewById(R.id.startstop);

        mainActivity.stopHike();
        startstop.setText("Start");

        mainActivity.stopService(mainActivity.timerIntent);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();

            if(b != null) {
                TimerView timer = (TimerView) HomeFragment.this.getActivity().findViewById(R.id.timer);
                timer.setTime(b.getLong(TimerService.TIMER));
            }
        }
    };
}
