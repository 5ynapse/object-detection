/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cvfxtest;

import javafx.scene.Camera;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import cams.*;

/**
 *
 * @author bogdan
 */
//public class myCamera implements Kamera {
public class myCamera {

    // the OpenCV object that realizes the video capture
    private VideoCapture capture;
    // a flag to change the button behavior
    private boolean cameraActive;

    myCamera() {

        this.capture = new VideoCapture();
        this.cameraActive = false;

    }

}
