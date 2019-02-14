package com.pvdgames.moonrocket;


import android.content.Context;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLRenderer implements GLSurfaceView.Renderer {

    private Context context;
    private GameWorld gameWorld;

    private boolean loading = true;

    public OpenGLRenderer(Context ctx) {

        context = ctx;
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        gl.glClearColor(0.7f, 0.7f, 1f, 0.0f);

        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glCullFace(GL10.GL_BACK);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        gameWorld = new GameWorld(context, gl, GameWorld.MODE_HARD);

        MainActivity.loadingDialog.dismiss();
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {

        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        float correction;

        if (width > height) {

            correction = (float)width/(float)height;
        }
        else if (height > width) {

            correction = (float)height/(float)width;
        }
        else {

            correction = 1f;
        }

        correction *= 10f;

        gl.glOrthof(-correction, correction, 0f, 20f, -1f, 1f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        gameWorld.limitSimulationArea(correction);

        loading = false;
    }

    public void noTouch() {

        if (loading) return;

        gameWorld.noTouch();
    }

    public void touchLeft() {

        if (loading) return;

        gameWorld.turnLeft();
    }

    public void touchRight() {

        if (loading) return;

        gameWorld.turnRight();
    }

    public void doubleTouch() {

        if (loading) return;

        gameWorld.accelerate();
    }

    public void onDrawFrame(final GL10 gl) {

        gameWorld.simulate();
    }
}
