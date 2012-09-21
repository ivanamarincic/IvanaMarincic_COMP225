package com.example.vortex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.vortex.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

public class VortexRenderer implements GLSurfaceView.Renderer {
    private static final String LOG_TAG = VortexRenderer.class.getSimpleName();

    // a raw buffer to hold indices allowing a reuse of points.
    private ShortBuffer _indexBuffer;
    
    // a raw buffer to hold the vertices
    private FloatBuffer _vertexBuffer;
    
    // a raw buffer to hold the colors
    private FloatBuffer _colorBuffer;
    
    private FloatBuffer mTextureBuffer; 
   
    private int [] mTextureList = null; 
   
    private int _nrOfVertices = 0;
    
    private final Context context;

    private float _xAngle;
    private float _yAngle;
    

    private float _red = 0.9f;
    private float _green = 0.2f;
    private float _blue = 0.2f;
    
    VortexRenderer(Context context) {
        this.context = context;
     }
    
    

    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // preparation
        // enable the differentiation of which side may be visible 
        gl.glEnable(GL10.GL_CULL_FACE);
        // which is the front? the one which is drawn counter clockwise
        gl.glFrontFace(GL10.GL_CCW);
        // which one should NOT be drawn
        gl.glCullFace(GL10.GL_BACK);
        
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        
//        float lightAmbient[] = new float[] { 0.2f, 0.2f, 0.2f, 1 };
//        float lightDiffuse[] = new float[] { 1, 1, 1, 1 };
//        float[] lightPos = new float[] { 1, 1, 1, 1 };
//        gl.glEnable(GL10.GL_LIGHTING);
//        gl.glEnable(GL10.GL_LIGHT0);
//        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbient, 0);
//        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuse, 0);
//        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPos, 0);
//    
//        gl.glEnable(GL10.GL_DEPTH_TEST);
//        
        
        
        Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.drawable.pyramid_texture); 
        gl.glEnable(GL10.GL_TEXTURE_2D); 
        
        mTextureList = new int[1]; 
        gl.glGenTextures(1, mTextureList, 0); 
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureList[0]); 
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR); 
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR); 
        gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE); 
        gl.glClientActiveTexture(GL10.GL_TEXTURE0); 
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY); 
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, b, 0); 
        b.recycle();
        
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
        gl.glClearColor(_red, _green, _blue, 1.0f);
        
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
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer); 
        gl.glDrawElements(GL10.GL_TRIANGLES, _nrOfVertices, GL10.GL_UNSIGNED_SHORT, _indexBuffer);
    }
    
    public void setColor(float r, float g, float b){
    	_red=r;
    	_green=g;
    	_blue=b;
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
                0.3f, 0f, 0.5f, 0.5f, // point 0 red
                1f, 0f, 1f, 1f, // point 1 green
                1f, 0f, 1f, 1f, // point 2 blue
                0.3f, 0f, 0.5f, 0.5f, // point 3 white
        };
        
        short[] indices = new short[] {
                0, 1, 3, // rwg
                0, 2, 1, // rbg
                0, 3, 2, // rbw
                1, 2, 3, // bwg
        };
        
        float texturecoords[] = new float[] {
        		-1f, 1f,  
        		1f, 1f,  
        		0.0f, -1f, 
        		0f, 0f
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
        
       
        ByteBuffer tbb = ByteBuffer.allocateDirect(texturecoords.length * 4); 
        tbb.order(ByteOrder.nativeOrder());
        mTextureBuffer = tbb.asFloatBuffer();
       
        
        
        _vertexBuffer.put(coords);
        _indexBuffer.put(indices);
        _colorBuffer.put(colors);
         mTextureBuffer.put(texturecoords); 
        
         _vertexBuffer.position(0);
        _indexBuffer.position(0);
        _colorBuffer.position(0);
        mTextureBuffer.position(0); 

    }
    
}