/**
 * 
 */
package org.openkinect.freenect.processing;

import java.nio.ByteBuffer;

import org.openkinect.freenect.FrameMode;

import processing.core.PApplet;

/**
 * @author sam
 * 
 */
public class DepthFrame extends AbstractFrame {

    public DepthFrame(PApplet parent, Kinect kinect) {
        super(parent, kinect);
        this.kinect.debug("Created DepthFrame");
    }

    @Override
    public void setData(FrameMode mode, ByteBuffer frame, int timestamp) {
        if (processImage) {
            this.sdata = frame.asShortBuffer();

            for (int y = 0; y < this.image.height; y++) {
                for (int x = 0; x < this.image.width; x++) {
                    int offset = x + y * this.image.width;
                    short depth = this.sdata.get(offset);
                    int d = Math.round((1 - (depth / 2047f)) * 255f);
                    int pixel = (0xFF) << 24 | (d & 0xFF) << 16
                            | (d & 0xFF) << 8 | (d & 0xFF) << 0;
                    this.image.pixels[offset] = pixel;
                }
            }
            this.image.updatePixels();
            calculateFps();
        }
    }
}
