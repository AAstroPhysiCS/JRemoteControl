package client;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

public class VideoCapture extends ClientUtil {

    private Socket socket;
    private Robot robot;

    public VideoCapture(Socket socket) {
        this.socket = socket;
        startVideoCapture();
    }

    private void startVideoCapture() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        BufferedImage image = robot.createScreenCapture(new Rectangle(screenDimension));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", output);
            socket.getOutputStream().write(output.toByteArray());
            socket.getOutputStream().flush();
            socket.getOutputStream().close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
