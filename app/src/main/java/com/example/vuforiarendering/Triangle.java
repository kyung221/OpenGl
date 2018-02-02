package com.example.vuforiarendering;

import android.opengl.GLES20;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by alchera on 18. 2. 1.
 */

public class Triangle {
    public static final String vertexShaderCode = " \n" + "\n"
            + "uniform mat4 uMVPMatrix; \n"
            + "attribute vec4 vPosition; \n"
            + "void main() \n" + "{ \n"
            + "   gl_Position = uMVPMatrix * vPosition; \n"
            + "} \n";


    public static final String fragmentShaderCode = " \n" + "\n"
            + "precision mediump float; \n" + " \n"
            + "uniform vec4 vColor; \n" + " \n"
            + "void main() \n"
            + "{ \n" + "   gl_FragColor = vColor; \n"
            + "} \n";

    private FloatBuffer vertexBuffer; //vertex array
    private final int mProgram;

    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    static final int COORDS_PER_VERTEX = 3;

    private float[] vertices = { //시계반대방향
            0.0f,  0.622008459f, 0.0f, // top
            -0.5f, -0.311004243f, 0.0f, // bottom left
            0.5f, -0.311004243f, 0.0f  // bottom right
    };
    private final int vertexCount = vertices.length/COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    public Triangle() {
        //setup vertex-array buffer. vertices in float. a float ha 4 bytes.
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();//convert byte to float
        vertexBuffer.put(vertices);//transfer the data into buffer
        vertexBuffer.position(0);//rewind

        int vertexShader = MyGLRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER,
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

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix,0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
