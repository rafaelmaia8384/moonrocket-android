package com.pvdgames.moonrocket;

import android.content.Context;

import javax.microedition.khronos.opengles.GL10;

import at.emini.physics2D.Body;
import at.emini.physics2D.Shape;
import at.emini.physics2D.util.FXVector;

public class RocketObject extends Body {

    private static final float ACCELERATION = 20f;
    private static final float ROTATION = 3f;
    private static final float TOTAL_FUEL = 10f;

    private GL10 gl;

    private float objWidth;
    private float objHeight;

    private boolean turnLeft = false;
    private boolean turnRight = false;
    private boolean accelerate = false;

    private float currFuel = TOTAL_FUEL;

    private boolean colliding = false;

    private float screenLimit = 10f;

    private Sprite sprite;

    private SmokeEmitter smoke;
    private ExplosionEmitter explosion;

    public RocketObject(Context ctx, GL10 glRef, Shape shape, float objWidth, float objHeight, float texWidth, float texHeight, float xPos, float yPos) {

        super(new FXVector(xPos, yPos), shape, true);

        gl = glRef;

        this.objWidth = objWidth;
        this.objHeight = objHeight;

        sprite = new Sprite(ctx, glRef, R.drawable.texrocket, texWidth, texHeight);

        smoke = new SmokeEmitter(ctx, glRef, this);
        explosion = new ExplosionEmitter(ctx, glRef, this);
    }

    public boolean isAccelerating() {

        return !explosion.isExploding();
    }

    public void noTouch() {

        turnLeft = false;
        turnRight = false;
        accelerate = false;
    }

    public void turnLeft() {

        turnLeft = true;
        turnRight = false;
    }

    public void turnRight() {

        turnRight = true;
        turnLeft = false;
    }

    public void accelerate() {

        accelerate = true;
    }

    public void setScreenLimit(float limit) {

        screenLimit = limit;
    }

    public void collisionBegin(FXVector currentGravity) {

        colliding = true;
        explosion.explode(currentGravity);
    }

    public void collisionEnd() {

        colliding = false;
    }

    public void update(float dt) {

        smoke.update(dt);
        explosion.update(dt);

        if (accelerate) {

            if (currFuel > 0.0001f) {

                FXVector vector = new FXVector((float)Math.sin(-this.rotation2FX()), (float)Math.cos(-this.rotation2FX()));

                this.applyForce(vector, ACCELERATION * dt);

                smoke.emit();

                currFuel -= dt;
            }
            else {

                smoke.stop();
            }
        }
        else {

            smoke.stop();

            if (turnLeft) {

                this.angularVelocity2FX(this.angularVelocity2FX() - ROTATION * dt);
            }
            else if (turnRight) {

                this.angularVelocity2FX(this.angularVelocity2FX() + ROTATION * dt);
            }
        }

        if (this.positionFX().xAsFloat() < -screenLimit) {

            this.setPositionFX(new FXVector(screenLimit, this.positionFX().yAsFloat()));
        }
        else if (this.positionFX().xAsFloat() > screenLimit) {

            this.setPositionFX(new FXVector(-screenLimit, this.positionFX().yAsFloat()));
        }
    }

    public void draw() {

        explosion.draw();

        if (!colliding) {

            smoke.draw();

            gl.glPushMatrix();

            gl.glTranslatef(super.positionFX().xAsFloat(), super.positionFX().yAsFloat(), 0f);
            gl.glRotatef((float) Math.toDegrees(super.rotation2FX()), 0f, 0f, 1f);
            gl.glScalef(objWidth, objHeight, 1f);

            sprite.draw();

            gl.glPopMatrix();
        }
    }
}
