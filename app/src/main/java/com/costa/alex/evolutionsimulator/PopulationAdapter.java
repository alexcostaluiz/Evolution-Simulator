package com.costa.alex.evolutionsimulator;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Alex on 10/18/2017.
 * Population adapter for population container (GridView)
 */
class PopulationAdapter extends ArrayAdapter<Creature> {

    private MainActivity main;
    private ArrayList<Creature> dataSet;

    PopulationAdapter(Context context, int resource, ArrayList<Creature> creatures) {
        super(context, resource, creatures);
        dataSet = creatures;
        main = (MainActivity) context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return dataSet.get(position);
    }
}
