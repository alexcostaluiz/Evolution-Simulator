package com.costa.alex.evolutionsimulator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by Alex on 10/21/2017.
 * Population Fragment (GridView of all creatures)
 */
public class PopulationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.population_fragment, container, false);
        MainActivity main = (MainActivity) container.getContext();
        main.setPopulationContainer((GridView) v.findViewById(R.id.population_container));
        return v;
    }
}
