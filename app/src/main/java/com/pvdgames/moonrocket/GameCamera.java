package com.pvdgames.moonrocket;

import android.opengl.GLU;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import at.emini.physics2D.Body;

public class GameCamera {

    private static final float FIXED_OFFSET = 10f;
    private static final float DAMAGE_VELOCITY = 20f;

    private GL10 gl;
    private Body player;
    private float posY;

    public GameCamera(GL10 glRef, Body playerBody) {

        gl = glRef;
        player = playerBody;

        posY = -4f;
    }

    public void update(float dt) {

        float playerPosY = player.positionFX().yAsFloat();
        float dist = playerPosY - posY - FIXED_OFFSET;

        dist *= 0.5f;

        posY += dist * dt;
        posY += player.velocityFX().yAsFloat() * dt * 1.07f;

        if (posY < 0f) {

            posY = 0f;
        }

        float velocity = (float)Math.sqrt(player.velocityFX().lengthSquareFX());

        float damageX = 0;
        float damageY = 0;

        if (velocity > DAMAGE_VELOCITY) {

            damageX = new Random().nextFloat() * 0.2f - 0.1f;
            damageX *= velocity * 0.02f;
            damageY = (float)Math.random() * 0.2f - 0.1f;
            damageY *= velocity * 0.02f;
        }

        gl.glLoadIdentity();
        GLU.gluLookAt(gl, damageX, posY + damageY, 0f, damageX, posY + damageY, -1f, 0f, 1f, 0f);
    }
}
