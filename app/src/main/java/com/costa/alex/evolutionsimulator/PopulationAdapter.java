package com.costa.alex.evolutionsimulator;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * Created by Alex on 10/18/2017.
 * Population adapter for population container (RecyclerView)
 */
class PopulationAdapter extends RecyclerView.Adapter<PopulationAdapter.CreatureHolder> {

    private MainActivity main;
    private ArrayList<Creature> dataSet;
    private int size;

    class CreatureHolder extends RecyclerView.ViewHolder {

        FrameLayout container;

        CreatureHolder(FrameLayout container) {
            super(container);
            this.container = container;
        }
    }

    PopulationAdapter(Context context, ArrayList<Creature> dataSet) {
        main = (MainActivity) context;
        size  = main.getResources().getDimensionPixelSize(R.dimen.container);
        this.dataSet = dataSet;
    }

    @Override
    public CreatureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FrameLayout container = new FrameLayout(main);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(size, size);
        container.setLayoutParams(lp);
        return new CreatureHolder(container);
    }

    @Override
    public void onBindViewHolder(CreatureHolder holder, int position) {
        Creature creature = dataSet.get(position);
        if(creature.getParent() != null) {
            ((FrameLayout)creature.getParent()).removeView(creature);
        }
        holder.container.addView(creature);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
