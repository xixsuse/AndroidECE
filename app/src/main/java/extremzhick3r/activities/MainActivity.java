package extremzhick3r.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

import extremzhick3r.R;
import extremzhick3r.fragment.AltimeterFragment;
import extremzhick3r.fragment.CompassFragment;
import extremzhick3r.fragment.HomeFragment;
import extremzhick3r.fragment.MapsFragment;
import extremzhick3r.manager.HikeManager;
import extremzhick3r.services.LocationService;

class NavItem {
    String title;
    String subtitle;
    int icon;

    public NavItem(String title, String subtitle, int icon) {
        this.title = title;
        this.subtitle = subtitle;
        this.icon = icon;
    }
}

class DrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavItem> navItems;

    public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
        this.context = context;
        this.navItems = navItems;
    }

    @Override
    public int getCount() {
        return navItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_drawer, null);
        }
        else {
            view = convertView;
        }

        TextView titleView = (TextView) view.findViewById(R.id.drawer_list_title);
        TextView subtitleView = (TextView) view.findViewById(R.id.drawer_list_subtitle);
        ImageView iconView = (ImageView) view.findViewById(R.id.drawer_list_icon);

        titleView.setText(navItems.get(position).title);
        subtitleView.setText(navItems.get(position).subtitle);
        iconView.setImageResource(navItems.get(position).icon);

        return view;
    }
}

class HikeListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavItem> navItems;

    public HikeListAdapter(Context context, ArrayList<NavItem> navItems) {
        this.context = context;
        this.navItems = navItems;
    }

    @Override
    public int getCount() {
        return navItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_hike, null);
        }
        else {
            view = convertView;
        }

        TextView titleView = (TextView) view.findViewById(R.id.hike_list_title);
        TextView subtitleView = (TextView) view.findViewById(R.id.hike_list_subtitle);
        ImageView iconView = (ImageView) view.findViewById(R.id.hike_list_icon);

        titleView.setText(navItems.get(position).title);
        subtitleView.setText(navItems.get(position).subtitle);
        iconView.setImageResource(navItems.get(position).icon);

        return view;
    }
}

public class MainActivity extends AppCompatActivity {

    private HikeManager hikeManager;
    private Toolbar toolbar;
    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private NavigationView drawerPane;
    private ArrayList<NavItem> navItems;
    private DrawerListAdapter drawerAdapter;
    private Intent locationIntent;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the Hiking manager
        hikeManager = new HikeManager(10000);

        // Find the drawer layout
        drawerLayout = (DrawerLayout) findViewById(R.id.main_layout);

        // Create the menu list
        navItems = new ArrayList<NavItem>();
        navItems.add(new NavItem("Home", "O shit waddup", R.drawable.ic_home));
        navItems.add(new NavItem("Your hikes", "See wat u done mate", R.drawable.ic_hike));
        navItems.add(new NavItem("Compass", "Where u goin'", R.drawable.ic_compass));
        navItems.add(new NavItem("Altimeter", "How high are u (420m)", R.drawable.ic_altimeter));
        navItems.add(new NavItem("Map", "Want to find u arse", R.drawable.ic_map));
        navItems.add(new NavItem("Settings", "Wanna set dat boi", R.drawable.ic_settings));

        // Populate the NavigationView with options
        drawerPane = (NavigationView) findViewById(R.id.drawer_panel);
        drawerList = (ListView) findViewById(R.id.nav_list);
        drawerAdapter = new DrawerListAdapter(this, navItems);
        drawerList.setAdapter(drawerAdapter);

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
        this.registerReceiver(receiver, new IntentFilter(LocationService.SERVICE));
        locationIntent = new Intent(this, LocationService.class);
        this.startService(locationIntent);

        // Select home at startup
        selectItemFromDrawer(0);
    }

    private void selectItemFromDrawer(int position) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        switch(navItems.get(position).title) {
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

        toolbar.setTitle(navItems.get(position).title);
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
                Log.v("MAIN","LOCATION");

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
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Extremz Hick3r")
                .setMessage("Are you sure you want to close the hike without saving?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hikeManager.saveHike(MainActivity.this);
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
