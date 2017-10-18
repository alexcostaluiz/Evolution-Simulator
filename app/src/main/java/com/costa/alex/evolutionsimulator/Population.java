package com.costa.alex.evolutionsimulator;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Alex on 10/17/2017.
 * Population of all creatures and all species
 */
class Population extends ArrayList<Creature> {

    private Context context;
    private int size;

    private ArrayList<Species> allSpecies = new ArrayList<>();

    Population(Context context, int size) {
        this.context = context;
        this.size = size;
        new PopulationGenerationThread().execute(this);
    }

    /* Getters */
    ArrayList<Species> getAllSpecies() {
        return allSpecies;
    }

    int getSize() {
        return size;
    }

    Context getContext() {
        return context;
    }
}
