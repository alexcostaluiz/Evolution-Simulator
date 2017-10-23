package com.costa.alex.evolutionsimulator;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static int DEFAULT_MAX_NODE = 5;
    public static int DEFAULT_POPULATION_SIZE = 1000;
    public static ArrayList<Long> allID = new ArrayList<>();

    public static float GRAVITY = 9.80665f*200f/3600f; //Constant 9.80 m/s^2 acceleration due to gravity
    //public static float AIR_FRICTION(float v, float radius) {
    //    return 0.5f*1.204f*v*1f*(float)Math.PI*radius*radius;
    //}
    public static float AIR_FRICTION = .999f; //Applied every 60th of a second

    private Random rand;
    private Population population;
    private long seed;
    private int populationSize;
    private int maxNode;

    private RelativeLayout root;
    private EditText maxNodes, cPopulationSize, customSeed;
    private LinearLayout initializer;
    private RelativeLayout progress;
    private GridView populationContainer;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    private View ground;
    private TextView time, distance;
    private SimulateThread simulateThread;

    boolean generated = false;

    AlertDialog creatureInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = (RelativeLayout) findViewById(R.id.root);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);

        ground = findViewById(R.id.ground);
        time = (TextView) findViewById(R.id.time_display);
        distance = (TextView) findViewById(R.id.distance_display);
    }

    public boolean generate(View v) {
        if(!generated) {
            generated = true;
            seed = (customSeed.getText().toString().equals("")) ? Math.round(Math.random()*1000000000.0) : Long.valueOf(customSeed.getText().toString());
            Log.e("SEED", String.valueOf(seed));
            rand = new Random(seed);
            maxNode = (maxNodes.getText().toString().equals("")) ? DEFAULT_MAX_NODE : Integer.valueOf(maxNodes.getText().toString());
            if(maxNode > 13) {
                Toast.makeText(this, "Max nodes cannot exceed 13. It's not recommended to exceed 8 nodes for performance sake.", Toast.LENGTH_LONG).show();
                generated = false;
                return false;
            }
            else if(maxNode < 3) {
                Toast.makeText(this, "Max nodes must be at least 3. A creature with 2 nodes is like you with no arms or legs.", Toast.LENGTH_LONG).show();
                generated = false;
                return false;
            }
            populationSize = (cPopulationSize.getText().toString().equals("")) ? DEFAULT_POPULATION_SIZE : Integer.valueOf(cPopulationSize.getText().toString());

            initializer.setVisibility(View.GONE);
            progress = (RelativeLayout) getLayoutInflater().inflate(R.layout.progress_indicator, root, false);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) progress.getLayoutParams();
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            progress.setLayoutParams(lp);
            root.addView(progress);
            ((ProgressBar)progress.getChildAt(1)).setMax(populationSize);
            population = new Population(this, populationSize);
            return true;
        }
        return false;
    }

    public void preparePopulationContainer() {
        findViewById(R.id.copyright).setVisibility(View.GONE);
        findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        findViewById(R.id.tab_layout).setVisibility(View.VISIBLE);
        viewPager.setCurrentItem(1, false);
        populationContainer.setVisibility(View.VISIBLE);
        populationContainer.setAdapter(new PopulationAdapter(this, 0, population));
        populationContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Creature creature = (Creature) view;
                View creatureInfoView = getLayoutInflater().inflate(R.layout.creature_info, root, false);
                ((TextView)creatureInfoView.findViewById(R.id.species_name)).setText(creature.getSpeciesName());
                ((TextView)creatureInfoView.findViewById(R.id.uuid_number)).setText(String.valueOf(creature.getID()));
                Creature copy = creature.copy();
                copy.setZoom(1f);
                Paint outline = new Paint();
                outline.setColor(0x8a000000);
                outline.setStyle(Paint.Style.STROKE);
                outline.setStrokeWidth(10);
                copy.setOutline(outline);
                ((FrameLayout)creatureInfoView.findViewById(R.id.creature_image)).addView(copy);
                View title = getLayoutInflater().inflate(R.layout.creature_info_title, root, false);
                String sTitle = "Creature #" + (position+1);
                ((TextView)title.findViewById(R.id.creature_info_title)).setText(sTitle);
                creatureInfo = new AlertDialog.Builder(MainActivity.this)
                        .setCustomTitle(title)
                        .setView(creatureInfoView)
                        .create();
                creatureInfo.show();
            }
        });
    }

    public void closeCreatureInfo(View v) {
        if(creatureInfo != null) {
            if (creatureInfo.isShowing()) {
                creatureInfo.hide();
                creatureInfo = null;
            }
        }
    }

    public void simulateCreature(View v) {
        FrameLayout parent = (FrameLayout) creatureInfo.findViewById(R.id.creature_image);
        if (parent != null) {
            Creature creature = (Creature) parent.getChildAt(0);
            parent.removeView(creature);
            creatureInfo.hide();
            creatureInfo = null;
            ground.setVisibility(View.VISIBLE);
            time.setVisibility(View.VISIBLE);
            distance.setVisibility(View.VISIBLE);
            findViewById(R.id.tap_anywhere).setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
            findViewById(R.id.toolbar).setVisibility(View.GONE);
            root.addView(creature);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) creature.getLayoutParams();
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            lp.addRule(RelativeLayout.ABOVE, R.id.ground);
            lp.bottomMargin = 200;
            creature.setSimulating(true);
            temp = creature;
            simulateThread = new SimulateThread(this);
        }
    }

    Creature temp;

    public void startSimulation(View v) {
        findViewById(R.id.tap_anywhere).setVisibility(View.GONE);
        simulateThread.execute(temp);
    }

    public TextView getTimeDisplay() {
        return time;
    }

    public TextView getDistanceDisplay() {
        return distance;
    }

    public void simulate(Creature creature) {
        creature.applyGravity(ground.getY() - creature.getHeight());
        for(int i = 0; i < creature.getNodeCount(); i++) {
            Node n = creature.nodes.get(i);
            //n.applyForces(creature.getY() + creature.getHeight() - n.getHeight());
        }
    }

    public void setInitialVariables(Creature creature) {
        creature.x = creature.getX();
        creature.y = creature.getY();
        /*for(int i = 0; i < creature.getNodeCount(); i++) {
            Node n = creature.nodes.get(i);
            n.setInitialVariables();
        }*/
    }

    public void showInfo(View v) {
        switch (v.getId()) {
            case R.id.seed_info :
                new AlertDialog.Builder(this)
                        .setTitle("Seed Information")
                        .setMessage("This is a bunch of information about how seeds work.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //closes dialog
                            }
                        })
                        .show();
                break;
            case R.id.node_info :
                new AlertDialog.Builder(this)
                        .setTitle("Node Information")
                        .setMessage("This is a bunch of information about how nodes work.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //closes dialog
                            }
                        })
                        .show();
                break;
            case R.id.population_info :
                new AlertDialog.Builder(this)
                        .setTitle("Population Information")
                        .setMessage("This is a bunch of information about how populations work.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //closes dialog
                            }
                        })
                        .show();
                break;
        }
    }

    public void setPopulationContainer(GridView v) {
        populationContainer = v;
    }

    public void setInitializer(LinearLayout v) {
        initializer = v;
        customSeed = (EditText) v.findViewById(R.id.custom_seed);
        maxNodes = (EditText) v.findViewById(R.id.max_nodes);
        cPopulationSize = (EditText) v.findViewById(R.id.population_size);
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

    public View getRoot() {
        return root;
    }
}
