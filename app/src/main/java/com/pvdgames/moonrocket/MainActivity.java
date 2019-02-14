package com.pvdgames.moonrocket;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;


public class MainActivity extends AppCompatActivity {

    private OpenGLRenderer openGLRenderer;

    public static Dialog loadingDialog;

    private int screenWidth;
    private int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        loadingDialog = new Dialog(this, R.style.AppTheme);
        loadingDialog.setContentView(R.layout.loading_layout);
        loadingDialog.show();

        GLSurfaceView view = new GLSurfaceView(this);
        openGLRenderer = new OpenGLRenderer(this);
        view.setRenderer(openGLRenderer);

        setContentView(view);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        int action = e.getAction();

        if (e.getPointerCount() == 1) {

            if (action == MotionEvent.ACTION_UP) {

                openGLRenderer.noTouch();
            }
            else if (action == MotionEvent.ACTION_DOWN) {

                if (e.getX() < screenWidth/2) {

                    openGLRenderer.touchLeft();
                }
                else {

                    openGLRenderer.touchRight();
                }
            }
        }
        else {

            openGLRenderer.doubleTouch();
        }

        return true;
    }
}
