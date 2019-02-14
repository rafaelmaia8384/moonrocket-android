package com.pvdgames.moonrocket;

import android.content.Context;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import at.emini.physics2D.Body;
import at.emini.physics2D.util.FXVector;

public class ExplosionEmitter {

    private static final int MAX_PARTICLES = 200;
    private static final int MAX_TRASHES = 100;
    private static final float VELOCITY = 15f;

    private GL10 gl;

    private Body objBody;

    private Sprite spriteSmoke;
    private Sprite spriteTrash1;
    private Sprite spriteTrash2;
    private Sprite spriteTrash3;
    private Sprite spriteTrash4;

    private boolean exploding = false;

    private class Particle {

        protected float xpos;
        protected float ypos;
        protected float xvel;
        protected float yvel;
        protected int life;
        protected float size;
    }

    private class Trash {

        protected float xpos;
        protected float ypos;
        protected float xvel;
        protected float yvel;
        protected int type;
        protected float rot;
        protected float size;
        protected boolean rotNeg;
    }

    private FXVector gravity;

    private Particle[] smoke = new Particle[MAX_PARTICLES];
    private Trash[] trash = new Trash[MAX_TRASHES];

    public ExplosionEmitter(Context ctx, GL10 glRef, Body body) {

        gl = glRef;
        objBody = body;
        spriteSmoke = new Sprite(ctx, glRef, R.drawable.texsmoke, 1f, 1f);
        spriteTrash1 = new Sprite(ctx, glRef, R.drawable.texexplosion1, 1f, 1f);
        spriteTrash2 = new Sprite(ctx, glRef, R.drawable.texexplosion2, 1f, 1f);
        spriteTrash3 = new Sprite(ctx, glRef, R.drawable.texexplosion3, 1f, 1f);
        spriteTrash4 = new Sprite(ctx, glRef, R.drawable.texexplosion4, 1f, 1f);

        for (int a = 0; a < MAX_PARTICLES; a++) {

            double rad = new Random().nextInt(360);

            smoke[a] = new Particle();

            smoke[a].xpos = 0f;
            smoke[a].ypos = 0f;
            smoke[a].xvel = (float)Math.cos(Math.toRadians(rad)) * VELOCITY;
            smoke[a].yvel = (float)Math.sin(Math.toRadians(rad)) * VELOCITY;
            smoke[a].life = a;
            smoke[a].size = (float)Math.random() / 2f + 0.2f;
        }

        for (int b = 0; b < MAX_TRASHES; b++) {

            double rad = new Random().nextInt(360);

            trash[b] = new Trash();

            trash[b].type = (int)((float)Math.random() * 4.9f);

            trash[b].xpos = 0f;
            trash[b].ypos = 0f;
            trash[b].xvel = new Random().nextFloat() * (float)Math.cos(Math.toRadians(rad)) * VELOCITY;
            trash[b].yvel = (float)Math.random() * (float)Math.sin(Math.toRadians(rad)) * VELOCITY;
            trash[b].rot = new Random().nextFloat();
            trash[b].size = trash[b].rot;
            trash[b].rotNeg = Math.abs(trash[b].rot) < 0.5f;
        }
    }

    public boolean isExploding() {

        return exploding;
    }

    public void explode(FXVector currentGravity) {

        exploding = true;

        gravity = currentGravity;
    }

    public void update(float dt) {

        for (int a = 0; a < MAX_PARTICLES; a++) {

            if (smoke[a].life > 0) {

                if (exploding) {

                    smoke[a].xpos += smoke[a].xvel * dt;
                    smoke[a].ypos += smoke[a].yvel * dt;
                    smoke[a].xvel *= 0.98f;
                    smoke[a].yvel *= 0.98f;
                    smoke[a].life -= (int) (MAX_PARTICLES * dt);
                }
                else {

                    smoke[a].xpos = objBody.positionFX().xAsFloat();
                    smoke[a].ypos = objBody.positionFX().yAsFloat();
                }
            }
        }

        for (int b = 0; b < MAX_TRASHES; b++) {

            if (exploding) {

                trash[b].xvel += gravity.xAsFloat() * dt;
                trash[b].yvel += gravity.yAsFloat() * dt;
                trash[b].xpos += trash[b].xvel * dt;
                trash[b].ypos += trash[b].yvel * dt;
                trash[b].rot += 2f * dt;
            }
            else {

                trash[b].xpos = objBody.positionFX().xAsFloat();
                trash[b].ypos = objBody.positionFX().yAsFloat();
            }
        }
    }

    public void draw() {

        if (!exploding) return;

        for (int a = 0; a < MAX_PARTICLES; a++) {

            if (smoke[a].life > 0) {

                float delta = (float)Math.abs(smoke[a].life - MAX_PARTICLES)/(float)MAX_PARTICLES;

                if (smoke[a].life > MAX_PARTICLES * 0.75f) {

                    gl.glColor4f(1f, 0f, 0f, 0.8f - delta);
                }
                else if (smoke[a].life > MAX_PARTICLES * 0.55f) {

                    gl.glColor4f(1f, 1f, 0f, 0.7f - delta);
                }
                else {

                    gl.glColor4f(0.6f, 0.6f, 0.6f, 0.7f - delta);
                }

                gl.glPushMatrix();

                float scale = smoke[a].size * delta * 5f;

                gl.glTranslatef(smoke[a].xpos, smoke[a].ypos, 0f);
                gl.glScalef(scale, scale, 1f);

                spriteSmoke.draw();

                gl.glPopMatrix();
            }
        }

        gl.glColor4f(1f, 1f, 1f, 1f);

        for (int b = 0; b < MAX_TRASHES; b++) {

            gl.glPushMatrix();

            gl.glTranslatef(trash[b].xpos, trash[b].ypos, 0f);

            if (trash[b].rotNeg) {

                gl.glRotatef((float)Math.toDegrees(trash[b].rot), 0f, 0f, -1f);
            }
            else {

                gl.glRotatef((float)Math.toDegrees(trash[b].rot), 0f, 0f, 1f);
            }

            gl.glScalef(trash[b].size * 0.2f + 0.1f, trash[b].size * 0.2f + 0.1f, 1f);

            if (trash[b].type == 0) {

                spriteTrash1.draw();
            }
            else if (trash[b].type == 1) {

                spriteTrash2.draw();
            }
            else if (trash[b].type == 2) {

                spriteTrash3.draw();
            }
            else {

                spriteTrash4.draw();
            }

            gl.glPopMatrix();
        }
    }
}