package com.costa.alex.evolutionsimulator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static int DEFAULT_MAX_NODE = 5;
    public static int DEFAULT_POPULATION_SIZE = 1000;

    private Random rand;
    private Population population;
    private int seed;
    private int populationSize;
    private int maxNode;

    private RelativeLayout root;
    private EditText maxNodes, cPopulationSize, customSeed;
    private LinearLayout initializer;
    private RelativeLayout progress;
    private RecyclerView populationContainer;

    boolean generated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seed = 69;
        rand = new Random(seed);

        root = (RelativeLayout) findViewById(R.id.root);
        initializer = (LinearLayout) findViewById(R.id.initializer);
        populationContainer = (RecyclerView) findViewById(R.id.population_container);
        maxNodes = (EditText) findViewById(R.id.max_nodes);
        cPopulationSize = (EditText) findViewById(R.id.population_size);
        customSeed = (EditText) findViewById(R.id.custom_seed);
    }

    public void generate(View v) {
        if(!generated) {
            generated = true;
            seed = (customSeed.getText().toString().equals("")) ? (int)Math.round(Math.random()*100) : Integer.valueOf(customSeed.getText().toString());
            rand = new Random(seed);
            maxNode = (maxNodes.getText().toString().equals("")) ? DEFAULT_MAX_NODE : Integer.valueOf(maxNodes.getText().toString());
            populationSize = (cPopulationSize.getText().toString().equals("")) ? DEFAULT_POPULATION_SIZE : Integer.valueOf(cPopulationSize.getText().toString());

            initializer.setVisibility(View.GONE);
            progress = (RelativeLayout) getLayoutInflater().inflate(R.layout.progress_indicator, root, false);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) progress.getLayoutParams();
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            progress.setLayoutParams(lp);
            root.addView(progress);
            ((ProgressBar)progress.getChildAt(1)).setMax(populationSize);
            population = new Population(this, populationSize);
        }
    }

    public void preparePopulationContainer() {
        populationContainer.setVisibility(View.VISIBLE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 6);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        populationContainer.setLayoutManager(gridLayoutManager);
        populationContainer.setAdapter(new PopulationAdapter(this, population));
    }

    public Random getRand() {
        return rand;
    }

    public Population getPopulation() {
        return population;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

    public RelativeLayout getProgress() {
        return progress;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public int getMaxNode() {
        return maxNode;
    }
}
