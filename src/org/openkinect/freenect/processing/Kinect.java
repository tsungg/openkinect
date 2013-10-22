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

    /**
     * return the version of the library.
     * 
     * @return String
     */
    public static String version() {
        return VERSION;
    }

    private PApplet parent;
    private Method kinectEventMethod;
    private Context context;
    private Device device;
    private DepthFrame depthFrame;

    private RGBFrame rgbFrame;
    private boolean debug;

    /**
     * Constructor, sets the PApplet we are running inside as the parent.
     * 
     * @param parent
     */
    public Kinect(PApplet parent) {
        this.parent = parent;
        this.depthFrame = new DepthFrame(parent, this);
        this.rgbFrame = new RGBFrame(parent, this);

        /**
         * try { this.kinectEventMethod = this.parent.getClass().getMethod(
         * "kinectEvent", new Class[] { Kinect.class }); } catch
         * (NoSuchMethodException e) { throw new RuntimeException(
         * "You appear to be missing the kinectEvent() method.", e); } catch
         * (SecurityException e) { throw new RuntimeException(
         * "Security exception when trying to load the kinectEvent() method.",
         * e); }
         **/
    }

    public void debug(String message) {
        if (this.debug) {
            System.out.println(message);
        }
    }

    public void enableDebug(boolean debug) {
        this.debug = debug;
    }

    public void enableDepth(boolean b) {
        this.depthFrame.setProcessImage(b);
    }

    public void enableRGB(boolean b) {
        this.rgbFrame.setProcessImage(b);
    }

    /**
     * Get the depth image.
     * 
     * @return
     */
    public PImage getDepthImage() {
        if (this.depthFrame != null) {
            return this.depthFrame.getImage();
        } else {
            System.err.println("DepthFrame not initialized properly.");
            return new PImage(640, 480);
        }
    }

    /**
     * Get video image from the kinect - either normal rgb or infrared.
     * 
     * @return
     */
    public PImage getVideoImage() {
        if (this.rgbFrame != null) {
            return this.rgbFrame.getImage();
        } else {
            System.err.println("RBGFrame not initialized properly.");
            return new PImage(640, 480);
        }
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
        this.context = Freenect.createContext();
        if (this.context.numDevices() < 1) {
            System.err.println("No Kinect devices found.");
        }
        this.device = this.context.openDevice(num);

        this.device.startVideo(new VideoHandler() {
            @Override
            public void onFrameReceived(FrameMode mode, ByteBuffer frame,
                    int timestamp) {
                if (rgbFrame != null) {
                    rgbFrame.setData(mode, frame, timestamp);
                }
            }
        });

        this.device.startDepth(new DepthHandler() {
            @Override
            public void onFrameReceived(FrameMode mode, ByteBuffer frame,
                    int timestamp) {
                if (depthFrame != null) {
                    depthFrame.setData(mode, frame, timestamp);
                }
            }
        });
    }

    /**
     * Stop the Kinect.
     */
    public void stop() {
        if (this.context != null) {
            this.context.shutdown();
        }
    }
}
