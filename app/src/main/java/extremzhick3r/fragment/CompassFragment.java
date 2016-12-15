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

import extremzhick3r.R;
import extremzhick3r.services.CompassService;

public class CompassFragment extends Fragment {
    private Intent compassIntent;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();

            if(b != null) {
                ImageView compass = (ImageView) CompassFragment.this.getActivity().findViewById(R.id.compass_image);
                compass.setRotation(((int)b.getFloat(CompassService.AZIMUTH)/5)*5);
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
        this.getActivity().registerReceiver(receiver, new IntentFilter(
                CompassService.SERVICE));

        compassIntent = new Intent(this.getActivity(), CompassService.class);
        this.getActivity().startService(compassIntent);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.getActivity().unregisterReceiver(receiver);
        this.getActivity().stopService(compassIntent);

    }

}
