package com.pvdgames.moonrocket;

import android.content.Context;

import javax.microedition.khronos.opengles.GL10;

import at.emini.physics2D.util.FXVector;

public class MovingSpriteObject extends SpriteObject {

    private float width;

    private FXVector moveSpeed;

    public MovingSpriteObject(Context ctx, GL10 glRef, int texture, float width, float height, float texWidth, float texHeight, float posX, float posY, FXVector moveSpeed) {

        super(ctx, glRef, texture, width, height, texWidth, texHeight, posX, posY);

        this.width = width;

        this.moveSpeed = moveSpeed;
    }

    public void update(float dt) {

        float xPos = super.getPosX() + moveSpeed.xAsFloat() * dt;
        float yPos = super.getPosY() + moveSpeed.yAsFloat() * dt;

        if (moveSpeed.xAsFloat() > 0f) {

            if (xPos > 20f + width * 0.5f) {

                xPos = -20f - width * 0.5f;
            }
        }
        else {

            if (xPos < -20f - width * 0.5f) {

                xPos = 20f + width * 0.5f;
            }
        }

        super.setPosX(xPos);
        super.setPosY(yPos);
    }
}
