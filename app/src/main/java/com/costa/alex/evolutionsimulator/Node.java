package com.costa.alex.evolutionsimulator;

import android.animation.ArgbEvaluator;
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
 * Node of a Creature
 */
public class Node extends View {

    private MainActivity main;

    public static float DEFAULT_FRICTION = 1f;

    private float zoom = 1f;

    private float friction = DEFAULT_FRICTION;
    private int width, height;
    private float radius;
    private Paint backgroundPaint;
    private int attachedMuscles = 0;
    private RectF coords = new RectF(-1f, -1f, -1f, -1f);

    public float x, y, vx, vy, ax, ay;

    /* Constructors */
    public Node(Context context) {
        this(context, null);
    }

    public Node(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Node(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Node(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        main = (MainActivity) context;
        init();
    }

    public Node(Context context, float friction) {
        super(context);
        this.friction = friction;
        backgroundPaint = new Paint();
        backgroundPaint.setColor((Integer)new ArgbEvaluator().evaluate((this.friction-.9f)*10, 0xffe08e79, 0xffffffff));
        backgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        backgroundPaint.setStrokeWidth(2f);
        this.setZ(1f);
    }

    public Node(Context context, float zoom, float friction, int width, int height, float radius,
                Paint backgroundPaint, int attachedMuscles, RectF coords) {
        super(context);
        this.main = (MainActivity) context;
        this.zoom = zoom;
        this.friction = friction;
        this.width = width;
        this.height = height;
        this.radius = radius;
        this.backgroundPaint = new Paint(backgroundPaint);
        this.attachedMuscles = attachedMuscles;
        this.coords = new RectF(coords);
        this.setZ(1f);
    }

    /* Init */
    private void init() {
        friction = (main.getRand().nextFloat()/10f) + .9f;
        backgroundPaint = new Paint();
        backgroundPaint.setColor((Integer)new ArgbEvaluator().evaluate((friction-.9f)*10, 0xffe08e79, 0xffffffff));
        backgroundPaint.setStyle(Paint.Style.FILL);
        this.setZ(1f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
        radius = Math.min(w, h)*0.5f;
    }

    /* Draw */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(width/2f, height/2f, radius, backgroundPaint);
    }

    public Node copy() {
        return new Node(main, zoom, friction, width, height, radius, backgroundPaint, attachedMuscles, coords);
    }

    /* Simulation Methods */
    void applyForces(float ground) {
        vy += MainActivity.GRAVITY;
        if(y == ground) vy = 0;
        vy *= MainActivity.AIR_FRICTION;
        if (x == 0) vx = 0f;
        vx *= MainActivity.AIR_FRICTION;
        if(y == ground) vx *= friction; //kinetic friction
        x += vx;
        if(y <= ground) y += vy;
        y = (y > ground) ? ground : y;
        Log.e("DIMENSIONS", "" + vx + " : " + x + " : " + y);
    }

    void setInitialVariables() {
        x = coords.left;
        y = coords.top;
    }

    /* Getters and Setters */
    public float getFriction() {
        return this.friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public int getAttachedMuscles() {
        return this.attachedMuscles;
    }

    public void setAttachedMuscles(int attachedMuscles) {
        this.attachedMuscles = attachedMuscles;
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
