package extremzhick3r.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import extremzhick3r.R;
import extremzhick3r.services.AltimeterService;


public class AltimeterFragment extends Fragment {
    private Intent altimeterIntent;

    public AltimeterFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();

            if(b != null) {
                TextView textAltitude = (TextView) AltimeterFragment.this.getActivity().findViewById(R.id.altitude);
                textAltitude.setText(Integer.toString((int) b.getFloat(AltimeterService.ALTIMETER)));
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.getActivity().registerReceiver(receiver, new IntentFilter(
                AltimeterService.SERVICE));

        altimeterIntent = new Intent(this.getActivity(), AltimeterService.class);
        this.getActivity().startService(altimeterIntent);

        return inflater.inflate(R.layout.fragment_altimeter, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.getActivity().unregisterReceiver(receiver);
        this.getActivity().stopService(altimeterIntent);
    }
}
