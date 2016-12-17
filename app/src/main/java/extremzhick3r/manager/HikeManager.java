package extremzhick3r.manager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class HikeManager {
    private static final String FILENAME = "hikemanager";
    private static final String EXTENSION = ".json";
    private JSONObject hikes;
    private long lastTime, interval, startTime;
    private boolean isStarted;

    public HikeManager(long interval) {
        lastTime = 0;
        this.interval = interval;
    }

    public void start() {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        isStarted = true;
        startTime = System.currentTimeMillis();

        try { hikes = new JSONObject("{date:"+date+",hike:[]}"); }
        catch (JSONException e) { e.printStackTrace(); }
    }

    public void stop() {
        isStarted = false;

        try { hikes.put("time", System.currentTimeMillis() - startTime); }
        catch (JSONException e) { e.printStackTrace(); }
    }

    public boolean getState() { return isStarted; }

    public void saveHike(Activity a){
        if(getPoints().length() <= 0)
            return;

        try {
            String uuid = UUID.randomUUID().toString();
            hikes.put("id", uuid);

            FileOutputStream fos = a.openFileOutput(FILENAME+uuid+EXTENSION, Context.MODE_PRIVATE);
            fos.write(hikes.toString().getBytes());
            fos.close();
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    public static ArrayList<JSONObject> loadHikes(Activity a) {
        ArrayList<JSONObject> allHikes = new ArrayList<JSONObject>();
        File[] files = a.getFilesDir().listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return (name.startsWith(FILENAME) && name.endsWith(EXTENSION));
            }
        });

        for(File f : files)
            allHikes.add(loadJSON(a, f.getName()));

        return allHikes;
    }

    public static JSONObject loadHike(Activity a, String uuid){
        File f = a.getFileStreamPath(FILENAME+uuid+EXTENSION);
        return loadJSON(a, f.getName());
    }

    public static JSONObject loadJSON(Activity a, String fileName) {
        JSONObject hike = null;

        try {
            int n;
            FileInputStream fis = a.openFileInput(fileName);
            StringBuffer fileContent = new StringBuffer();
            byte[] buffer = new byte[1024];

            while ((n = fis.read(buffer)) != -1)
                fileContent.append(new String(buffer, 0, n));

            hike = new JSONObject(fileContent.toString());
        }
        catch (JSONException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }

        return hike;
    }

    public static boolean deleteHike(Activity a, final String uuid) {
        return a.getFileStreamPath(FILENAME + uuid + EXTENSION).delete();
    }

    public void addHikePoint(double lat, double lng){
        try {
            if(!isStarted)
                return;

            if(System.currentTimeMillis() - lastTime < interval)
                return;

            Double[] coord = new Double[]{lat,lng};
            hikes.getJSONArray("hike").put(new JSONArray(coord));
        }
        catch (JSONException e) { e.printStackTrace(); }
    }

    public JSONArray getPoints() {
        JSONArray array = null;
        try {
            array = hikes.getJSONArray("hike");
        }
        catch (JSONException e) {
            array = new JSONArray();
            e.printStackTrace();
        }
        catch (NullPointerException e) { e.printStackTrace(); }

        return array;
    }
}
