package com.pvdgames.moonrocket;

import android.content.Context;

import com.gltext.GLText;

import javax.microedition.khronos.opengles.GL10;

import at.emini.physics2D.Body;

public class GamePanel {

    private Context context;
    private GL10 gl;
    private Body objBody;
    private GLText glText;

    private float sc;

    private SpriteObject path;
    private SpriteObject earth;
    private SpriteObject moon;
    private SpriteObject rocket;

    public GamePanel(Context ctx, GL10 glRef, Body body) {

        context = ctx;
        gl = glRef;
        objBody = body;
    }

    public void loadPanel(float screenCorrection) {

        sc = screenCorrection;

        path = new SpriteObject(context, gl, R.drawable.texpath, 0.05f, 9.25f, 1f, 40f, -screenCorrection + 1.5f, 10f);
        earth = new SpriteObject(context, gl, R.drawable.texearth, 1f, 1f, 1f, 1f, -screenCorrection + 1.5f, 1.5f);
        moon = new SpriteObject(context, gl, R.drawable.texmoon, 0.8f, 0.8f, 1f, 1f, -screenCorrection + 1.5f, 18.5f);
        rocket = new SpriteObject(context, gl, R.drawable.texrockettiny, 0.35f, 0.7f, 1f, 1f, 0f, 0f);

        glText = new GLText(gl, context.getAssets());
        glText.load( "DEADCRT.ttf", 20, 2, 2);
    }

    private int ticks = 0;
    private int fps = 0;
    private long time = System.currentTimeMillis();

    public void drawPanel() {

        ticks++;

        if (System.currentTimeMillis() - time > 1000L) {

            fps = ticks;

            ticks = 0;

            time = System.currentTimeMillis();
        }

        gl.glPushMatrix();
        gl.glLoadIdentity();

        gl.glScalef(0.05f, 0.05f, 1f);

        glText.begin(1f, 0.2f, 0.2f, 1f);
        glText.draw("FPS: " + fps, -10f / 0.05f, 15f / 0.05f);
        glText.end();

        gl.glPopMatrix();

        gl.glLoadIdentity();

        path.draw();
        earth.draw();
        moon.draw();

        gl.glPushMatrix();

        gl.glTranslatef(-sc + 1.5f, 2.5f + objBody.positionFX().yAsFloat() / 100f, 0f);
        gl.glRotatef((float) Math.toDegrees(objBody.rotation2FX()), 0f, 0f, 1f);

        rocket.draw();

        gl.glPopMatrix();
    }
}
