package com.pvdgames.moonrocket;

import android.content.Context;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import at.emini.physics2D.Body;

public class SmokeEmitter {

    private static final int MAX_PARTICLES = 200;
    private static final float VELOCITY = 15f;

    private GL10 gl;

    private Body objBody;
    private Sprite spriteSmoke;

    private boolean emitting = false;

    private class Particle {

        protected float xpos;
        protected float ypos;
        protected float xvel;
        protected float yvel;
        protected int life;
        protected float size;
    }

    private Particle[] smoke = new Particle[MAX_PARTICLES];

    public SmokeEmitter(Context ctx, GL10 glRef, Body body) {

        gl = glRef;
        objBody = body;
        spriteSmoke = new Sprite(ctx, glRef, R.drawable.texsmoke, 1f, 1f);

        for (int a = 0; a < MAX_PARTICLES; a++) {

            smoke[a] = new Particle();

            smoke[a].xpos = -10f;
            smoke[a].ypos = -10f;
            smoke[a].xvel = 0f;
            smoke[a].yvel = 0f;
            smoke[a].life = a;
            smoke[a].size = new Random().nextFloat() / 2f + 0.2f;
        }
    }

    public void emit() {

        emitting = true;
    }

    public void stop() {

        emitting = false;
    }

    public void update(float dt) {

        for (int a = 0; a < MAX_PARTICLES; a++) {

            if (smoke[a].life < 0) {

                if (emitting) {

                    float delta = (new Random().nextFloat() * 2 - 1)/15f;

                    smoke[a].xpos = objBody.positionFX().xAsFloat() + (float)Math.sin(objBody.rotation2FX()) * 1.5f;
                    smoke[a].ypos = objBody.positionFX().yAsFloat() - (float)Math.cos(-objBody.rotation2FX()) * 1.5f;
                    smoke[a].xvel = (float)Math.sin(objBody.rotation2FX() + delta) * VELOCITY * dt;
                    smoke[a].xvel += objBody.velocityFX().xAsFloat() * dt;
                    smoke[a].yvel = -(float)(Math.cos(objBody.rotation2FX() + delta) * VELOCITY * dt);
                    smoke[a].yvel += objBody.velocityFX().yAsFloat() * dt;
                    smoke[a].life = new Random().nextInt(MAX_PARTICLES);
                }
            }
            else {

                smoke[a].xpos += smoke[a].xvel;
                smoke[a].ypos += smoke[a].yvel;
                smoke[a].xvel *= 0.96f;
                smoke[a].yvel *= 0.96f;
                smoke[a].life -= (int)(MAX_PARTICLES * dt);
            }
        }
    }

    public void draw() {

        for (int a = 0; a < MAX_PARTICLES; a++) {

            if (smoke[a].life > 0) {

                float delta = (float)Math.abs(smoke[a].life - MAX_PARTICLES)/(float)MAX_PARTICLES;

                if (smoke[a].life > MAX_PARTICLES * 0.85f) {

                    gl.glColor4f(1f, 0.2f, 0f, 0.8f - delta);
                }
                else if (smoke[a].life > MAX_PARTICLES * 0.75f) {

                    gl.glColor4f(1f, 1f, 0f, 0.7f - delta);
                }
                else {

                    gl.glColor4f(0.6f, 0.6f, 0.6f, 0.6f - delta);
                }

                gl.glPushMatrix();

                float scale = smoke[a].size * delta * 4f;

                gl.glTranslatef(smoke[a].xpos, smoke[a].ypos, 0f);
                gl.glScalef(scale, scale, 1f);
                spriteSmoke.draw();

                gl.glPopMatrix();
            }
        }

        gl.glColor4f(1f, 1f, 1f, 1f);
    }
}