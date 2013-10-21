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

    public AbstractFrame(PApplet parent) {
        this.parent = parent;
        image = parent.createImage(640, 480, PConstants.RGB);
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

}
