package com.example.vuforiarendering;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    private MyGLSurfaceView mGLView;//use GLSurface View

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLView = new MyGLSurfaceView(this); //allocate a GLSurfaceView
        setContentView(mGLView);
    }



    //activity가 background로 갔을때 call back
    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    //onPause후 call back
    @Override
    protected void onResume(){
        super.onResume();
        mGLView.onResume();
    }
}
