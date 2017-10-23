package com.costa.alex.evolutionsimulator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Alex on 10/17/2017.
 * Creature
 */
public class Creature extends ViewGroup {

    private MainActivity main;
    private float zoom = 0.4f;

    private long ID;
    private int nodeCount;
    private int muscleCount;
    private boolean alive = true;
    private boolean simulating = false;

    private Paint outline;
    private int width, height;
    private Rect rect;

    ArrayList<Node> nodes = new ArrayList<>();
    ArrayList<Muscle> muscles = new ArrayList<>();

    ArrayList<int[]> pairs = new ArrayList<>();
    int[] pair = new int[2];

    ArrayList<Node> laidNodes = new ArrayList<>();

    float y = 0, vy = 0, x = 0, vx = 0;

    /* Constructors */
    public Creature(Context context) {
        this(context, null);
    }

    public Creature(Context context, AttributeSet attrs) {
        super(context, attrs);
        main = (MainActivity) context;
        init();
    }

    public Creature(Context context, float zoom, long ID, int nodeCount, int muscleCount, boolean alive,
                    Paint outline, int width, int height, Rect rect, ArrayList<Node> nodes, ArrayList<Muscle> muscles,
                    ArrayList<int[]> pairs, int[] pair, ArrayList<Node> laidNodes) {
        super(context);
        this.main = (MainActivity) context;
        this.zoom = zoom;
        this.ID = ID;
        this.nodeCount = nodeCount;
        this.muscleCount = muscleCount;
        this.alive = alive;
        this.outline = new Paint(outline);
        this.width = width;
        this.height = height;
        this.rect = new Rect(rect);
        this.nodes = new ArrayList<>();
        for(Node n : nodes) {
            Node copy = n.copy();
            this.nodes.add(copy);
            this.addView(copy);
        }
        this.muscles = new ArrayList<>();
        for(Muscle m : muscles) {
            Muscle copy = m.copy();
            this.muscles.add(copy);
            this.addView(copy);
        }
        this.pairs = new ArrayList<>();
        for(int[] ints : pairs) {
            this.pairs.add(ints.clone());
        }
        this.pair = new int[2];
        this.pair[0] = pair[0];
        this.pair[1] = pair[1];
        this.laidNodes = new ArrayList<>();
        for(Node n : laidNodes) {
            this.laidNodes.add(n.copy());
        }
        this.setWillNotDraw(false);
    }

    public void applyGravity(float ground) {
        vy += MainActivity.GRAVITY;
        if(y == ground) vy = 0;
        vy *= MainActivity.AIR_FRICTION;

        if(y <= ground) y += vy;
        if(y > ground) y = ground;
    }

    private void init() {
        this.setWillNotDraw(false);
        outline = new Paint();
        outline.setColor(0x8a000000);
        outline.setStyle(Paint.Style.STROKE);
        outline.setStrokeWidth(getResources().getDimension(R.dimen.creature_outline));
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
        float size = getResources().getDimension(R.dimen.creature) * zoom;
        if(!simulating) {
            setMeasuredDimension(Math.round(size), Math.round(size));
        }
        else {
            float left = size;
            float top = size;
            float right = 0;
            float bottom = 0;
            for(int i = 0;  i < nodeCount; i++) {
                left = (nodes.get(i).getCoords().left < left) ? nodes.get(i).getCoords().left : left;
                top = (nodes.get(i).getCoords().top < top) ? nodes.get(i).getCoords().top : top;
                right = (nodes.get(i).getCoords().right > right) ? nodes.get(i).getCoords().right : right;
                bottom = (nodes.get(i).getCoords().bottom > bottom) ? nodes.get(i).getCoords().bottom : bottom;
            }
            for(int i = 0; i < nodeCount; i++) {
                RectF old = nodes.get(i).getCoords();
                nodes.get(i).setCoords(old.left - left, old.top - top, old.right - left, old.bottom - top);
            }
            for(int i = 0; i < muscleCount; i++) {
                RectF old = muscles.get(i).getCoords();
                muscles.get(i).setCoords(old.left - left, old.top - top, old.right - left, old.bottom - top);
            }
            setMeasuredDimension(Math.round(right-left), Math.round(bottom-top));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        pairs.clear();
        generateFromDNA();
    }

    private void generateFromDNA() {
        for(int i = 0; i < muscleCount; i++) {
            Muscle m = muscles.get(i);
            RectF c = m.getCoords();
            m.layout(Math.round(c.left*zoom), Math.round(c.top*zoom),
                    Math.round(c.right*zoom), Math.round(c.bottom*zoom));
            m.setRotation(m.getInitialRotation());
        }
        for(int i = 0; i < nodeCount; i++) {
            Node n = nodes.get(i);
            RectF c = n.getCoords();
            n.layout(Math.round(c.left*zoom), Math.round(c.top*zoom),
                    Math.round(c.right*zoom), Math.round(c.bottom*zoom));
        }
    }

    private void generateRandomLayout(Muscle muscle, int i) {
        Log.e("TEST", "generating Random");
        //Important variables
        final float width = getResources().getDimension(R.dimen.creature);
        final float height = getResources().getDimension(R.dimen.creature);
        float mHeightF = getResources().getDimension(R.dimen.muscle_height);
        float mHeightF2 = getResources().getDimension(R.dimen.muscle_height)/2f;
        float mWidthF = muscle.getMinWidth() * muscle.getExtendedWidthFactor();
        float nDimensionF = getResources().getDimension(R.dimen.node)/2f;

        muscle.setPivotX(0f);
        muscle.setPivotY(mHeightF2);

        if(i == 0) {
            //Generate random pair for first muscle
            int a = main.getRand().nextInt(nodeCount);
            int b = main.getRand().nextInt(nodeCount);
            while (b == a) {
                b = main.getRand().nextInt(nodeCount);
                Log.e("test", "a");
            }
            muscle.setNodes(a, b);

            //Layout first muscle
            float left = muscle.getLeftPos();
            float top = muscle.getTopPos();
            float right = left+mWidthF;
            float bottom = top+mHeightF;

            muscle.setCoords(left, top, right, bottom);
            muscle.setZoom(1f);

            //Get nodes from muscle and layout n1
            Node n1 = nodes.get(muscle.getNodes()[0]);
            Node n2 = nodes.get(muscle.getNodes()[1]);
            pair[0] = muscle.getNodes()[0];
            pair[1] = muscle.getNodes()[1];

            n1.setCoords(left-nDimensionF, top-nDimensionF+mHeightF2, left+nDimensionF, bottom+nDimensionF-mHeightF2);
            n1.setAttachedMuscles(n1.getAttachedMuscles()+1);
            n1.setZoom(1f);

            //Find acceptable random muscle rotation
            float rotation;
            float eLeft;
            float eTop;
            float eRight;
            float eBottom;
            float newWidthF;
            int p = 0;
            do {
                Log.e("test", "b");
                newWidthF = mWidthF - p;
                rotation = main.getRand().nextFloat()*360f;
                float centerX = n1.getCoords().centerX();
                float centerY = n1.getCoords().centerY();
                double rad = (rotation*Math.PI)/180.0;
                float dX = (float)(newWidthF * Math.cos(rad));
                float dY = (float)(newWidthF * Math.sin(rad));
                eLeft = centerX+dX-nDimensionF;
                eTop = centerY+dY-nDimensionF;
                eRight = eLeft+(nDimensionF*2f);
                eBottom = eTop+(nDimensionF*2f);
                p++;
            }
            while(eLeft < 0 || eTop < 0 || eRight > width || eBottom > height);

            muscle.setRotation(rotation);
            muscle.setInitialRotation(rotation);
            muscle.setMinWidth(Math.round(newWidthF/muscle.getExtendedWidthFactor()));

            //Layout corresponding node
            Log.e("N2 COORDS", "" + (n1.getY()+nDimensionF) + " " + (n1.getX()+nDimensionF) + " " + eLeft + " " + eTop + " " + eRight + " " + eBottom + " " + rotation + " " + newWidthF);
            n2.setCoords(eLeft, eTop, eRight, eBottom);
            n2.setAttachedMuscles(n2.getAttachedMuscles()+1);
            n2.setZoom(1f);

            //Add objects to ArrayLists
            laidNodes.add(n1);
            laidNodes.add(n2);
            pairs.add(pair.clone());
        }
        else {
            //Get random laid node from which to start next muscle
            int nodePos = main.getRand().nextInt(laidNodes.size());
            while(nodes.get(nodes.indexOf(laidNodes.get(nodePos))).getAttachedMuscles() >= nodeCount-1) {
                nodePos = main.getRand().nextInt(laidNodes.size());
                Log.e("test", "c");
            }

            //Get random 2nd node to add at end of muscle
            int nodePos2;
            boolean duplication;
            int trueNodePos;
            do {
                Log.e("test", "d");
                duplication = false;
                trueNodePos = nodes.indexOf(laidNodes.get(nodePos));
                nodePos2 = main.getRand().nextInt(nodes.size());
                pair[0] = trueNodePos;
                pair[1] = nodePos2;
                for (int[] ints : pairs) {
                    int j = ints[0];
                    int k = ints[1];
                    if ((j == pair[0] && k == pair[1]) || (j == pair[1] && k == pair[0])) {
                        duplication = true;
                    }
                }
            }
            while(nodePos2 == trueNodePos || duplication);

            //Save pair
            pairs.add(pair.clone());
            muscle.setNodes(pair[0], pair[1]);

            //Get corresponding nodes
            Node start = laidNodes.get(nodePos);
            Node end = nodes.get(nodePos2);
            start.setAttachedMuscles(start.getAttachedMuscles()+1);
            end.setAttachedMuscles(end.getAttachedMuscles()+1);

            //If end node already exists in creature
            if(laidNodes.contains(end)) {
                float centerStartX = start.getCoords().centerX();
                float centerStartY = start.getCoords().centerY();
                float centerEndX = end.getCoords().centerX();
                float centerEndY = end.getCoords().centerY();
                float dY = centerEndY - centerStartY;
                float dX = centerEndX - centerStartX;
                float h = (float) Math.sqrt((dX*dX) + (dY*dY));
                float left = centerStartX;
                float top = centerStartY-mHeightF2;
                double rad = Math.atan2(dY, dX);
                float rotation = (float)((rad * 180.0)/Math.PI);

                muscle.setMinWidth(Math.round(h/muscle.getExtendedWidthFactor()));
                muscle.setCoords(left, top, left+h, top+mHeightF);
                muscle.setRotation(rotation);
                muscle.setInitialRotation(rotation);
                muscle.setZoom(1f);
            }
            //If end node is new node to layout
            else {
                float x = start.getCoords().left;
                float y = start.getCoords().top;
                float left = x+nDimensionF;
                float top = y+nDimensionF-mHeightF2;

                //Find acceptable random muscle rotation
                float rotation;
                float eLeft;
                float eTop;
                float eRight;
                float eBottom;
                float newWidthF;
                int p = 0;
                do {
                    Log.e("test", "e");
                    newWidthF = mWidthF - p;
                    rotation = main.getRand().nextFloat()*360f;
                    float centerX = start.getCoords().centerX();
                    float centerY = start.getCoords().centerY();
                    double rad = (rotation*Math.PI)/180.0;
                    float dX = (float)(newWidthF * Math.cos(rad));
                    float dY = (float)(newWidthF * Math.sin(rad));
                    eLeft = centerX+dX-nDimensionF;
                    eTop = centerY+dY-nDimensionF;
                    eRight = eLeft+(nDimensionF*2f);
                    eBottom = eTop+(nDimensionF*2f);
                    p++;
                }
                while(eLeft < 0 || eTop < 0 || eRight > width || eBottom > height);

                muscle.setCoords(left, top, left+mWidthF, top+mHeightF);
                muscle.setRotation(rotation);
                muscle.setInitialRotation(rotation);
                muscle.setMinWidth(Math.round(newWidthF/muscle.getExtendedWidthFactor()));
                muscle.setZoom(1f);
                end.setCoords(eLeft, eTop, eRight, eBottom);
                end.setZoom(1f);
                laidNodes.add(end);
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
        do {
            ID = main.getRand().nextLong();
        }
        while(ID < 0 || MainActivity.allID.contains(ID));
        MainActivity.allID.add(ID);
        nodeCount = main.getRand().nextInt(main.getMaxNode() - 2) + 3;
        int maxMuscleCount = UsefulMaths.factorial(nodeCount) / (2 * UsefulMaths.factorial(nodeCount - 2));
        int minMuscleCount = nodeCount - 1;
        int difference = maxMuscleCount - minMuscleCount;
        muscleCount = main.getRand().nextInt(difference + 1) + minMuscleCount;
        Log.e("Creature " + String.valueOf(ID), "Nodes: " + nodeCount + " Muscles: " + muscleCount);
        for (int i = 0; i < nodeCount; i++) {
            Node node = new Node(main);
            nodes.add(node);
            this.addView(node);
        }
        for (int i = 0; i < muscleCount; i++) {
            Muscle muscle = new Muscle(main);
            generateRandomLayout(muscle, i);
            muscles.add(muscle);
            this.addView(muscle);
        }
    }

    public void kill() {
        alive = false;
    }

    public Creature copy() {
        return new Creature(main, zoom, ID, nodeCount, muscleCount, alive, outline, width,
                height, rect, nodes, muscles, pairs, pair, laidNodes);
    }

    /* Getters and Setters */
    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
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

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public Paint getOutline() {
        return outline;
    }

    public void setOutline(Paint outline) {
        this.outline = outline;
    }

    public Rect getRect() {
        return rect;
    }

    public String getSpeciesName() {
        return "S" + nodeCount + muscleCount;
    }

    public void setSimulating(boolean simulating) {
        this.simulating = simulating;
    }

    public boolean isSimulating() {
        return this.simulating;
    }
}
