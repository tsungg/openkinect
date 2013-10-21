/**
 * 
 */
package org.openkinect.freenect.processing;

import java.nio.ByteBuffer;

import processing.core.PImage;

/**
 * @author sam
 * 
 */
public interface Frame {
    /**
     * 
     * @param frame
     */
    public void setData(ByteBuffer frame);

    /**
     * 
     * @return
     */
    public PImage getImage();
}
