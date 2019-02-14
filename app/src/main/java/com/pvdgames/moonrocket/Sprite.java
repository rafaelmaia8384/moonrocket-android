package com.pvdgames.moonrocket;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import static javax.microedition.khronos.opengles.GL10.GL_TEXTURE_ENV;
import static javax.microedition.khronos.opengles.GL10.GL_TEXTURE_ENV_MODE;

public class Sprite {

    private Context context;
    private GL10 gl;
    private int texId;

    private float[] shapeMatrix = new float[] {

            1f,1f,0f,
            -1f,1f,0f,
            1f,-1f,0f,
            -1f,-1f,0f,
    };

    private float[] textureMatrix = new float[] {

            1.0f, 0.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
    };

    private Vertex shape;
    private Vertex texture;

    public Sprite(Context ctx, GL10 glRef, int resource, float texWidth, float texHeight) {

        context = ctx;
        gl = glRef;

        textureMatrix[0] *= texWidth;
        textureMatrix[4] *= texWidth;
        textureMatrix[5] *= texHeight;
        textureMatrix[7] *= texHeight;

        shape = new Vertex(shapeMatrix);
        texture = new Vertex(textureMatrix);

        texId = loadTexture(resource);
    }

    private int loadTexture(int res) {

        int[] textures = new int[1];

        gl.glGenTextures(1, textures, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
        gl.glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);

        InputStream in = context.getResources().openRawResource(res);

        Bitmap image;

        try {

            image = BitmapFactory.decodeStream(in);
        }
        finally {

            try {

                in.close();
            }
            catch (IOException e ){ }
        }

        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, image, 0);

        image.recycle();

        return textures[0];
    }

    public void draw() {

        gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, shape.buffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texture.buffer);

        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, shape.vertex.length / 3);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }
}
