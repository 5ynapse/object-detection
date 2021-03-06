/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cams;

/**
 *
 * @author bogdan
 */
public interface Kamera {
    
    
    	/**
	 * Default frame width for implementing cameras
	 */
	public static final int DEFAULT_WIDTH = 320;
	/**
	 * Default frame height for implementing cameras
	 */
	public static final int DEFAULT_HEIGHT = 240;
	/**
	 * Default compression quality for implementing cameras
	 */
	public static final int DEFAULT_QUALITY = 30;
	
	/**
	 * Reads a new image from the camera and returns it.
	 * @return a new image from the camera.
	 */
	//Object read();
	/**
	 * Gets a frame from the camera and returns a compressed data format
	 * in a byte array in order to send.
	 * 
	 * @return compressed data for sending
	 */
	//byte[] getData();
	/**
	 * Gets the compression quality of the frame.
	 * @return compression quality
	 */
	//int getQuality();
	/**
	 * Gets the Frames per Second count of the camera.
	 * @return camera fps
	 */
	//int getFPS();
	/**
	 * Sets the Frames per Second count of the camera.
	 * @param fps camera fps
	 */
	//void setFPS(int fps);
	/**
	 * Sets the compression quality of the frame.
	 * @param quality compression quality
	 */
	//void setQuality(int quality);
    
}
