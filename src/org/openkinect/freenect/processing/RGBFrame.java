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
public class RGBFrame extends AbstractFrame {

    private boolean infrared = false;

    public RGBFrame(PApplet parent, Kinect kinect) {
        super(parent, kinect);
        this.kinect.debug("Initialized RGBFrame");
    }

    /**
     * Enable infrared mode.
     * 
     * @param infrared
     */
    public void setInfrared(Boolean infrared) {
        this.infrared = infrared;
    }

    @Override
    public void setData(FrameMode mode, ByteBuffer frame, int timestamp) {

        if (infrared) {
            this.sdata = frame.asShortBuffer();

            for (int y = 0; y < this.image.height; y++) {
                for (int x = 0; x < this.image.width; x++) {
                    int offset = x + y * this.image.width;
                    short depth = this.sdata.get(offset);
                    int d = PApplet.constrain(depth, 0, 255);
                    int pixel = (0xFF) << 24 | (d & 0xFF) << 16
                            | (d & 0xFF) << 8 | (d & 0xFF) << 0;
                    this.image.pixels[offset] = pixel;
                }
            }
        } else {
            for (int y = 0; y < this.image.height; y++) {
                for (int x = 0; x < this.image.width; x++) {
                    int offset = 3 * (y * this.image.width + x);

                    int r = frame.get(offset + 2) & 0xFF;
                    int g = frame.get(offset + 1) & 0xFF;
                    int b = frame.get(offset + 0) & 0xFF;

                    int pixel = (0xFF) << 24 | (b & 0xFF) << 16
                            | (g & 0xFF) << 8 | (r & 0xFF) << 0;

                    this.image.pixels[x + this.image.width * y] = pixel;
                }
            }
        }
        this.image.updatePixels();
        calculateFps();
    }
}
