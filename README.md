IvanaMarincic_COMP225
=====================

OpenGL
-------

I was exploring the OpenGL ES API through creating a pyramid that one can spin while the background color changes. 
The code is based on several different tutorials that I have found. By following the tutorials I have learned several things about the structure when using OpenGL.
First, there are two classes that are crucial, a View class which extends GLSurfaceView and a Renderer class which implements GLSurfaceView.Renderer. And there is of course the class that extends Activity with the onCreate() method, but that class can be combined with the View class.
Optional are other classes that are responsible for lets say modeling individual objects, and they don't implement or extend anything, but their methods are called in the View and Renderer classes.
In my case, I dealt only with graphical material, so I did not need to use any of the xml files under the res folder. However, one thing that can be very relevant to working with graphics are the drawable folders in the res folders. Drawables seem to contain .png files. 
There are normally 4 different drawable files which refer to the density of the screen. Images in those drawable folders are specified in the drawable method in the R.java file, which generates handles those images automatically. Then these can be accessed by calling the method such as R.drawable.theImage. 
That is one way to use images in android app development, and it is useful in assigning texture to an object. I attempted doing so by insreting an image called pyramid_texture.png under drawable-hdpi, which served as the texture of the 3D model of the pyramid. 
However, I had couple of issues with that, it didn't create the texture when I ran the application on my Android device, but it works on the emulator. In addition, it was as if the image was stretched over the pyramid rather than applied to each face of the pyramid, which I still need to work on how to achieve that.
On a side note, I found that working with OpenGL means digesting a lot of information and there are lots of different features to it. 

Shapes
------

So far I have learned that when one works with OpenGL ES, one has to work basic primitive shapes. Essentially, everything is composed of triangles. So a square is actually two triangels together. The more complex the shape is, the more triangles we need to define.
I imagine to create a very complicated shape that is a polygon, I would use a four loop that would create each of those triangles and loop through different coordinates.

Coordinates
-----------
We are given a default x,y,z coordinate system and the convention is the usual one, negative x values are to the left, positive to the right of the origin, negative y values are below, positive above the origin, and z values are negative behind, positive are in front of the origin.
Working with my pyramid, I have found that the boundaries of the screen seem to be from -1.0 and 1.0, that is if for example y = -1.0 we are talking the very bottom of the screen. 

Color
-------

When working in OpenGL ES the color scheme that is used is a standard RBG one. For example, in my code we will see the glColor4f method which takes four floats as inputs. These four floats correspond to red, green, blue and a so called alpha value.
The numbers we can put in are technically any however they really range from -1.0 to 1.0. (For example, if we were to put in 5.0 then it would treat it as the maximum 1.0.) Those correspond to the intensity of the color, with 1.0 being the most intesive. Colors can also be mixed, which is how I created the purple in the pyramid.

Imports and Methods
-------------------

In order to use OpenGL ES one has to import certain packages, most importantly javax.microedition.khronos.egl.*, and javax.microedition.khronos.opengles.* packages, plus various other packages depending on the specific methods called out within the class. 
There are certain important methods that are always used when working with OpenGL. 

In the View class we will always see a constructor classView(Context content) that is the same for almost every app I believe. However, the Renderer class is what makes the app work.
The important methods in the Renderer class are:
 - the onDrawFrame() method
 - the onSurfaceCreated() method
 - the onSurfaceChanged() method
 

onDrawFrame()
--------------
This method in a way contains all the crucial information to draw what we want on the screen. It is the place where the background color is set using the .glClearColor method. One very useful method I have learned is the .glLoadIdentity method which sets the everything to static. In this particular case, if I commented out this line and
you were to slide your finger across the pyramid it wouldn't stop spinning. If there is no touch input, it resets the rotation matrix to a still one. 
Then we have several pointers which create arrays of whatever we want to work with, in this case, we need vertices, colors and texture. Depending if there are any rotations involved, the onDrawFrame method is also the place to set up rotation angles.
Finally we need the glDrawElements method that will draw the shapes we define.

onSurfaceCreated() 
-----------------

My first impression is that this would be the most lengthy and most detailed method when working with graphics, because that is the place where we define all the specifications of a shape. Mainly lighting and texture. I attempted to work with lighting, however I focused on texture but I left the bit of lighting code as commented. 
When it comes to modeling 3D shapes it seems like we need to clarify what is the front and what is the back of the face. The norm in OpenGL is that the the plane that rotates counter-clockwise is considered as the front face and what rotates clock-wise is considered as the back face. This method is a place where we clarify what is the front and the back face.
A quick scan through the method and we notice a lot of enable methods. These are important to literally enable certain features and operations, like lighting and texture, as well as the creation of arrays of vertices and colors.
Within this method we will most likely end up calling out other methods. In this case I called the initTriangle method which defines the pyramid. In case shapes were developed in different classes we would treat those as objects and use the method .draw. 
Any methods regarding rotations, animation, uploading an image will in most cases end up being called out here.

onSurfaceChanged()
------------------
I have not come to understand the details about this method, however the most essential part of it is that it is the place where we define and describe the camera and the so called viewport.
If we are changing perspectives this is a good place to start. 
There are two kinds of views on a certain object: there is orthogonal projection and perspective.
The easiest way to think about orthogonal projection is that it is like a snapshot of a still image en face. Anything behind the object is not visible. If we assigned multiple points on the outer rim of a shape and projected them perpendicular to the plane we are looking at which is the screen, and then connected those points on the screen, the image we would obtain 
is the orthogonal projection. This works for static shapes and when we don't have any rotations involved. Just movement within one plance.
Perspective is what gives a visual relationship to objects distanced from each other. Things that are closer to our eyes appear bigger and things that are further away appear smaller. In terms of computer graphics, the screen is like a window through which we watch the scenario that contains different shapes.

Other Methods
--------------

Other methods used in the appliation are methods that define the rotation that is going on when sliding the finger across the screen and forming the pyramid itself.

Other Methods: onTouchEvent()
------------------------------
Found in the View class. The two main things that are crucial in my code are two if statements that relate to what happens if MotionEvent.ACTION_DOWN and MotionEvent.ACTION_MOVE.
Action Down means the default state when no touch is applied. The method gets the default values.
Action Move means all the action while a touch and/or motion is active. The method handles all the changes related to when the user applies touch to the screen.

Other Methods: initTriangle()
-----------------------------
This is a custom method in the Renderer class. This method contains everything we need to set up the pyramid. First of, there are several sets of coordinates. 
The first set of coordinates describes the position of the vertices. The second set assigns a color combination to each vertex. The third set describe the indices of the individual faces of the pyramid. Each set of indices is a triangle. The last array specifies the coordinates of the texture.
Following are methods from the ByteBuffer class. I am not quite sure what the specific meaning of this class is, but any of the four aspects that describe the pyramid (coordinates, colors, indices and texture), anything that requires an array of a certain data type requires the ByteBuffer.allocateDirect method.

Summary of Things I am Comfortable with
----------------------------------------
Working with OpenGL and following various tutorials has taught me a number of things that I am comfortable working with at this point:
- setting up 3D shapes
- assigning colors to shapes and backgrounds
- defining rotations
- working with touch functions 
- I am familiar with the process of applying texture but have not mastered this yet
