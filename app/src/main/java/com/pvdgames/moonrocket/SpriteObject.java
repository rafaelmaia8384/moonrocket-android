package com.pvdgames.moonrocket;

import android.content.Context;

import javax.microedition.khronos.opengles.GL10;

public class SpriteObject extends Sprite {

    private GL10 gl;

    private float objWidth;
    private float objHeight;
    private float posX;
    private float posY;

    public SpriteObject(Context ctx, GL10 glRef, int texture, float width, float height, float texWidth, float texHeight, float posX, float posY) {

        super(ctx, glRef, texture, texWidth, texHeight);

        gl = glRef;

        objWidth = width;
        objHeight = height;
        this.posX = posX;
        this.posY = posY;
    }

    public float getPosX() {

        return this.posX;
    }

    public float getPosY() {

        return this.posY;
    }

    public void setPosX(float posX) {

        this.posX = posX;
    }

    public void setPosY(float posY) {

        this.posY = posY;
    }

    public void draw() {

        gl.glPushMatrix();

        gl.glTranslatef(posX, posY, 0f);
        gl.glScalef(objWidth, objHeight, 1f);

        super.draw();

        gl.glPopMatrix();
    }
}
