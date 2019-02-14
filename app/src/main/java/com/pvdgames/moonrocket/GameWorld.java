package com.pvdgames.moonrocket;

import android.content.Context;
import android.os.SystemClock;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import at.emini.physics2D.Body;
import at.emini.physics2D.Contact;
import at.emini.physics2D.Shape;
import at.emini.physics2D.World;
import at.emini.physics2D.util.FXVector;

public class GameWorld extends World {

    public static final int MODE_EASY = 0;
    public static final int MODE_MEDIUM = 1;
    public static final int MODE_HARD = 2;

    private static final int MAX_CLOUDS1 = 20;
    private static final int MAX_CLOUDS2 = 20;

    private static final FXVector[] VECTORS_ROCKET = {

            new FXVector(0f, 2f),
            new FXVector(0.5f, 1f),
            new FXVector(1f, -1f),
            new FXVector(0.8f, -2f),
            new FXVector(-0.8f, -2f),
            new FXVector(-1f, -1f),
            new FXVector(-0.5f, 1f)
    };

    private Context context;
    private GL10 gl;

    private float contactImpulse;

    private long startTime = SystemClock.uptimeMillis();

    private SpriteObject spriteForest;
    private SpriteObject spriteGround;

    private MovingSpriteObject[] clouds1 = new MovingSpriteObject[MAX_CLOUDS1];
    private MovingSpriteObject[] clouds2 = new MovingSpriteObject[MAX_CLOUDS2];

    private AirplaneObject airplane1;

    private RocketObject rocket;

    private GamePanel gamePanel;
    private GameCamera camera;

    private GameSound gameSound;

    public GameWorld(Context ctx, GL10 glRef, int gameMode) {

        context = ctx;
        gl = glRef;

        if (gameMode == MODE_EASY) {

            contactImpulse = 6f;
        }
        else if (gameMode == MODE_MEDIUM) {

            contactImpulse = 5f;
        }
        else {

            contactImpulse = 6f;
        }

        super.setGravity(new FXVector(0f, -9.8f));

        spriteForest = new SpriteObject(context, gl, R.drawable.texforest, 20f, 4f, 1f, 1f, 0f, 4f);
        spriteGround = new SpriteObject(context, gl, R.drawable.texground, 20f, 4f, 4f, 1f, 0f, 0f);

        for (int a = 0; a < MAX_CLOUDS1; a++) {

            float width = ((float)(a + 1)/(float)MAX_CLOUDS1) * 6f + 1f;
            float xSpeed = width * 0.1f;
            float posX = new Random().nextFloat() * 30f - 15f;
            float posY = (float)Math.random() * (float)MAX_CLOUDS1 + 15f;

            clouds1[a] = new MovingSpriteObject(context, gl, R.drawable.texcloud, width, width * 0.5f, 1f, 1f, posX, posY, new FXVector(xSpeed, 0f));
        }

        for (int a = 0; a < MAX_CLOUDS2; a++) {

            float width = ((float)(a + 1)/(float)MAX_CLOUDS1) * 6f + 10f;
            float xSpeed = width * 0.1f;
            float posX = new Random().nextFloat() * 30f - 15f;
            float posY = (float)Math.random() * (float)MAX_CLOUDS1 * 2f + 40f;

            clouds2[a] = new MovingSpriteObject(context, gl, R.drawable.texcloud, width, width * 0.5f, 1f, 1f, posX, posY, new FXVector(xSpeed, 0f));
        }

        airplane1 = new AirplaneObject(context, gl, this, 15f, 15f);

        Shape groundShape = new Shape(new FXVector[]{ new FXVector(20f, 2f), new FXVector(20f, -2f), new FXVector(-20f, -2f), new FXVector(-20f, 2f)});

        groundShape.setFrictionFX(0.7f);
        groundShape.setElasticityFX(0.6f);

        Body groundBody = new Body(new FXVector(0f, 0f), groundShape, false);

        Shape rocketShape = new Shape(VECTORS_ROCKET);
        rocket = new RocketObject(context, gl, rocketShape, 1f, 2f, 1f, 1f, 0f, 6f);

        super.addBody(groundBody);
        super.addBody(rocket);

        gamePanel = new GamePanel(ctx, gl, rocket);
        camera = new GameCamera(gl, rocket);

        gameSound = new GameSound(context);
    }

    public void limitSimulationArea(float limit) {

        gamePanel.loadPanel(limit);

        rocket.setScreenLimit(limit);
        this.setSimulationArea(-(int)(limit+5), (int)limit+5);
    }

    public void noTouch() {

        rocket.noTouch();

        gameSound.stopTurbine();
    }

    public void turnLeft() {

        rocket.turnLeft();
    }

    public void turnRight() {

        rocket.turnRight();
    }

    public void accelerate() {

        rocket.accelerate();

        if (rocket.isAccelerating()) {

            gameSound.startTurbine();
        }
    }

    public void simulate() {

        long endTime = SystemClock.uptimeMillis();

        long dt = endTime - startTime;

        if (dt < 16) {

            return;
        }

        updateWorld(dt);
        drawObjects();

        startTime = endTime;
    }

    private void updateWorld(long dt) {

        float elapsedTime = dt/1000f;

        for (int a = 0; a < MAX_CLOUDS1; a++) {

            clouds1[a].update(elapsedTime);
        }

        for (int a = 0; a < MAX_CLOUDS2; a++) {

            clouds2[a].update(elapsedTime);
        }

        airplane1.update(elapsedTime);

        rocket.update(elapsedTime);

        if (this.getContactCount() > 0) {

            Contact[] contacts = this.getContacts();

            for (int a = 0; a < contacts.length; a++) {

                if (contacts[a] != null) {

                    float totalImpulse = contacts[a].getImpulseContact1FX() + contacts[a].getImpulseContact2FX();

                    if (Math.abs(totalImpulse) > contactImpulse) {

                        rocket.collisionBegin(this.getGravity());

                        gameSound.explode();
                    }
                }
            }
        }
        else {

            rocket.collisionEnd();
        }

        camera.update(elapsedTime);

        super.setTimestepFX(elapsedTime);
        super.tick();
    }

    private void drawObjects() {

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        spriteForest.draw();
        spriteGround.draw();

        for (int a = 0; a < MAX_CLOUDS1; a++) {

            clouds1[a].draw();
        }

        airplane1.draw();

        rocket.draw();

        for (int a = 0; a < MAX_CLOUDS2; a++) {

            gl.glColor4f(1f, 1f, 1f, 0.5f);

            clouds2[a].draw();

            gl.glColor4f(1f, 1f, 1f, 1f);
        }

        gamePanel.drawPanel();
    }
}
