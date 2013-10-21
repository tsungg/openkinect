/**
 * 
 */
package org.openkinect.freenect.processing;

import java.nio.ByteBuffer;

import processing.core.PApplet;

/**
 * @author sam
 * 
 */
public class DepthFrame extends AbstractFrame {

    public DepthFrame(PApplet parent) {
        super(parent);
    }

    @Override
    public void setData(ByteBuffer frame) {
        sdata = frame.asShortBuffer();

        if (processImage) {
            for (int y = 0; y < image.height; y++) {
                for (int x = 0; x < image.width; x++) {
                    final int offset = x + y * image.width;
                    final short depth = sdata.get(offset);
                    final int d = Math.round((1 - (depth / 2047f)) * 255f);
                    final int pixel = (0xFF) << 24 | (d & 0xFF) << 16
                            | (d & 0xFF) << 8 | (d & 0xFF) << 0;

                    image.pixels[offset] = pixel;// pixel;
                }
            }
            image.updatePixels();
        }
    }
}
