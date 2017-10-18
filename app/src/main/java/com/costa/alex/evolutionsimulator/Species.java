package com.costa.alex.evolutionsimulator;

import java.util.ArrayList;

/**
 * Created by Alex on 10/17/2017.
 * Species as an ArrayList of creatures.
 */
class Species extends ArrayList<Creature> {

    private int tag;

    Species(int tag) {
        this.tag = tag;
    }

    /* Getter and Setter */
    int getTag() {
        return tag;
    }
}
