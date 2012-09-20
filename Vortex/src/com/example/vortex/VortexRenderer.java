package com.example.vortex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;

public class VortexRenderer implements GLSurfaceView.Renderer {
    private static final String LOG_TAG = VortexRenderer.class.getSimpleName();

    // a raw buffer to hold indices allowing a reuse of points.
    private ShortBuffer _indexBuffer;
    
    // a raw buffer to hold the vertices
    private FloatBuffer _vertexBuffer;
    
    // a raw buffer to hold the colors
    private FloatBuffer _colorBuffer;
    
    private int _nrOfVertices = 0;

    private float _xAngle;
    private float _yAngle;
    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    	boolean SEE_THRU = true;
        // preparation
        // enable the differentiation of which side may be visible 
        gl.glEnable(GL10.GL_CULL_FACE);
        // which is the front? the one which is drawn counter clockwise
        gl.glFrontFace(GL10.GL_CCW);
        // which one should NOT be drawn
        gl.glCullFace(GL10.GL_BACK);
        
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        
        float lightAmbient[] = new float[] { 0.2f, 0.2f, 0.2f, 1 };
        float lightDiffuse[] = new float[] { 1, 1, 1, 1 };
        float[] lightPos = new float[] { 1, 1, 1, 1 };
        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbient, 0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuse, 0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPos, 0);
    
        gl.glEnable(GL10.GL_DEPTH_TEST);
        
        if (SEE_THRU) {
            gl.glDisable(GL10.GL_DEPTH_TEST);
            gl.glEnable(GL10.GL_BLEND);
            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
         }
        
        
        initTriangle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        gl.glViewport(0, 0, w, h);
    }
    
    public void setXAngle(float angle) {
        _xAngle = angle;
    }
    
    public float getXAngle() {
        return _xAngle;
    }
    
    public void setYAngle(float angle) {
        _yAngle = angle;
    }
    
    public float getYAngle() {
        return _yAngle;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // define the color we want to be displayed as the "clipping wall"
        gl.glClearColor(0f, 0f, 0f, 1.0f);
        
        // reset the matrix - good to fix the rotation to a static angle
        gl.glLoadIdentity();
        
        // clear the color buffer and the depth buffer to show the ClearColor
        // we called above...
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    
        // set rotation
        gl.glRotatef(_xAngle, 1f, 0f, 0f);
        gl.glRotatef(_yAngle, 0f, 1f, 0f);
        
        //gl.glColor4f(0.5f, 0f, 0f, 0.5f);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _vertexBuffer);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, _colorBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES, _nrOfVertices, GL10.GL_UNSIGNED_SHORT, _indexBuffer);
    }
    
    private void initTriangle() {
        float[] coords = {
                -0.5f, -0.5f, 0.5f, // 0
                0.5f, -0.5f, 0.5f, // 1
                0f, -0.5f, -0.5f, // 2
                0f, 0.5f, 0f, // 3
        };
        _nrOfVertices = coords.length;
        
        float[] colors = {
                1f, 0f, 0f, 1f, // point 0 red
                0f, 1f, 0f, 1f, // point 1 green
                0f, 0f, 1f, 1f, // point 2 blue
                1f, 1f, 1f, 1f, // point 3 white
        };
        
        short[] indices = new short[] {
                0, 1, 3, // rwg
                0, 2, 1, // rbg
                0, 3, 2, // rbw
                1, 2, 3, // bwg
        };

        // float has 4 bytes, coordinate * 4 bytes
        ByteBuffer vbb = ByteBuffer.allocateDirect(coords.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        _vertexBuffer = vbb.asFloatBuffer();
        
        // short has 2 bytes, indices * 2 bytes
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        _indexBuffer = ibb.asShortBuffer();
        
        // float has 4 bytes, colors (RGBA) * 4 bytes
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        _colorBuffer = cbb.asFloatBuffer();
        
        _vertexBuffer.put(coords);
        _indexBuffer.put(indices);
        _colorBuffer.put(colors);
        
        _vertexBuffer.position(0);
        _indexBuffer.position(0);
        _colorBuffer.position(0);
    }
}