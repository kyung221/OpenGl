package com.example.vuforiarendering.SampleApplication;

import android.opengl.GLES20;

import com.example.vuforiarendering.MyGLRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by alchera on 18. 2. 2.
 */

public class Cube {
    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private ShortBuffer drawListBuffer;
    private FloatBuffer normalBuffer;

    static final int COORDS_PER_VERTEX = 3;
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    public static final String vertexShaderCode = " \n" + "\n"
            + "uniform mat4 uMVPMatrix;"
            + "attribute vec4 vPosition; \n"
            + "attribute vec4 aColor; \n"
            + "varying vec4 vColor; \n"
            + "attribute vec3 vNormal; \n"
            + "void main() \n" + "{ \n"
            + "   vColor = aColor;"
            + "   gl_Position = vPosition * uMVPMatrix; \n"
            + "} \n";

    public static final String fragmentShaderCode = " \n" + "\n"
            + "precision mediump float; \n" + " \n"
            + "varying vec4 vColor; \n" + " \n"
            + "void main() \n"
            + "{ \n" + "   gl_FragColor = vColor; \n"
            + "} \n";

    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    private int mNormalHandle;
    private final int mProgram;

    static float[] vertices = {
            -1.00f, -1.00f, 1.00f, // front
            1.00f, -1.00f, 1.00f,
            1.00f, 1.00f, 1.00f,
            -1.00f, 1.00f, 1.00f,

            -1.00f, -1.00f, -1.00f, // back
            1.00f, -1.00f, -1.00f,
            1.00f, 1.00f, -1.00f,
            -1.00f, 1.00f, -1.00f,

            -1.00f, -1.00f, -1.00f, // left
            -1.00f, -1.00f, 1.00f,
            -1.00f, 1.00f, 1.00f,
            -1.00f, 1.00f, -1.00f,

            1.00f, -1.00f, -1.00f, // right
            1.00f, -1.00f, 1.00f,
            1.00f, 1.00f, 1.00f,
            1.00f, 1.00f, -1.00f,

            -1.00f, 1.00f, 1.00f, // top
            1.00f, 1.00f, 1.00f,
            1.00f, 1.00f, -1.00f,
            -1.00f, 1.00f, -1.00f,

            -1.00f, -1.00f, 1.00f, // bottom
            1.00f, -1.00f, 1.00f,
            1.00f, -1.00f, -1.00f,
            -1.00f, -1.00f, -1.00f
    };

    //정점 배열의 정점 인덱스를 이용하여 각 면마다 2개의 삼각형(ccw)를 구성
    static short[] indices={
            0, 1, 2, 0, 2, 3, // front
            4, 6, 5, 4, 7, 6, //back
            8, 9, 10, 8, 10, 11, //left
            12, 14, 13, 12, 15, 14, //right
            16, 17, 18, 16, 18, 19, //top
            20, 22, 21, 20, 23, 22//bottom
    };

    float[] colors = {
            0.0f,  1.0f,  0.0f,  1.0f,
            0.0f,  1.0f,  0.0f,  1.0f,
            1.0f,  0.5f,  0.0f,  1.0f,
            1.0f,  0.5f,  0.0f,  1.0f,
            1.0f,  0.0f,  0.0f,  1.0f,
            1.0f,  0.0f,  0.0f,  1.0f,
            0.0f,  0.0f,  1.0f,  1.0f,
            1.0f,  0.0f,  1.0f,  1.0f
    };

   float []normals = {
            0, 0, 1,  0, 0, 1,  0, 0, 1,  0, 0, 1,

            0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1,

            -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0,

            1, 0, 0,  1, 0, 0,  1, 0, 0,  1, 0, 0,

            0, 1, 0,  0, 1, 0,  0, 1, 0,  0, 1, 0,

            0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0,
    };

    public Cube() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();//convert byte to float
        vertexBuffer.put(vertices);//transfer the data into buffer
        vertexBuffer.position(0);//rewind

        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length*2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);

        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer = cbb.asFloatBuffer();//convert byte to float
        colorBuffer.put(colors);//transfer the data into buffer
        colorBuffer.position(0);//rewind

        ByteBuffer nb = ByteBuffer.allocateDirect(normals.length * 4);
        nb.order(ByteOrder.nativeOrder());
        normalBuffer = nb.asFloatBuffer();
        normalBuffer.put(normals);
        normalBuffer.position(0);

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

        mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glVertexAttribPointer(mColorHandle, 3, GLES20.GL_FLOAT,
                false, 0, colorBuffer);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");


//
//        mNormalHandle = GLES20.glGetAttribLocation(mProgram,"vNormal");
//        GLES20.glEnableVertexAttribArray(mNormalHandle);
//        GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT,
//                false, 0, normalBuffer);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle,1,false,mvpMatrix,0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 36,
                GLES20.GL_UNSIGNED_SHORT,drawListBuffer);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mColorHandle);
    }
}
