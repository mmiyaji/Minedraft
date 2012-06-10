package gui;

import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FOG;
import static org.lwjgl.opengl.GL11.GL_FOG_COLOR;
import static org.lwjgl.opengl.GL11.GL_FOG_DENSITY;
import static org.lwjgl.opengl.GL11.GL_FOG_END;
import static org.lwjgl.opengl.GL11.GL_FOG_HINT;
import static org.lwjgl.opengl.GL11.GL_FOG_MODE;
import static org.lwjgl.opengl.GL11.GL_FOG_START;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDeleteLists;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glFog;
import static org.lwjgl.opengl.GL11.glFogf;
import static org.lwjgl.opengl.GL11.glFogi;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3d;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

/**
* A LWJGL port of the awesome MineFront Pre-ALPHA 0.02 Controls: W/UP =
* forward; A/LEFT = strafe left; D/RIGHT = strafe right; S/DOWN = backward;
* SPACE = fly up; SHIFT = fly down; CONTROL = move faster; TAB = move slower; Q
* = increase walking speed; Z = decrease walking speed; O = increase mouse
* speed; L = decrease mouse speed; C = reset position
*
* @author Oskar Veerhoek, Yan Chernikov
*/
public class Minedraft {

    /**
* Defines if the application is resizable.
*/
    public static final boolean resizable = true;
    /*
* Defines if the application is running. Set to false to terminate the
* program.
*/
    public static volatile boolean running = true;
    /**
* The position of the player as a 3D vector (xyz).
*/
    public static Vector3f position = new Vector3f(0, 0, 0);
    public static Vector3f tposition = new Vector3f(0, 0, 0);
    /**
* The rotation of the axis (where to the player looks). The X component
* stands for the rotation along the x-axis, where 0 is dead ahead, 180 is
* backwards, and 360 is automically set to 0 (dead ahead). The value must
* be between (including) 0 and 360. The Y component stands for the rotation
* along the y-axis, where 0 is looking straight ahead, -90 is straight up,
* and 90 is straight down. The value must be between (including) -90 and
* 90.
*/
    public static Vector3f rotation = new Vector3f(0, 0, 0);
    /**
* The minimal distance from the camera where objects are rendered.
*/
    public static float zNear = 0.3f;
    /**
* The width and length of the floor and ceiling. Don't put anything above
* 1000, or OpenGL will start to freak out, though.
*/
    public static final int gridSize = 10;
    /**
* The size of tiles, where 0.5 is the standard size. Increasing the size by
* results in smaller tiles, and vice versa.
*/
    public static final float tileSize = 0.25f;
    /**
* The maximal distance from the camera where objects are rendered.
*/
    public static float zFar = 20f;
    /**
* The distance where fog starts appearing.
*/
    public static float fogNear = 9f;
    /**
* The distance where the fog stops appearing (fully black here)
*/
    public static float fogFar = 13f;
    /**
* The color of the fog in rgba.
*/
    public static Color fogColor = new Color(0f, 0f, 0f, 1f);
    /**
* Defines if the application utilizes full-screen.
*/
    public static final boolean fullscreen = false;
    /**
* Defines the walking speed, where 10 is the standard.
*/
    public static int walkingSpeed = 10;
    /**
* Defines the mouse speed.
*/
    public static int mouseSpeed = 2;
    /**
* Defines if the application utilizes vertical synchronization (eliminates
* screen tearing; caps fps to 60fps)
*/
    public static final boolean vsync = true;
    /**
* Defines if the applications prints its frames-per-second to the console.
*/
    public static boolean printFPS = false;
    /**
* Defines the maximum angle at which the player can look up.
*/
    public static final int maxLookUp = 85;
    /**
* Defines the minimum angle at which the player can look down.
*/
    public static final int maxLookDown = -85;
    /**
* The height of the ceiling.
*/
    public static final float ceilingHeight = 10;
    /**
* The height of the floor.
*/
    public static final float floorHeight = -1;
    /**
* Defines the field of view.
*/
    public static final float rotateSpan = 1f;
 
    public static int fov = 68;
    private static int fps;
    private static long lastFPS;
    private static long lastFrame;

    private static long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    private static int getDelta() {
        long currentTime = getTime();
        int delta = (int) (currentTime - lastFrame);
        lastFrame = getTime();
        return delta;
    }

    public static void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            if (printFPS) {
                System.out.println("FPS: " + fps);
            }
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }
    Minedraft(){
        try {
            if (fullscreen) {
                Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
            } else {
                Display.setResizable(resizable);
                Display.setDisplayMode(new DisplayMode(800, 600));
            }
            Display.setTitle("Minedraft");
            Display.setVSyncEnabled(vsync);
            Display.create();
        } catch (LWJGLException ex) {
            System.err.println("Display initialization failed.");
            System.exit(1);
        }

        if (fullscreen) {
            Mouse.setGrabbed(true);
        } else {
            Mouse.setGrabbed(false);
        }

        if (!GLContext.getCapabilities().OpenGL11) {
            System.err.println("Your OpenGL version doesn't support the required functionality.");
            Display.destroy();
            System.exit(1);
        }

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(fov, (float) Display.getWidth() / (float) Display.getHeight(), zNear, zFar);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glEnable(GL_ALPHA_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_FOG);

        {
            FloatBuffer fogColours = BufferUtils.createFloatBuffer(4);
            fogColours.put(new float[]{fogColor.r, fogColor.g, fogColor.b, fogColor.a});
            glClearColor(fogColor.r, fogColor.g, fogColor.b, fogColor.a);
            fogColours.flip();
            glFog(GL_FOG_COLOR, fogColours);
            glFogi(GL_FOG_MODE, GL_LINEAR);
            glHint(GL_FOG_HINT, GL_NICEST);
            glFogf(GL_FOG_START, fogNear);
            glFogf(GL_FOG_END, fogFar);
            glFogf(GL_FOG_DENSITY, 0.005f);
        }

        int floorTexture = glGenTextures();
        {
            InputStream in = null;
            try {
                in = new FileInputStream("res/grass1.png");
                PNGDecoder decoder = new PNGDecoder(in);
                ByteBuffer buffer = BufferUtils.createByteBuffer(4 * decoder.getWidth() * decoder.getHeight());
                decoder.decode(buffer, decoder.getWidth() * 4, Format.RGBA);
                buffer.flip();
                in.close();
                glBindTexture(GL_TEXTURE_2D, floorTexture);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
                glBindTexture(GL_TEXTURE_2D, 0);
            } catch (FileNotFoundException ex) {
                System.err.println("Failed to find the texture files.");
                Display.destroy();
                System.exit(1);
            } catch (IOException ex) {
                System.err.println("Failed to load the texture files.");
                Display.destroy();
                System.exit(1);
            }
        }

        int ceilingDisplayList = glGenLists(1);
        glNewList(ceilingDisplayList, GL_COMPILE);
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex3f(-gridSize, ceilingHeight, -gridSize);
        glTexCoord2f(gridSize * 10 * tileSize, 0);
        glVertex3f(gridSize, ceilingHeight, -gridSize);
        glTexCoord2f(gridSize * 10 * tileSize, gridSize * 10 * tileSize);
        glVertex3f(gridSize, ceilingHeight, gridSize);
        glTexCoord2f(0, gridSize * 10 * tileSize);
        glVertex3f(-gridSize, ceilingHeight, gridSize);
        glEnd();
        glEndList();

        int wallDisplayList = setWall(1);
        int floorDisplayList = setFloor(1);
//        int objectDisplayList = setTarget(1);
        int objectDisplayList = setSphere(1);

        getDelta();
        lastFPS = getTime();

        while (running) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glBindTexture(GL_TEXTURE_2D, floorTexture);

            glEnable(GL_CULL_FACE);
            glDisable(GL_DEPTH_TEST);
            glCallList(floorDisplayList);
            glCallList(ceilingDisplayList);
            glCallList(wallDisplayList);
            glEnable(GL_DEPTH_TEST);
            glDisable(GL_CULL_FACE);
            glBindTexture(GL_TEXTURE_2D, 0);
            glCallList(objectDisplayList);

            glLoadIdentity();
            glRotatef(rotation.x, 1, 0, 0);
            glRotatef(rotation.y, 0, 1, 0);
            glRotatef(rotation.z, 0, 0, 1);
            glTranslatef(position.x, position.y, position.z);

            inputScanner();
//            GL11.glPushMatrix();
//            	tposition.x +=0.01;
//            	glTranslatef(tposition.x, tposition.y, tposition.z);
//            	glCallList(objectDisplayList);
//            GL11.glPopMatrix();
            if (resizable) {
                if (Display.wasResized()) {
                    glViewport(0, 0, Display.getWidth(), Display.getHeight());
                    glMatrixMode(GL_PROJECTION);
                    glLoadIdentity();
                    gluPerspective(fov, (float) Display.getWidth() / (float) Display.getHeight(), zNear, zFar);
                    glMatrixMode(GL_MODELVIEW);
                    glLoadIdentity();
                }
            }
            if (printFPS) {
                updateFPS();
            }
            Display.update();
            if (vsync) {
                Display.sync(60);
            }
            if (Display.isCloseRequested()) {
                running = false;
            }
        }
        glDeleteTextures(floorTexture);
        glDeleteLists(floorDisplayList, 1);
        glDeleteLists(ceilingDisplayList, 1);
        glDeleteLists(wallDisplayList, 1);
        glDeleteLists(objectDisplayList, 1);
        Display.destroy();
        System.exit(0);
    }
    public static void main(String[] args) {
    	new Minedraft();
    }
    public int setWall(int meta){
        int wallDisplayList = glGenLists(meta);
        glNewList(wallDisplayList, GL_COMPILE);

        glBegin(GL_QUADS);

        // North wall

        glTexCoord2f(0, 0);
        glVertex3f(-gridSize, floorHeight, -gridSize);
        glTexCoord2f(0, gridSize * 10 * tileSize);
        glVertex3f(gridSize, floorHeight, -gridSize);
        glTexCoord2f(gridSize * 10 * tileSize, gridSize * 10 * tileSize);
        glVertex3f(gridSize, ceilingHeight, -gridSize);
        glTexCoord2f(gridSize * 10 * tileSize, 0);
        glVertex3f(-gridSize, ceilingHeight, -gridSize);

        // West wall

        glTexCoord2f(0, 0);
        glVertex3f(-gridSize, floorHeight, -gridSize);
        glTexCoord2f(gridSize * 10 * tileSize, 0);
        glVertex3f(-gridSize, ceilingHeight, -gridSize);
        glTexCoord2f(gridSize * 10 * tileSize, gridSize * 10 * tileSize);
        glVertex3f(-gridSize, ceilingHeight, +gridSize);
        glTexCoord2f(0, gridSize * 10 * tileSize);
        glVertex3f(-gridSize, floorHeight, +gridSize);

        // East wall

        glTexCoord2f(0, 0);
        glVertex3f(+gridSize, floorHeight, -gridSize);
        glTexCoord2f(gridSize * 10 * tileSize, 0);
        glVertex3f(+gridSize, floorHeight, +gridSize);
        glTexCoord2f(gridSize * 10 * tileSize, gridSize * 10 * tileSize);
        glVertex3f(+gridSize, ceilingHeight, +gridSize);
        glTexCoord2f(0, gridSize * 10 * tileSize);
        glVertex3f(+gridSize, ceilingHeight, -gridSize);

        // South wall

        glTexCoord2f(0, 0);
        glVertex3f(-gridSize, floorHeight, +gridSize);
        glTexCoord2f(gridSize * 10 * tileSize, 0);
        glVertex3f(-gridSize, ceilingHeight, +gridSize);
        glTexCoord2f(gridSize * 10 * tileSize, gridSize * 10 * tileSize);
        glVertex3f(+gridSize, ceilingHeight, +gridSize);
        glTexCoord2f(0, gridSize * 10 * tileSize);
        glVertex3f(+gridSize, floorHeight, +gridSize);

        glEnd();

        glEndList();
        return wallDisplayList;
    }
    public int setFloor(int meta){
        int floorDisplayList = glGenLists(meta);
        glNewList(floorDisplayList, GL_COMPILE);
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex3f(-gridSize, floorHeight, -gridSize);
        glTexCoord2f(0, gridSize * 10 * tileSize);
        glVertex3f(-gridSize, floorHeight, gridSize);
        glTexCoord2f(gridSize * 10 * tileSize, gridSize * 10 * tileSize);
        glVertex3f(gridSize, floorHeight, gridSize);
        glTexCoord2f(gridSize * 10 * tileSize, 0);
        glVertex3f(gridSize, floorHeight, -gridSize);
        glEnd();
        glEndList();
        return floorDisplayList;
    }
    public int setTarget(int meta){
        int objectDisplayList = glGenLists(meta);
        glNewList(objectDisplayList, GL_COMPILE);
        {
            double topPoint = 0.75;
            glBegin(GL_TRIANGLES);
            glColor4f(1, 1, 0, 1f);
            glVertex3d(0, topPoint, -5);
            glColor4f(0, 0, 1, 1f);
            glVertex3d(-1, -0.75, -4);
            glColor4f(0, 0, 1, 1f);
            glVertex3d(1, -.75, -4);

            glColor4f(1, 1, 0, 1f);
            glVertex3d(0, topPoint, -5);
            glColor4f(0, 0, 1, 1f);
            glVertex3d(1, -0.75, -4);
            glColor4f(0, 0, 1, 1f);
            glVertex3d(1, -0.75, -6);

            glColor4f(1, 1, 0, 1f);
            glVertex3d(0, topPoint, -5);
            glColor4f(0, 0, 1, 1f);
            glVertex3d(1, -0.75, -6);
            glColor4f(0, 0, 1, 1f);
            glVertex3d(-1, -.75, -6);
            
            glColor4f(1, 1, 0, 1f);
            glVertex3d(0, topPoint, -5);
            glColor4f(0, 0, 1, 1f);
            glVertex3d(-1, -0.75, -6);
            glColor4f(0, 0, 1, 1f);
            glVertex3d(-1, -.75, -4);
            
            glEnd();
            glColor4f(1, 1, 1, 1);
        }
        glEndList();
        return objectDisplayList;
    }
    public int setSphere(int meta){
        int objectDisplayList = glGenLists(meta);
        glNewList(objectDisplayList, GL_COMPILE);
        {
        	Sphere sphere = new Sphere(); 
        	sphere.setDrawStyle(GLU.GLU_LINE);
        	GL11.glPushMatrix();
        		GL11.glTranslatef(0.0f, 0.0f, -2.0f);
        		sphere.draw(0.5f, 16, 16);
        	GL11.glPopMatrix();
        }
        glEndList();
        return objectDisplayList;
    }
    public void inputScanner(){
        int delta = getDelta();
        boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W);
        boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S);
        boolean keyLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A);
        boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D);
        boolean flyUp = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
        boolean flyDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
        boolean moveFaster = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
        boolean moveSlower = Keyboard.isKeyDown(Keyboard.KEY_TAB);
//        if (moveFaster && !moveSlower) {
//            walkingSpeed *= 4f;
//        }
//        if (moveSlower && !moveFaster) {
//            walkingSpeed /= 10f;
//        }
        if(keyRight && !flyDown && !flyUp){
            if (rotation.y + rotateSpan >= 360) {
                rotation.y = rotation.y + rotateSpan - 360;
            } else if (rotation.y + rotateSpan < 0) {
                rotation.y = 360 - rotation.y + rotateSpan;
            } else {
                rotation.y += rotateSpan;
            }
        }
        if(keyLeft && !flyDown && !flyUp){
        	float rotateSpan = -1f;
            if (rotation.y + rotateSpan >= 360) {
                rotation.y = rotation.y + rotateSpan - 360;
            } else if (rotation.y + rotateSpan < 0) {
                rotation.y = 360 - rotation.y + rotateSpan;
            } else {
                rotation.y += rotateSpan;
            }
        }
        if(keyUp && !flyDown && !flyUp){
        	float rotateSpan = 1f;
            if (rotation.x - rotateSpan >= maxLookDown && rotation.x - rotateSpan <= maxLookUp) {
                rotation.x += -rotateSpan;
            } else if (rotation.x - rotateSpan < maxLookDown) {
                rotation.x = maxLookDown;
            } else if (rotation.x - rotateSpan > maxLookUp) {
                rotation.x = maxLookUp;
            }
        }
        if(keyDown && !flyDown && !flyUp){
        	float rotateSpan = -1f;
            if (rotation.x - rotateSpan >= maxLookDown && rotation.x - rotateSpan <= maxLookUp) {
                rotation.x += -rotateSpan;
            } else if (rotation.x - rotateSpan < maxLookDown) {
                rotation.x = maxLookDown;
            } else if (rotation.x - rotateSpan > maxLookUp) {
                rotation.x = maxLookUp;
            }
        }

//        if (keyUp && keyRight && !keyLeft && !keyDown) {
//            float angle = rotation.y + 45;
//            Vector3f newPosition = new Vector3f(position);
//            float schuine = (walkingSpeed * 0.0002f) * delta;
//            float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
//            float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
//            newPosition.z += aanliggende;
//            newPosition.x -= overstaande;
//            position.z = newPosition.z;
//            position.x = newPosition.x;
//        }
//        if (keyUp && keyLeft && !keyRight && !keyDown) {
//            float angle = rotation.y - 45;
//            Vector3f newPosition = new Vector3f(position);
//            float schuine = (walkingSpeed * 0.0002f) * delta;
//            float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
//            float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
//            newPosition.z += aanliggende;
//            newPosition.x -= overstaande;
//            position.z = newPosition.z;
//            position.x = newPosition.x;
//        }
//        if (keyUp && !keyLeft && !keyRight && !keyDown) {
//            float angle = rotation.y;
//            Vector3f newPosition = new Vector3f(position);
//            float schuine = (walkingSpeed * 0.0002f) * delta;
//            float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
//            float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
//            newPosition.z += aanliggende;
//            newPosition.x -= overstaande;
//            position.z = newPosition.z;
//            position.x = newPosition.x;
//        }
//        if (keyDown && keyLeft && !keyRight && !keyUp) {
//            float angle = rotation.y - 135;
//            Vector3f newPosition = new Vector3f(position);
//            float schuine = (walkingSpeed * 0.0002f) * delta;
//            float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
//            float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
//            newPosition.z += aanliggende;
//            newPosition.x -= overstaande;
//            position.z = newPosition.z;
//            position.x = newPosition.x;
//        }
//        if (keyDown && keyRight && !keyLeft && !keyUp) {
//            float angle = rotation.y + 135;
//            Vector3f newPosition = new Vector3f(position);
//            float schuine = (walkingSpeed * 0.0002f) * delta;
//            float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
//            float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
//            newPosition.z += aanliggende;
//            newPosition.x -= overstaande;
//            position.z = newPosition.z;
//            position.x = newPosition.x;
//        }
//        if (keyDown && !keyUp && !keyLeft && !keyRight) {
//            float angle = rotation.y;
//            Vector3f newPosition = new Vector3f(position);
//            float schuine = -(walkingSpeed * 0.0002f) * delta;
//            float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
//            float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
//            newPosition.z += aanliggende;
//            newPosition.x -= overstaande;
//            position.z = newPosition.z;
//            position.x = newPosition.x;
//        }
//        if (keyLeft && !keyRight && !keyUp && !keyDown) {
//            float angle = rotation.y - 90;
//            Vector3f newPosition = new Vector3f(position);
//            float schuine = (walkingSpeed * 0.0002f) * delta;
//            float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
//            float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
//            newPosition.z += aanliggende;
//            newPosition.x -= overstaande;
//            position.z = newPosition.z;
//            position.x = newPosition.x;
//        }
//        if (keyRight && !keyLeft && !keyUp && !keyDown) {
//            float angle = rotation.y + 90;
//            Vector3f newPosition = new Vector3f(position);
//            float schuine = (walkingSpeed * 0.0002f) * delta;
//            float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
//            float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
//            newPosition.z += aanliggende;
//            newPosition.x -= overstaande;
//            position.z = newPosition.z;
//            position.x = newPosition.x;
//        }
        if (flyDown && keyUp) {
            double newPositionY = (walkingSpeed * 0.0002) * delta;
            position.y -= newPositionY;
        }
        if (flyDown && keyDown) {
            double newPositionY = (walkingSpeed * 0.0002) * delta;
            position.y += newPositionY;
        }
        if (flyDown && keyRight) {
          float angle = rotation.y + 90;
          Vector3f newPosition = new Vector3f(position);
          float schuine = (walkingSpeed * 0.0002f) * delta;
          float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
          float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
          newPosition.z += aanliggende;
          newPosition.x -= overstaande;
          position.z = newPosition.z;
          position.x = newPosition.x;
        }
        if (flyDown && keyLeft) {
          float angle = rotation.y - 90;
          Vector3f newPosition = new Vector3f(position);
          float schuine = (walkingSpeed * 0.0002f) * delta;
          float aanliggende = schuine * (float) Math.cos(Math.toRadians(angle));
          float overstaande = (float) (Math.sin(Math.toRadians(angle)) * schuine);
          newPosition.z += aanliggende;
          newPosition.x -= overstaande;
          position.z = newPosition.z;
          position.x = newPosition.x;
        }
        if (flyUp && keyUp) {
            position.z += tileSize;
         }
        if (flyUp && keyDown) {
            position.z -= tileSize;
          }
        if (flyUp && keyRight) {
            position.x -= tileSize;
          }
        if (flyUp && keyLeft) {
            position.x += tileSize;
          }

//        if (moveFaster && !moveSlower) {
//            walkingSpeed /= 4f;
//        }
//        if (moveSlower && !moveFaster) {
//            walkingSpeed *= 10f;
//        }
//        while (Mouse.next()) {
//            if (Mouse.isButtonDown(0)) {
//                Mouse.setGrabbed(true);
//            }
//            if (Mouse.isButtonDown(1)) {
//                Mouse.setGrabbed(false);
//            }
//
//        }
        while (Keyboard.next()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
                position = new Vector3f(0, 0, 0);
                rotation = new Vector3f(0, 0, 0);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
                mouseSpeed += 1;
                System.out.println("Mouse speed changed to " + mouseSpeed + ".");
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
                if (mouseSpeed - 1 > 0) {
                    mouseSpeed -= 1;
                    System.out.println("Mouse speed changed to " + mouseSpeed + ".");
                }
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
                System.out.println("Walking speed changed to " + walkingSpeed + ".");
                walkingSpeed += 1;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
                System.out.println("Walking speed changed to " + walkingSpeed + ".");
                walkingSpeed -= 1;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_F11)) {
                try {
                    Display.setFullscreen(!Display.isFullscreen());
                    if (!Display.isFullscreen()) {
                        Display.setResizable(resizable);
                        Display.setDisplayMode(new DisplayMode(800, 600));
                        glViewport(0, 0, Display.getWidth(), Display.getHeight());
                        glMatrixMode(GL_PROJECTION);
                        glLoadIdentity();
                        gluPerspective(fov, (float) Display.getWidth() / (float) Display.getHeight(), zNear, zFar);
                        glMatrixMode(GL_MODELVIEW);
                        glLoadIdentity();
                    } else {
                        glViewport(0, 0, Display.getWidth(), Display.getHeight());
                        glMatrixMode(GL_PROJECTION);
                        glLoadIdentity();
                        gluPerspective(fov, (float) Display.getWidth() / (float) Display.getHeight(), zNear, zFar);
                        glMatrixMode(GL_MODELVIEW);
                        glLoadIdentity();
                    }
                } catch (LWJGLException ex) {
                    Logger.getLogger(Minedraft.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                if (!Mouse.isGrabbed() || Display.isFullscreen()) {
                    running = false;
                } else {
                    Mouse.setGrabbed(false);
                }
            }
        }
    }
}