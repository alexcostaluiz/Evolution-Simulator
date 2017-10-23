package com.costa.alex.evolutionsimulator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Alex on 10/17/2017.
 * Muscles of a Creature
 */
public class Muscle extends View {

    private MainActivity main;

    public static float DEFAULT_EXTENDED_WIDTH_FACTOR = 2f;
    public static int MINIMUM_WIDTH = 100;

    private float zoom = 1f;

    private float extendedWidthFactor = DEFAULT_EXTENDED_WIDTH_FACTOR;
    private int minWidth;
    private float shrinkTime, stretchTime;
    private float initialRotation;
    private int left, top;
    private int width, height;
    private int[] nodes = {-1, -1};
    private RectF coords = new RectF(-1f, -1f, -1f, -1f);

    private Paint backgroundPaint;
    private Rect rect = new Rect(0, 0, 0, 0);

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

    public Muscle(Context context, float zoom, float extendedWidthFactor, int minWidth, float shrinkTime,
                  float stretchTime, float initialRotation, int left, int top, int width, int height,
                  int[] nodes, RectF coords, Paint backgroundPaint, Rect rect, float pivotX, float pivotY) {
        super(context);
        this.main = (MainActivity) context;
        this.zoom = zoom;
        this.extendedWidthFactor = extendedWidthFactor;
        this.minWidth = minWidth;
        this.shrinkTime = shrinkTime;
        this.stretchTime = stretchTime;
        this.initialRotation = initialRotation;
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
        this.nodes = new int[2];
        this.nodes[0] = nodes[0];
        this.nodes[1] = nodes[1];
        this.coords = new RectF(coords);
        this.backgroundPaint = new Paint(backgroundPaint);
        this.rect = new Rect(rect);
        this.setPivotX(pivotX);
        this.setPivotY(pivotY);
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
        extendedWidthFactor = main.getRand().nextFloat()+1f;
        minWidth = main.getRand().nextInt(40)+MINIMUM_WIDTH;
        float test = main.getRand().nextFloat();
        shrinkTime = (test <= .5f) ? test : test/2; //TODO: this is wrong
        stretchTime = 1 - shrinkTime;
        initialRotation = main.getRand().nextFloat()*360;
        int parentWidth = Math.round(getResources().getDimension(R.dimen.creature));
        int nodeWidth = Math.round(getResources().getDimension(R.dimen.node)/2f);
        int height = Math.round(getResources().getDimension(R.dimen.muscle_height)/2f);
        int nLeft;
        int nRight;
        do {
            left = main.getRand().nextInt(parentWidth);
            nLeft = left - nodeWidth;
            nRight = left + nodeWidth;
        }
        while(nLeft < 0 || nRight > parentWidth);

        int nTop;
        int nBottom;
        do {
            top = main.getRand().nextInt(parentWidth);
            nTop = top - nodeWidth + height;
            nBottom = top + nodeWidth + height;
        }
        while(nTop < 0 || nBottom > parentWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
        rect.set(0, 0, width, height);
    }

    /* Draw */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(rect, backgroundPaint);
    }

    public Muscle copy() {
        return new Muscle(main, zoom, extendedWidthFactor, minWidth, shrinkTime, stretchTime,
                initialRotation, left, top, width, height, nodes, coords, backgroundPaint, rect,
                getPivotX(), getPivotY());
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

    public int[] getNodes() {
        return nodes;
    }

    public void setNodes(int n1, int n2) {
        this.nodes[0] = n1;
        this.nodes[1] = n2;
    }

    public float getInitialRotation() {
        return initialRotation;
    }

    public void setInitialRotation(float initialRotation) {
        this.initialRotation = initialRotation;
    }

    public int getLeftPos() {
        return left;
    }

    public int getTopPos() {
        return top;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public int getMinWidth() {
        return this.minWidth;
    }

    public RectF getCoords() {
        return coords;
    }

    public void setCoords(float left, float top, float right, float bottom) {
        this.coords.set(left, top, right, bottom);
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public float getZoom() {
        return this.zoom;
    }
}
