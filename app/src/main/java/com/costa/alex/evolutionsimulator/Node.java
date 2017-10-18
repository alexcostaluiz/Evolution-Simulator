package com.costa.alex.evolutionsimulator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Alex on 10/17/2017.
 * Node of a Creature
 */
public class Node extends View {

    private MainActivity main;

    public static float DEFAULT_FRICTION = 1f;

    private float friction = DEFAULT_FRICTION;

    private int width, height;
    private float radius;

    private Paint backgroundPaint;

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

    /* Init */
    private void init() {
        friction = main.getRand().nextFloat();
        backgroundPaint = new Paint();
        backgroundPaint.setColor(0xffe08e79);
        backgroundPaint.setAlpha(Math.round(255*friction));
        backgroundPaint.setStyle(Paint.Style.FILL);
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

    /* Getters and Setters */
    public float getFriction() {
        return this.friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }
}
