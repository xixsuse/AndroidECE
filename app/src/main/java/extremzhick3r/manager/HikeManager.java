package extremzhick3r.manager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HikeManager {
    static final String FILENAME = "hikemanager";
    JSONObject hikes;
    long lastTime, interval;

    public HikeManager(long interval) {
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            hikes = new JSONObject("{date:'"+date+"',hikes:[]}");
            lastTime = 0;
            this.interval = interval;
        }
        catch (JSONException e) { e.printStackTrace(); }
    }

    public void saveHike(Activity a){
        try {
            int position = a.getFilesDir().list(new FilenameFilter() {
                public boolean accept(File dir, String name)
                {
                    return (name.startsWith("hikemanager") && name.endsWith(".json"));
                }
            }).length;

            FileOutputStream fos = a.openFileOutput(FILENAME+position+".json", Context.MODE_PRIVATE);
            fos.write(hikes.toString().getBytes());
            fos.close();
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    public void loadHike(Activity a, int position){
        try {
            int n;
            FileInputStream fis = a.openFileInput(FILENAME+position+".json");
            StringBuffer fileContent = new StringBuffer("");
            byte[] buffer = new byte[1024];

            while ((n = fis.read(buffer)) != -1)
                fileContent.append(new String(buffer, 0, n));

            hikes = new JSONObject(fileContent.toString());
            Log.v("MANAGER",hikes.toString());
        }
        catch (JSONException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }
    }

    public void addHikePoint(double lat, double lng){
        try {
            if(System.currentTimeMillis() - lastTime < interval)
                return;

            JSONArray manager = hikes.getJSONArray("hikes");
            Double[] coord = new Double[]{lat,lng};

            if(manager.length() == 0)
                manager.put(new JSONArray());
            manager.getJSONArray(0).put(new JSONArray(coord));
        }
        catch (JSONException e) { e.printStackTrace(); }
    }

    public JSONArray getPoints() {
        JSONArray array;
        try {
            array = hikes.getJSONArray("hikes").getJSONArray(0);
        }
        catch (JSONException e) {
            array = new JSONArray();
            e.printStackTrace();
        }

        return array;
    }
}
