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
public class RGBFrame extends AbstractFrame {

    public RGBFrame(PApplet parent) {
        super(parent);
    }

    @Override
    public void setData(ByteBuffer frame) {
        if (processImage) {
            for (int y = 0; y < image.height; y++) {
                for (int x = 0; x < image.width; x++) {
                    int offset = 3 * (y * image.width + x);

                    int r = frame.get(offset + 2) & 0xFF;
                    int g = frame.get(offset + 1) & 0xFF;
                    int b = frame.get(offset + 0) & 0xFF;

                    int pixel = (0xFF) << 24 | (b & 0xFF) << 16
                            | (g & 0xFF) << 8 | (r & 0xFF) << 0;
                    image.pixels[x + image.width * y] = pixel;
                }
            }

            image.updatePixels();
        }
    }

}
