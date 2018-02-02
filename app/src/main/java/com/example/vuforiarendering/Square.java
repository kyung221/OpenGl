package com.example.vuforiarendering;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by alchera on 18. 2. 1.
 */

public class Square {
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    private final int mProgram;

    static final int COORDS_PER_VERTEX = 3;

    static float[] vertices = {
            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f,  0.5f, 0.0f }; // top right

    private short drawOrder[] = {0,1,2,0,2,3};
    float color[] = { 0.2f, 0.709803922f, 0.898039216f, 1.0f };

    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    private final int vertexStride = COORDS_PER_VERTEX * 4;

    public static final String vertexShaderCode = " \n" + "\n"
            + "uniform mat4 uMVPMatrix;"
            + "attribute vec4 vPosition; \n"
            + "void main() \n" + "{ \n"
            + "   gl_Position = vPosition * uMVPMatrix; \n"
            + "} \n";

    public static final String fragmentShaderCode = " \n" + "\n"
            + "precision mediump float; \n" + " \n"
            + "uniform vec4 vColor; \n" + " \n"
            + "void main() \n"
            + "{ \n" + "   gl_FragColor = vColor; \n"
            + "} \n";


    public Square() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();//convert byte to float
        vertexBuffer.put(vertices);//transfer the data into buffer
        vertexBuffer.position(0);//rewind

        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length*2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        //create empty openGL ES program
        mProgram = GLES20.glCreateProgram();
        //add the vertex shader
        GLES20.glAttachShader(mProgram, vertexShader);
        //add fragment shader
        GLES20.glAttachShader(mProgram, fragmentShader);
        //link
        GLES20.glLinkProgram(mProgram);
    }


    public void draw(float[] mvpMatrix) {
        //add program to OpenGL es environment
        GLES20.glUseProgram(mProgram);

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        mColorHandle = GLES20.glGetUniformLocation(mProgram,"vColor");
        GLES20.glUniform4fv(mColorHandle,1,color,0);
        MyGLRenderer.checkGlError("glGetUniformLocation");

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle,1,false,mvpMatrix,0);
        MyGLRenderer.checkGlError("glUniformMtrix4fv");

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT,drawListBuffer);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
