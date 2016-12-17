package extremzhick3r.fragment;

import android.app.Fragment;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import extremzhick3r.R;


public class SettingsFragment extends Fragment {

    public SettingsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        final Button cant = (Button) getActivity().findViewById(R.id.stump);
        final ImageView youdidit = (ImageView) getActivity().findViewById(R.id.toldyou);

        cant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                youdidit.setVisibility(View.VISIBLE);
                cant.setVisibility(View.INVISIBLE);
            }
        });

        youdidit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                youdidit.setVisibility(View.INVISIBLE);
                cant.setVisibility(View.VISIBLE);
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
}
