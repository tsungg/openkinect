/**
 * ##library.name## ##library.sentence## ##library.url##
 * 
 * Copyright ##copyright## ##author##
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * @author ##author##
 * @modified ##date##
 * @version ##library.prettyVersion## (##library.version##)
 */

package org.openkinect.freenect.processing;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import org.openkinect.freenect.Context;
import org.openkinect.freenect.DepthHandler;
import org.openkinect.freenect.Device;
import org.openkinect.freenect.FrameMode;
import org.openkinect.freenect.Freenect;
import org.openkinect.freenect.VideoHandler;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * @author sam
 * 
 */
public class Kinect {
    public final static String VERSION = "##library.prettyVersion##";
    public final static int WIDTH = 640;
    public final static int HEIGHT = 480;

    /**
     * return the version of the library.
     * 
     * @return String
     */
    public static String version() {
        return VERSION;
    }

    private PApplet parent;
    private Context context;
    private Device device;
    private DepthFrame depthFrame;
    private RGBFrame rgbFrame;
    private boolean debug = true;
    private int[] depth;
    private Method kinectEventMethod;

    /**
     * Constructor, sets the PApplet we are running inside as the parent.
     * 
     * @param parent
     */
    public Kinect(PApplet parent) {
        // this.parent = parent;
        this.depthFrame = new DepthFrame(parent, this);
        this.rgbFrame = new RGBFrame(parent, this);
        this.depth = new int[WIDTH * HEIGHT];

        debug("Initialized Kinect");
    }

    /**
     * Debug method for internal use. Prints the passed in message to stdout if
     * the debug flag is set to true.
     * 
     * @param message
     *            the message to be printed
     */
    protected void debug(String message) {
        if (this.debug) {
            System.out.println(message);
        }
    }

    /**
     * Enable or disable debug mode.
     * 
     * @param debug
     *            set to true if you want to see debug output, false otherwise.
     *            Defaults to false.
     */
    public void enableDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * Enable or disable the depth handler. This method is deprecated, and kept
     * for compatibility with the original version of this library.
     * 
     * @param b
     */
    @Deprecated
    public void enableDepth(boolean b) {
        if (b) {
            startDepth();
        } else {
            stopDepth();
        }
    }

    /**
     * Enable or disable the RGB video handler.
     * 
     * @param b
     */
    @Deprecated
    public void enableRGB(boolean b) {
        if (b) {
            startVideo();
        } else {
            stopVideo();
        }
    }

    /**
     * Return the FPS of the depth handler.
     * 
     * @return the fps
     */
    public float getDepthFPS() {
        return this.depthFrame.fps;
    }

    /**
     * Get the depth image.
     * 
     * @return
     */
    public PImage getDepthImage() {
        return this.depthFrame.getImage();
    }

    /**
     * Return the raw depth information from the sensor.
     * 
     * @return
     */
    public int[] getRawDepth() {
        ShortBuffer sb = this.depthFrame.getRawData();

        if (sb != null) {
            for (int i = 0; i < depth.length; i++) {
                depth[i] = sb.get(i);
            }
        } else {
            // build array full of 0s rather than returning null.
            for (int i = 0; i < depth.length; i++) {
                depth[i] = 0;
            }
        }

        return depth;
    }

    /**
     * Return the FPS of the video handler.
     * 
     * @return the fps
     */
    public float getVideoFPS() {
        return this.rgbFrame.fps;
    }

    /**
     * Get video image from the kinect - either normal rgb or infrared.
     * 
     * @return
     */
    public PImage getVideoImage() {
        return this.rgbFrame.getImage();
    }

    /**
     * Enable or disable processing of the depth information. Set this to false
     * if you don't need to render the grayscale depth image, i.e. if you want
     * to use the depth information to render 3d points.
     * 
     * @param processImage
     *            set to true if you want the library to process the image,
     *            false otherwise.
     */
    public void processDepthImage(boolean processImage) {
        debug("Setting depth processImage to: " + processImage);
        this.depthFrame.processImage = processImage;
    }

    /**
     * Start the kinect.
     */
    public void start() {
        this.start(0);
    }

    /**
     * Start the kinect setting some num parameter.
     * 
     * TODO: figure out what num does in this context.
     * 
     * @param num
     */
    public void start(int num) {
        debug("Starting Kinect");
        this.context = Freenect.createContext();
        if (this.context.numDevices() < 1) {
            System.err.println("No Kinect devices found.");
        }

        this.device = this.context.openDevice(num);

    }

    /**
     * Start the depth handler running.
     * 
     * @see DepthFrame#setData(FrameMode, ByteBuffer, int)
     */
    public void startDepth() {
        debug("Starting depth handler");
        if (this.device != null) {
            this.device.startDepth(new DepthHandler() {
                @Override
                public void onFrameReceived(FrameMode mode, ByteBuffer frame,
                        int timestamp) {
                    if (depthFrame != null) {
                        depthFrame.setData(mode, frame, timestamp);
                    }
                }
            });
        } else {
            System.err
                    .println("ERROR: startDepth called before Kinect was started.");
        }
    }

    /**
     * Start the video handler.
     * 
     * @see RGBFrame#setData(FrameMode, ByteBuffer, int)
     */
    public void startVideo() {
        debug("Starting video handler");
        if (this.device != null) {
            this.device.startVideo(new VideoHandler() {
                @Override
                public void onFrameReceived(FrameMode mode, ByteBuffer frame,
                        int timestamp) {
                    if (rgbFrame != null) {
                        rgbFrame.setData(mode, frame, timestamp);
                    }
                }
            });
        } else {
            System.err
                    .println("ERROR: startVideo called before Kinect was started.");
        }
    }

    /**
     * Stop the Kinect.
     */
    public void shutdown() {
        debug("Stopping Kinect");
        if (this.context != null) {
            this.context.shutdown();
        }
    }

    /**
     * Stop the depth handler.
     */
    public void stopDepth() {
        debug("Stopping depth handler");
        if (this.device != null) {
            this.device.stopDepth();
        } else {
            System.err
                    .println("ERROR: stopDepth called before Kinect was started.");
        }
    }

    /**
     * Stop the video handler.
     */
    public void stopVideo() {
        debug("Stopping video handler");
        if (this.device != null) {
            this.device.stopVideo();
        } else {
            System.err
                    .println("ERROR: stopVideo called before Kinect was started.");
        }
    }
}