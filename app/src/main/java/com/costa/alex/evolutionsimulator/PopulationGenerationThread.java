package com.costa.alex.evolutionsimulator;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Alex on 10/17/2017.
 * Population generation thread
 */
class PopulationGenerationThread extends AsyncTask<Population, Integer, Boolean> {

    private MainActivity main;
    private Population population;

    @Override
    protected Boolean doInBackground(Population... params) {
        population = params[0];
        main = (MainActivity) population.getContext();
        generate(population.getSize());
        return true;
    }

    @Override
    protected void onPostExecute(Boolean complete) {
        main.setPopulation(population);
        Toast.makeText(main, "Population Created!", Toast.LENGTH_SHORT).show();
        main.getProgress().setVisibility(View.GONE);
        main.preparePopulationContainer();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        ((ProgressBar)main.getProgress().getChildAt(1)).setProgress(values[0]);
    }

    private void generate(int size) {
        for(int i = 0; i < size; i++) {
            Creature creature = new Creature(main);
            population.add(creature);
            assignCreatureSpecies(creature);
            publishProgress(i+1);
        }
    }

    private ArrayList<Creature> assignCreatureSpecies(Creature creature) {
        int nodeCount = creature.getNodeCount();
        int muscleCount = creature.getMuscleCount();
        int tag = (muscleCount == 10) ? 510 : (nodeCount*10) + muscleCount;
        for(Species s : population.getAllSpecies()) {
            if(s.getTag() == tag) {
                s.add(creature);
                return s;
            }
        }
        Species newSpecies = new Species(tag);
        newSpecies.add(creature);
        population.getAllSpecies().add(newSpecies);
        return newSpecies;
    }
}
