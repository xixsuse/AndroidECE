package extremzhick3r.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.mypopsy.maps.StaticMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;

import extremzhick3r.R;
import extremzhick3r.manager.HikeManager;
import extremzhick3r.manager.ListAdapterManager;
import extremzhick3r.manager.ZoomManager;
import extremzhick3r.view.TimerView;


public class HikeListFragment extends Fragment {
    private ArrayList<JSONObject> hikes;
    public HikeListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hike_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        hikes = HikeManager.loadHikes(this.getActivity());
        ListView listView = (ListView) this.getActivity().findViewById(R.id.hikelist);
        ListAdapterManager listAdapterManager = new ListAdapterManager(this.getActivity(), R.layout.item_hike, R.id.hike_list_title, R.id.hike_list_subtitle, R.id.hike_list_icon);

        // Create list hikes
        for (int i=0,j=hikes.size(); i < j; i++) {
            try {
                LatLngBounds bounds;
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                JSONArray points = hikes.get(i).getJSONArray("hike");
                StaticMap.GeoPoint[] g = new StaticMap.GeoPoint[points.length()];

                if(points.length() <= 0)
                    continue;

                for(int k=0, l=points.length(); k<l; k++) {
                    builder.include(new LatLng(points.getJSONArray(k).getDouble(0), points.getJSONArray(k).getDouble(1)));
                    g[k] = new StaticMap.GeoPoint(points.getJSONArray(k).getDouble(0), points.getJSONArray(k).getDouble(1));
                }

                bounds = builder.build();
                StaticMap m = new StaticMap().center(points.getJSONArray(0).getDouble(0), points.getJSONArray(0).getDouble(1))
                                .zoom(12).size(100, 100).path(g).zoom(ZoomManager.getBoundsZoomLevel(bounds, 100, 100));

                listAdapterManager.add(
                        "Date " + hikes.get(i).getString("date"),
                        "Duration: " + TimerView.milliToTimer(hikes.get(i).getLong("time")),
                        m.toURL());
            }
            catch (JSONException e) { e.printStackTrace(); }
            catch (MalformedURLException e) { e.printStackTrace(); }
            catch (NullPointerException e) { e.printStackTrace(); }
        }

        listView.setAdapter(listAdapterManager);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromList(position);
            }
        });
    }

    private void selectItemFromList(int position) {
        HikeFragment hikeFragment = new HikeFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        hikeFragment.setHike(hikes.get(position));

        fragmentTransaction.replace(R.id.fragment_container, hikeFragment, "hike");
        fragmentTransaction.addToBackStack("hike");
        fragmentTransaction.commit();
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
}
