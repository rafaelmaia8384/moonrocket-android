package com.pvdgames.moonrocket;

import android.content.Context;

import javax.microedition.khronos.opengles.GL10;

import at.emini.physics2D.Body;
import at.emini.physics2D.Shape;
import at.emini.physics2D.World;
import at.emini.physics2D.util.FXVector;

public class AirplaneObject extends MovingSpriteObject {

    private static final float speedX = -10f;
    private static final float width = 3f;

    private Body objBody;

    public AirplaneObject(Context ctx, GL10 glRef, World world, float posX, float posY) {

        super(ctx, glRef, R.drawable.texairplane, 3f, 1f, 1f, 1f, posX, posY, new FXVector(speedX, 0f));

        Shape shape = Shape.createRectangle(3, 1);
        shape.setMassFX(Shape.MAX_MASS_FX);

        objBody = new Body(new FXVector(posX, posY), shape, true);
        objBody.setGravityAffected(false);
        objBody.velocityFX().assign(new FXVector(speedX, 0f));

        world.addBody(objBody);
    }

    public void update(float dt) {

        super.update(dt);

        objBody.velocityFX().assign(new FXVector(speedX, 0f));

        if (objBody.positionFX().xAsFloat() < -20f - width * 0.5f) {

            float x = 20f + width * 0.5f;

            objBody.setPositionFX(new FXVector(x, objBody.positionFX().yAsFloat()));
        }
    }

    public void draw() {

        super.draw();
    }
}
