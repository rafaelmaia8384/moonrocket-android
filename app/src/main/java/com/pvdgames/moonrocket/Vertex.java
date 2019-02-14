package com.pvdgames.moonrocket;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Vertex {

    public FloatBuffer buffer;
    public float[] vertex;

    public Vertex(float[] vertexArray)
    {
        vertex = vertexArray;

        ByteBuffer factory = ByteBuffer.allocateDirect (vertex.length * 4);
        factory.order (ByteOrder.nativeOrder ());

        buffer = factory.asFloatBuffer ();
        buffer.put (vertex);
        buffer.position (0);
    }
}
