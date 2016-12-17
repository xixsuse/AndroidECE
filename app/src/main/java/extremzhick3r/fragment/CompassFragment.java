package extremzhick3r.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import extremzhick3r.R;
import extremzhick3r.services.CompassService;
import extremzhick3r.services.LocationService;

public class CompassFragment extends Fragment {
    private Intent compassIntent;
    private BroadcastReceiver compassReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();

            if(b != null) {
                ImageView compass = (ImageView) CompassFragment.this.getActivity().findViewById(R.id.compass_image);
                compass.setRotation(((int)b.getFloat(CompassService.AZIMUTH)/5)*5);
            }
        }
    };
    private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();

            if(b != null) {
                TextView latlng = (TextView) CompassFragment.this.getActivity().findViewById(R.id.compass_latlng);
                latlng.setText( "Latitude: "+ Float.toString(b.getFloat(LocationService.LATITUDE)) + "\n" +
                                "Longitude: " + Float.toString(b.getFloat(LocationService.LONGITUDE)));
            }
        }
    };

    public CompassFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.fragment_compass, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.getActivity().registerReceiver(compassReceiver, new IntentFilter(
                CompassService.SERVICE));
        this.getActivity().registerReceiver(locationReceiver, new IntentFilter(
                LocationService.SERVICE));

        compassIntent = new Intent(this.getActivity(), CompassService.class);
        this.getActivity().startService(compassIntent);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.getActivity().unregisterReceiver(compassReceiver);
        this.getActivity().unregisterReceiver(locationReceiver);
        this.getActivity().stopService(compassIntent);
    }

}
