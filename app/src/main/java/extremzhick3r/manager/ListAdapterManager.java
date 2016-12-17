package extremzhick3r.manager;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

class NavItemManager {
    String title;
    String subtitle;
    Bitmap icon;

    public NavItemManager(String title, String subtitle, Bitmap icon) {
        this.title = title;
        this.subtitle = subtitle;
        this.icon = icon;
    }
}

class DownloadBitmapTask extends AsyncTask<URL, Void, Bitmap> {
    Bitmap bmImage;

    public DownloadBitmapTask() {}

    protected Bitmap doInBackground(URL... urls) {
        Bitmap b = null;
        URL u = urls[0];

        try {
            b = BitmapFactory.decodeStream(u.openConnection().getInputStream());
        } catch (Exception e) {e.printStackTrace();}

        return b;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage = result;
    }
}

public class ListAdapterManager extends BaseAdapter {

    private Context context;
    private ArrayList<NavItemManager> navItems;
    private int layout, title, subtitle, icon;

    public ListAdapterManager(Context context, int layout, int title, int subtitle, int icon) {
        this.context = context;
        this.navItems = new ArrayList<NavItemManager>();
        this.layout = layout;
        this.title = title;
        this.subtitle = subtitle;
        this.icon = icon;
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
            view = inflater.inflate(layout, null);
        }
        else {
            view = convertView;
        }

        TextView titleView = (TextView) view.findViewById(title);
        TextView subtitleView = (TextView) view.findViewById(subtitle);
        ImageView iconView = (ImageView) view.findViewById(icon);

        titleView.setText(navItems.get(position).title);
        subtitleView.setText(navItems.get(position).subtitle);
        iconView.setImageBitmap(navItems.get(position).icon);

        return view;
    }

    public void add(String title, String subtitle, URL url) {
        try {
            Bitmap b = new DownloadBitmapTask().execute(url).get();
            ListAdapterManager.this.add(title, subtitle, b);
        }
        catch (Exception e) { e.printStackTrace(); }
    }

    public void add(String title, String subtitle, int icon) {
        Bitmap b = BitmapFactory.decodeResource(context.getResources(), icon);
        this.add(title, subtitle, b);
    }

    public void add(String title, String subtitle, Bitmap icon) {
        navItems.add(new NavItemManager(title, subtitle, icon));
    }

    public String getTitle(int position) {
        return navItems.get(position).title;
    }
}
