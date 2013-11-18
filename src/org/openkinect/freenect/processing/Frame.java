/**
 * 
 */
package org.openkinect.freenect.processing;

import java.nio.ByteBuffer;

import org.openkinect.freenect.FrameMode;

import processing.core.PImage;

/**
 * @author sam
 * 
 */
public interface Frame {
    /**
     * 
     * @param mode
     *            TODO
     * @param frame
     * @param timestamp
     *            TODO
     */
    public void setData(FrameMode mode, ByteBuffer frame, int timestamp);

    /**
     * 
     * @return
     */
    public PImage getImage();
}
