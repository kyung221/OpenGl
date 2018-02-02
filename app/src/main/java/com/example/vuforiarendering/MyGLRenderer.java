package com.example.vuforiarendering;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.vuforiarendering.SampleApplication.Cube;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by alchera on 18. 2. 1.
 * OpenGl Custom renderer used with GLSurfaceView
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";
    private Triangle triangle;
    private Square square;
    private Cube mCube;
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];

    public float mAngle;
    //called once to set up the view's OpenGL ES environmnet
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f, 0.0f,0.0f,1.0f);
        triangle = new Triangle();
        square = new Square();
        mCube = new Cube();
    }

    //called if the geometry of the view changes,ex.orientation change
    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        //set camera position
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3,
                0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix,0,mProjectionMatrix,0,mViewMatrix,0);
        mCube.draw(mMVPMatrix);

//        Matrix.setRotateM(mRotationMatrix, 0, mAngle,0,0,1.0f);
//
//        Matrix.multiplyMM(scratch,0,mMVPMatrix,0,mRotationMatrix,0);
//
//        mCube.draw(scratch);


    }
    //called for each redraw of the view
    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0,0,width,height);

        float ratio = (float)width/height;
        Matrix.frustumM(mProjectionMatrix,0,-ratio,ratio,-1,1,3,7);
    }


    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
    public float getAngle(){
        return mAngle;
    }
    public void setAngle(float angle){
        mAngle = angle;
    }
}
