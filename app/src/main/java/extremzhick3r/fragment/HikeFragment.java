package extremzhick3r.fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mypopsy.maps.StaticMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

import extremzhick3r.R;
import extremzhick3r.activities.MainActivity;
import extremzhick3r.manager.HikeManager;
import extremzhick3r.manager.ListAdapterManager;
import extremzhick3r.manager.ZoomManager;
import extremzhick3r.view.TimerView;


public class HikeFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap map;
    private JSONObject hike;

    public HikeFragment() {}

    public void setHike(JSONObject hike) {
        this.hike = hike;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_hike, container, false);

        mapView = (MapView) rootView.findViewById(R.id.hike_view);
        mapView.onCreate(savedInstanceState);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) { e.printStackTrace(); }

        mapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FloatingActionButton delete = (FloatingActionButton) getActivity().findViewById(R.id.delete_hike);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(HikeFragment.this.getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Do you really want to delete your hike?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteHike();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
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
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        JSONArray points;

        // Save map
        map = googleMap;

        try {
            points = hike.getJSONArray("hike");
        }
        catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        for(int j=0, k=points.length(); j<k; j++) {
            try {
                builder.include(new LatLng(points.getJSONArray(j).getDouble(0), points.getJSONArray(j).getDouble(1)));
            }
            catch (JSONException e) { e.printStackTrace(); }
        }

        drawPoints(points);
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 10));
    }

    private void drawPoints(JSONArray points) {
        if(points == null)
            return;

        int length = points.length();
        LatLng[] latLngs = new LatLng[length];

        try {
            for(int i=0, j=length; i<j; i++) {
                latLngs[i] = new LatLng(
                        points.getJSONArray(i).getDouble(0),
                        points.getJSONArray(i).getDouble(1)
                );
            }
        }
        catch (JSONException e) { e.printStackTrace(); }

        map.clear();
        map.addPolyline(new PolylineOptions().add(latLngs).width(5).color(Color.RED));
    }

    private void deleteHike() {
        try {
            HikeManager.deleteHike(this.getActivity(), hike.getString("id"));
        }
        catch (JSONException e) { e.printStackTrace(); }

        this.getActivity().onBackPressed();
    }
}
