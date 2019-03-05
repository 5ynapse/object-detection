/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cvfxtest;

import java.awt.image.BufferedImage;
import java.util.List;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Core;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

/**
 *
 * @author bogdan
 */
public class Cvfxtest extends Application {

    // the OpenCV object that realizes the video capture
    private VideoCapture capture;
    // a flag to change the button behavior
    private boolean cameraActive;
    private ImageView currentFrame;
    // Setting the image view
    ImageView imageView;

    @Override
    public void init() {
        System.out.println("test");
        // load the OpenCV native library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // create and print on screen a 3x3 identity matrix
        //System.out.println("Create a 3x3 identity matrix...");
        //Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
        //System.out.println("mat = " + mat.dump());
        //end of opencv
        this.capture = new VideoCapture();
        this.cameraActive = false;
    }

    @Override
    public void start(Stage primaryStage) {

        // start the video capture
        if (!this.cameraActive) {
            startCamera();
        } else {

        }

        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");

            }
        });

        // Creating a Group object
        Group root = new Group(imageView);

        //		Scene scene = new Scene(root, 582, 508);
        //StackPane root = new StackPane();
        //root.getChildren().add(btn);      
        Scene scene = new Scene(root, 800, 800);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void startCamera() {

        // set a fixed width for the frame
        //this.currentFrame.setFitWidth(600);
        // preserve image ratio
        //this.currentFrame.setPreserveRatio(true);
        this.capture.open(0);

        // is the video stream available?
        if (this.capture.isOpened()) {
            this.cameraActive = true;

            //Mat frame = grabFrame();
            Mat frame = new Mat();

            //Object Detection
            //https://github.com/opencv-java/opencv-java-tutorials/blob/master/docs/source/08-object-detection.rst
            //Remove noise
            //In order to use the morphological operators and obtain good results we need to process the image and remove the noise,
            //change the image to HSV allows to get the contours easily.
            Mat blurredImage = new Mat();
            Mat hsvImage = new Mat();
            Mat mask = new Mat();
            Mat morphOutput = new Mat();

            //String filenameFaceCascade = args.length > 2 ? args[0] : "../data/haarcascade_frontalface_alt.xml";
            String filenameFaceCascade = "/home/bogdan/NetBeansProjects/cvfxtest/src/data/haarcascade_frontalface_alt.xml";
            String filenameEyesCascade = "/home/bogdan/NetBeansProjects/cvfxtest/src/data/haarcascade_eye_tree_eyeglasses.xml";

            System.out.print(filenameFaceCascade);

            CascadeClassifier faceCascade = new CascadeClassifier();
            CascadeClassifier eyesCascade = new CascadeClassifier();

            if (!faceCascade.load(filenameFaceCascade)) {
                System.err.println("\n--(!)Error loading face cascade: " + filenameFaceCascade);
                System.exit(0);
            }
            if (!eyesCascade.load(filenameEyesCascade)) {
                System.err.println("--(!)Error loading eyes cascade: " + filenameEyesCascade);
                System.exit(0);
            }

            // read the current frame
            while (this.capture.read(frame)) {

                // if the frame is not empty, process it
                if (!frame.empty()) {

                    // remove some noise
                    Imgproc.blur(frame, blurredImage, new Size(7, 7));
                    // convert the frame to HSV
                    Imgproc.cvtColor(blurredImage, hsvImage, Imgproc.COLOR_BGR2HSV);

                    //Values of HSV image
                    //With the sliders we can modify the values of the HSV Image, the image will be updtated in real time, 
                    //that allows to increase or decrease the capactity to recognize object into the image. .
                    // get thresholding values from the UI
                    // remember: H ranges 0-180, S and V range 0-255
                    //Scalar minValues = new Scalar(this.hueStart.getValue(), this.saturationStart.getValue(),this.valueStart.getValue());
                    //Scalar maxValues = new Scalar(this.hueStop.getValue(), this.saturationStop.getValue(), this.valueStop.getValue());
                    //
                    Scalar minValues = new Scalar(20, 60, 50);
                    Scalar maxValues = new Scalar(50, 200, 255);

                    // threshold HSV image to select tennis balls
                    Core.inRange(hsvImage, minValues, maxValues, mask);
                    
                    /*
                    
                    byte[] data1 = new byte[frame.rows() * frame.cols() * (int) (frame.elemSize())];
                    frame.get(0, 0, data1);

                    // Creating the buffered image
                    BufferedImage bufImage = new BufferedImage(frame.cols(), frame.rows(),
                            BufferedImage.TYPE_BYTE_GRAY);

                    // Setting the data elements to the image
                    bufImage.getRaster().setDataElements(0, 0, frame.cols(), frame.rows(), data1);

                    // Creating a WritableImage
                    WritableImage writableImage = SwingFXUtils.toFXImage(bufImage, null);

                    // Setting the image view
                    //ImageView 
                    imageView = new ImageView(writableImage);

                    // Setting the position of the image
                    imageView.setX(5);
                    imageView.setY(5);

                    // setting the fit height and width of the image view
                    //       imageView.setFitHeight(600);
                    //     imageView.setFitWidth(600);
                    // Setting the preserve ratio of the image view
                    imageView.setPreserveRatio(true);
                     */
                    //-- Show what you got        
                    //-- 3. Apply the classifier to the frame
                    HighGui.imshow("Capture - Face detection", mask);
                    //HighGui.imshow("Capture - Face detection", detectAndDisplay(hsvImage, faceCascade, eyesCascade));
                    //HighGui.imshow("Capture - Face detection", detectAndDisplay(frame, faceCascade, eyesCascade));

                    // get the input from the keyboard
                    int keyboard = HighGui.waitKey(30);
                    if (keyboard == 'q' || keyboard == 27) {
                        break;
                    }

                    System.out.println("Image Read");
                } else {
                    System.err.println(" -- Frame not captured -- Break!");
                    break;
                }
            }
            // convert and show the frame
            //Image imageToShow = Utils.mat2Image(frame);
        } else {
            // log the error
            System.err.println("Impossible to open the camera connection...");

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public Mat detectAndDisplay(Mat frame, CascadeClassifier faceCascade, CascadeClassifier eyesCascade) {
        Mat frameGray = new Mat();
        Imgproc.cvtColor(frame, frameGray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(frameGray, frameGray);

        // -- Detect faces
        MatOfRect faces = new MatOfRect();
        faceCascade.detectMultiScale(frameGray, faces);

        List<Rect> listOfFaces = faces.toList();
        for (Rect face : listOfFaces) {
            Point center = new Point(face.x + face.width / 2, face.y + face.height / 2);
            Imgproc.ellipse(frame, center, new Size(face.width / 2, face.height / 2), 0, 0, 360,
                    new Scalar(255, 0, 255));

            Mat faceROI = frameGray.submat(face);
            // -- In each face, detect eyes
            MatOfRect eyes = new MatOfRect();
            eyesCascade.detectMultiScale(faceROI, eyes);
            List<Rect> listOfEyes = eyes.toList();
            for (Rect eye : listOfEyes) {
                Point eyeCenter = new Point(face.x + eye.x + eye.width / 2, face.y + eye.y + eye.height / 2);
                int radius = (int) Math.round((eye.width + eye.height) * 0.25);
                Imgproc.circle(frame, eyeCenter, radius, new Scalar(255, 0, 0), 4);
            }

        }

        return frame;
    }

    private class currentFrame {

        public currentFrame() {
        }
    }

}
