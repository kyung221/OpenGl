package com.example.vuforiarendering;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * Created by alchera on 18. 2. 1.
 */

public class MyGLSurfaceView extends GLSurfaceView {
    private final MyGLRenderer mRenderer;
    private final float TOUCH_SCALE_FACTOR = 180.0f/320;
    private float mPreviousX;
    private float mPreviousY;

    public MyGLSurfaceView(Context context) {
        super(context);

        //create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mRenderer = new MyGLRenderer();

        setRenderer(mRenderer);
        //drawing data가 변할때 render
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
    @Override
    public boolean onTouchEvent(MotionEvent e) {
    float x = e.getX();
    float y = e.getY();

    switch(e.getAction()){
        case MotionEvent.ACTION_MOVE:

            float dx = x-mPreviousX;
            float dy = y-mPreviousY;

            if(y>getHeight()/2){
                dx = dx*-1;
            }
            if (x<getWidth() /2){
                dy=dy*-1;
            }
       mRenderer.setAngle(mRenderer.getAngle()+((dx+dy)*TOUCH_SCALE_FACTOR));
       requestRender();
    }
    mPreviousX = x;
    mPreviousY = y;
    return true;
    }
}