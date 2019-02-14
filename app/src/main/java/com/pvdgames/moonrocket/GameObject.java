package com.pvdgames.moonrocket;

public class GameObject {

    private int texture;
    private float objWidth;
    private float objHeight;
    private float texWidth;
    private float texHeight;

    public GameObject(int texId, float objectWidth, float objectHeight, float textureWidth, float textureHeight) {

        texture = texId;
        objWidth = objectWidth;
        objHeight = objectHeight;
        texWidth = textureWidth;
        texHeight = textureHeight;
    }

    public int getTexId() {

        return texture;
    }

    public float getObjWidth() {

        return objWidth;
    }

    public float getObjHeight() {

        return objHeight;
    }

    public float getTexWidth() {

        return texWidth;
    }

    public float getTexHeight() {

        return texHeight;
    }
}
