/**
 * 
 */
package org.openkinect.freenect.processing;

import java.nio.ShortBuffer;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

/**
 * @author sam
 * 
 */
public abstract class AbstractFrame implements Frame {

    protected boolean processImage = false;
    protected PApplet parent;
    protected ShortBuffer sdata;
    protected PImage image;
    protected float fps;
    protected long currentTime;
    protected Kinect kinect;

    public AbstractFrame(PApplet parent, Kinect kinect) {
        this.parent = parent;
        this.kinect = kinect;
        this.image = parent.createImage(Kinect.WIDTH, Kinect.HEIGHT,
                PConstants.RGB);
        this.currentTime = System.currentTimeMillis();
    }

    /**
     * @return
     */
    public PImage getImage() {
        return image;
    }

    /**
     * Set boolean flag indicating whether or not we should try to process the
     * depth image data coming from the Kinect.
     * 
     * @param processImage
     */
    public void setProcessImage(boolean processImage) {
        this.processImage = processImage;
    }

    /**
     * Return the raw ShortBuffer data currently stored in the class.
     * 
     * @return
     */
    public ShortBuffer getRawData() {
        return this.sdata;
    }

    /**
     * Calculate our internal current frame rate. If debug is true on the Kinect
     * instance this value is printed to stdout.
     */
    protected void calculateFps() {
        long now = System.currentTimeMillis();
        long elapsedTime = now - this.currentTime;
        float currentFps = 1000.0f / elapsedTime;
        this.fps = PApplet.lerp(this.fps, currentFps, 0.1f);
        kinect.debug(this.getClass().getSimpleName() + " FPS: " + this.fps);
        this.currentTime = now;
    }
}
