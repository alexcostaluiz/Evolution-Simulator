package com.costa.alex.evolutionsimulator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Alex on 10/17/2017.
 * Creature
 */
public class Creature extends ViewGroup {

    private MainActivity main;

    private UUID cUUID;
    private int nodeCount;
    private int muscleCount;
    private boolean alive = true;

    private Paint outline;
    private int width, height;
    private Rect rect;

    ArrayList<Node> nodes = new ArrayList<>();
    ArrayList<Muscle> muscles = new ArrayList<>();

    /* Constructors */
    public Creature(Context context) {
        this(context, null);
    }

    public Creature(Context context, AttributeSet attrs) {
        super(context, attrs);
        main = (MainActivity) context;
        init();
    }

    private void init() {
        this.setWillNotDraw(false);
        outline = new Paint();
        outline.setColor(0xff424242);
        outline.setStyle(Paint.Style.STROKE);
        outline.setStrokeWidth(2);
        generate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
        rect = new Rect(0, 0, width, height);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            if(child.getVisibility() == View.GONE) {
                continue;
            }

            measureChild(child, widthMeasureSpec, heightMeasureSpec);

        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        final int width = this.getMeasuredWidth();
        final int height = this.getMeasuredHeight();
        for(int i = 0; i < count; i++) {
            View child = null;
            if(i % 2 == 0) {
                if(i/2 < nodes.size()) {
                    child = nodes.get(i/2);
                }
                else if(i/2 < muscles.size()){
                    child = muscles.get(i/2);
                }
            }
            else {
                if((i-1)/2 < muscles.size()) {
                    child = muscles.get((i - 1)/2);
                }
                else if((i-1)/2 < nodes.size()) {
                    child = nodes.get((i-1)/2);
                }
            }
            if(child instanceof Node) {
                //layout child
            }
            else if(child != null /*is muscle*/){
                //layout child
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(rect, outline);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    private void generate() {
        cUUID = java.util.UUID.randomUUID();
        nodeCount = main.getRand().nextInt(main.getMaxNode() - 2) + 3;
        int maxMuscleCount = UsefulMaths.factorial(nodeCount) / (2 * UsefulMaths.factorial(nodeCount - 2));
        int minMuscleCount = nodeCount - 1;
        int difference = maxMuscleCount - minMuscleCount;
        muscleCount = main.getRand().nextInt(difference + 1) + minMuscleCount;
        Log.w("Creature " + cUUID.toString(), "Nodes: " + nodeCount + " Muscles: " + muscleCount);
        for (int i = 0; i < muscleCount; i++) {
            Muscle muscle = new Muscle(main);
            muscles.add(muscle);
            this.addView(muscle);
        }
        for (int i = 0; i < nodeCount; i++) {
            Node node = new Node(main);
            nodes.add(node);
            this.addView(node);
        }
    }

    public void kill() {
        alive = false;
    }

    /* Getters and Setters */
    public UUID getUUID() {
        return cUUID;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(int nodeCount) {
        this.nodeCount = nodeCount;
    }

    public int getMuscleCount() {
        return muscleCount;
    }

    public void setMuscleCount(int muscleCount) {
        this.muscleCount = muscleCount;
    }

    public boolean isLiving() {
        return alive;
    }
}
