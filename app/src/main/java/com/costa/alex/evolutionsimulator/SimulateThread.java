package com.costa.alex.evolutionsimulator;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Alex on 10/22/2017.
 * Simulation background thread
 */
class SimulateThread extends AsyncTask<Creature, Integer, Boolean> {

    private MainActivity main;
    private Creature creature;

    SimulateThread(Context context) {
        super();
        main = (MainActivity) context;
    }

    @Override
    protected Boolean doInBackground(Creature... params) {
        creature = params[0];
        main.setInitialVariables(creature);
        for(int i = 0; i < 300; i++) {
            main.simulate(creature);
            publishProgress(i+1);
            try {
                Thread.sleep(16, 666667);
            }
            catch (Exception e) {
                Log.e("ERROR", e.toString());
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int nodeCount = creature.getNodeCount();
        int muscleCount = creature.getMuscleCount();
        /*for(int i = 0; i < nodeCount; i++) {
            Node n = creature.nodes.get(i);
            n.setX(n.x);
            n.setY(n.y);
            n.invalidate();
            n.requestLayout();
        }*/
        creature.setX(creature.x);
        creature.setY(creature.y);
        creature.invalidate();
        creature.requestLayout();
        String time = String.valueOf(values[0]/60.0);
        int index = time.indexOf(".");
        main.getTimeDisplay().setText(time.substring(0, index+2));
        String distance = String.valueOf(creature.getX());
        int index2 = distance.indexOf(".");
        main.getDistanceDisplay().setText(distance.substring(0, index2+2));
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if(aBoolean) {
            Toast.makeText(main, "Simulation Complete!", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(main, "Simulation Failed.", Toast.LENGTH_LONG).show();
        }
    }
}
