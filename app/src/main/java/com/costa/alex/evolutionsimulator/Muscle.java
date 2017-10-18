package com.costa.alex.evolutionsimulator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Alex on 10/17/2017.
 * Muscles of a Creature
 */
public class Muscle extends View {

    private MainActivity main;

    public static float DEFAULT_EXTENDED_WIDTH_FACTOR = 2f;
    public static int MINIMUM_WIDTH = 200;

    private float extendedWidthFactor = DEFAULT_EXTENDED_WIDTH_FACTOR;
    private float shrinkTime, stretchTime;
    private int width, height;
    private Node[] nodes = new Node[2];

    private Paint backgroundPaint;
    private Rect rect;

    /* Constructors */
    public Muscle(Context context) {
        this(context, null);
    }

    public Muscle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Muscle(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Muscle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        main = (MainActivity) context;
        init();
    }

    /* Init */
    private void init() {
        generate();
        backgroundPaint = new Paint();
        backgroundPaint.setColor(0xff774f38);
        backgroundPaint.setAlpha(Math.round(255*(extendedWidthFactor-1)));
        backgroundPaint.setStyle(Paint.Style.FILL);
    }

    private void generate() {
        extendedWidthFactor = main.getRand().nextFloat()+1;
        this.setMinimumWidth(main.getRand().nextInt(201)+MINIMUM_WIDTH);
        float test = main.getRand().nextFloat();
        shrinkTime = (test <= .5f) ? test : test/2;
        stretchTime = 1 - shrinkTime;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
        rect  = new Rect(0, 0, width, height);
    }

    /* Draw */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(rect, backgroundPaint);
    }

    /* Getters and Setters */
    public float getExtendedWidthFactor() {
        return extendedWidthFactor;
    }

    public float getShrinkTime() {
        return shrinkTime;
    }

    public float getStretchTime() {
        return stretchTime;
    }

    public Node[] getNodes() {
        return nodes;
    }

    public void setNodes(Node[] nodes) {
        this.nodes = nodes;
    }
}
