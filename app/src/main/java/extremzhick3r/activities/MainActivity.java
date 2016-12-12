package extremzhick3r.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
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
import extremzhick3r.fragment.MapsFragment;

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
            view = inflater.inflate(R.layout.drawer_item, null);
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

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private NavigationView drawerPane;
    private ArrayList<NavItem> navItems;
    private DrawerListAdapter drawerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the drawer layout
        drawerLayout = (DrawerLayout) findViewById(R.id.main_layout);

        // Create the menu list
        navItems = new ArrayList<NavItem>();
        navItems.add(new NavItem("Home", "O shit waddup", R.drawable.ic_home));
        navItems.add(new NavItem("Compass", "Where u goin'", R.drawable.ic_compass));
        navItems.add(new NavItem("Altimeter", "How high are u (420m)", R.drawable.ic_altimeter));
        navItems.add(new NavItem("Map", "Want to find u arse", R.drawable.ic_map));

        // Populate the Navigtion Drawer with options
        drawerPane = (NavigationView) findViewById(R.id.drawer_panel);
        drawerList = (ListView) findViewById(R.id.nav_list);
        drawerAdapter = new DrawerListAdapter(this, navItems);
        drawerList.setAdapter(drawerAdapter);

        // Create toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.drawable.ic_menu);

        // Drawer item click listeners
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
                Log.d("LOG_TAG", "navigation clicked");
            }
        });
    }

    private void selectItemFromDrawer(int position) {
        Fragment fragment;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

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

            default:
                return;
        }

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        toolbar.setTitle(navItems.get(position).title);
        drawerList.setItemChecked(position, true);
        drawerLayout.closeDrawer(drawerPane);
    }
}
