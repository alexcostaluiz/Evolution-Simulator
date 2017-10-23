package com.costa.alex.evolutionsimulator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Alex on 10/21/2017.
 * Home Fragment (graphs and generation actions)
 */
public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);
        MainActivity main = (MainActivity) container.getContext();
        main.setInitializer((LinearLayout) v.findViewById(R.id.initializer));
        return v;
    }
}
