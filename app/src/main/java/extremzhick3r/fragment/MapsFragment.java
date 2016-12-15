package extremzhick3r.fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import extremzhick3r.R;
import extremzhick3r.services.LocationService;


public class MapsFragment extends Fragment implements OnMapReadyCallback {

    MapView mapView;
    private GoogleMap map;
    private Intent locationIntent;
    private Location here;

    public MapsFragment() {}

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();

            if(b != null) {
                here.setLatitude(b.getFloat(LocationService.LATITUDE));
                here.setLongitude(b.getFloat(LocationService.LONGITUDE));
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        here = new Location(LocationManager.GPS_PROVIDER);

        this.getActivity().registerReceiver(receiver, new IntentFilter(
                LocationService.SERVICE));
        locationIntent = new Intent(this.getActivity(), LocationService.class);
        this.getActivity().startService(locationIntent);

        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        mapView = (MapView) rootView.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // For showing a move to my location button
        if (ActivityCompat.checkSelfPermission(MapsFragment.this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MapsFragment.this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        map.setMyLocationEnabled(true);

        // For dropping a marker at a point on the Map
        //LatLng sydney = new LatLng(-34, 151);
        //map.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

        // For zooming automatically to the location of the marker
        //CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
        //map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
