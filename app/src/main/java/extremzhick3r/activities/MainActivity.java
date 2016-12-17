package extremzhick3r.activities;


import android.Manifest;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.content.Context;
import android.widget.ListView;

import extremzhick3r.R;
import extremzhick3r.fragment.AltimeterFragment;
import extremzhick3r.fragment.CompassFragment;
import extremzhick3r.fragment.HikeFragment;
import extremzhick3r.fragment.HikeListFragment;
import extremzhick3r.fragment.HomeFragment;
import extremzhick3r.fragment.MapsFragment;
import extremzhick3r.manager.HikeManager;
import extremzhick3r.manager.ListAdapterManager;
import extremzhick3r.services.LocationService;

public class MainActivity extends AppCompatActivity {

    final int APP_PERMISSION_REQUEST_LOCATION = 1;

    private HikeManager hikeManager;
    private Toolbar toolbar;
    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private NavigationView drawerPane;
    private ListAdapterManager drawerAdapter;
    private Intent locationIntent;
    private Fragment fragment;
    public Intent timerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request permissions
        requestAppPermissions();

        // Create the Hiking manager
        hikeManager = new HikeManager(10000);

        // Find the drawer layout
        drawerLayout = (DrawerLayout) findViewById(R.id.main_layout);

        // Populate the NavigationView with options
        drawerPane = (NavigationView) findViewById(R.id.drawer_panel);
        drawerList = (ListView) findViewById(R.id.nav_list);
        drawerAdapter = new ListAdapterManager(this, R.layout.item_drawer, R.id.drawer_list_title, R.id.drawer_list_subtitle, R.id.drawer_list_icon);
        drawerList.setAdapter(drawerAdapter);

        // Create the menu list
        drawerAdapter.add("Home", "O shit waddup", R.drawable.ic_home);
        drawerAdapter.add("Your hikes", "See wat u done mate", R.drawable.ic_hike);
        drawerAdapter.add("Compass", "Where u goin'", R.drawable.ic_compass);
        drawerAdapter.add("Altimeter", "How high are u (420m)", R.drawable.ic_altimeter);
        drawerAdapter.add("Map", "Want to find u arse", R.drawable.ic_map);
        drawerAdapter.add("Settings", "Wanna set dat boi", R.drawable.ic_settings);

        // Create toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.ic_menu);

        // Drawer item click listeners to open menu on click
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });

        // Toolbar home button
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Start the location service
        startLocationService();

        // Select home at startup
        selectItemFromDrawer(0);
    }

    private void selectItemFromDrawer(int position) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        switch(drawerAdapter.getTitle(position)) {
            case "Your hikes":
                fragment = new HikeListFragment();
                break;

            case "Compass":
                fragment = new CompassFragment();
                break;

            case "Map":
                fragment = new MapsFragment();
                break;

            case "Altimeter":
                fragment = new AltimeterFragment();
                break;

            case "Home":
                fragment = new HomeFragment();
                break;

            default:
                return;
        }

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        toolbar.setTitle(drawerAdapter.getTitle(position));
        drawerList.setItemChecked(position, true);
        drawerLayout.closeDrawer(drawerPane);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();

            if(b != null) {
                hikeManager.addHikePoint(
                        b.getFloat(LocationService.LATITUDE),
                        b.getFloat(LocationService.LONGITUDE)
                );

                // If we are on the map screen
                if(fragment.getClass() == MapsFragment.class) {
                    // Draw points
                    ((MapsFragment) fragment).drawPoints(hikeManager.getPoints());
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.unregisterReceiver(receiver);
        this.stopService(locationIntent);
    }

    @Override
    public void onBackPressed() {
        if(hikeManager.getState() && fragment.getClass() != HikeFragment.class)
            new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("You didn't stop your hike!")
                .setMessage("Do you want to quit without saving?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopService(timerIntent);
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
        else
            super.onBackPressed();
    }

    public boolean getState() { return hikeManager.getState(); }
    public void startHike() {
        hikeManager.start();
    }
    public void stopHike() {
        hikeManager.stop();
        hikeManager.saveHike(this);
    }

    public void startLocationService() {
        this.registerReceiver(receiver, new IntentFilter(LocationService.SERVICE));
        locationIntent = new Intent(this, LocationService.class);
        this.startService(locationIntent);
    }

    public void requestAppPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show explanation of why but I'm too lazy to do it
            } else {
                this.requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, APP_PERMISSION_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case APP_PERMISSION_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startLocationService();
            break;
        }
    }
}
